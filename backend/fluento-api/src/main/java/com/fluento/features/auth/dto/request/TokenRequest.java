package com.fluento.features.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank
        String token
) {
}
