package com.fluento.features.user.dto;

import com.fluento.features.course.enums.LearningLevel;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateUserProfileRequest(
        @Size(max = 500)
        String avatarUrl,

        @Size(max = 10)
        String nativeLanguageCode,

        @Size(max = 10)
        List<@Size(max = 10) String> targetLanguageCodes,

        LearningLevel learningLevel,

        @Min(5)
        @Max(240)
        Integer dailyGoalMinutes
) {
}
