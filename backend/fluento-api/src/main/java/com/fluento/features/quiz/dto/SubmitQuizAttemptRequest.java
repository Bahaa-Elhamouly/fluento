package com.fluento.features.quiz.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SubmitQuizAttemptRequest(
        @NotEmpty
        @Size(max = 100)
        List<@Valid QuizAttemptAnswerRequest> answers
) {
}
