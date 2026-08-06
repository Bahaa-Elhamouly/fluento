package com.fluento.features.grammar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.grammar.dto.GrammarPracticeAnswerRequest;
import com.fluento.features.grammar.dto.GrammarPracticeResultResponse;
import com.fluento.features.grammar.dto.SubmitGrammarPracticeRequest;
import com.fluento.features.grammar.entity.GrammarExercise;
import com.fluento.features.grammar.entity.GrammarPracticeAttempt;
import com.fluento.features.grammar.entity.GrammarPracticeAttemptAnswer;
import com.fluento.features.grammar.entity.GrammarRule;
import com.fluento.features.grammar.enums.GrammarExerciseType;
import com.fluento.features.grammar.repository.GrammarExerciseRepository;
import com.fluento.features.grammar.repository.GrammarPracticeAttemptAnswerRepository;
import com.fluento.features.grammar.repository.GrammarPracticeAttemptRepository;
import com.fluento.features.grammar.repository.GrammarRuleRepository;
import com.fluento.features.progress.entity.XpEvent;
import com.fluento.features.progress.repository.XpEventRepository;
import com.fluento.features.progress.service.ProgressService;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrammarPracticeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GrammarRuleRepository grammarRuleRepository;

    @Mock
    private GrammarExerciseRepository grammarExerciseRepository;

    @Mock
    private GrammarPracticeAttemptRepository attemptRepository;

    @Mock
    private GrammarPracticeAttemptAnswerRepository answerRepository;

    @Mock
    private XpEventRepository xpEventRepository;

    @Mock
    private ProgressService progressService;

    private GrammarPracticeService grammarPracticeService;

    @BeforeEach
    void setUp() {
        grammarPracticeService = new GrammarPracticeService(
                userRepository,
                grammarRuleRepository,
                grammarExerciseRepository,
                attemptRepository,
                answerRepository,
                xpEventRepository,
                progressService,
                new ObjectMapper()
        );
    }

    @Test
    void submitAttemptScoresAnswersAndAwardsXp() {
        User user = user();
        GrammarRule rule = rule();
        GrammarExercise exercise = exercise(rule);

        when(userRepository.findByIdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.of(user));
        when(grammarRuleRepository.findByIdAndDeletedAtIsNull(rule.getId()))
                .thenReturn(Optional.of(rule));
        when(grammarExerciseRepository
                .findByRule_IdAndEnabledTrueAndDeletedAtIsNullOrderByPositionAsc(
                        rule.getId()
                ))
                .thenReturn(List.of(exercise));
        when(attemptRepository.save(any(GrammarPracticeAttempt.class)))
                .thenAnswer(invocation -> {
                    GrammarPracticeAttempt attempt = invocation.getArgument(0);
                    if (attempt.getId() == null) {
                        attempt.setId(UUID.randomUUID());
                    }
                    return attempt;
                });
        when(answerRepository.save(any(GrammarPracticeAttemptAnswer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GrammarPracticeResultResponse response =
                grammarPracticeService.submitAttempt(
                        user.getId(),
                        rule.getId(),
                        new SubmitGrammarPracticeRequest(
                                List.of(
                                        new GrammarPracticeAnswerRequest(
                                                exercise.getId(),
                                                "Second position"
                                        )
                                )
                        )
                );

        assertThat(response.score()).isEqualTo(2);
        assertThat(response.maxScore()).isEqualTo(2);
        assertThat(response.percentage()).isEqualTo(100);
        assertThat(response.xpEarned()).isEqualTo(10);
        assertThat(response.feedback()).singleElement()
                .satisfies(feedback -> assertThat(feedback.correct()).isTrue());

        ArgumentCaptor<XpEvent> xpEvent = ArgumentCaptor.forClass(XpEvent.class);
        verify(xpEventRepository).save(xpEvent.capture());
        assertThat(xpEvent.getValue().getXp()).isEqualTo(10);
        verify(progressService).recordLearningActivity(user);
        verify(progressService).evaluateAchievements(user);
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

    private GrammarRule rule() {
        GrammarRule rule = new GrammarRule();
        rule.setId(UUID.randomUUID());
        rule.setLanguage(language());
        rule.setTitle("German verb position");
        rule.setSlug("german-verb-position");
        rule.setExplanation("Verb second.");
        rule.setLevel(LearningLevel.BEGINNER);
        rule.setEnabled(true);
        rule.setPosition(1);
        return rule;
    }

    private GrammarExercise exercise(GrammarRule rule) {
        GrammarExercise exercise = new GrammarExercise();
        exercise.setId(UUID.randomUUID());
        exercise.setRule(rule);
        exercise.setType(GrammarExerciseType.MULTIPLE_CHOICE);
        exercise.setPrompt("Where is the verb?");
        exercise.setOptionsJson("[\"First position\",\"Second position\"]");
        exercise.setCorrectAnswer("Second position");
        exercise.setExplanation("Verb second.");
        exercise.setLevel(LearningLevel.BEGINNER);
        exercise.setPoints(2);
        exercise.setPosition(1);
        exercise.setEnabled(true);
        return exercise;
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
