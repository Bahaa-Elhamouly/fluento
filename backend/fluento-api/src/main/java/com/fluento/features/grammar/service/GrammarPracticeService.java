package com.fluento.features.grammar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.grammar.dto.GrammarExerciseResponse;
import com.fluento.features.grammar.dto.GrammarPracticeFeedbackResponse;
import com.fluento.features.grammar.dto.GrammarPracticeResultResponse;
import com.fluento.features.grammar.dto.SubmitGrammarPracticeRequest;
import com.fluento.features.grammar.entity.GrammarExercise;
import com.fluento.features.grammar.entity.GrammarPracticeAttempt;
import com.fluento.features.grammar.entity.GrammarPracticeAttemptAnswer;
import com.fluento.features.grammar.entity.GrammarRule;
import com.fluento.features.grammar.repository.GrammarExerciseRepository;
import com.fluento.features.grammar.repository.GrammarPracticeAttemptAnswerRepository;
import com.fluento.features.grammar.repository.GrammarPracticeAttemptRepository;
import com.fluento.features.grammar.repository.GrammarRuleRepository;
import com.fluento.features.progress.entity.XpEvent;
import com.fluento.features.progress.enums.XpEventType;
import com.fluento.features.progress.repository.XpEventRepository;
import com.fluento.features.progress.service.ProgressService;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrammarPracticeService {

    private static final TypeReference<List<String>> STRING_LIST_TYPE =
            new TypeReference<>() {
            };
    private static final int XP_PER_POINT = 5;

    private final UserRepository userRepository;
    private final GrammarRuleRepository grammarRuleRepository;
    private final GrammarExerciseRepository grammarExerciseRepository;
    private final GrammarPracticeAttemptRepository attemptRepository;
    private final GrammarPracticeAttemptAnswerRepository answerRepository;
    private final XpEventRepository xpEventRepository;
    private final ProgressService progressService;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<GrammarExerciseResponse> getExercises(UUID ruleId) {
        if (grammarRuleRepository.findByIdAndDeletedAtIsNull(ruleId).isEmpty()) {
            throw new ResourceNotFoundException("Grammar rule", ruleId);
        }

        return grammarExerciseRepository
                .findByRule_IdAndEnabledTrueAndDeletedAtIsNullOrderByPositionAsc(ruleId)
                .stream()
                .map(this::toExerciseResponse)
                .toList();
    }

    @Transactional
    public GrammarPracticeResultResponse submitAttempt(
            UUID userId,
            UUID ruleId,
            SubmitGrammarPracticeRequest request
    ) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        GrammarRule rule = grammarRuleRepository.findByIdAndDeletedAtIsNull(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Grammar rule", ruleId));
        List<GrammarExercise> exercises = grammarExerciseRepository
                .findByRule_IdAndEnabledTrueAndDeletedAtIsNullOrderByPositionAsc(ruleId);
        Map<UUID, GrammarExercise> exercisesById = exercises.stream()
                .collect(Collectors.toMap(GrammarExercise::getId, Function.identity()));
        request.answers().forEach(answer -> {
            if (!exercisesById.containsKey(answer.exerciseId())) {
                throw new ResourceNotFoundException(
                        "Grammar exercise",
                        answer.exerciseId()
                );
            }
        });
        Map<UUID, String> submittedAnswers = request.answers().stream()
                .collect(Collectors.toMap(
                        answer -> answer.exerciseId(),
                        answer -> answer.answer().trim(),
                        (first, ignored) -> first
                ));

        int maxScore = exercises.stream().mapToInt(GrammarExercise::getPoints).sum();
        int score = 0;
        GrammarPracticeAttempt attempt = new GrammarPracticeAttempt();
        attempt.setUser(user);
        attempt.setRule(rule);
        attempt.setMaxScore(maxScore);
        attempt.setScore(0);
        attempt.setPercentage(0);
        attempt.setXpEarned(0);
        GrammarPracticeAttempt savedAttempt = attemptRepository.save(attempt);

        for (GrammarExercise exercise : exercises) {
            String submittedAnswer = submittedAnswers.get(exercise.getId());
            boolean correct = submittedAnswer != null
                    && normalize(submittedAnswer).equals(normalize(exercise.getCorrectAnswer()));
            int pointsAwarded = correct ? exercise.getPoints() : 0;
            score += pointsAwarded;

            GrammarPracticeAttemptAnswer answer = new GrammarPracticeAttemptAnswer();
            answer.setAttempt(savedAttempt);
            answer.setExercise(exercise);
            answer.setSubmittedAnswer(submittedAnswer == null ? "" : submittedAnswer);
            answer.setCorrect(correct);
            answer.setPointsAwarded(pointsAwarded);
            answerRepository.save(answer);
        }

        int percentage = maxScore == 0 ? 0 : Math.round((score * 100f) / maxScore);
        int xpEarned = score * XP_PER_POINT;
        savedAttempt.setScore(score);
        savedAttempt.setPercentage(percentage);
        savedAttempt.setXpEarned(xpEarned);
        attemptRepository.save(savedAttempt);

        if (xpEarned > 0) {
            xpEventRepository.save(toXpEvent(user, rule, savedAttempt));
            progressService.recordLearningActivity(user);
            progressService.evaluateAchievements(user);
        }

        return new GrammarPracticeResultResponse(
                savedAttempt.getId(),
                rule.getId(),
                score,
                maxScore,
                percentage,
                xpEarned,
                exercises.stream()
                        .map(exercise -> toFeedback(exercise, submittedAnswers.get(exercise.getId())))
                        .toList()
        );
    }

    private GrammarExerciseResponse toExerciseResponse(GrammarExercise exercise) {
        return new GrammarExerciseResponse(
                exercise.getId(),
                exercise.getRule().getId(),
                exercise.getType(),
                exercise.getPrompt(),
                parseOptions(exercise.getOptionsJson()),
                exercise.getExplanation(),
                exercise.getLevel(),
                exercise.getPoints(),
                exercise.getPosition()
        );
    }

    private GrammarPracticeFeedbackResponse toFeedback(
            GrammarExercise exercise,
            String submittedAnswer
    ) {
        boolean correct = submittedAnswer != null
                && normalize(submittedAnswer).equals(normalize(exercise.getCorrectAnswer()));
        return new GrammarPracticeFeedbackResponse(
                exercise.getId(),
                correct,
                correct ? exercise.getPoints() : 0,
                exercise.getPoints(),
                correct ? "Correct answer" : "Review the rule explanation and try again"
        );
    }

    private XpEvent toXpEvent(
            User user,
            GrammarRule rule,
            GrammarPracticeAttempt attempt
    ) {
        XpEvent xpEvent = new XpEvent();
        xpEvent.setUser(user);
        xpEvent.setEventType(XpEventType.GRAMMAR_PRACTICE_COMPLETED);
        xpEvent.setSourceId(attempt.getId());
        xpEvent.setXp(attempt.getXpEarned());
        xpEvent.setDescription("Completed grammar practice: " + rule.getTitle());
        return xpEvent;
    }

    private String normalize(String value) {
        return value.trim().toLowerCase();
    }

    private List<String> parseOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.isBlank()) {
            return List.of();
        }

        try {
            return objectMapper.readValue(optionsJson, STRING_LIST_TYPE);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Grammar exercise options are not valid JSON", exception);
        }
    }
}
