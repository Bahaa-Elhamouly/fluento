package com.fluento.features.subscription.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import com.fluento.features.subscription.dto.SubscriptionEntitlementResponse;
import com.fluento.features.subscription.dto.SubscriptionPlanResponse;
import com.fluento.features.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/plans")
    public ApiResponse<List<SubscriptionPlanResponse>> plans() {
        return ApiResponse.success(
                "Subscription plans retrieved successfully",
                subscriptionService.getPlans()
        );
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ApiResponse<SubscriptionEntitlementResponse> mySubscription(
            @AuthenticationPrincipal FluentoUserDetails userDetails
    ) {
        return ApiResponse.success(
                "Subscription entitlement retrieved successfully",
                subscriptionService.getEntitlement(userDetails.getId())
        );
    }
}
