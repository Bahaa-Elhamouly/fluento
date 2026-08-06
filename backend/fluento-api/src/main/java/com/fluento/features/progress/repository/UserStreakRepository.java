package com.fluento.features.progress.repository;

import com.fluento.features.progress.entity.UserStreak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserStreakRepository extends JpaRepository<UserStreak, UUID> {

    Optional<UserStreak> findByUser_IdAndDeletedAtIsNull(UUID userId);
}
