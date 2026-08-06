package com.fluento.features.quiz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.entity.Course;
import com.fluento.features.course.entity.CourseModule;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.CourseStatus;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.lesson.entity.Lesson;
import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.enums.LessonType;
import com.fluento.features.lesson.repository.LessonRepository;
import com.fluento.features.quiz.dto.QuizResponse;
import com.fluento.features.quiz.entity.Quiz;
import com.fluento.features.quiz.entity.QuizQuestion;
import com.fluento.features.quiz.enums.QuizQuestionType;
import com.fluento.features.quiz.enums.QuizStatus;
import com.fluento.features.quiz.repository.QuizQuestionRepository;
import com.fluento.features.quiz.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizCatalogServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizQuestionRepository quizQuestionRepository;

    private QuizCatalogService quizCatalogService;

    @BeforeEach
    void setUp() {
        quizCatalogService = new QuizCatalogService(
                lessonRepository,
                quizRepository,
                quizQuestionRepository,
                new ObjectMapper()
        );
    }

    @Test
    void getPublishedQuizForLessonReturnsQuizWithQuestions() {
        Lesson lesson = lesson();
        Quiz quiz = quiz(lesson);
        QuizQuestion question = question(quiz);

        when(lessonRepository.existsById(lesson.getId())).thenReturn(true);
        when(quizRepository
                .findFirstByLesson_IdAndStatusAndDeletedAtIsNullOrderByPositionAsc(
                        lesson.getId(),
                        QuizStatus.PUBLISHED
                ))
                .thenReturn(Optional.of(quiz));
        when(quizQuestionRepository
                .findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quiz.getId()))
                .thenReturn(List.of(question));

        QuizResponse response =
                quizCatalogService.getPublishedQuizForLesson(lesson.getId());

        assertThat(response.id()).isEqualTo(quiz.getId());
        assertThat(response.lessonId()).isEqualTo(lesson.getId());
        assertThat(response.questions()).hasSize(1);
        assertThat(response.questions().getFirst().options())
                .containsExactly("Hello", "Goodbye", "Thanks");
    }

    @Test
    void getPublishedQuizForLessonThrowsWhenLessonDoesNotExist() {
        UUID lessonId = UUID.randomUUID();
        when(lessonRepository.existsById(lessonId)).thenReturn(false);

        assertThatThrownBy(
                () -> quizCatalogService.getPublishedQuizForLesson(lessonId)
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getPublishedQuizForLessonThrowsWhenNoPublishedQuizExists() {
        UUID lessonId = UUID.randomUUID();
        when(lessonRepository.existsById(lessonId)).thenReturn(true);
        when(quizRepository
                .findFirstByLesson_IdAndStatusAndDeletedAtIsNullOrderByPositionAsc(
                        lessonId,
                        QuizStatus.PUBLISHED
                ))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> quizCatalogService.getPublishedQuizForLesson(lessonId)
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getPublishedQuizReturnsQuizById() {
        Quiz quiz = quiz(lesson());
        when(quizRepository.findByIdAndStatusAndDeletedAtIsNull(
                quiz.getId(),
                QuizStatus.PUBLISHED
        ))
                .thenReturn(Optional.of(quiz));
        when(quizQuestionRepository
                .findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quiz.getId()))
                .thenReturn(List.of());

        QuizResponse response =
                quizCatalogService.getPublishedQuiz(quiz.getId());

        assertThat(response.title()).isEqualTo("Greetings check");
        assertThat(response.questions()).isEmpty();
    }

    @Test
    void getPublishedQuizThrowsWhenMissing() {
        UUID quizId = UUID.randomUUID();
        when(quizRepository.findByIdAndStatusAndDeletedAtIsNull(
                quizId,
                QuizStatus.PUBLISHED
        ))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizCatalogService.getPublishedQuiz(quizId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Quiz quiz(Lesson lesson) {
        Quiz quiz = new Quiz();
        quiz.setId(UUID.randomUUID());
        quiz.setLesson(lesson);
        quiz.setTitle("Greetings check");
        quiz.setDescription("Check greeting comprehension");
        quiz.setStatus(QuizStatus.PUBLISHED);
        quiz.setPassingScore(70);
        quiz.setPosition(1);
        return quiz;
    }

    private QuizQuestion question(Quiz quiz) {
        QuizQuestion question = new QuizQuestion();
        question.setId(UUID.randomUUID());
        question.setQuiz(quiz);
        question.setType(QuizQuestionType.MULTIPLE_CHOICE);
        question.setPrompt("What does Hallo mean?");
        question.setOptionsJson("[\"Hello\",\"Goodbye\",\"Thanks\"]");
        question.setCorrectAnswer("Hello");
        question.setExplanation("Hallo means hello.");
        question.setPoints(1);
        question.setPosition(1);
        return question;
    }

    private Lesson lesson() {
        Lesson lesson = new Lesson();
        lesson.setId(UUID.randomUUID());
        lesson.setModule(module());
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
