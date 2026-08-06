package com.fluento.features.admin.dto;

import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.enums.LessonType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminLessonRequest(
        @NotBlank
        @Size(max = 180)
        String title,

        @Size(max = 1000)
        String description,

        @NotNull
        LessonType type,

        @NotNull
        LessonStatus status,

        @NotBlank
        @Size(max = 5000)
        String content,

        @Min(1)
        int estimatedMinutes,

        int position
) {
}
