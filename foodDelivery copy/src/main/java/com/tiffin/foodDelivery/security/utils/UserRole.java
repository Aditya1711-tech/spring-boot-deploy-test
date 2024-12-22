package com.tiffin.foodDelivery.security.utils;

import com.tiffin.foodDelivery.exceptions.client.RecordNotFoundException;
import com.tiffin.foodDelivery.utils.constants.MessageConstants;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tiffin.foodDelivery.security.utils.UserPermission.*;

@Getter
public enum UserRole {

    // For reference, PMS = Project Management Service

    ADMIN("Admin",Set.of(
            ADMIN_READ,             // Admin rights
            ADMIN_UPDATE,
            ADMIN_DELETE,
            ADMIN_CREATE,

            USER_READ,              // User rights
            USER_UPDATE,
            USER_DELETE,
            USER_CREATE
    )),

    PMS_MANAGER("PMS Manager", Set.of(

            USER_READ,              // User rights
            USER_UPDATE,
            USER_DELETE,
            USER_CREATE
    )),

    USER("User", Set.of(
            USER_READ,
            USER_UPDATE,
            USER_DELETE,
            USER_CREATE
    ));

    private final String label;
    private final Set<UserPermission> permissions;

    UserRole(String label, Set<UserPermission> permissions) {
        this.label = label;
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = permissions
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

    public static UserRole getByLabel(String label) {
        for (UserRole role : values()) {
            if (role.getLabel().equalsIgnoreCase(label)) {
                return role;
            }
        }
        throw new RecordNotFoundException(MessageConstants.UserError.USER_ROLE_NOT_FOUND);
    }

    // Method to get a list of permission strings
    public List<String> getPermissionStrings() {
        return this.permissions
                .stream()
                .map(UserPermission::getPermission)
                .collect(Collectors.toList());
    }
}

