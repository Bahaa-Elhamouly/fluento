package com.fluento.features.ai.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AiSessionResponse(
        UUID id,
        String languageCode,
        String title,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<AiMessageResponse> messages
) {
}
