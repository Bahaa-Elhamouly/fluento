package com.fluento.features.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAiSessionRequest(
        @NotBlank
        @Size(max = 10)
        String languageCode,

        @Size(max = 180)
        String title
) {
}
