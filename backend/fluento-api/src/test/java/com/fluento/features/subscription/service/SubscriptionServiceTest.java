package com.fluento.features.subscription.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.subscription.dto.SubscriptionEntitlementResponse;
import com.fluento.features.subscription.dto.SubscriptionPlanResponse;
import com.fluento.features.subscription.entity.SubscriptionPlan;
import com.fluento.features.subscription.entity.UserSubscription;
import com.fluento.features.subscription.enums.BillingInterval;
import com.fluento.features.subscription.enums.SubscriptionStatus;
import com.fluento.features.subscription.repository.SubscriptionPlanRepository;
import com.fluento.features.subscription.repository.UserSubscriptionRepository;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void getPlansReturnsEnabledPlans() {
        SubscriptionPlan plan = plan("pro-monthly", true);
        when(subscriptionPlanRepository
                .findByEnabledTrueAndDeletedAtIsNullOrderByPositionAsc())
                .thenReturn(List.of(plan));

        List<SubscriptionPlanResponse> response =
                subscriptionService.getPlans();

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().code()).isEqualTo("pro-monthly");
        assertThat(response.getFirst().premium()).isTrue();
    }

    @Test
    void getEntitlementReturnsPremiumForActiveSubscription() {
        User user = user();
        UserSubscription subscription = subscription(user, plan("pro", true));

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userSubscriptionRepository
                .findFirstByUser_IdAndStatusInAndDeletedAtIsNullOrderByCurrentPeriodEndDesc(
                        user.getId(),
                        Set.of(
                                SubscriptionStatus.TRIALING,
                                SubscriptionStatus.ACTIVE
                        )
                ))
                .thenReturn(Optional.of(subscription));

        SubscriptionEntitlementResponse response =
                subscriptionService.getEntitlement(user.getId());

        assertThat(response.premium()).isTrue();
        assertThat(response.subscription()).isNotNull();
    }

    @Test
    void getEntitlementReturnsFreeWhenSubscriptionIsExpired() {
        User user = user();
        UserSubscription subscription = subscription(user, plan("pro", true));
        subscription.setCurrentPeriodEnd(LocalDateTime.now().minusDays(1));

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userSubscriptionRepository
                .findFirstByUser_IdAndStatusInAndDeletedAtIsNullOrderByCurrentPeriodEndDesc(
                        user.getId(),
                        Set.of(
                                SubscriptionStatus.TRIALING,
                                SubscriptionStatus.ACTIVE
                        )
                ))
                .thenReturn(Optional.of(subscription));

        SubscriptionEntitlementResponse response =
                subscriptionService.getEntitlement(user.getId());

        assertThat(response.premium()).isFalse();
        assertThat(response.subscription()).isNull();
    }

    @Test
    void getEntitlementThrowsWhenUserIsMissing() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> subscriptionService.getEntitlement(userId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    static User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Sara");
        user.setLastName("Ahmed");
        user.setEmail("sara@example.com");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        user.setEnabled(true);
        return user;
    }

    static SubscriptionPlan plan(String code, boolean premium) {
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setId(UUID.randomUUID());
        plan.setCode(code);
        plan.setName("Pro");
        plan.setDescription("Premium plan");
        plan.setPrice(BigDecimal.valueOf(9.99));
        plan.setCurrency("USD");
        plan.setBillingInterval(BillingInterval.MONTHLY);
        plan.setPremium(premium);
        plan.setEnabled(true);
        plan.setPosition(1);
        return plan;
    }

    static UserSubscription subscription(
            User user,
            SubscriptionPlan plan
    ) {
        UserSubscription subscription = new UserSubscription();
        subscription.setId(UUID.randomUUID());
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setCurrentPeriodStart(LocalDateTime.now().minusDays(1));
        subscription.setCurrentPeriodEnd(LocalDateTime.now().plusDays(30));
        return subscription;
    }
}
