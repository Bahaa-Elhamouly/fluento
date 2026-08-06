package com.fluento.features.auth.repository;

import com.fluento.features.auth.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, UUID> {

    Optional<EmailVerificationToken> findByTokenHashAndUsedAtIsNullAndDeletedAtIsNull(
            String tokenHash
    );
}
