package com.fluento.features.progress.dto;

import com.fluento.features.progress.enums.AchievementType;

import java.time.LocalDateTime;
import java.util.UUID;

public record AchievementResponse(
        UUID id,
        AchievementType type,
        String title,
        String description,
        int xpReward,
        boolean unlocked,
        LocalDateTime unlockedAt
) {
}
