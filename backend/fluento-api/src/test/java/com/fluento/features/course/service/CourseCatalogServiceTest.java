package com.fluento.features.course.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.dto.CourseModuleResponse;
import com.fluento.features.course.dto.CourseResponse;
import com.fluento.features.course.dto.LanguageResponse;
import com.fluento.features.course.entity.Course;
import com.fluento.features.course.entity.CourseModule;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.CourseStatus;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.course.repository.CourseModuleRepository;
import com.fluento.features.course.repository.CourseRepository;
import com.fluento.features.course.repository.LanguageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseCatalogServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseModuleRepository courseModuleRepository;

    @InjectMocks
    private CourseCatalogService courseCatalogService;

    @Test
    void getEnabledLanguagesReturnsPublicLanguageContracts() {
        Language german = language(
                UUID.randomUUID(),
                "de",
                "German",
                "Deutsch"
        );

        when(languageRepository
                .findByEnabledTrueAndDeletedAtIsNullOrderByNameAsc())
                .thenReturn(List.of(german));

        List<LanguageResponse> response =
                courseCatalogService.getEnabledLanguages();

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().code()).isEqualTo("de");
        assertThat(response.getFirst().nativeName()).isEqualTo("Deutsch");
    }

    @Test
    void getPublishedCoursesWithoutLanguageReturnsAllPublishedCourses() {
        Course course = course(
                UUID.randomUUID(),
                language(
                        UUID.randomUUID(),
                        "es",
                        "Spanish",
                        "Español"
                ),
                "spanish-foundations"
        );

        when(courseRepository
                .findByStatusAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                        CourseStatus.PUBLISHED
                ))
                .thenReturn(List.of(course));

        List<CourseResponse> response =
                courseCatalogService.getPublishedCourses(null);

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().languageCode()).isEqualTo("es");
        assertThat(response.getFirst().slug())
                .isEqualTo("spanish-foundations");
    }

    @Test
    void getPublishedCoursesFiltersByNormalizedLanguageCode() {
        when(courseRepository
                .findByLanguage_CodeAndStatusAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                        "fr",
                        CourseStatus.PUBLISHED
                ))
                .thenReturn(List.of());

        courseCatalogService.getPublishedCourses(" FR ");

        verify(courseRepository)
                .findByLanguage_CodeAndStatusAndDeletedAtIsNullOrderByPositionAscTitleAsc(
                        "fr",
                        CourseStatus.PUBLISHED
                );
    }

    @Test
    void getCourseBySlugThrowsWhenMissing() {
        when(courseRepository.findBySlugAndDeletedAtIsNull("missing"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> courseCatalogService.getCourseBySlug("missing")
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getCourseModulesReturnsOrderedModuleContracts() {
        UUID courseId = UUID.randomUUID();
        Course course = course(
                courseId,
                language(UUID.randomUUID(), "de", "German", "Deutsch"),
                "german-foundations"
        );
        CourseModule module = module(
                UUID.randomUUID(),
                course,
                "German Essentials"
        );

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(courseModuleRepository
                .findByCourse_IdAndDeletedAtIsNullOrderByPositionAsc(
                        courseId
                ))
                .thenReturn(List.of(module));

        List<CourseModuleResponse> response =
                courseCatalogService.getCourseModules(courseId);

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().courseId()).isEqualTo(courseId);
        assertThat(response.getFirst().title())
                .isEqualTo("German Essentials");
    }

    @Test
    void getCourseModulesThrowsWhenCourseDoesNotExist() {
        UUID courseId = UUID.randomUUID();
        when(courseRepository.existsById(courseId)).thenReturn(false);

        assertThatThrownBy(
                () -> courseCatalogService.getCourseModules(courseId)
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Language language(
            UUID id,
            String code,
            String name,
            String nativeName
    ) {
        Language language = new Language();
        language.setId(id);
        language.setCode(code);
        language.setName(name);
        language.setNativeName(nativeName);
        language.setEnabled(true);
        return language;
    }

    private Course course(
            UUID id,
            Language language,
            String slug
    ) {
        Course course = new Course();
        course.setId(id);
        course.setLanguage(language);
        course.setTitle("Course");
        course.setSlug(slug);
        course.setDescription("Course description");
        course.setLevel(LearningLevel.BEGINNER);
        course.setStatus(CourseStatus.PUBLISHED);
        course.setPosition(1);
        return course;
    }

    private CourseModule module(
            UUID id,
            Course course,
            String title
    ) {
        CourseModule module = new CourseModule();
        module.setId(id);
        module.setCourse(course);
        module.setTitle(title);
        module.setDescription("Module description");
        module.setPosition(1);
        return module;
    }
}
