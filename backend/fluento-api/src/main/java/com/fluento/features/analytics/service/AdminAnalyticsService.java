package com.fluento.features.analytics.service;

import com.fluento.features.analytics.dto.AdminAnalyticsResponse;
import com.fluento.features.course.enums.CourseStatus;
import com.fluento.features.course.repository.CourseModuleRepository;
import com.fluento.features.course.repository.CourseRepository;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.grammar.repository.GrammarRuleRepository;
import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.repository.LessonRepository;
import com.fluento.features.progress.repository.LessonProgressRepository;
import com.fluento.features.progress.repository.XpEventRepository;
import com.fluento.features.quiz.enums.QuizStatus;
import com.fluento.features.quiz.repository.QuizAttemptRepository;
import com.fluento.features.quiz.repository.QuizRepository;
import com.fluento.features.subscription.enums.SubscriptionStatus;
import com.fluento.features.subscription.repository.UserSubscriptionRepository;
import com.fluento.features.user.repository.UserRepository;
import com.fluento.features.vocabulary.repository.VocabularyTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {

    private static final List<SubscriptionStatus> ACTIVE_SUBSCRIPTION_STATUSES =
            List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.TRIALING);

    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final LanguageRepository languageRepository;
    private final CourseRepository courseRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final LessonRepository lessonRepository;
    private final VocabularyTermRepository vocabularyTermRepository;
    private final GrammarRuleRepository grammarRuleRepository;
    private final QuizRepository quizRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final XpEventRepository xpEventRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    @Transactional(readOnly = true)
    public AdminAnalyticsResponse getOverview() {
        return new AdminAnalyticsResponse(
                userRepository.countByDeletedAtIsNull(),
                userRepository.countByEnabledTrueAndDeletedAtIsNull(),
                userSubscriptionRepository.countByStatusInAndDeletedAtIsNull(
                        ACTIVE_SUBSCRIPTION_STATUSES
                ),
                languageRepository.countByEnabledTrueAndDeletedAtIsNull(),
                courseRepository.countByDeletedAtIsNull(),
                courseRepository.countByStatusAndDeletedAtIsNull(
                        CourseStatus.PUBLISHED
                ),
                courseModuleRepository.countByDeletedAtIsNull(),
                lessonRepository.countByDeletedAtIsNull(),
                lessonRepository.countByStatusAndDeletedAtIsNull(
                        LessonStatus.PUBLISHED
                ),
                vocabularyTermRepository.countByDeletedAtIsNull(),
                grammarRuleRepository.countByDeletedAtIsNull(),
                quizRepository.countByDeletedAtIsNull(),
                quizRepository.countByStatusAndDeletedAtIsNull(
                        QuizStatus.PUBLISHED
                ),
                lessonProgressRepository.countByCompletedTrueAndDeletedAtIsNull(),
                xpEventRepository.sumTotalXp(),
                quizAttemptRepository.countByDeletedAtIsNull(),
                quizAttemptRepository.countByPassedTrueAndDeletedAtIsNull(),
                quizAttemptRepository.averagePercentage()
        );
    }
}
