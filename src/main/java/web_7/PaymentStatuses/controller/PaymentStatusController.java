package web_7.PaymentStatuses.controller;

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
import web_7.PaymentStatuses.model.PaymentStatusModel;
import web_7.PaymentStatuses.service.PaymentStatusService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class PaymentStatusController {
    @Autowired
    private PaymentStatusService paymentStatusService;

    @GetMapping("/payment-statuses")
    public String paymentStatuses(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("paymentStatus")) {
            model.addAttribute("paymentStatus", new PaymentStatusModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<PaymentStatusModel> paymentStatusPage = paymentStatusService.getAllPaymentStatuses(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, paymentStatusPage, page, pageSize);
        model.addAttribute("paymentStatuses", paymentStatusPage.getContent());

        return "payment_statuses";
    }

    @PostMapping("/payment-statuses/add")
    public String addPaymentStatus(@Valid @ModelAttribute("paymentStatus") PaymentStatusModel paymentStatus,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {

        if (paymentStatusService.existsByPaymentStatusTitle(paymentStatus.getPaymentStatusTitle())) {
            bindingResult.rejectValue("paymentStatusTitle", "title.exists", "Статус платежа с таким названием уже существует");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.paymentStatus", bindingResult);
            redirectAttributes.addFlashAttribute("paymentStatus", paymentStatus);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/payment-statuses";
        }

        paymentStatusService.addPaymentStatus(paymentStatus);
        return "redirect:/admin/payment-statuses";
    }

    @PostMapping("/payment-statuses/update")
    public String updatePaymentStatus(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                                      RedirectAttributes redirectAttributes,
                                      @Valid @ModelAttribute("paymentStatus") PaymentStatusModel paymentStatus,
                                      BindingResult bindingResult) {

        if (paymentStatusService.existsByPaymentStatusTitle(paymentStatus.getPaymentStatusTitle())) {
            bindingResult.rejectValue("paymentStatusTitle", "title.exists", "Статус платежа с таким названием уже существует");
        }

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, paymentStatus.getPaymentStatusId());
            return "redirect:/admin/payment-statuses";
        }

        paymentStatusService.updatePaymentStatus(paymentStatus);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/payment-statuses";
    }

    @PostMapping("/payment-statuses/delete")
    public String deletePaymentStatus(@RequestParam Long id) {
        try {
            paymentStatusService.deletePaymentStatus(id);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/payment-statuses";
    }

    @GetMapping("/payment-statuses/search")
    public String searchPaymentStatuses(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("paymentStatus")) {
            model.addAttribute("paymentStatus", new PaymentStatusModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<PaymentStatusModel> paymentStatuses = paymentStatusService.searchPaymentStatuses(keyword, searchBy);
        int totalItems = paymentStatuses.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<PaymentStatusModel> pagedPaymentStatuses = paymentStatuses.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedPaymentStatuses, page, pageSize, totalItems);
        model.addAttribute("paymentStatuses", pagedPaymentStatuses);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "payment_statuses";
    }
}