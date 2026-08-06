package com.fluento.features.notification.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.notification.dto.NotificationFeedResponse;
import com.fluento.features.notification.dto.NotificationResponse;
import com.fluento.features.notification.entity.Notification;
import com.fluento.features.notification.enums.NotificationType;
import com.fluento.features.notification.repository.NotificationRepository;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void getFeedReturnsUnreadCountAndNotifications() {
        User user = user();
        Notification notification = notification(user);

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(notificationRepository
                .countByUser_IdAndReadAtIsNullAndDeletedAtIsNull(user.getId()))
                .thenReturn(1L);
        when(notificationRepository
                .findTop20ByUser_IdAndDeletedAtIsNullOrderByCreatedAtDesc(
                        user.getId()
                ))
                .thenReturn(List.of(notification));

        NotificationFeedResponse response =
                notificationService.getFeed(user.getId());

        assertThat(response.unreadCount()).isEqualTo(1);
        assertThat(response.notifications()).hasSize(1);
        assertThat(response.notifications().getFirst().read()).isFalse();
    }

    @Test
    void getFeedThrowsWhenUserIsMissing() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> notificationService.getFeed(userId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void markAsReadSetsReadTimestamp() {
        User user = user();
        Notification notification = notification(user);

        when(notificationRepository.findByIdAndUser_IdAndDeletedAtIsNull(
                notification.getId(),
                user.getId()
        ))
                .thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        NotificationResponse response = notificationService.markAsRead(
                user.getId(),
                notification.getId()
        );

        assertThat(response.read()).isTrue();
        assertThat(response.readAt()).isNotNull();
        verify(notificationRepository).save(notification);
    }

    @Test
    void markAsReadDoesNotRewriteAlreadyReadNotification() {
        User user = user();
        Notification notification = notification(user);
        notification.setReadAt(LocalDateTime.now().minusDays(1));

        when(notificationRepository.findByIdAndUser_IdAndDeletedAtIsNull(
                notification.getId(),
                user.getId()
        ))
                .thenReturn(Optional.of(notification));

        NotificationResponse response = notificationService.markAsRead(
                user.getId(),
                notification.getId()
        );

        assertThat(response.read()).isTrue();
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void markAsReadThrowsWhenNotificationIsMissing() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();

        when(notificationRepository.findByIdAndUser_IdAndDeletedAtIsNull(
                notificationId,
                userId
        ))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> notificationService.markAsRead(userId, notificationId)
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Notification notification(User user) {
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setUser(user);
        notification.setType(NotificationType.DAILY_REMINDER);
        notification.setTitle("Daily practice");
        notification.setMessage("Complete one lesson today.");
        notification.setActionUrl("/dashboard");
        return notification;
    }

    private User user() {
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
}
