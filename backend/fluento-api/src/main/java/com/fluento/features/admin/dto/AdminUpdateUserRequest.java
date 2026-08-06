package com.fluento.features.admin.dto;

import com.fluento.features.user.enums.Role;
import jakarta.validation.constraints.Size;

public record AdminUpdateUserRequest(
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        String lastName,

        Role role,

        Boolean enabled,

        Boolean emailVerified
) {
}
