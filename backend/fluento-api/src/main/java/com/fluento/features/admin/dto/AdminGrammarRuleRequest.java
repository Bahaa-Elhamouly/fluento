package com.fluento.features.admin.dto;

import com.fluento.features.course.enums.LearningLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AdminGrammarRuleRequest(
        @NotBlank
        @Size(max = 12)
        String languageCode,

        @NotBlank
        @Size(max = 180)
        String title,

        @NotBlank
        @Size(max = 200)
        String slug,

        @NotBlank
        @Size(max = 3000)
        String explanation,

        @NotNull
        LearningLevel level,

        boolean enabled,

        @Min(1)
        int position,

        List<@Valid AdminGrammarExampleRequest> examples
) {
}
