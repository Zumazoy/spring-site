package web_7.Roles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Roles.model.RoleModel;
import web_7.Roles.repository.RoleRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<RoleModel> getAllRoles(int page, int size) {
        return roleRepository.findAllByOrderByRoleIdAsc(PageRequest.of(page, size));
    }

    @Override
    public void addRole(RoleModel role) {
        roleRepository.save(role);
    }

    @Override
    public void updateRole(RoleModel role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    @Override
    public Optional<RoleModel> findById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public List<RoleModel> searchRoles(String keyword, String searchBy) {
        List<RoleModel> roles;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return roleRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "roleTitle":
                roles = roleRepository.findByRoleTitle(keyword);
                break;
            default:
                roles = roleRepository.findAllByOrderByRoleIdAsc();
        }

        return roles.stream()
                .sorted(Comparator.comparing(RoleModel::getRoleId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByRoleTitle(String roleTitle) {
        return roleRepository.existsByRoleTitle(roleTitle);
    }
}