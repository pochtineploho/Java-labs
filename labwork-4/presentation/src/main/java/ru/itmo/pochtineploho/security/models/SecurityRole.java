package ru.itmo.pochtineploho.security.models;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.itmo.pochtineploho.models.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum SecurityRole {
    USER(Set.of(
            Permission.CAT_CREATE,
            Permission.CAT_DELETE,
            Permission.CAT_UPDATE,
            Permission.CAT_READ)
    ),

    ADMIN(Set.of(
            Permission.CAT_CREATE_ALL,
            Permission.CAT_DELETE_ALL,
            Permission.CAT_UPDATE_ALL,
            Permission.CAT_READ_ALL,
            Permission.OWNER_CREATE,
            Permission.OWNER_DELETE,
            Permission.OWNER_UPDATE,
            Permission.OWNER_READ));

    static SecurityRole roleOf(Role role) {
        return switch (role) {
            case USER -> SecurityRole.USER;
            case ADMIN -> SecurityRole.ADMIN;
        };
    }

    private final Set<Permission> permissions;

    SecurityRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
