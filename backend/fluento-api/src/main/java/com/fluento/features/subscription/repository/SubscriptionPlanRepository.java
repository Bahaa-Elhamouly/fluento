package com.fluento.features.subscription.repository;

import com.fluento.features.subscription.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionPlanRepository
        extends JpaRepository<SubscriptionPlan, UUID> {

    List<SubscriptionPlan> findByEnabledTrueAndDeletedAtIsNullOrderByPositionAsc();

    Optional<SubscriptionPlan> findByCodeAndEnabledTrueAndDeletedAtIsNull(
            String code
    );
}
