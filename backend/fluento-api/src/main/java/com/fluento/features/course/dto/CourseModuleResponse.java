package com.fluento.features.course.dto;

import java.util.UUID;

public record CourseModuleResponse(
        UUID id,
        UUID courseId,
        String title,
        String description,
        int position
) {
}
