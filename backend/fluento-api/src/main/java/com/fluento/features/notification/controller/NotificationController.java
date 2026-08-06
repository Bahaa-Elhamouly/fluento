package com.fluento.features.notification.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import com.fluento.features.notification.dto.NotificationFeedResponse;
import com.fluento.features.notification.dto.NotificationResponse;
import com.fluento.features.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<NotificationFeedResponse> feed(
            @AuthenticationPrincipal FluentoUserDetails userDetails
    ) {
        return ApiResponse.success(
                "Notifications retrieved successfully",
                notificationService.getFeed(userDetails.getId())
        );
    }

    @PatchMapping("/{notificationId}/read")
    public ApiResponse<NotificationResponse> markAsRead(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @PathVariable UUID notificationId
    ) {
        return ApiResponse.success(
                "Notification marked as read",
                notificationService.markAsRead(
                        userDetails.getId(),
                        notificationId
                )
        );
    }
}
