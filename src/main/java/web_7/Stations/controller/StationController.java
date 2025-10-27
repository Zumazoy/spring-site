package web_7.Stations.controller;

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
import web_7.Stations.model.StationModel;
import web_7.Stations.service.StationService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN', 'SCHEDULE_CONTROLLER')")
public class StationController {
    @Autowired
    private StationService stationService;

    @GetMapping("/stations")
    public String stations(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("station")) {
            model.addAttribute("station", new StationModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<StationModel> stationPage = stationService.getAllStations(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, stationPage, page, pageSize);
        model.addAttribute("stations", stationPage.getContent());

        return "stations";
    }

    @PostMapping("/stations/add")
    public String addStation(@Valid @ModelAttribute("station") StationModel station,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (stationService.existsByStationName(station.getStationName())) {
            bindingResult.rejectValue("stationName", "stationName.exists", "Станция с таким названием уже существует");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.station", bindingResult);
            redirectAttributes.addFlashAttribute("station", station);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/stations";
        }

        stationService.addStation(station);
        return "redirect:/admin/stations";
    }

    @PostMapping("/stations/update")
    public String updateStation(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                                RedirectAttributes redirectAttributes,
                                @Valid @ModelAttribute("station") StationModel station,
                                BindingResult bindingResult) {

        StationModel updatedStation = stationService.findById(station.getId())
                .orElseThrow(() -> new RuntimeException("Станция не найдена"));

        if (!station.getStationName().equals(updatedStation.getStationName()) &&
                stationService.existsByStationName(station.getStationName())) {
            bindingResult.rejectValue("stationName", "stationName.exists", "Станция с таким названием уже существует");
        }

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, station.getId());
            return "redirect:/admin/stations";
        }

        updatedStation.setStationName(station.getStationName());
        updatedStation.setCity(station.getCity());
        updatedStation.setCountry(station.getCountry());

        stationService.updateStation(updatedStation);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/stations";
    }

    @PostMapping("/stations/delete")
    public String deleteStation(@RequestParam Long id) {
        try {
            stationService.deleteStation(id);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/stations";
    }

    @GetMapping("/stations/search")
    public String searchStations(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("station")) {
            model.addAttribute("station", new StationModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<StationModel> stations = stationService.searchStations(keyword, searchBy);
        int totalItems = stations.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<StationModel> pagedStations = stations.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedStations, page, pageSize, totalItems);
        model.addAttribute("stations", pagedStations);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "stations";
    }
}