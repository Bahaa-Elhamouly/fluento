package com.fluento.features.admin.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.admin.dto.AdminUpdateUserRequest;
import com.fluento.features.admin.dto.AdminUserResponse;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AdminUserResponse> getUsers() {
        return userRepository
                .findByDeletedAtIsNullOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminUserResponse getUser(UUID userId) {
        return toResponse(getActiveUser(userId));
    }

    @Transactional
    public AdminUserResponse updateUser(
            UUID userId,
            AdminUpdateUserRequest request
    ) {
        User user = getActiveUser(userId);

        if (request.firstName() != null) {
            user.setFirstName(request.firstName().trim());
        }
        if (request.lastName() != null) {
            user.setLastName(request.lastName().trim());
        }
        if (request.role() != null) {
            user.setRole(request.role());
        }
        if (request.enabled() != null) {
            user.setEnabled(request.enabled());
        }
        if (request.emailVerified() != null) {
            user.setEmailVerified(request.emailVerified());
        }

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = getActiveUser(userId);
        user.setDeletedAt(LocalDateTime.now());
        user.setEnabled(false);
        userRepository.save(user);
    }

    private User getActiveUser(UUID userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    private AdminUserResponse toResponse(User user) {
        return new AdminUserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled(),
                user.isEmailVerified(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
