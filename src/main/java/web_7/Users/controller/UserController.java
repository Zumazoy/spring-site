package web_7.Users.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web_7.PaginationValidUtils;
import web_7.Passports.model.PassportModel;
import web_7.Passports.service.PassportService;
import web_7.Users.model.UserModel;
import web_7.Users.model.roles;
import web_7.Users.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PassportService passportService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public String users(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<UserModel> userPage = userService.getAllUsers(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, userPage, page, pageSize);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("passports", passportService.getAllPassports());
        model.addAttribute("roles", roles.values());

        return "users";
    }

    @PostMapping("/users/add")
    public String addUser(@Valid @ModelAttribute UserModel user,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        user.setRegistrationDate(LocalDateTime.now());

        if (user.getPassport() != null && user.getPassport().getId() != null) {
            if (passportService.isPassportAssignedToAnotherUser(user.getPassport().getId(), null)) {
                bindingResult.rejectValue("passport", "passport.assigned",
                        "Этот паспорт уже привязан к другому пользователю");
            }
        }

        if (userService.existsByLogin(user.getLogin())) {
            bindingResult.rejectValue("login", "login.exists", "Логин уже занят");
        }
        if (!user.getEmail().isEmpty() && user.getEmail() != null && userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "Email уже занят");
        }
        if (userService.existsByPhoneNumber(user.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "phone.exists", "Номер телефона уже занят");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/users";
        }

        if (user.getEmail() != null && user.getEmail().isEmpty()) {
            user.setEmail(null);
        }

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                             RedirectAttributes redirectAttributes,
                             @Valid @ModelAttribute UserModel user,
                             BindingResult bindingResult) {

        UserModel updatedUser = userService.findById(user.getId()).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (user.getPassport() != null && user.getPassport().getId() != null) {
            if (passportService.isPassportAssignedToAnotherUser(
                    user.getPassport().getId(), user.getId())) {
                bindingResult.rejectValue("passport", "passport.assigned",
                        "Этот паспорт уже привязан к другому пользователю");
            } else {
                PassportModel passport = passportService.findById(user.getPassport().getId())
                        .orElseThrow(() -> new RuntimeException("Паспорт не найден"));
                updatedUser.setPassport(passport);
            }
        } else {
            updatedUser.setPassport(null);
        }

        if (!user.getLogin().equals(updatedUser.getLogin()) && userService.existsByLogin(user.getLogin())) {
            bindingResult.rejectValue("login", "login.exists", "Логин уже занят");
        }
        if (!user.getEmail().isEmpty() && user.getEmail() != null && !user.getEmail().equals(updatedUser.getEmail()) && userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "Email уже занят");
        }
        if (!user.getPhoneNumber().equals(updatedUser.getPhoneNumber()) &&
                userService.existsByPhoneNumber(user.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "phone.exists", "Номер телефона уже занят");
        }

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, user.getId());
            return "redirect:/admin/users";
        }

        if (user.getEmail().isEmpty()) updatedUser.setEmail(null);
        updatedUser.setName(user.getName());
        updatedUser.setSurname(user.getSurname());
        updatedUser.setMiddleName(user.getMiddleName());
        updatedUser.setLogin(user.getLogin());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setPhoneNumber(user.getPhoneNumber());
        updatedUser.setRoles(user.getRoles());

        userService.updateUser(updatedUser);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            // Обработка, что его нельзя удалить
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/search")
    public String searchUsers(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<UserModel> users = userService.searchUsers(keyword, searchBy);
        int totalItems = users.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<UserModel> pagedUsers = users.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedUsers, page, pageSize, totalItems);
        model.addAttribute("users", pagedUsers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);
        model.addAttribute("passports", passportService.getAllPassports());
        model.addAttribute("roles", roles.values());

        return "users";
    }
}