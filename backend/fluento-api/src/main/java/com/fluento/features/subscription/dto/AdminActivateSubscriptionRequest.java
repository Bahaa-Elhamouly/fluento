package com.fluento.features.subscription.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminActivateSubscriptionRequest(
        @NotBlank
        @Email
        String userEmail,

        @NotBlank
        String planCode,

        @NotNull
        @Min(1)
        Integer durationDays
) {
}
