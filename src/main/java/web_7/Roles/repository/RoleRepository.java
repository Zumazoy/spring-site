package web_7.Roles.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Roles.model.RoleModel;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    List<RoleModel> findByRoleTitle(String roleTitle);
    List<RoleModel> findAllByOrderByRoleIdAsc();
    Page<RoleModel> findAllByOrderByRoleIdAsc(Pageable pageable);

    boolean existsByRoleTitle(String roleTitle);
}