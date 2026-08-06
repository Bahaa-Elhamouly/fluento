package com.fluento.features.email.service;

import com.fluento.features.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class LoggingEmailDeliveryService implements EmailDeliveryService {

    @Override
    public void sendEmailVerification(
            User user,
            String rawToken,
            LocalDateTime expiresAt
    ) {
        log.info(
                "Email verification token issued for user={} expiresAt={}",
                user.getEmail(),
                expiresAt
        );
        log.debug("Development email verification token={}", rawToken);
    }

    @Override
    public void sendPasswordReset(
            User user,
            String rawToken,
            LocalDateTime expiresAt
    ) {
        log.info(
                "Password reset token issued for user={} expiresAt={}",
                user.getEmail(),
                expiresAt
        );
        log.debug("Development password reset token={}", rawToken);
    }
}
