package com.fluento.features.auth.service;

import com.fluento.common.enums.ErrorCode;
import com.fluento.common.exception.BusinessException;
import com.fluento.features.auth.entity.RefreshToken;
import com.fluento.features.auth.repository.RefreshTokenRepository;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    private static final long REFRESH_EXPIRATION_MS = 86_400_000L;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenService(
                refreshTokenRepository,
                new SecureTokenService(),
                REFRESH_EXPIRATION_MS
        );
    }

    @Test
    void createStoresHashAndReturnsRawToken() {
        User user = user();

        RefreshTokenResult result = refreshTokenService.create(user);

        ArgumentCaptor<RefreshToken> tokenCaptor =
                ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(tokenCaptor.capture());

        RefreshToken savedToken = tokenCaptor.getValue();
        assertThat(result.refreshToken()).isNotBlank();
        assertThat(savedToken.getUser()).isEqualTo(user);
        assertThat(savedToken.getTokenHash()).hasSize(64);
        assertThat(savedToken.getTokenHash())
                .isNotEqualTo(result.refreshToken());
        assertThat(savedToken.getExpiresAt())
                .isAfter(LocalDateTime.now().plusHours(23));
        assertThat(savedToken.getRevokedAt()).isNull();
    }

    @Test
    void rotateRevokesExistingTokenAndCreatesReplacement() {
        User user = user();
        RefreshToken existingToken = refreshToken(
                user,
                "old-refresh-token",
                LocalDateTime.now().plusDays(1)
        );

        when(refreshTokenRepository
                .findByTokenHashAndRevokedAtIsNullAndDeletedAtIsNull(
                        hash("old-refresh-token")
                ))
                .thenReturn(Optional.of(existingToken));

        RefreshTokenResult result =
                refreshTokenService.rotate("old-refresh-token");

        ArgumentCaptor<RefreshToken> tokenCaptor =
                ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository, atLeastOnce())
                .save(tokenCaptor.capture());

        List<RefreshToken> savedTokens = tokenCaptor.getAllValues();
        assertThat(existingToken.getRevokedAt()).isNotNull();
        assertThat(result.refreshToken()).isNotBlank();
        assertThat(result.refreshToken()).isNotEqualTo("old-refresh-token");
        assertThat(savedTokens)
                .anyMatch(token -> token.getTokenHash()
                        .equals(hash(result.refreshToken())));
    }

    @Test
    void rotateRejectsExpiredTokenAndRevokesIt() {
        User user = user();
        RefreshToken expiredToken = refreshToken(
                user,
                "expired-refresh-token",
                LocalDateTime.now().minusMinutes(1)
        );

        when(refreshTokenRepository
                .findByTokenHashAndRevokedAtIsNullAndDeletedAtIsNull(
                        hash("expired-refresh-token")
                ))
                .thenReturn(Optional.of(expiredToken));

        assertThatThrownBy(
                () -> refreshTokenService.rotate("expired-refresh-token")
        )
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        exception -> assertThat(exception.getErrorCode())
                                .isEqualTo(ErrorCode.REFRESH_TOKEN_EXPIRED)
                );
        assertThat(expiredToken.getRevokedAt()).isNotNull();
        verify(refreshTokenRepository).save(expiredToken);
    }

    @Test
    void revokeRejectsUnknownToken() {
        when(refreshTokenRepository
                .findByTokenHashAndRevokedAtIsNullAndDeletedAtIsNull(
                        hash("unknown-token")
                ))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> refreshTokenService.revoke("unknown-token")
        )
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        exception -> assertThat(exception.getErrorCode())
                                .isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN)
                );
    }

    @Test
    void revokeMarksTokenAsRevoked() {
        RefreshToken refreshToken = refreshToken(
                user(),
                "refresh-token",
                LocalDateTime.now().plusDays(1)
        );

        when(refreshTokenRepository
                .findByTokenHashAndRevokedAtIsNullAndDeletedAtIsNull(
                        hash("refresh-token")
                ))
                .thenReturn(Optional.of(refreshToken));

        refreshTokenService.revoke("refresh-token");

        assertThat(refreshToken.getRevokedAt()).isNotNull();
        verify(refreshTokenRepository).save(refreshToken);
    }

    private RefreshToken refreshToken(
            User user,
            String rawToken,
            LocalDateTime expiresAt
    ) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hash(rawToken));
        refreshToken.setExpiresAt(expiresAt);
        return refreshToken;
    }

    private User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Sara");
        user.setLastName("Ahmed");
        user.setEmail("sara@example.com");
        user.setPasswordHash("encoded-password");
        user.setRole(Role.USER);
        user.setEnabled(true);
        return user;
    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(
                    digest.digest(
                            rawToken.getBytes(StandardCharsets.UTF_8)
                    )
            );
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
