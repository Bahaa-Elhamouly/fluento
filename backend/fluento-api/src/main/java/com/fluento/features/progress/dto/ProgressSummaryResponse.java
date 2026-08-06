package com.fluento.features.progress.dto;

import java.util.List;

public record ProgressSummaryResponse(
        long totalXp,
        long completedLessons,
        long activeDaysLastSevenDays,
        int currentStreakDays,
        int longestStreakDays,
        int dailyGoalMinutes,
        int dailyGoalXp,
        List<AchievementResponse> achievements,
        List<LessonProgressResponse> recentLessons
) {
}
