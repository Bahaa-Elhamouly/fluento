package com.fluento.features.subscription.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.subscription.dto.AdminActivateSubscriptionRequest;
import com.fluento.features.subscription.dto.SubscriptionResponse;
import com.fluento.features.subscription.entity.SubscriptionPlan;
import com.fluento.features.subscription.entity.UserSubscription;
import com.fluento.features.subscription.enums.SubscriptionStatus;
import com.fluento.features.subscription.repository.SubscriptionPlanRepository;
import com.fluento.features.subscription.repository.UserSubscriptionRepository;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminSubscriptionService {

    private static final Set<SubscriptionStatus> ACTIVE_STATUSES = Set.of(
            SubscriptionStatus.TRIALING,
            SubscriptionStatus.ACTIVE,
            SubscriptionStatus.PAST_DUE
    );

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionService subscriptionService;

    @Transactional
    public SubscriptionResponse activate(
            AdminActivateSubscriptionRequest request
    ) {
        User user = userRepository
                .findByEmailIgnoreCaseAndDeletedAtIsNull(request.userEmail())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User",
                                request.userEmail()
                        )
                );
        SubscriptionPlan plan = subscriptionPlanRepository
                .findByCodeAndEnabledTrueAndDeletedAtIsNull(
                        request.planCode().trim().toLowerCase()
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Subscription plan",
                                request.planCode()
                        )
                );

        userSubscriptionRepository
                .findFirstByUser_IdAndStatusInAndDeletedAtIsNullOrderByCurrentPeriodEndDesc(
                        user.getId(),
                        ACTIVE_STATUSES
                )
                .ifPresent(existing -> {
                    existing.setStatus(SubscriptionStatus.CANCELED);
                    existing.setCancelAtPeriodEnd(false);
                    userSubscriptionRepository.save(existing);
                });

        LocalDateTime now = LocalDateTime.now();
        UserSubscription subscription = new UserSubscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setCurrentPeriodStart(now);
        subscription.setCurrentPeriodEnd(now.plusDays(request.durationDays()));
        subscription.setCancelAtPeriodEnd(false);

        return subscriptionService.toSubscriptionResponse(
                userSubscriptionRepository.save(subscription)
        );
    }

    @Transactional
    public SubscriptionResponse cancel(UUID userId) {
        UserSubscription subscription = userSubscriptionRepository
                .findFirstByUser_IdAndStatusInAndDeletedAtIsNullOrderByCurrentPeriodEndDesc(
                        userId,
                        ACTIVE_STATUSES
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Active subscription",
                                userId
                        )
                );

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscription.setCancelAtPeriodEnd(false);

        return subscriptionService.toSubscriptionResponse(
                userSubscriptionRepository.save(subscription)
        );
    }
}
