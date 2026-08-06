package com.fluento.features.progress.repository;

import com.fluento.features.progress.entity.UserAchievement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserAchievementRepository
        extends JpaRepository<UserAchievement, UUID> {

    boolean existsByUser_IdAndAchievement_IdAndDeletedAtIsNull(
            UUID userId,
            UUID achievementId
    );

    @EntityGraph(attributePaths = "achievement")
    List<UserAchievement> findByUser_IdAndDeletedAtIsNullOrderByUnlockedAtDesc(
            UUID userId
    );
}
