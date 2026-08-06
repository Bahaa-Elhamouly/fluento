package com.fluento.features.notification.dto;

import com.fluento.features.notification.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        NotificationType type,
        String title,
        String message,
        String actionUrl,
        boolean read,
        LocalDateTime readAt,
        LocalDateTime createdAt
) {
}
