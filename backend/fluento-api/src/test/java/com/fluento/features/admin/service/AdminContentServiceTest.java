package com.fluento.features.admin.service;

import com.fluento.common.exception.BusinessException;
import com.fluento.features.admin.dto.AdminCourseModuleRequest;
import com.fluento.features.admin.dto.AdminCourseRequest;
import com.fluento.features.admin.dto.AdminLessonRequest;
import com.fluento.features.course.dto.CourseResponse;
import com.fluento.features.course.entity.Course;
import com.fluento.features.course.entity.CourseModule;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.CourseStatus;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.course.repository.CourseModuleRepository;
import com.fluento.features.course.repository.CourseRepository;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.lesson.dto.LessonDetailResponse;
import com.fluento.features.lesson.entity.Lesson;
import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.enums.LessonType;
import com.fluento.features.lesson.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminContentServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseModuleRepository courseModuleRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private AdminContentService adminContentService;

    @Test
    void createCourseCreatesCourseForLanguage() {
        Language language = language();
        when(courseRepository.existsBySlugAndDeletedAtIsNull("german-foundations"))
                .thenReturn(false);
        when(languageRepository.findByCodeAndDeletedAtIsNull("de"))
                .thenReturn(Optional.of(language));
        when(courseRepository.save(any(Course.class)))
                .thenAnswer(invocation -> {
                    Course course = invocation.getArgument(0);
                    course.setId(UUID.randomUUID());
                    return course;
                });

        CourseResponse response = adminContentService.createCourse(
                new AdminCourseRequest(
                        "DE",
                        "German Foundations",
                        "German-Foundations",
                        "Description",
                        LearningLevel.BEGINNER,
                        CourseStatus.PUBLISHED,
                        1
                )
        );

        assertThat(response.languageCode()).isEqualTo("de");
        assertThat(response.slug()).isEqualTo("german-foundations");
    }

    @Test
    void createCourseRejectsDuplicateSlug() {
        when(courseRepository.existsBySlugAndDeletedAtIsNull("existing"))
                .thenReturn(true);

        assertThatThrownBy(
                () -> adminContentService.createCourse(
                        new AdminCourseRequest(
                                "de",
                                "Existing",
                                "existing",
                                "Description",
                                LearningLevel.BEGINNER,
                                CourseStatus.DRAFT,
                                1
                        )
                )
        )
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void createModuleCreatesModuleForCourse() {
        Course course = course();
        when(courseRepository.findByIdAndDeletedAtIsNull(course.getId()))
                .thenReturn(Optional.of(course));
        when(courseModuleRepository.save(any(CourseModule.class)))
                .thenAnswer(invocation -> {
                    CourseModule module = invocation.getArgument(0);
                    module.setId(UUID.randomUUID());
                    return module;
                });

        var response = adminContentService.createModule(
                course.getId(),
                new AdminCourseModuleRequest("Essentials", "Basics", 1)
        );

        assertThat(response.courseId()).isEqualTo(course.getId());
        assertThat(response.title()).isEqualTo("Essentials");
    }

    @Test
    void createLessonCreatesLessonForModule() {
        CourseModule module = module();
        when(courseModuleRepository.findByIdAndDeletedAtIsNull(module.getId()))
                .thenReturn(Optional.of(module));
        when(lessonRepository.save(any(Lesson.class)))
                .thenAnswer(invocation -> {
                    Lesson lesson = invocation.getArgument(0);
                    lesson.setId(UUID.randomUUID());
                    return lesson;
                });

        LessonDetailResponse response = adminContentService.createLesson(
                module.getId(),
                new AdminLessonRequest(
                        "Greetings",
                        "Intro",
                        LessonType.READING,
                        LessonStatus.PUBLISHED,
                        "Hallo means hello.",
                        8,
                        1
                )
        );

        assertThat(response.moduleId()).isEqualTo(module.getId());
        assertThat(response.content()).contains("Hallo");
    }

    @Test
    void deleteCourseSoftDeletesCourse() {
        Course course = course();
        when(courseRepository.findByIdAndDeletedAtIsNull(course.getId()))
                .thenReturn(Optional.of(course));

        adminContentService.deleteCourse(course.getId());

        assertThat(course.getDeletedAt()).isNotNull();
        verify(courseRepository).save(course);
    }

    private CourseModule module() {
        CourseModule module = new CourseModule();
        module.setId(UUID.randomUUID());
        module.setCourse(course());
        module.setTitle("Essentials");
        module.setDescription("Basics");
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
