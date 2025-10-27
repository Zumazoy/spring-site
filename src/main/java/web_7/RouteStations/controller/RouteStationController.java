package web_7.RouteStations.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web_7.PaginationValidUtils;
import web_7.RouteStations.model.RouteStationModel;
import web_7.RouteStations.service.RouteStationService;
import web_7.Routes.service.RouteService;
import web_7.Stations.service.StationService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN', 'SCHEDULE_CONTROLLER')")
public class RouteStationController {
    @Autowired
    private RouteStationService routeStationService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private StationService stationService;

    @GetMapping("/route-stations")
    public String routeStations(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("routeStation")) {
            model.addAttribute("routeStation", new RouteStationModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<RouteStationModel> routeStationPage = routeStationService.getAllRouteStations(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, routeStationPage, page, pageSize);
        model.addAttribute("routeStations", routeStationPage.getContent());
        model.addAttribute("routes", routeService.getAllRoutes());
        model.addAttribute("stations", stationService.getAllStations());

        return "route_stations";
    }

    @PostMapping("/route-stations/add")
    public String addRouteStation(@Valid @ModelAttribute RouteStationModel routeStation,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.routeStation", bindingResult);
            redirectAttributes.addFlashAttribute("routeStation", routeStation);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/route-stations";
        }

        routeStationService.addRouteStation(routeStation);
        return "redirect:/admin/route-stations";
    }

    @PostMapping("/route-stations/update")
    public String updateRouteStation(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                                     RedirectAttributes redirectAttributes,
                                     @Valid @ModelAttribute RouteStationModel routeStation,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, routeStation.getId());
            return "redirect:/admin/route-stations";
        }

        RouteStationModel updatedRouteStation = routeStationService.findById(routeStation.getId())
                .orElseThrow(() -> new RuntimeException("Промежуточная станция не найдена"));

        updatedRouteStation.setRoute(routeStation.getRoute());
        updatedRouteStation.setStation(routeStation.getStation());
        updatedRouteStation.setOrderNumber(routeStation.getOrderNumber());
        updatedRouteStation.setArrivalTime(routeStation.getArrivalTime());
        updatedRouteStation.setDepartureTime(routeStation.getDepartureTime());

        routeStationService.updateRouteStation(updatedRouteStation);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/route-stations";
    }

    @PostMapping("/route-stations/delete")
    public String deleteRouteStation(@RequestParam Long id) {
        try {
            routeStationService.deleteRouteStation(id);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/route-stations";
    }

    @GetMapping("/route-stations/search")
    public String searchRouteStations(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("routeStation")) {
            model.addAttribute("routeStation", new RouteStationModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<RouteStationModel> routeStations = routeStationService.searchRouteStations(keyword, searchBy);
        int totalItems = routeStations.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<RouteStationModel> pagedRouteStations = routeStations.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedRouteStations, page, pageSize, totalItems);
        model.addAttribute("routeStations", pagedRouteStations);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "route_stations";
    }
}