package com.fluento.features.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendAiMessageRequest(
        @NotBlank
        @Size(max = 2000)
        String message
) {
}
