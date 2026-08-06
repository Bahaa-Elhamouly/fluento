package com.fluento.features.progress.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LessonProgressResponse(
        UUID lessonId,
        UUID courseId,
        UUID moduleId,
        String lessonTitle,
        boolean completed,
        LocalDateTime completedAt,
        Integer score,
        int xpEarned
) {
}
