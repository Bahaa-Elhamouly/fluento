package com.fluento.features.quiz.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.quiz.dto.QuizResponse;
import com.fluento.features.quiz.service.QuizCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class QuizCatalogController {

    private final QuizCatalogService quizCatalogService;

    @GetMapping("/lessons/{lessonId}/quiz")
    public ApiResponse<QuizResponse> quizForLesson(
            @PathVariable UUID lessonId
    ) {
        return ApiResponse.success(
                "Quiz retrieved successfully",
                quizCatalogService.getPublishedQuizForLesson(lessonId)
        );
    }

    @GetMapping("/quizzes/{quizId}")
    public ApiResponse<QuizResponse> quiz(@PathVariable UUID quizId) {
        return ApiResponse.success(
                "Quiz retrieved successfully",
                quizCatalogService.getPublishedQuiz(quizId)
        );
    }
}
