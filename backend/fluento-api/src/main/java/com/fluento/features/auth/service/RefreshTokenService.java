package com.fluento.features.auth.service;

import com.fluento.common.enums.ErrorCode;
import com.fluento.common.exception.BusinessException;
import com.fluento.features.auth.entity.RefreshToken;
import com.fluento.features.auth.repository.RefreshTokenRepository;
import com.fluento.features.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureTokenService secureTokenService;
    private final long refreshExpirationMs;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            SecureTokenService secureTokenService,
            @Value("${app.jwt.refresh-expiration-ms}")
            long refreshExpirationMs
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.secureTokenService = secureTokenService;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @Transactional
    public RefreshTokenResult create(User user) {
        String rawToken = secureTokenService.generateRawToken();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(secureTokenService.hash(rawToken));
        refreshToken.setExpiresAt(
                LocalDateTime.now().plusNanos(refreshExpirationMs * 1_000_000)
        );

        refreshTokenRepository.save(refreshToken);

        return new RefreshTokenResult(rawToken, user);
    }

    @Transactional
    public RefreshTokenResult rotate(String rawToken) {
        RefreshToken existingToken = findUsableToken(rawToken);
        LocalDateTime now = LocalDateTime.now();

        if (existingToken.isExpired(now)) {
            existingToken.setRevokedAt(now);
            refreshTokenRepository.save(existingToken);
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        existingToken.setRevokedAt(now);
        refreshTokenRepository.save(existingToken);

        return create(existingToken.getUser());
    }

    @Transactional
    public void revoke(String rawToken) {
        RefreshToken refreshToken = findUsableToken(rawToken);
        refreshToken.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken findUsableToken(String rawToken) {
        return refreshTokenRepository
                .findByTokenHashAndRevokedAtIsNullAndDeletedAtIsNull(
                        hash(rawToken)
                )
                .orElseThrow(
                        () -> new BusinessException(
                                ErrorCode.INVALID_REFRESH_TOKEN
                        )
                );
    }

    private String hash(String rawToken) {
        return secureTokenService.hash(rawToken);
    }
}
