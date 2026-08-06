package com.fluento.features.admin.dto;

import com.fluento.features.user.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminUserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Role role,
        boolean enabled,
        boolean emailVerified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
