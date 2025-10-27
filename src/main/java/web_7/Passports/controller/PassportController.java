package web_7.Passports.controller;

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
import web_7.Passports.model.PassportModel;
import web_7.Passports.service.PassportService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class PassportController {
    @Autowired
    private PassportService passportService;

    @GetMapping("/passports")
    public String passports(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("passport")) {
            model.addAttribute("passport", new PassportModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<PassportModel> passportPage = passportService.getAllPassports(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, passportPage, page, pageSize);
        model.addAttribute("passports", passportPage.getContent());

        return "passports";
    }

    @PostMapping("/passports/add")
    public String addPassport(@Valid @ModelAttribute("passport") PassportModel passport,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (passportService.existsBySerialAndNumber(passport.getSerial(), passport.getNumber())) {
            bindingResult.rejectValue("serial", "serial.exists", "Паспорт с такой серией и номером уже существует");
            bindingResult.rejectValue("number", "number.exists", "Паспорт с такой серией и номером уже существует");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passport", bindingResult);
            redirectAttributes.addFlashAttribute("passport", passport);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/passports";
        }

        passportService.addPassport(passport);
        return "redirect:/admin/passports";
    }

    @PostMapping("/passports/update")
    public String updatePassport(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                                 RedirectAttributes redirectAttributes,
                                 @Valid @ModelAttribute("passport") PassportModel passport,
                                 BindingResult bindingResult) {

        PassportModel updatedPassport = passportService.findById(passport.getId())
                .orElseThrow(() -> new RuntimeException("Паспорт не найден"));

        if ((!Objects.equals(passport.getSerial(), updatedPassport.getSerial()) ||
                !Objects.equals(passport.getNumber(), updatedPassport.getNumber())) &&
                passportService.existsBySerialAndNumber(passport.getSerial(), passport.getNumber())) {
            bindingResult.rejectValue("serial", "serial.exists", "Паспорт с такой серией и номером уже существует");
            bindingResult.rejectValue("number", "number.exists", "Паспорт с такой серией и номером уже существует");
        }

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, passport.getId());
            return "redirect:/admin/passports";
        }

        updatedPassport.setSerial(passport.getSerial());
        updatedPassport.setNumber(passport.getNumber());
        updatedPassport.setDatePassport(passport.getDatePassport());

        passportService.updatePassport(updatedPassport);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/passports";
    }

    @PostMapping("/passports/delete")
    public String deletePassport(@RequestParam Long id, RedirectAttributes redirectAttributes) {

        PassportModel passport = passportService.findById(id)
                .orElseThrow(() -> new RuntimeException("Паспорт не найден"));

        if (passport.getUser() != null) {
            redirectAttributes.addFlashAttribute("deleteErrors", "Паспорт нельзя удалить т.к. его использует пользователь");
            return "redirect:/admin/passports";
        }

        passportService.deletePassport(id);
        return "redirect:/admin/passports";
    }

    @GetMapping("/passports/search")
    public String searchPassports(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("passport")) {
            model.addAttribute("passport", new PassportModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<PassportModel> passports = passportService.searchPassports(keyword, searchBy);
        int totalItems = passports.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<PassportModel> pagedPassports = passports.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedPassports, page, pageSize, totalItems);
        model.addAttribute("passports", pagedPassports);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "passports";
    }
}