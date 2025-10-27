package web_7;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web_7.Users.model.UserModel;
import web_7.Users.model.roles;
import web_7.Users.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String mainPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean is_admin = auth.getAuthorities().stream().anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals("ADMIN") || grantedAuthority.getAuthority().equals("SCHEDULE_CONTROLLER"));
        model.addAttribute("isAuthenticated", auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken));
        model.addAttribute("isAdmin", is_admin);
        model.addAttribute("login", auth.getName());

        return "home";
    }

    @GetMapping("/login")
    public String loginPage(Model model, @ModelAttribute("loginError") Map<String, String> loginError) {
        if (loginError != null && !loginError.isEmpty()) {
            model.addAttribute("loginError", loginError);
        }

        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError",
                Collections.singletonMap("authError", "Неверный логин или пароль"));
        return "login";
    }

    @GetMapping("/registration")
    private String regPage(Model model, @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        return "registration";
    }

    @PostMapping("/registration")
    private String registration(@Valid @ModelAttribute UserModel user,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        user.setRegistrationDate(LocalDateTime.now());

        if (user.getEmail() != null && user.getEmail().isEmpty()) {
            user.setEmail(null);
        }

        if (userService.existsByLogin(user.getLogin())) {
            bindingResult.rejectValue("login", "login.exists", "Логин уже занят");
        }
        if (user.getEmail() != null && userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "Email уже занят");
        }
        if (userService.existsByPhoneNumber(user.getPhoneNumber())) {
            bindingResult.rejectValue("phoneNumber", "phone.exists", "Номер телефона уже занят");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(roles.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return "redirect:/login";
    }

    @GetMapping("/secret-btn-adm-add")
    public String makeMeAdmin() {
        return getCustomRole(roles.ADMIN, "ADMIN");
    }
    @GetMapping("/secret-btn-sch-add")
    public String makeMeSchedule() {
        return getCustomRole(roles.SCHEDULE_CONTROLLER, "SCHEDULE_CONTROLLER");
    }

    private String getCustomRole(roles role, String title_role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return "/";
        }

        String username = auth.getName();
        try {
            UserModel user = userService.findByLogin(username);
            if (user.getRoles().contains(role)) {
                System.out.println("У пользователя " + username + " уже есть роль " + title_role);
                return "redirect:/";
            }

            Set<roles> new_roles = new HashSet<>(user.getRoles());
            new_roles.add(role);
            user.setRoles(new_roles);
            userService.updateUser(user);
            System.out.println("Роль " + title_role + " успешно добавлена пользователю " + username);
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении роли " + title_role + e.getMessage());
        }

        return "redirect:/";
    }
}
