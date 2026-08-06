package com.fluento.features.user.repository;

import com.fluento.features.user.entity.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, UUID> {

    @EntityGraph(attributePaths = "user")
    Optional<UserProfile> findByUser_IdAndDeletedAtIsNull(UUID userId);
}
