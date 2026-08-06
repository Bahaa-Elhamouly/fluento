package com.fluento.features.admin.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.admin.dto.AdminUpdateUserRequest;
import com.fluento.features.admin.dto.AdminUserResponse;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    @Test
    void getUsersReturnsActiveUsers() {
        User user = user(Role.USER);
        when(userRepository.findByDeletedAtIsNullOrderByCreatedAtDesc())
                .thenReturn(List.of(user));

        List<AdminUserResponse> response = adminUserService.getUsers();

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().email()).isEqualTo("sara@example.com");
    }

    @Test
    void updateUserAppliesAdminChanges() {
        User user = user(Role.USER);
        when(userRepository.findByIdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        AdminUserResponse response = adminUserService.updateUser(
                user.getId(),
                new AdminUpdateUserRequest(
                        " Admin ",
                        " User ",
                        Role.ADMIN,
                        false,
                        true
                )
        );

        assertThat(response.firstName()).isEqualTo("Admin");
        assertThat(response.lastName()).isEqualTo("User");
        assertThat(response.role()).isEqualTo(Role.ADMIN);
        assertThat(response.enabled()).isFalse();
        assertThat(response.emailVerified()).isTrue();
    }

    @Test
    void deleteUserSoftDeletesAndDisablesUser() {
        User user = user(Role.USER);
        when(userRepository.findByIdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.of(user));

        adminUserService.deleteUser(user.getId());

        assertThat(user.getDeletedAt()).isNotNull();
        assertThat(user.isEnabled()).isFalse();
        verify(userRepository).save(user);
    }

    @Test
    void getUserThrowsWhenMissing() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findByIdAndDeletedAtIsNull(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminUserService.getUser(userId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private User user(Role role) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Sara");
        user.setLastName("Ahmed");
        user.setEmail("sara@example.com");
        user.setPasswordHash("hash");
        user.setRole(role);
        user.setEnabled(true);
        user.setEmailVerified(false);
        return user;
    }
}
