package web_7.TicketStatuses.controller;

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
import web_7.TicketStatuses.model.TicketStatusModel;
import web_7.TicketStatuses.service.TicketStatusService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class TicketStatusController {

    @Autowired
    private TicketStatusService ticketStatusService;

    @GetMapping("/ticket-statuses")
    public String ticketStatuses(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("ticketStatus")) {
            model.addAttribute("ticketStatus", new TicketStatusModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<TicketStatusModel> ticketStatusPage = ticketStatusService.getAllTicketStatuses(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, ticketStatusPage, page, pageSize);
        model.addAttribute("ticketStatuses", ticketStatusPage.getContent());

        return "ticket_statuses";
    }

    @PostMapping("/ticket-statuses/add")
    public String addTicketStatus(@Valid @ModelAttribute("ticketStatus") TicketStatusModel ticketStatus,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        if (ticketStatusService.existsByTicketStatusTitle(ticketStatus.getTicketStatusTitle())) {
            bindingResult.rejectValue("ticketStatusTitle", "title.exists", "Статус с таким названием уже существует");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ticketStatus", bindingResult);
            redirectAttributes.addFlashAttribute("ticketStatus", ticketStatus);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/ticket-statuses";
        }

        ticketStatusService.addTicketStatus(ticketStatus);
        return "redirect:/admin/ticket-statuses";
    }

    @PostMapping("/ticket-statuses/update")
    public String updateTicketStatus(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                                     RedirectAttributes redirectAttributes,
                                     @Valid @ModelAttribute("ticketStatus") TicketStatusModel ticketStatus,
                                     BindingResult bindingResult) {

        TicketStatusModel updatedStatus = ticketStatusService.findById(ticketStatus.getTicketStatusId())
                .orElseThrow(() -> new RuntimeException("Статус билета не найден"));

        if (!ticketStatus.getTicketStatusTitle().equals(updatedStatus.getTicketStatusTitle()) &&
                ticketStatusService.existsByTicketStatusTitle(ticketStatus.getTicketStatusTitle())) {
            bindingResult.rejectValue("ticketStatusTitle", "title.exists", "Статус с таким названием уже существует");
        }

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, ticketStatus.getTicketStatusId());
            return "redirect:/admin/ticket-statuses";
        }

        updatedStatus.setTicketStatusTitle(ticketStatus.getTicketStatusTitle());
        ticketStatusService.updateTicketStatus(updatedStatus);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/ticket-statuses";
    }

    @PostMapping("/ticket-statuses/delete")
    public String deleteTicketStatus(@RequestParam Long ticketStatusId) {
        try {
            ticketStatusService.deleteTicketStatus(ticketStatusId);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/ticket-statuses";
    }

    @GetMapping("/ticket-statuses/search")
    public String searchTicketStatuses(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("ticketStatus")) {
            model.addAttribute("ticketStatus", new TicketStatusModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<TicketStatusModel> ticketStatuses = ticketStatusService.searchTicketStatuses(keyword, searchBy);
        int totalItems = ticketStatuses.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<TicketStatusModel> pagedTicketStatuses = ticketStatuses.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedTicketStatuses, page, pageSize, totalItems);
        model.addAttribute("ticketStatuses", pagedTicketStatuses);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "ticket_statuses";
    }
}