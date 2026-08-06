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
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthRecoveryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecureTokenService secureTokenService;

    @Mock
    private EmailDeliveryService emailDeliveryService;

    @InjectMocks
    private AuthRecoveryService authRecoveryService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                authRecoveryService,
                "emailVerificationExpirationMinutes",
                1440L
        );
        ReflectionTestUtils.setField(
                authRecoveryService,
                "passwordResetExpirationMinutes",
                30L
        );
    }

    @Test
    void issueEmailVerificationTokenStoresHashAndReturnsRawToken() {
        User user = user();
        when(userRepository.findByEmailIgnoreCaseAndDeletedAtIsNull(
                "sara@example.com"
        ))
                .thenReturn(Optional.of(user));
        when(secureTokenService.generateRawToken()).thenReturn("raw-token");
        when(secureTokenService.hash("raw-token")).thenReturn("hash");

        TokenIssuedResponse response =
                authRecoveryService.issueEmailVerificationToken(
                        " SARA@example.com "
                );

        assertThat(response.token()).isEqualTo("raw-token");
        assertThat(response.expiresAt()).isAfter(LocalDateTime.now());
        verify(emailVerificationTokenRepository)
                .save(any(EmailVerificationToken.class));
        verify(emailDeliveryService)
                .sendEmailVerification(
                        any(User.class),
                        any(String.class),
                        any(LocalDateTime.class)
                );
    }

    @Test
    void verifyEmailMarksUserVerifiedAndTokenUsed() {
        User user = user();
        EmailVerificationToken token = emailToken(user);
        when(secureTokenService.hash("raw-token")).thenReturn("hash");
        when(emailVerificationTokenRepository
                .findByTokenHashAndUsedAtIsNullAndDeletedAtIsNull("hash"))
                .thenReturn(Optional.of(token));

        authRecoveryService.verifyEmail("raw-token");

        assertThat(user.isEmailVerified()).isTrue();
        assertThat(token.getUsedAt()).isNotNull();
        verify(userRepository).save(user);
        verify(emailVerificationTokenRepository).save(token);
    }

    @Test
    void verifyEmailRejectsExpiredToken() {
        EmailVerificationToken token = emailToken(user());
        token.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        when(secureTokenService.hash("raw-token")).thenReturn("hash");
        when(emailVerificationTokenRepository
                .findByTokenHashAndUsedAtIsNullAndDeletedAtIsNull("hash"))
                .thenReturn(Optional.of(token));

        assertThatThrownBy(() -> authRecoveryService.verifyEmail("raw-token"))
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        exception -> assertThat(exception.getErrorCode())
                                .isEqualTo(ErrorCode.INVALID_AUTH_TOKEN)
                );
    }

    @Test
    void forgotPasswordReturnsNullWhenEmailIsMissing() {
        when(userRepository.findByEmailIgnoreCaseAndDeletedAtIsNull(
                "missing@example.com"
        ))
                .thenReturn(Optional.empty());

        TokenIssuedResponse response =
                authRecoveryService.issuePasswordResetToken(
                        "missing@example.com"
                );

        assertThat(response).isNull();
        verify(passwordResetTokenRepository, never()).save(any());
    }

    @Test
    void resetPasswordUpdatesPasswordAndUsesToken() {
        User user = user();
        PasswordResetToken token = passwordToken(user);
        when(secureTokenService.hash("raw-token")).thenReturn("hash");
        when(passwordResetTokenRepository
                .findByTokenHashAndUsedAtIsNullAndDeletedAtIsNull("hash"))
                .thenReturn(Optional.of(token));
        when(passwordEncoder.encode("NewPassword1"))
                .thenReturn("encoded-new-password");

        authRecoveryService.resetPassword("raw-token", "NewPassword1");

        assertThat(user.getPasswordHash()).isEqualTo("encoded-new-password");
        assertThat(token.getUsedAt()).isNotNull();
        verify(userRepository).save(user);
        verify(passwordResetTokenRepository).save(token);
    }

    private EmailVerificationToken emailToken(User user) {
        EmailVerificationToken token = new EmailVerificationToken();
        token.setId(UUID.randomUUID());
        token.setUser(user);
        token.setTokenHash("hash");
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        return token;
    }

    private PasswordResetToken passwordToken(User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setId(UUID.randomUUID());
        token.setUser(user);
        token.setTokenHash("hash");
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        return token;
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
