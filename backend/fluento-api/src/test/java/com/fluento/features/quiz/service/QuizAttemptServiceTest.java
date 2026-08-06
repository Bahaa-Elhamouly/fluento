package com.fluento.features.quiz.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.entity.Course;
import com.fluento.features.course.entity.CourseModule;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.CourseStatus;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.lesson.entity.Lesson;
import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.enums.LessonType;
import com.fluento.features.progress.entity.XpEvent;
import com.fluento.features.progress.repository.XpEventRepository;
import com.fluento.features.progress.service.ProgressService;
import com.fluento.features.quiz.dto.QuizAttemptAnswerRequest;
import com.fluento.features.quiz.dto.QuizAttemptResultResponse;
import com.fluento.features.quiz.dto.SubmitQuizAttemptRequest;
import com.fluento.features.quiz.entity.Quiz;
import com.fluento.features.quiz.entity.QuizAttempt;
import com.fluento.features.quiz.entity.QuizAttemptAnswer;
import com.fluento.features.quiz.entity.QuizQuestion;
import com.fluento.features.quiz.enums.QuizQuestionType;
import com.fluento.features.quiz.enums.QuizStatus;
import com.fluento.features.quiz.repository.QuizAttemptAnswerRepository;
import com.fluento.features.quiz.repository.QuizAttemptRepository;
import com.fluento.features.quiz.repository.QuizQuestionRepository;
import com.fluento.features.quiz.repository.QuizRepository;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizAttemptServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizQuestionRepository quizQuestionRepository;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private QuizAttemptAnswerRepository quizAttemptAnswerRepository;

    @Mock
    private XpEventRepository xpEventRepository;

    @Mock
    private ProgressService progressService;

    @InjectMocks
    private QuizAttemptService quizAttemptService;

    @Test
    @SuppressWarnings("unchecked")
    void submitAttemptScoresPassingAttemptAndAwardsXp() {
        User user = user();
        Quiz quiz = quiz();
        QuizQuestion question = question(quiz, "Hello");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(quizRepository.findByIdAndStatusAndDeletedAtIsNull(
                quiz.getId(),
                QuizStatus.PUBLISHED
        ))
                .thenReturn(Optional.of(quiz));
        when(quizQuestionRepository
                .findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quiz.getId()))
                .thenReturn(List.of(question));
        when(quizAttemptRepository.save(any(QuizAttempt.class)))
                .thenAnswer(invocation -> {
                    QuizAttempt attempt = invocation.getArgument(0);
                    attempt.setId(UUID.randomUUID());
                    return attempt;
                });

        QuizAttemptResultResponse response = quizAttemptService.submitAttempt(
                user.getId(),
                quiz.getId(),
                new SubmitQuizAttemptRequest(List.of(
                        new QuizAttemptAnswerRequest(
                                question.getId(),
                                " hello "
                        )
                ))
        );

        assertThat(response.score()).isEqualTo(2);
        assertThat(response.maxScore()).isEqualTo(2);
        assertThat(response.percentage()).isEqualTo(100);
        assertThat(response.passed()).isTrue();
        assertThat(response.xpEarned()).isEqualTo(20);
        assertThat(response.feedback().getFirst().correct()).isTrue();

        ArgumentCaptor<List<QuizAttemptAnswer>> answers =
                ArgumentCaptor.forClass(List.class);
        verify(quizAttemptAnswerRepository).saveAll(answers.capture());
        assertThat(answers.getValue()).hasSize(1);
        assertThat(answers.getValue().getFirst().isCorrect()).isTrue();

        verify(xpEventRepository).save(any(XpEvent.class));
        verify(progressService).recordLearningActivity(user);
        verify(progressService).evaluateAchievements(user);
    }

    @Test
    void submitAttemptDoesNotAwardXpWhenAttemptFails() {
        User user = user();
        Quiz quiz = quiz();
        QuizQuestion question = question(quiz, "Hello");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(quizRepository.findByIdAndStatusAndDeletedAtIsNull(
                quiz.getId(),
                QuizStatus.PUBLISHED
        ))
                .thenReturn(Optional.of(quiz));
        when(quizQuestionRepository
                .findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quiz.getId()))
                .thenReturn(List.of(question));
        when(quizAttemptRepository.save(any(QuizAttempt.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QuizAttemptResultResponse response = quizAttemptService.submitAttempt(
                user.getId(),
                quiz.getId(),
                new SubmitQuizAttemptRequest(List.of(
                        new QuizAttemptAnswerRequest(
                                question.getId(),
                                "Goodbye"
                        )
                ))
        );

        assertThat(response.score()).isZero();
        assertThat(response.percentage()).isZero();
        assertThat(response.passed()).isFalse();
        assertThat(response.xpEarned()).isZero();
        verify(xpEventRepository, never()).save(any());
    }

    @Test
    void submitAttemptTreatsMissingAnswerAsIncorrect() {
        User user = user();
        Quiz quiz = quiz();
        QuizQuestion question = question(quiz, "Hello");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(quizRepository.findByIdAndStatusAndDeletedAtIsNull(
                quiz.getId(),
                QuizStatus.PUBLISHED
        ))
                .thenReturn(Optional.of(quiz));
        when(quizQuestionRepository
                .findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quiz.getId()))
                .thenReturn(List.of(question));
        when(quizAttemptRepository.save(any(QuizAttempt.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        QuizAttemptResultResponse response = quizAttemptService.submitAttempt(
                user.getId(),
                quiz.getId(),
                new SubmitQuizAttemptRequest(List.of(
                        new QuizAttemptAnswerRequest(UUID.randomUUID(), "Hello")
                ))
        );

        assertThat(response.feedback().getFirst().correct()).isFalse();
        assertThat(response.feedback().getFirst().message())
                .contains("Hallo means hello");
    }

    @Test
    void submitAttemptThrowsWhenUserIsMissing() {
        UUID userId = UUID.randomUUID();
        UUID quizId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> quizAttemptService.submitAttempt(
                        userId,
                        quizId,
                        new SubmitQuizAttemptRequest(List.of())
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void submitAttemptThrowsWhenQuizIsMissing() {
        User user = user();
        UUID quizId = UUID.randomUUID();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(quizRepository.findByIdAndStatusAndDeletedAtIsNull(
                quizId,
                QuizStatus.PUBLISHED
        ))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> quizAttemptService.submitAttempt(
                        user.getId(),
                        quizId,
                        new SubmitQuizAttemptRequest(List.of())
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private QuizQuestion question(Quiz quiz, String correctAnswer) {
        QuizQuestion question = new QuizQuestion();
        question.setId(UUID.randomUUID());
        question.setQuiz(quiz);
        question.setType(QuizQuestionType.MULTIPLE_CHOICE);
        question.setPrompt("What does Hallo mean?");
        question.setOptionsJson("[\"Hello\",\"Goodbye\"]");
        question.setCorrectAnswer(correctAnswer);
        question.setExplanation("Hallo means hello.");
        question.setPoints(2);
        question.setPosition(1);
        return question;
    }

    private Quiz quiz() {
        Quiz quiz = new Quiz();
        quiz.setId(UUID.randomUUID());
        quiz.setLesson(lesson());
        quiz.setTitle("Greetings check");
        quiz.setDescription("Check greetings");
        quiz.setStatus(QuizStatus.PUBLISHED);
        quiz.setPassingScore(70);
        quiz.setPosition(1);
        return quiz;
    }

    private User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Sara");
        user.setLastName("Ahmed");
        user.setEmail("sara@example.com");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        user.setEnabled(true);
        return user;
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
