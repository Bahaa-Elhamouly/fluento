package com.fluento.features.analytics.dto;

public record AdminAnalyticsResponse(
        long totalUsers,
        long activeUsers,
        long activeSubscriptions,
        long enabledLanguages,
        long totalCourses,
        long publishedCourses,
        long totalModules,
        long totalLessons,
        long publishedLessons,
        long vocabularyTerms,
        long grammarRules,
        long totalQuizzes,
        long publishedQuizzes,
        long completedLessons,
        long totalXpAwarded,
        long quizAttempts,
        long passedQuizAttempts,
        double averageQuizPercentage
) {
}
