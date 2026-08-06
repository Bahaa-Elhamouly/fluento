package com.fluento.features.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminCourseModuleRequest(
        @NotBlank
        @Size(max = 160)
        String title,

        @Size(max = 1000)
        String description,

        int position
) {
}
