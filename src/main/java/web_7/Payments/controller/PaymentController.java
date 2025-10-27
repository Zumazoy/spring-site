package web_7.Payments.controller;

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
import web_7.PaymentStatuses.service.PaymentStatusService;
import web_7.Payments.model.PaymentModel;
import web_7.Payments.service.PaymentService;
import web_7.Tickets.service.TicketService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentStatusService paymentStatusService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/payments")
    public String payments(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("payment")) {
            model.addAttribute("payment", new PaymentModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<PaymentModel> paymentPage = paymentService.getAllPayments(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, paymentPage, page, pageSize);
        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("paymentStatuses", paymentStatusService.getAllPaymentStatuses());
        model.addAttribute("tickets", ticketService.getAllTickets());

        return "payments";
    }

    @PostMapping("/payments/add")
    public String addPayment(@Valid @ModelAttribute PaymentModel payment,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        payment.setPaymentDate(LocalDateTime.now());

        if (payment.getTicket() != null) {
            if (ticketService.isTicketAssignedToAnotherPayment(payment.getTicket().getId(), null)) {
                bindingResult.rejectValue("ticket", "ticket.assigned",
                        "Этот билет уже привязан к другому платежу");
            }
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.payment", bindingResult);
            redirectAttributes.addFlashAttribute("payment", payment);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/payments";
        }

        payment.setPaymentStatus(paymentStatusService.findById(payment.getPaymentStatus().getPaymentStatusId()));

        paymentService.addPayment(payment);
        return "redirect:/admin/payments";
    }

    @PostMapping("/payments/update")
    public String updatePayment(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                                RedirectAttributes redirectAttributes,
                                @Valid @ModelAttribute PaymentModel payment,
                                BindingResult bindingResult) {

        PaymentModel updatedPayment = paymentService.findById(payment.getId())
                .orElseThrow(() -> new RuntimeException("Платеж не найден"));

        if (payment.getTicket() != null && payment.getTicket().getId() != null) {
            if (ticketService.isTicketAssignedToAnotherPayment(payment.getTicket().getId(), null)) {
                bindingResult.rejectValue("ticket", "ticket.assigned",
                        "Этот билет уже привязан к другому платежу");
            }
        }

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, payment.getId());
            return "redirect:/admin/payments";
        }

        updatedPayment.setTicket(payment.getTicket());
        updatedPayment.setPaymentStatus(payment.getPaymentStatus());
        updatedPayment.setAmount(payment.getAmount());

        paymentService.updatePayment(updatedPayment);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/payments";
    }

    @PostMapping("/payments/delete")
    public String deletePayment(@RequestParam Long id) {
        try {
            paymentService.deletePayment(id);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/payments";
    }

    @GetMapping("/payments/search")
    public String searchPayments(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("payment")) {
            model.addAttribute("payment", new PaymentModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<PaymentModel> payments = paymentService.searchPayments(keyword, searchBy);
        int totalItems = payments.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<PaymentModel> pagedPayments = payments.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedPayments, page, pageSize, totalItems);
        model.addAttribute("payments", pagedPayments);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "payments";
    }
}