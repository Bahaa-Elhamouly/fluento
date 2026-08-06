package com.fluento.features.notification.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.notification.dto.NotificationFeedResponse;
import com.fluento.features.notification.dto.NotificationResponse;
import com.fluento.features.notification.entity.Notification;
import com.fluento.features.notification.repository.NotificationRepository;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public NotificationFeedResponse getFeed(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }

        return new NotificationFeedResponse(
                notificationRepository
                        .countByUser_IdAndReadAtIsNullAndDeletedAtIsNull(
                                userId
                        ),
                notificationRepository
                        .findTop20ByUser_IdAndDeletedAtIsNullOrderByCreatedAtDesc(
                                userId
                        )
                        .stream()
                        .map(this::toResponse)
                        .toList()
        );
    }

    @Transactional
    public NotificationResponse markAsRead(
            UUID userId,
            UUID notificationId
    ) {
        Notification notification = notificationRepository
                .findByIdAndUser_IdAndDeletedAtIsNull(notificationId, userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Notification",
                                notificationId
                        )
                );

        if (notification.getReadAt() == null) {
            notification.setReadAt(LocalDateTime.now());
            notification = notificationRepository.save(notification);
        }

        return toResponse(notification);
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getActionUrl(),
                notification.getReadAt() != null,
                notification.getReadAt(),
                notification.getCreatedAt()
        );
    }
}
