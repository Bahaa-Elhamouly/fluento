package com.fluento.features.lesson.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.lesson.dto.LessonDetailResponse;
import com.fluento.features.lesson.dto.LessonSummaryResponse;
import com.fluento.features.lesson.service.LessonCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class LessonCatalogController {

    private final LessonCatalogService lessonCatalogService;

    @GetMapping("/modules/{moduleId}/lessons")
    public ApiResponse<List<LessonSummaryResponse>> lessons(
            @PathVariable UUID moduleId
    ) {
        return ApiResponse.success(
                "Lessons retrieved successfully",
                lessonCatalogService.getPublishedLessons(moduleId)
        );
    }

    @GetMapping("/lessons/{lessonId}")
    public ApiResponse<LessonDetailResponse> lesson(
            @PathVariable UUID lessonId
    ) {
        return ApiResponse.success(
                "Lesson retrieved successfully",
                lessonCatalogService.getPublishedLesson(lessonId)
        );
    }
}
