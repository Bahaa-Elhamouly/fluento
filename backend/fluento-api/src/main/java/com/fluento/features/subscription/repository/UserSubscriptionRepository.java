package com.fluento.features.subscription.repository;

import com.fluento.features.subscription.entity.UserSubscription;
import com.fluento.features.subscription.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserSubscriptionRepository
        extends JpaRepository<UserSubscription, UUID> {

    @EntityGraph(attributePaths = "plan")
    Optional<UserSubscription> findFirstByUser_IdAndStatusInAndDeletedAtIsNullOrderByCurrentPeriodEndDesc(
            UUID userId,
            Collection<SubscriptionStatus> statuses
    );

    long countByStatusInAndDeletedAtIsNull(Collection<SubscriptionStatus> statuses);
}
