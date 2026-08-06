package com.fluento.features.lesson.dto;

import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.enums.LessonType;

import java.util.UUID;

public record LessonDetailResponse(
        UUID id,
        UUID moduleId,
        UUID courseId,
        String title,
        String description,
        LessonType type,
        LessonStatus status,
        String content,
        int estimatedMinutes,
        int position
) {
}
