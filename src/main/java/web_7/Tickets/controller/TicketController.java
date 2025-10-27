package web_7.Tickets.controller;

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
import web_7.Schedules.service.ScheduleService;
import web_7.TicketStatuses.service.TicketStatusService;
import web_7.Tickets.model.TicketModel;
import web_7.Tickets.service.TicketService;
import web_7.Users.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketStatusService ticketStatusService;

    @GetMapping("/tickets")
    public String tickets(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("ticket")) {
            model.addAttribute("ticket", new TicketModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<TicketModel> ticketPage = ticketService.getAllTickets(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, ticketPage, page, pageSize);
        model.addAttribute("tickets", ticketPage.getContent());
        model.addAttribute("schedules", scheduleService.getAllSchedules());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("ticketStatuses", ticketStatusService.getAllTicketStatuses());

        return "tickets";
    }

    @PostMapping("/tickets/add")
    public String addTicket(@Valid @ModelAttribute TicketModel ticket,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (ticketService.existsByCarriageAndSeat(ticket.getCarriageNumber(), ticket.getSeatNumber())) {
            bindingResult.rejectValue("carriageNumber", "carriage.exists", "Это место в этом вагоне уже занято");
            bindingResult.rejectValue("seatNumber", "seat.exists", "Это место в этом вагоне уже занято");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ticket", bindingResult);
            redirectAttributes.addFlashAttribute("ticket", ticket);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/tickets";
        }

        ticketService.addTicket(ticket);
        return "redirect:/admin/tickets";
    }

    @PostMapping("/tickets/update")
    public String updateTicket(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                               RedirectAttributes redirectAttributes,
                               @Valid @ModelAttribute TicketModel ticket,
                               BindingResult bindingResult) {

        TicketModel updatedTicket = ticketService.findById(ticket.getId()).orElseThrow(() -> new RuntimeException("Билет не найден"));

        if ((!Objects.equals(ticket.getCarriageNumber(), updatedTicket.getCarriageNumber()) ||
                !Objects.equals(ticket.getSeatNumber(), updatedTicket.getSeatNumber())) &&
                ticketService.existsByCarriageAndSeat(ticket.getCarriageNumber(), ticket.getSeatNumber())) {
            bindingResult.rejectValue("carriageNumber", "carriage.exists", "Это место в этом вагоне уже занято");
            bindingResult.rejectValue("seatNumber", "seat.exists", "Это место в этом вагоне уже занято");
        }

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, ticket.getId());
            return "redirect:/admin/tickets";
        }

        System.out.println(ticket.getTicketStatus().getTicketStatusId());

        updatedTicket.setSchedule(ticket.getSchedule());
        updatedTicket.setUser(ticket.getUser());
        updatedTicket.setTicketStatus(ticket.getTicketStatus());
        updatedTicket.setCarriageNumber(ticket.getCarriageNumber());
        updatedTicket.setSeatNumber(ticket.getSeatNumber());
        updatedTicket.setPrice(ticket.getPrice());

        ticketService.updateTicket(updatedTicket);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/tickets";
    }

    @PostMapping("/tickets/delete")
    public String deleteTicket(@RequestParam Long id) {
        try {
            ticketService.deleteTicket(id);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/tickets";
    }

    @GetMapping("/tickets/search")
    public String searchTickets(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("ticket")) {
            model.addAttribute("ticket", new TicketModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<TicketModel> tickets = ticketService.searchTickets(keyword, searchBy);
        int totalItems = tickets.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<TicketModel> pagedTickets = tickets.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedTickets, page, pageSize, totalItems);
        model.addAttribute("tickets", pagedTickets);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "tickets";
    }
}