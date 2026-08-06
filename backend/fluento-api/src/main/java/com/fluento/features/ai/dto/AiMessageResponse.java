package com.fluento.features.ai.dto;

import com.fluento.features.ai.enums.AiMessageRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record AiMessageResponse(
        UUID id,
        AiMessageRole role,
        String content,
        String provider,
        String model,
        LocalDateTime createdAt
) {
}
