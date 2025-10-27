package web_7.Roles.controller;

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
import web_7.Roles.model.RoleModel;
import web_7.Roles.service.RoleService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public String roles(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("role")) {
            model.addAttribute("role", new RoleModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<RoleModel> rolePage = roleService.getAllRoles(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, rolePage, page, pageSize);
        model.addAttribute("roles", rolePage.getContent());

        return "roles";
    }

    @PostMapping("/roles/add")
    public String addRole(@Valid @ModelAttribute("role") RoleModel role,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if (roleService.existsByRoleTitle(role.getRoleTitle())) {
            bindingResult.rejectValue("roleTitle", "roleTitle.exists", "Роль с таким названием уже существует");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.role", bindingResult);
            redirectAttributes.addFlashAttribute("role", role);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/roles";
        }

        roleService.addRole(role);
        return "redirect:/admin/roles";
    }

    @PostMapping("/roles/update")
    public String updateRole(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                             RedirectAttributes redirectAttributes,
                             @Valid @ModelAttribute("role") RoleModel role,
                             BindingResult bindingResult) {

        RoleModel updatedRole = roleService.findById(role.getRoleId()).orElseThrow(() -> new RuntimeException("Роль не найдена"));

        if (!role.getRoleTitle().equals(updatedRole.getRoleTitle()) &&
                roleService.existsByRoleTitle(role.getRoleTitle())) {
            bindingResult.rejectValue("roleTitle", "roleTitle.exists", "Роль с таким названием уже существует");
        }

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, role.getRoleId());
            return "redirect:/admin/roles";
        }

        updatedRole.setRoleTitle(role.getRoleTitle());
        roleService.updateRole(updatedRole);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/roles";
    }

    @PostMapping("/roles/delete")
    public String deleteRole(@RequestParam Long id) {
        roleService.deleteRole(id);
        return "redirect:/admin/roles";
    }

    @GetMapping("/roles/search")
    public String searchRoles(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("role")) {
            model.addAttribute("role", new RoleModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<RoleModel> roles = roleService.searchRoles(keyword, searchBy);
        int totalItems = roles.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<RoleModel> pagedRoles = roles.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedRoles, page, pageSize, totalItems);
        model.addAttribute("roles", pagedRoles);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "roles";
    }
}