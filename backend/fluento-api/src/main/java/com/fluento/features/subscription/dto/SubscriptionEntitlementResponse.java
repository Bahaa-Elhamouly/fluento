package com.fluento.features.subscription.dto;

public record SubscriptionEntitlementResponse(
        boolean premium,
        SubscriptionResponse subscription
) {
}
