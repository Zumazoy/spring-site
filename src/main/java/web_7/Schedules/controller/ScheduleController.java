package web_7.Schedules.controller;

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
import web_7.Routes.service.RouteService;
import web_7.Schedules.model.ScheduleModel;
import web_7.Schedules.service.ScheduleService;
import web_7.Trains.service.TrainService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN', 'SCHEDULE_CONTROLLER')")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private RouteService routeService;

    @GetMapping("/schedules")
    public String schedules(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("schedule")) {
            model.addAttribute("schedule", new ScheduleModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<ScheduleModel> schedulePage = scheduleService.getAllSchedules(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, schedulePage, page, pageSize);
        model.addAttribute("schedules", schedulePage.getContent());
        model.addAttribute("trains", trainService.getAllTrains());
        model.addAttribute("routes", routeService.getAllRoutes());

        return "schedules";
    }

    @PostMapping("/schedules/add")
    public String addSchedule(@Valid @ModelAttribute ScheduleModel schedule,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.schedule", bindingResult);
            redirectAttributes.addFlashAttribute("schedule", schedule);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/schedules";
        }

        scheduleService.addSchedule(schedule);
        return "redirect:/admin/schedules";
    }

    @PostMapping("/schedules/update")
    public String updateSchedule(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                                 RedirectAttributes redirectAttributes,
                                 @Valid @ModelAttribute ScheduleModel schedule,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, schedule.getId());
            return "redirect:/admin/schedules";
        }

        ScheduleModel updatedSchedule = scheduleService.findById(schedule.getId())
                .orElseThrow(() -> new RuntimeException("Расписание не найдено"));

        updatedSchedule.setTrain(schedule.getTrain());
        updatedSchedule.setRoute(schedule.getRoute());
        updatedSchedule.setDepartureDate(schedule.getDepartureDate());
        updatedSchedule.setDepartureTime(schedule.getDepartureTime());
        updatedSchedule.setArrivalTime(schedule.getArrivalTime());

        scheduleService.updateSchedule(updatedSchedule);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/schedules";
    }

    @PostMapping("/schedules/delete")
    public String deleteSchedule(@RequestParam Long id) {
        try {
            scheduleService.deleteSchedule(id);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/schedules";
    }

    @GetMapping("/schedules/search")
    public String searchSchedules(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("schedule")) {
            model.addAttribute("schedule", new ScheduleModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<ScheduleModel> schedules = scheduleService.searchSchedules(keyword, searchBy);
        int totalItems = schedules.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<ScheduleModel> pagedSchedules = schedules.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedSchedules, page, pageSize, totalItems);
        model.addAttribute("schedules", pagedSchedules);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "schedules";
    }
}