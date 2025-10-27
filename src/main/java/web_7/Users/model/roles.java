package web_7.Users.model;

import org.springframework.security.core.GrantedAuthority;

public enum roles implements GrantedAuthority {
    USER, ADMIN, SCHEDULE_CONTROLLER;

    @Override
    public String getAuthority() {
        return name();
    }
}
