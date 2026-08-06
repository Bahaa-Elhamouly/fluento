package com.fluento.features.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.lesson.repository.LessonRepository;
import com.fluento.features.quiz.dto.QuizQuestionResponse;
import com.fluento.features.quiz.dto.QuizResponse;
import com.fluento.features.quiz.entity.Quiz;
import com.fluento.features.quiz.entity.QuizQuestion;
import com.fluento.features.quiz.enums.QuizStatus;
import com.fluento.features.quiz.repository.QuizQuestionRepository;
import com.fluento.features.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizCatalogService {

    private static final TypeReference<List<String>> STRING_LIST_TYPE =
            new TypeReference<>() {
            };

    private final LessonRepository lessonRepository;
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public QuizResponse getPublishedQuizForLesson(UUID lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new ResourceNotFoundException("Lesson", lessonId);
        }

        Quiz quiz = quizRepository
                .findFirstByLesson_IdAndStatusAndDeletedAtIsNullOrderByPositionAsc(
                        lessonId,
                        QuizStatus.PUBLISHED
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Quiz for lesson",
                                lessonId
                        )
                );

        return toResponse(quiz);
    }

    @Transactional(readOnly = true)
    public QuizResponse getPublishedQuiz(UUID quizId) {
        Quiz quiz = quizRepository
                .findByIdAndStatusAndDeletedAtIsNull(
                        quizId,
                        QuizStatus.PUBLISHED
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException("Quiz", quizId)
                );

        return toResponse(quiz);
    }

    private QuizResponse toResponse(Quiz quiz) {
        List<QuizQuestionResponse> questions = quizQuestionRepository
                .findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quiz.getId())
                .stream()
                .map(this::toQuestionResponse)
                .toList();

        return new QuizResponse(
                quiz.getId(),
                quiz.getLesson().getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getPassingScore(),
                quiz.getPosition(),
                questions
        );
    }

    private QuizQuestionResponse toQuestionResponse(QuizQuestion question) {
        return new QuizQuestionResponse(
                question.getId(),
                question.getType(),
                question.getPrompt(),
                parseOptions(question.getOptionsJson()),
                question.getPoints(),
                question.getPosition()
        );
    }

    private List<String> parseOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.isBlank()) {
            return List.of();
        }

        try {
            return objectMapper.readValue(optionsJson, STRING_LIST_TYPE);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Quiz question options are not valid JSON",
                    exception
            );
        }
    }
}
