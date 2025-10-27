package web_7.Roles.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "roles")
public class RoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Size(min = 3, max = 30, message = "Название роли должно быть от 3 до 30 символов")
    @NotNull(message = "Название роли не может быть null")
    @NotBlank(message = "Название роли не может быть пустым")
    @Column(unique = true)
    private String roleTitle;

    public RoleModel() {}

    public RoleModel(Long roleId, String roleTitle) {
        this.roleId = roleId;
        this.roleTitle = roleTitle;
    }

    public Long getRoleId() {
        return roleId;
    }
    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public void setRoleTitle(String titleRole) {
        this.roleTitle = titleRole;
    }
}
