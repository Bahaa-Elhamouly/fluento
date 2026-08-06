package com.fluento.features.auth.dto.response;

import com.fluento.features.user.enums.Role;

import java.util.UUID;

public record UserSummaryResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Role role,
        boolean emailVerified
) {
}