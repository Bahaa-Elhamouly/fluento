package com.fluento.features.user.dto;

import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.user.enums.Role;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Role role,
        boolean emailVerified,
        String avatarUrl,
        String nativeLanguageCode,
        List<String> targetLanguageCodes,
        LearningLevel learningLevel,
        Integer dailyGoalMinutes
) {
}
