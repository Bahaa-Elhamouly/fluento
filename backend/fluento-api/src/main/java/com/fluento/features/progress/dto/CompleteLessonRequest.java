package com.fluento.features.progress.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CompleteLessonRequest(
        @Min(0)
        @Max(100)
        Integer score
) {
}
