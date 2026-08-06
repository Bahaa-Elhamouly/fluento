package com.fluento.features.course.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.dto.CourseModuleResponse;
import com.fluento.features.course.dto.CourseResponse;
import com.fluento.features.course.dto.LanguageResponse;
import com.fluento.features.course.entity.Course;
import com.fluento.features.course.entity.CourseModule;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.CourseStatus;
import com.fluento.features.course.repository.CourseModuleRepository;
import com.fluento.features.course.repository.CourseRepository;
import com.fluento.features.course.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseCatalogService {

    private final LanguageRepository languageRepository;
    private final CourseRepository courseRepository;
    private final CourseModuleRepository courseModuleRepository;

    @Transactional(readOnly = true)
    public List<LanguageResponse> getEnabledLanguages() {
        return languageRepository
                .findByEnabledTrueAndDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(this::toLanguageResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getPublishedCourses(String languageCode) {
        List<Course> courses;

        if (languageCode == null || languageCode.isBlank()) {
            courses = courseRepository
                    .findByStatusAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                            CourseStatus.PUBLISHED
                    );
        } else {
            courses = courseRepository
                    .findByLanguage_CodeAndStatusAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                            languageCode.trim().toLowerCase(),
                            CourseStatus.PUBLISHED
                    );
        }

        return courses.stream()
                .map(this::toCourseResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseBySlug(String slug) {
        Course course = courseRepository
                .findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Course",
                                slug
                        )
                );

        return toCourseResponse(course);
    }

    @Transactional(readOnly = true)
    public List<CourseModuleResponse> getCourseModules(UUID courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", courseId);
        }

        return courseModuleRepository
                .findByCourse_IdAndDeletedAtIsNullOrderByPositionAsc(
                        courseId
                )
                .stream()
                .map(this::toCourseModuleResponse)
                .toList();
    }

    private LanguageResponse toLanguageResponse(Language language) {
        return new LanguageResponse(
                language.getId(),
                language.getCode(),
                language.getName(),
                language.getNativeName()
        );
    }

    private CourseResponse toCourseResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getLanguage().getCode(),
                course.getLanguage().getName(),
                course.getTitle(),
                course.getSlug(),
                course.getDescription(),
                course.getLevel(),
                course.getStatus(),
                course.getPosition()
        );
    }

    private CourseModuleResponse toCourseModuleResponse(
            CourseModule module
    ) {
        return new CourseModuleResponse(
                module.getId(),
                module.getCourse().getId(),
                module.getTitle(),
                module.getDescription(),
                module.getPosition()
        );
    }
}
