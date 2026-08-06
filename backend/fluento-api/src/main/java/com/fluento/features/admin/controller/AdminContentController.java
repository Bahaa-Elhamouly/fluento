package com.fluento.features.admin.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.admin.dto.AdminCourseModuleRequest;
import com.fluento.features.admin.dto.AdminCourseRequest;
import com.fluento.features.admin.dto.AdminGrammarRuleRequest;
import com.fluento.features.admin.dto.AdminLessonRequest;
import com.fluento.features.admin.dto.AdminQuizRequest;
import com.fluento.features.admin.dto.AdminVocabularyTermRequest;
import com.fluento.features.admin.service.AdminContentService;
import com.fluento.features.course.dto.CourseModuleResponse;
import com.fluento.features.course.dto.CourseResponse;
import com.fluento.features.grammar.dto.GrammarRuleDetailResponse;
import com.fluento.features.grammar.dto.GrammarRuleSummaryResponse;
import com.fluento.features.lesson.dto.LessonDetailResponse;
import com.fluento.features.quiz.dto.QuizResponse;
import com.fluento.features.vocabulary.dto.VocabularyTermResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/content")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminContentController {

    private final AdminContentService adminContentService;

    @GetMapping("/courses")
    public ApiResponse<List<CourseResponse>> courses() {
        return ApiResponse.success("Courses retrieved successfully", adminContentService.getCourses());
    }

    @PostMapping("/courses")
    public ApiResponse<CourseResponse> createCourse(@Valid @RequestBody AdminCourseRequest request) {
        return ApiResponse.success("Course created successfully", adminContentService.createCourse(request));
    }

    @PatchMapping("/courses/{courseId}")
    public ApiResponse<CourseResponse> updateCourse(
            @PathVariable UUID courseId,
            @Valid @RequestBody AdminCourseRequest request
    ) {
        return ApiResponse.success("Course updated successfully", adminContentService.updateCourse(courseId, request));
    }

    @DeleteMapping("/courses/{courseId}")
    public ApiResponse<Void> deleteCourse(@PathVariable UUID courseId) {
        adminContentService.deleteCourse(courseId);
        return ApiResponse.success("Course deleted successfully");
    }

    @GetMapping("/courses/{courseId}/modules")
    public ApiResponse<List<CourseModuleResponse>> modules(@PathVariable UUID courseId) {
        return ApiResponse.success("Modules retrieved successfully", adminContentService.getModules(courseId));
    }

    @PostMapping("/courses/{courseId}/modules")
    public ApiResponse<CourseModuleResponse> createModule(
            @PathVariable UUID courseId,
            @Valid @RequestBody AdminCourseModuleRequest request
    ) {
        return ApiResponse.success("Module created successfully", adminContentService.createModule(courseId, request));
    }

    @PatchMapping("/modules/{moduleId}")
    public ApiResponse<CourseModuleResponse> updateModule(
            @PathVariable UUID moduleId,
            @Valid @RequestBody AdminCourseModuleRequest request
    ) {
        return ApiResponse.success("Module updated successfully", adminContentService.updateModule(moduleId, request));
    }

    @DeleteMapping("/modules/{moduleId}")
    public ApiResponse<Void> deleteModule(@PathVariable UUID moduleId) {
        adminContentService.deleteModule(moduleId);
        return ApiResponse.success("Module deleted successfully");
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ApiResponse<List<LessonDetailResponse>> lessons(@PathVariable UUID moduleId) {
        return ApiResponse.success("Lessons retrieved successfully", adminContentService.getLessons(moduleId));
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ApiResponse<LessonDetailResponse> createLesson(
            @PathVariable UUID moduleId,
            @Valid @RequestBody AdminLessonRequest request
    ) {
        return ApiResponse.success("Lesson created successfully", adminContentService.createLesson(moduleId, request));
    }

    @PatchMapping("/lessons/{lessonId}")
    public ApiResponse<LessonDetailResponse> updateLesson(
            @PathVariable UUID lessonId,
            @Valid @RequestBody AdminLessonRequest request
    ) {
        return ApiResponse.success("Lesson updated successfully", adminContentService.updateLesson(lessonId, request));
    }

    @DeleteMapping("/lessons/{lessonId}")
    public ApiResponse<Void> deleteLesson(@PathVariable UUID lessonId) {
        adminContentService.deleteLesson(lessonId);
        return ApiResponse.success("Lesson deleted successfully");
    }

    @GetMapping("/vocabulary")
    public ApiResponse<List<VocabularyTermResponse>> vocabularyTerms() {
        return ApiResponse.success(
                "Vocabulary terms retrieved successfully",
                adminContentService.getVocabularyTerms()
        );
    }

    @PostMapping("/vocabulary")
    public ApiResponse<VocabularyTermResponse> createVocabularyTerm(
            @Valid @RequestBody AdminVocabularyTermRequest request
    ) {
        return ApiResponse.success(
                "Vocabulary term created successfully",
                adminContentService.createVocabularyTerm(request)
        );
    }

    @PatchMapping("/vocabulary/{termId}")
    public ApiResponse<VocabularyTermResponse> updateVocabularyTerm(
            @PathVariable UUID termId,
            @Valid @RequestBody AdminVocabularyTermRequest request
    ) {
        return ApiResponse.success(
                "Vocabulary term updated successfully",
                adminContentService.updateVocabularyTerm(termId, request)
        );
    }

    @DeleteMapping("/vocabulary/{termId}")
    public ApiResponse<Void> deleteVocabularyTerm(@PathVariable UUID termId) {
        adminContentService.deleteVocabularyTerm(termId);
        return ApiResponse.success("Vocabulary term deleted successfully");
    }

    @GetMapping("/grammar")
    public ApiResponse<List<GrammarRuleSummaryResponse>> grammarRules() {
        return ApiResponse.success("Grammar rules retrieved successfully", adminContentService.getGrammarRules());
    }

    @PostMapping("/grammar")
    public ApiResponse<GrammarRuleDetailResponse> createGrammarRule(
            @Valid @RequestBody AdminGrammarRuleRequest request
    ) {
        return ApiResponse.success(
                "Grammar rule created successfully",
                adminContentService.createGrammarRule(request)
        );
    }

    @PatchMapping("/grammar/{ruleId}")
    public ApiResponse<GrammarRuleDetailResponse> updateGrammarRule(
            @PathVariable UUID ruleId,
            @Valid @RequestBody AdminGrammarRuleRequest request
    ) {
        return ApiResponse.success(
                "Grammar rule updated successfully",
                adminContentService.updateGrammarRule(ruleId, request)
        );
    }

    @DeleteMapping("/grammar/{ruleId}")
    public ApiResponse<Void> deleteGrammarRule(@PathVariable UUID ruleId) {
        adminContentService.deleteGrammarRule(ruleId);
        return ApiResponse.success("Grammar rule deleted successfully");
    }

    @GetMapping("/lessons/{lessonId}/quizzes")
    public ApiResponse<List<QuizResponse>> quizzes(@PathVariable UUID lessonId) {
        return ApiResponse.success("Quizzes retrieved successfully", adminContentService.getQuizzes(lessonId));
    }

    @PostMapping("/lessons/{lessonId}/quizzes")
    public ApiResponse<QuizResponse> createQuiz(
            @PathVariable UUID lessonId,
            @Valid @RequestBody AdminQuizRequest request
    ) {
        return ApiResponse.success("Quiz created successfully", adminContentService.createQuiz(lessonId, request));
    }

    @PatchMapping("/quizzes/{quizId}")
    public ApiResponse<QuizResponse> updateQuiz(
            @PathVariable UUID quizId,
            @Valid @RequestBody AdminQuizRequest request
    ) {
        return ApiResponse.success("Quiz updated successfully", adminContentService.updateQuiz(quizId, request));
    }

    @DeleteMapping("/quizzes/{quizId}")
    public ApiResponse<Void> deleteQuiz(@PathVariable UUID quizId) {
        adminContentService.deleteQuiz(quizId);
        return ApiResponse.success("Quiz deleted successfully");
    }
}
