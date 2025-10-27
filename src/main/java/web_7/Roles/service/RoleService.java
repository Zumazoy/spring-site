package web_7.Roles.service;

import org.springframework.data.domain.Page;
import web_7.Roles.model.RoleModel;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Page<RoleModel> getAllRoles(int page, int size);
    void addRole(RoleModel role);
    void updateRole(RoleModel role);
    void deleteRole(Long roleId);
    Optional<RoleModel> findById(Long roleId);
    List<RoleModel> searchRoles(String keyword, String searchBy);

    boolean existsByRoleTitle(String roleTitle);
}