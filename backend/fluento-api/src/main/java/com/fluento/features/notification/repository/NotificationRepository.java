package com.fluento.features.notification.repository;

import com.fluento.features.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {

    List<Notification> findTop20ByUser_IdAndDeletedAtIsNullOrderByCreatedAtDesc(
            UUID userId
    );

    long countByUser_IdAndReadAtIsNullAndDeletedAtIsNull(UUID userId);

    Optional<Notification> findByIdAndUser_IdAndDeletedAtIsNull(
            UUID notificationId,
            UUID userId
    );
}
