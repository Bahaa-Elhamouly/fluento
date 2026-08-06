package com.fluento.features.admin.dto;

import com.fluento.features.course.enums.CourseStatus;
import com.fluento.features.course.enums.LearningLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminCourseRequest(
        @NotBlank
        @Size(max = 10)
        String languageCode,

        @NotBlank
        @Size(max = 160)
        String title,

        @NotBlank
        @Size(max = 180)
        String slug,

        @NotBlank
        @Size(max = 1000)
        String description,

        @NotNull
        LearningLevel level,

        @NotNull
        CourseStatus status,

        int position
) {
}
