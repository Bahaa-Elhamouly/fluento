package com.fluento.features.subscription.dto;

import com.fluento.features.subscription.enums.SubscriptionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        SubscriptionPlanResponse plan,
        SubscriptionStatus status,
        boolean premium,
        LocalDateTime currentPeriodStart,
        LocalDateTime currentPeriodEnd,
        boolean cancelAtPeriodEnd
) {
}
