package com.fluento.features.subscription.dto;

import com.fluento.features.subscription.enums.BillingInterval;

import java.math.BigDecimal;
import java.util.UUID;

public record SubscriptionPlanResponse(
        UUID id,
        String code,
        String name,
        String description,
        BigDecimal price,
        String currency,
        BillingInterval billingInterval,
        boolean premium,
        int position
) {
}
