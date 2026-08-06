package com.fluento.features.notification.dto;

import java.util.List;

public record NotificationFeedResponse(
        long unreadCount,
        List<NotificationResponse> notifications
) {
}
