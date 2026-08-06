package com.fluento.features.lesson.dto;

import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.enums.LessonType;

import java.util.UUID;

public record LessonSummaryResponse(
        UUID id,
        UUID moduleId,
        String title,
        String description,
        LessonType type,
        LessonStatus status,
        int estimatedMinutes,
        int position
) {
}
