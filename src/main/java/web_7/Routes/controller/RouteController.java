package web_7.Routes.controller;

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
import web_7.Routes.model.RouteModel;
import web_7.Routes.service.RouteService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN', 'SCHEDULE_CONTROLLER')")
public class RouteController {
    @Autowired
    private RouteService routeService;

    @GetMapping("/routes")
    public String routes(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("route")) {
            model.addAttribute("route", new RouteModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<RouteModel> routePage = routeService.getAllRoutes(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, routePage, page, pageSize);
        model.addAttribute("routes", routePage.getContent());

        return "routes";
    }

    @PostMapping("/routes/add")
    public String addRoute(@Valid @ModelAttribute("route") RouteModel route,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.route", bindingResult);
            redirectAttributes.addFlashAttribute("route", route);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/routes";
        }

        routeService.addRoute(route);
        return "redirect:/admin/routes";
    }

    @PostMapping("/routes/update")
    public String updateRoute(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                              RedirectAttributes redirectAttributes,
                              @Valid @ModelAttribute("route") RouteModel route,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, route.getId());
            return "redirect:/admin/routes";
        }

        RouteModel updatedRoute = routeService.findById(route.getId())
                .orElseThrow(() -> new RuntimeException("Маршрут не найден"));

        updatedRoute.setStartStation(route.getStartStation());
        updatedRoute.setEndStation(route.getEndStation());
        updatedRoute.setDistance(route.getDistance());
        updatedRoute.setTravelTime(route.getTravelTime());

        routeService.updateRoute(updatedRoute);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/routes";
    }

    @PostMapping("/routes/delete")
    public String deleteRoute(@RequestParam Long id) {
        try {
            routeService.deleteRoute(id);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/routes";
    }

    @GetMapping("/routes/search")
    public String searchRoutes(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("route")) {
            model.addAttribute("route", new RouteModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<RouteModel> routes = routeService.searchRoutes(keyword, searchBy);
        int totalItems = routes.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<RouteModel> pagedRoutes = routes.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedRoutes, page, pageSize, totalItems);
        model.addAttribute("routes", pagedRoutes);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "routes";
    }
}