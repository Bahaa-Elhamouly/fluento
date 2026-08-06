package com.fluento.features.lesson.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.repository.CourseModuleRepository;
import com.fluento.features.lesson.dto.LessonDetailResponse;
import com.fluento.features.lesson.dto.LessonSummaryResponse;
import com.fluento.features.lesson.entity.Lesson;
import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonCatalogService {

    private final CourseModuleRepository courseModuleRepository;
    private final LessonRepository lessonRepository;

    @Transactional(readOnly = true)
    public List<LessonSummaryResponse> getPublishedLessons(UUID moduleId) {
        if (!courseModuleRepository.existsById(moduleId)) {
            throw new ResourceNotFoundException("Course module", moduleId);
        }

        return lessonRepository
                .findByModule_IdAndStatusAndDeletedAtIsNullOrderByPositionAsc(
                        moduleId,
                        LessonStatus.PUBLISHED
                )
                .stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LessonDetailResponse getPublishedLesson(UUID lessonId) {
        Lesson lesson = lessonRepository
                .findByIdAndStatusAndDeletedAtIsNull(
                        lessonId,
                        LessonStatus.PUBLISHED
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Lesson",
                                lessonId
                        )
                );

        return toDetailResponse(lesson);
    }

    private LessonSummaryResponse toSummaryResponse(Lesson lesson) {
        return new LessonSummaryResponse(
                lesson.getId(),
                lesson.getModule().getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getType(),
                lesson.getStatus(),
                lesson.getEstimatedMinutes(),
                lesson.getPosition()
        );
    }

    private LessonDetailResponse toDetailResponse(Lesson lesson) {
        return new LessonDetailResponse(
                lesson.getId(),
                lesson.getModule().getId(),
                lesson.getModule().getCourse().getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getType(),
                lesson.getStatus(),
                lesson.getContent(),
                lesson.getEstimatedMinutes(),
                lesson.getPosition()
        );
    }
}
