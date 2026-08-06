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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.fluento.features.subscription.service.SubscriptionServiceTest.plan;
import static com.fluento.features.subscription.service.SubscriptionServiceTest.subscription;
import static com.fluento.features.subscription.service.SubscriptionServiceTest.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminSubscriptionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private AdminSubscriptionService adminSubscriptionService;

    @Test
    void activateCreatesActiveSubscriptionAndCancelsExistingOne() {
        User user = user();
        SubscriptionPlan plan = plan("pro-monthly", true);
        UserSubscription existing = subscription(user, plan);
        UserSubscription saved = subscription(user, plan);
        SubscriptionResponse mapped = new SubscriptionResponse(
                saved.getId(),
                null,
                SubscriptionStatus.ACTIVE,
                true,
                saved.getCurrentPeriodStart(),
                saved.getCurrentPeriodEnd(),
                false
        );

        when(userRepository.findByEmailIgnoreCaseAndDeletedAtIsNull(
                "sara@example.com"
        ))
                .thenReturn(Optional.of(user));
        when(subscriptionPlanRepository
                .findByCodeAndEnabledTrueAndDeletedAtIsNull("pro-monthly"))
                .thenReturn(Optional.of(plan));
        when(userSubscriptionRepository
                .findFirstByUser_IdAndStatusInAndDeletedAtIsNullOrderByCurrentPeriodEndDesc(
                        user.getId(),
                        Set.of(
                                SubscriptionStatus.TRIALING,
                                SubscriptionStatus.ACTIVE,
                                SubscriptionStatus.PAST_DUE
                        )
                ))
                .thenReturn(Optional.of(existing));
        when(userSubscriptionRepository.save(any(UserSubscription.class)))
                .thenReturn(saved);
        when(subscriptionService.toSubscriptionResponse(saved))
                .thenReturn(mapped);

        SubscriptionResponse response = adminSubscriptionService.activate(
                new AdminActivateSubscriptionRequest(
                        "sara@example.com",
                        "PRO-MONTHLY",
                        30
                )
        );

        assertThat(existing.getStatus()).isEqualTo(SubscriptionStatus.CANCELED);
        assertThat(response.premium()).isTrue();
    }

    @Test
    void cancelThrowsWhenNoActiveSubscriptionExists() {
        UUID userId = UUID.randomUUID();
        when(userSubscriptionRepository
                .findFirstByUser_IdAndStatusInAndDeletedAtIsNullOrderByCurrentPeriodEndDesc(
                        any(UUID.class),
                        any()
                ))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminSubscriptionService.cancel(userId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
