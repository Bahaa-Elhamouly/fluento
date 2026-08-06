package com.fluento.features.auth.dto.response;

import java.time.LocalDateTime;

public record TokenIssuedResponse(
        String token,
        LocalDateTime expiresAt
) {
}
