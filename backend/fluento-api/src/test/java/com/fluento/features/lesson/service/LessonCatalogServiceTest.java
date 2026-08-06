package com.fluento.features.lesson.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.entity.Course;
import com.fluento.features.course.entity.CourseModule;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.CourseStatus;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.course.repository.CourseModuleRepository;
import com.fluento.features.lesson.dto.LessonDetailResponse;
import com.fluento.features.lesson.dto.LessonSummaryResponse;
import com.fluento.features.lesson.entity.Lesson;
import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.enums.LessonType;
import com.fluento.features.lesson.repository.LessonRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonCatalogServiceTest {

    @Mock
    private CourseModuleRepository courseModuleRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonCatalogService lessonCatalogService;

    @Test
    void getPublishedLessonsReturnsLessonSummaries() {
        CourseModule module = module();
        Lesson lesson = lesson(module);

        when(courseModuleRepository.existsById(module.getId()))
                .thenReturn(true);
        when(lessonRepository
                .findByModule_IdAndStatusAndDeletedAtIsNullOrderByPositionAsc(
                        module.getId(),
                        LessonStatus.PUBLISHED
                ))
                .thenReturn(List.of(lesson));

        List<LessonSummaryResponse> response =
                lessonCatalogService.getPublishedLessons(module.getId());

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().moduleId()).isEqualTo(module.getId());
        assertThat(response.getFirst().title())
                .isEqualTo("German greetings");
        assertThat(response.getFirst().type()).isEqualTo(LessonType.READING);
    }

    @Test
    void getPublishedLessonsThrowsWhenModuleDoesNotExist() {
        UUID moduleId = UUID.randomUUID();
        when(courseModuleRepository.existsById(moduleId)).thenReturn(false);

        assertThatThrownBy(
                () -> lessonCatalogService.getPublishedLessons(moduleId)
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getPublishedLessonReturnsLessonDetail() {
        Lesson lesson = lesson(module());

        when(lessonRepository.findByIdAndStatusAndDeletedAtIsNull(
                lesson.getId(),
                LessonStatus.PUBLISHED
        ))
                .thenReturn(Optional.of(lesson));

        LessonDetailResponse response =
                lessonCatalogService.getPublishedLesson(lesson.getId());

        assertThat(response.id()).isEqualTo(lesson.getId());
        assertThat(response.moduleId()).isEqualTo(lesson.getModule().getId());
        assertThat(response.courseId())
                .isEqualTo(lesson.getModule().getCourse().getId());
        assertThat(response.content()).contains("Hallo");
    }

    @Test
    void getPublishedLessonThrowsWhenMissing() {
        UUID lessonId = UUID.randomUUID();
        when(lessonRepository.findByIdAndStatusAndDeletedAtIsNull(
                lessonId,
                LessonStatus.PUBLISHED
        ))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> lessonCatalogService.getPublishedLesson(lessonId)
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Lesson lesson(CourseModule module) {
        Lesson lesson = new Lesson();
        lesson.setId(UUID.randomUUID());
        lesson.setModule(module);
        lesson.setTitle("German greetings");
        lesson.setDescription("Learn greetings");
        lesson.setType(LessonType.READING);
        lesson.setStatus(LessonStatus.PUBLISHED);
        lesson.setContent("Hallo means hello.");
        lesson.setEstimatedMinutes(8);
        lesson.setPosition(1);
        return lesson;
    }

    private CourseModule module() {
        CourseModule module = new CourseModule();
        module.setId(UUID.randomUUID());
        module.setCourse(course());
        module.setTitle("German Essentials");
        module.setDescription("Essentials");
        module.setPosition(1);
        return module;
    }

    private Course course() {
        Course course = new Course();
        course.setId(UUID.randomUUID());
        course.setLanguage(language());
        course.setTitle("German Foundations");
        course.setSlug("german-foundations");
        course.setDescription("Description");
        course.setLevel(LearningLevel.BEGINNER);
        course.setStatus(CourseStatus.PUBLISHED);
        course.setPosition(1);
        return course;
    }

    private Language language() {
        Language language = new Language();
        language.setId(UUID.randomUUID());
        language.setCode("de");
        language.setName("German");
        language.setNativeName("Deutsch");
        language.setEnabled(true);
        return language;
    }
}
