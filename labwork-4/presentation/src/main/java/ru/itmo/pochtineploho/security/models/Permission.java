package ru.itmo.pochtineploho.security.models;

import lombok.Getter;

@Getter
public enum Permission {
    CAT_CREATE("cat:create"),
    CAT_UPDATE("cat:update"),
    CAT_READ("cat:read"),
    CAT_DELETE("cat:delete"),
    CAT_CREATE_ALL("cat:create_all"),
    CAT_READ_ALL("cat:read_all"),
    CAT_UPDATE_ALL("cat:update_all"),
    CAT_DELETE_ALL("cat:delete_all"),
    OWNER_CREATE("owner:create"),
    OWNER_READ("owner:read"),
    OWNER_UPDATE("owner:update"),
    OWNER_DELETE("owner:delete");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

}
