package com.fluento.features.quiz.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.progress.entity.XpEvent;
import com.fluento.features.progress.enums.XpEventType;
import com.fluento.features.progress.repository.XpEventRepository;
import com.fluento.features.progress.service.ProgressService;
import com.fluento.features.quiz.dto.QuizAttemptAnswerRequest;
import com.fluento.features.quiz.dto.QuizAttemptResultResponse;
import com.fluento.features.quiz.dto.QuizQuestionFeedbackResponse;
import com.fluento.features.quiz.dto.SubmitQuizAttemptRequest;
import com.fluento.features.quiz.entity.Quiz;
import com.fluento.features.quiz.entity.QuizAttempt;
import com.fluento.features.quiz.entity.QuizAttemptAnswer;
import com.fluento.features.quiz.entity.QuizQuestion;
import com.fluento.features.quiz.enums.QuizStatus;
import com.fluento.features.quiz.repository.QuizAttemptAnswerRepository;
import com.fluento.features.quiz.repository.QuizAttemptRepository;
import com.fluento.features.quiz.repository.QuizQuestionRepository;
import com.fluento.features.quiz.repository.QuizRepository;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {

    private static final int XP_PER_POINT = 10;

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizAttemptAnswerRepository quizAttemptAnswerRepository;
    private final XpEventRepository xpEventRepository;
    private final ProgressService progressService;

    @Transactional
    public QuizAttemptResultResponse submitAttempt(
            UUID userId,
            UUID quizId,
            SubmitQuizAttemptRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Quiz quiz = quizRepository.findByIdAndStatusAndDeletedAtIsNull(
                quizId,
                QuizStatus.PUBLISHED
        )
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", quizId));

        List<QuizQuestion> questions = quizQuestionRepository
                .findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quizId);
        Map<UUID, QuizAttemptAnswerRequest> submittedAnswers =
                request.answers()
                        .stream()
                        .collect(Collectors.toMap(
                                QuizAttemptAnswerRequest::questionId,
                                Function.identity(),
                                (first, ignored) -> first,
                                LinkedHashMap::new
                        ));

        int maxScore = questions.stream()
                .mapToInt(QuizQuestion::getPoints)
                .sum();
        int score = 0;
        List<QuizQuestionFeedbackResponse> feedback = new ArrayList<>();

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setMaxScore(maxScore);

        for (QuizQuestion question : questions) {
            QuizAttemptAnswerRequest answer =
                    submittedAnswers.get(question.getId());
            String submittedAnswer = answer == null ? "" : answer.answer();
            boolean correct = isCorrect(submittedAnswer, question);
            int pointsAwarded = correct ? question.getPoints() : 0;
            score += pointsAwarded;

            feedback.add(new QuizQuestionFeedbackResponse(
                    question.getId(),
                    correct,
                    pointsAwarded,
                    question.getPoints(),
                    correct
                            ? "Correct answer"
                            : question.getExplanation()
            ));
        }

        int percentage = maxScore == 0
                ? 0
                : Math.round((score * 100.0f) / maxScore);
        boolean passed = percentage >= quiz.getPassingScore();
        int xpEarned = passed ? score * XP_PER_POINT : 0;

        attempt.setScore(score);
        attempt.setPercentage(percentage);
        attempt.setPassed(passed);
        attempt.setXpEarned(xpEarned);
        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

        saveAttemptAnswers(savedAttempt, questions, submittedAnswers);
        if (passed && xpEarned > 0) {
            xpEventRepository.save(toXpEvent(user, quiz, savedAttempt));
            progressService.recordLearningActivity(user);
            progressService.evaluateAchievements(user);
        }

        return new QuizAttemptResultResponse(
                savedAttempt.getId(),
                quiz.getId(),
                score,
                maxScore,
                percentage,
                passed,
                xpEarned,
                feedback
        );
    }

    private void saveAttemptAnswers(
            QuizAttempt attempt,
            List<QuizQuestion> questions,
            Map<UUID, QuizAttemptAnswerRequest> submittedAnswers
    ) {
        List<QuizAttemptAnswer> answers = questions.stream()
                .map(question -> toAttemptAnswer(
                        attempt,
                        question,
                        submittedAnswers.get(question.getId())
                ))
                .toList();

        quizAttemptAnswerRepository.saveAll(answers);
    }

    private QuizAttemptAnswer toAttemptAnswer(
            QuizAttempt attempt,
            QuizQuestion question,
            QuizAttemptAnswerRequest answer
    ) {
        String submittedAnswer = answer == null ? "" : answer.answer();
        boolean correct = isCorrect(submittedAnswer, question);

        QuizAttemptAnswer attemptAnswer = new QuizAttemptAnswer();
        attemptAnswer.setAttempt(attempt);
        attemptAnswer.setQuestion(question);
        attemptAnswer.setSubmittedAnswer(submittedAnswer);
        attemptAnswer.setCorrect(correct);
        attemptAnswer.setPointsAwarded(correct ? question.getPoints() : 0);
        return attemptAnswer;
    }

    private boolean isCorrect(
            String submittedAnswer,
            QuizQuestion question
    ) {
        return normalizeAnswer(submittedAnswer)
                .equals(normalizeAnswer(question.getCorrectAnswer()));
    }

    private String normalizeAnswer(String answer) {
        if (answer == null) {
            return "";
        }

        return answer.trim().toLowerCase(Locale.ROOT);
    }

    private XpEvent toXpEvent(
            User user,
            Quiz quiz,
            QuizAttempt attempt
    ) {
        XpEvent xpEvent = new XpEvent();
        xpEvent.setUser(user);
        xpEvent.setEventType(XpEventType.QUIZ_PASSED);
        xpEvent.setSourceId(attempt.getId());
        xpEvent.setXp(attempt.getXpEarned());
        xpEvent.setDescription("Passed quiz: " + quiz.getTitle());
        return xpEvent;
    }
}
