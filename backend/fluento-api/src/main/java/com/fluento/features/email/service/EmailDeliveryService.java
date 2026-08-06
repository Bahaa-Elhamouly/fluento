package com.fluento.features.email.service;

import com.fluento.features.user.entity.User;

import java.time.LocalDateTime;

public interface EmailDeliveryService {

    void sendEmailVerification(
            User user,
            String rawToken,
            LocalDateTime expiresAt
    );

    void sendPasswordReset(
            User user,
            String rawToken,
            LocalDateTime expiresAt
    );
}
