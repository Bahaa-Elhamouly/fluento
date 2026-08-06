package com.fluento.features.subscription.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.subscription.dto.SubscriptionEntitlementResponse;
import com.fluento.features.subscription.dto.SubscriptionPlanResponse;
import com.fluento.features.subscription.dto.SubscriptionResponse;
import com.fluento.features.subscription.entity.SubscriptionPlan;
import com.fluento.features.subscription.entity.UserSubscription;
import com.fluento.features.subscription.enums.SubscriptionStatus;
import com.fluento.features.subscription.repository.SubscriptionPlanRepository;
import com.fluento.features.subscription.repository.UserSubscriptionRepository;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private static final Set<SubscriptionStatus> ENTITLED_STATUSES = Set.of(
            SubscriptionStatus.TRIALING,
            SubscriptionStatus.ACTIVE
    );

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> getPlans() {
        return subscriptionPlanRepository
                .findByEnabledTrueAndDeletedAtIsNullOrderByPositionAsc()
                .stream()
                .map(this::toPlanResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubscriptionEntitlementResponse getEntitlement(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }

        UserSubscription subscription = userSubscriptionRepository
                .findFirstByUser_IdAndStatusInAndDeletedAtIsNullOrderByCurrentPeriodEndDesc(
                        userId,
                        ENTITLED_STATUSES
                )
                .filter(this::isCurrentlyEntitled)
                .orElse(null);

        return new SubscriptionEntitlementResponse(
                subscription != null && subscription.getPlan().isPremium(),
                subscription == null ? null : toSubscriptionResponse(subscription)
        );
    }

    public SubscriptionPlanResponse toPlanResponse(SubscriptionPlan plan) {
        return new SubscriptionPlanResponse(
                plan.getId(),
                plan.getCode(),
                plan.getName(),
                plan.getDescription(),
                plan.getPrice(),
                plan.getCurrency(),
                plan.getBillingInterval(),
                plan.isPremium(),
                plan.getPosition()
        );
    }

    public SubscriptionResponse toSubscriptionResponse(
            UserSubscription subscription
    ) {
        return new SubscriptionResponse(
                subscription.getId(),
                toPlanResponse(subscription.getPlan()),
                subscription.getStatus(),
                subscription.getPlan().isPremium()
                        && isCurrentlyEntitled(subscription),
                subscription.getCurrentPeriodStart(),
                subscription.getCurrentPeriodEnd(),
                subscription.isCancelAtPeriodEnd()
        );
    }

    private boolean isCurrentlyEntitled(UserSubscription subscription) {
        return ENTITLED_STATUSES.contains(subscription.getStatus())
                && subscription.getCurrentPeriodEnd()
                .isAfter(LocalDateTime.now());
    }
}
