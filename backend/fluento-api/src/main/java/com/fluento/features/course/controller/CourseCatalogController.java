package com.fluento.features.course.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.course.dto.CourseModuleResponse;
import com.fluento.features.course.dto.CourseResponse;
import com.fluento.features.course.dto.LanguageResponse;
import com.fluento.features.course.service.CourseCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class CourseCatalogController {

    private final CourseCatalogService courseCatalogService;

    @GetMapping("/languages")
    public ApiResponse<List<LanguageResponse>> languages() {
        return ApiResponse.success(
                "Languages retrieved successfully",
                courseCatalogService.getEnabledLanguages()
        );
    }

    @GetMapping("/courses")
    public ApiResponse<List<CourseResponse>> courses(
            @RequestParam(required = false) String language
    ) {
        return ApiResponse.success(
                "Courses retrieved successfully",
                courseCatalogService.getPublishedCourses(language)
        );
    }

    @GetMapping("/courses/{slug}")
    public ApiResponse<CourseResponse> course(
            @PathVariable String slug
    ) {
        return ApiResponse.success(
                "Course retrieved successfully",
                courseCatalogService.getCourseBySlug(slug)
        );
    }

    @GetMapping("/courses/{courseId}/modules")
    public ApiResponse<List<CourseModuleResponse>> modules(
            @PathVariable UUID courseId
    ) {
        return ApiResponse.success(
                "Course modules retrieved successfully",
                courseCatalogService.getCourseModules(courseId)
        );
    }
}
