package com.fluento.features.auth.service;

import com.fluento.common.enums.ErrorCode;
import com.fluento.common.exception.BusinessException;
import com.fluento.features.auth.dto.response.TokenIssuedResponse;
import com.fluento.features.auth.entity.EmailVerificationToken;
import com.fluento.features.auth.entity.PasswordResetToken;
import com.fluento.features.auth.repository.EmailVerificationTokenRepository;
import com.fluento.features.auth.repository.PasswordResetTokenRepository;
import com.fluento.features.email.service.EmailDeliveryService;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthRecoveryService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureTokenService secureTokenService;
    private final EmailDeliveryService emailDeliveryService;

    @Value("${app.auth.email-verification-expiration-minutes}")
    private long emailVerificationExpirationMinutes;

    @Value("${app.auth.password-reset-expiration-minutes}")
    private long passwordResetExpirationMinutes;

    @Transactional
    public TokenIssuedResponse issueEmailVerificationToken(String email) {
        User user = userRepository
                .findByEmailIgnoreCaseAndDeletedAtIsNull(normalizeEmail(email))
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_AUTH_TOKEN,
                        "Unable to issue email verification token"
                ));

        String rawToken = secureTokenService.generateRawToken();
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusMinutes(emailVerificationExpirationMinutes);

        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);
        token.setTokenHash(secureTokenService.hash(rawToken));
        token.setExpiresAt(expiresAt);
        emailVerificationTokenRepository.save(token);
        emailDeliveryService.sendEmailVerification(user, rawToken, expiresAt);

        return new TokenIssuedResponse(rawToken, expiresAt);
    }

    @Transactional
    public void verifyEmail(String rawToken) {
        EmailVerificationToken token = emailVerificationTokenRepository
                .findByTokenHashAndUsedAtIsNullAndDeletedAtIsNull(
                        secureTokenService.hash(rawToken)
                )
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_AUTH_TOKEN
                ));

        LocalDateTime now = LocalDateTime.now();
        if (token.isExpired(now)) {
            throw new BusinessException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        User user = token.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        token.setUsedAt(now);
        emailVerificationTokenRepository.save(token);
    }

    @Transactional
    public TokenIssuedResponse issuePasswordResetToken(String email) {
        return userRepository
                .findByEmailIgnoreCaseAndDeletedAtIsNull(normalizeEmail(email))
                .map(this::createPasswordResetToken)
                .orElse(null);
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        PasswordResetToken token = passwordResetTokenRepository
                .findByTokenHashAndUsedAtIsNullAndDeletedAtIsNull(
                        secureTokenService.hash(rawToken)
                )
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_AUTH_TOKEN
                ));

        LocalDateTime now = LocalDateTime.now();
        if (token.isExpired(now)) {
            throw new BusinessException(ErrorCode.INVALID_AUTH_TOKEN);
        }

        User user = token.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        token.setUsedAt(now);
        passwordResetTokenRepository.save(token);
    }

    private TokenIssuedResponse createPasswordResetToken(User user) {
        String rawToken = secureTokenService.generateRawToken();
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusMinutes(passwordResetExpirationMinutes);

        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setTokenHash(secureTokenService.hash(rawToken));
        token.setExpiresAt(expiresAt);
        passwordResetTokenRepository.save(token);
        emailDeliveryService.sendPasswordReset(user, rawToken, expiresAt);

        return new TokenIssuedResponse(rawToken, expiresAt);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
