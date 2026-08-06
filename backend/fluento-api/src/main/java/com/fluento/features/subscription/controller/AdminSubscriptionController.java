package com.fluento.features.subscription.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.subscription.dto.AdminActivateSubscriptionRequest;
import com.fluento.features.subscription.dto.SubscriptionResponse;
import com.fluento.features.subscription.service.AdminSubscriptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/subscriptions")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AdminSubscriptionController {

    private final AdminSubscriptionService adminSubscriptionService;

    @PostMapping("/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SubscriptionResponse> activate(
            @Valid @RequestBody AdminActivateSubscriptionRequest request
    ) {
        return ApiResponse.success(
                "Subscription activated successfully",
                adminSubscriptionService.activate(request)
        );
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SubscriptionResponse> cancel(
            @PathVariable UUID userId
    ) {
        return ApiResponse.success(
                "Subscription canceled successfully",
                adminSubscriptionService.cancel(userId)
        );
    }
}
