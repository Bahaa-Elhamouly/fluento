package com.fluento.features.course.dto;

import com.fluento.features.course.enums.CourseStatus;
import com.fluento.features.course.enums.LearningLevel;

import java.util.UUID;

public record CourseResponse(
        UUID id,
        String languageCode,
        String languageName,
        String title,
        String slug,
        String description,
        LearningLevel level,
        CourseStatus status,
        int position
) {
}
