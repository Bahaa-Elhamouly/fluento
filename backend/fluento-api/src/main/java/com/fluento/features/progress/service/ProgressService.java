package com.fluento.features.progress.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.lesson.entity.Lesson;
import com.fluento.features.lesson.enums.LessonStatus;
import com.fluento.features.lesson.repository.LessonRepository;
import com.fluento.features.progress.dto.AchievementResponse;
import com.fluento.features.progress.dto.CompleteLessonRequest;
import com.fluento.features.progress.dto.LessonProgressResponse;
import com.fluento.features.progress.dto.ProgressSummaryResponse;
import com.fluento.features.progress.entity.Achievement;
import com.fluento.features.progress.entity.LessonProgress;
import com.fluento.features.progress.entity.UserAchievement;
import com.fluento.features.progress.entity.UserStreak;
import com.fluento.features.progress.entity.XpEvent;
import com.fluento.features.progress.enums.AchievementType;
import com.fluento.features.progress.enums.XpEventType;
import com.fluento.features.progress.repository.AchievementRepository;
import com.fluento.features.progress.repository.LessonProgressRepository;
import com.fluento.features.progress.repository.UserAchievementRepository;
import com.fluento.features.progress.repository.UserStreakRepository;
import com.fluento.features.progress.repository.XpEventRepository;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.entity.UserProfile;
import com.fluento.features.user.repository.UserProfileRepository;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private static final int XP_PER_LESSON_MINUTE = 10;
    private static final int MINIMUM_LESSON_XP = 10;
    private static final int DEFAULT_DAILY_GOAL_MINUTES = 10;
    private static final int XP_PER_GOAL_MINUTE = 5;

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final XpEventRepository xpEventRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserStreakRepository userStreakRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    public LessonProgressResponse completeLesson(
            UUID userId,
            UUID lessonId,
            CompleteLessonRequest request
    ) {
        LessonProgress existingProgress = lessonProgressRepository
                .findByUser_IdAndLesson_IdAndDeletedAtIsNull(userId, lessonId)
                .orElse(null);

        if (existingProgress != null && existingProgress.isCompleted()) {
            return toLessonProgressResponse(existingProgress);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Lesson lesson = lessonRepository
                .findByIdAndStatusAndDeletedAtIsNull(
                        lessonId,
                        LessonStatus.PUBLISHED
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException("Lesson", lessonId)
                );

        LessonProgress progress = existingProgress == null
                ? new LessonProgress()
                : existingProgress;
        progress.setUser(user);
        progress.setLesson(lesson);
        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        progress.setScore(request == null ? null : request.score());
        progress.setXpEarned(calculateLessonXp(lesson));

        LessonProgress savedProgress = lessonProgressRepository.save(progress);
        xpEventRepository.save(toXpEvent(user, lesson, savedProgress));
        recordLearningActivity(user);
        evaluateAchievements(user);

        return toLessonProgressResponse(savedProgress);
    }

    @Transactional(readOnly = true)
    public ProgressSummaryResponse getSummary(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(6)
                .toLocalDate()
                .atStartOfDay();

        UserStreak streak = userStreakRepository
                .findByUser_IdAndDeletedAtIsNull(userId)
                .orElse(null);
        int dailyGoalMinutes = getDailyGoalMinutes(userId);

        return new ProgressSummaryResponse(
                lessonProgressRepository.sumXpByUserId(userId),
                lessonProgressRepository
                        .countByUser_IdAndCompletedTrueAndDeletedAtIsNull(
                                userId
                        ),
                countActiveLearningDaysSince(
                        userId,
                        sevenDaysAgo
                ),
                streak == null ? 0 : streak.getCurrentStreakDays(),
                streak == null ? 0 : streak.getLongestStreakDays(),
                dailyGoalMinutes,
                dailyGoalMinutes * XP_PER_GOAL_MINUTE,
                getAchievements(userId),
                lessonProgressRepository
                        .findTop5ByUser_IdAndCompletedTrueAndDeletedAtIsNullOrderByCompletedAtDesc(
                                userId
                        )
                        .stream()
                        .map(this::toLessonProgressResponse)
                        .toList()
        );
    }

    @Transactional
    public void recordLearningActivity(User user) {
        LocalDate today = LocalDate.now();
        UserStreak streak = userStreakRepository
                .findByUser_IdAndDeletedAtIsNull(user.getId())
                .orElseGet(() -> createStreak(user));

        LocalDate lastActivityDate = streak.getLastActivityDate();
        if (today.equals(lastActivityDate)) {
            return;
        }

        if (lastActivityDate == null) {
            streak.setCurrentStreakDays(1);
        } else if (today.minusDays(1).equals(lastActivityDate)) {
            streak.setCurrentStreakDays(streak.getCurrentStreakDays() + 1);
        } else {
            streak.setCurrentStreakDays(1);
        }

        streak.setLongestStreakDays(
                Math.max(
                        streak.getLongestStreakDays(),
                        streak.getCurrentStreakDays()
                )
        );
        streak.setLastActivityDate(today);
        userStreakRepository.save(streak);
    }

    private UserStreak createStreak(User user) {
        UserStreak streak = new UserStreak();
        streak.setUser(user);
        streak.setCurrentStreakDays(0);
        streak.setLongestStreakDays(0);
        return streak;
    }

    private int calculateLessonXp(Lesson lesson) {
        return Math.max(
                MINIMUM_LESSON_XP,
                lesson.getEstimatedMinutes() * XP_PER_LESSON_MINUTE
        );
    }

    private long countActiveLearningDaysSince(
            UUID userId,
            LocalDateTime from
    ) {
        Set<LocalDate> activeDates = lessonProgressRepository
                .findByUser_IdAndCompletedTrueAndCompletedAtGreaterThanEqualAndDeletedAtIsNull(
                        userId,
                        from
                )
                .stream()
                .map(progress -> progress.getCompletedAt().toLocalDate())
                .collect(Collectors.toSet());

        return activeDates.size();
    }

    public void evaluateAchievements(User user) {
        long completedLessons = lessonProgressRepository
                .countByUser_IdAndCompletedTrueAndDeletedAtIsNull(user.getId());
        long totalXp = lessonProgressRepository.sumXpByUserId(user.getId());
        long quizPasses = xpEventRepository
                .countByUser_IdAndEventTypeAndDeletedAtIsNull(
                        user.getId(),
                        XpEventType.QUIZ_PASSED
                );

        unlockWhen(user, AchievementType.FIRST_LESSON, completedLessons >= 1);
        unlockWhen(user, AchievementType.FIVE_LESSONS, completedLessons >= 5);
        unlockWhen(user, AchievementType.XP_100, totalXp >= 100);
        unlockWhen(user, AchievementType.FIRST_QUIZ_PASS, quizPasses >= 1);
    }

    private void unlockWhen(
            User user,
            AchievementType type,
            boolean condition
    ) {
        if (!condition) {
            return;
        }

        achievementRepository
                .findByTypeAndEnabledTrueAndDeletedAtIsNull(type)
                .ifPresent(achievement -> unlock(user, achievement));
    }

    private void unlock(User user, Achievement achievement) {
        if (userAchievementRepository
                .existsByUser_IdAndAchievement_IdAndDeletedAtIsNull(
                        user.getId(),
                        achievement.getId()
                )) {
            return;
        }

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setUnlockedAt(LocalDateTime.now());
        userAchievementRepository.save(userAchievement);

        XpEvent xpEvent = new XpEvent();
        xpEvent.setUser(user);
        xpEvent.setEventType(XpEventType.ACHIEVEMENT_UNLOCKED);
        xpEvent.setSourceId(userAchievement.getId());
        xpEvent.setXp(achievement.getXpReward());
        xpEvent.setDescription("Unlocked achievement: " + achievement.getTitle());
        xpEventRepository.save(xpEvent);
    }

    private List<AchievementResponse> getAchievements(UUID userId) {
        Map<UUID, UserAchievement> unlockedByAchievementId =
                userAchievementRepository
                        .findByUser_IdAndDeletedAtIsNullOrderByUnlockedAtDesc(
                                userId
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                userAchievement -> userAchievement
                                        .getAchievement()
                                        .getId(),
                                userAchievement -> userAchievement,
                                (first, ignored) -> first,
                                HashMap::new
                        ));

        return achievementRepository
                .findByEnabledTrueAndDeletedAtIsNullOrderByTitleAsc()
                .stream()
                .map(achievement -> {
                    UserAchievement userAchievement =
                            unlockedByAchievementId.get(achievement.getId());
                    return new AchievementResponse(
                            achievement.getId(),
                            achievement.getType(),
                            achievement.getTitle(),
                            achievement.getDescription(),
                            achievement.getXpReward(),
                            userAchievement != null,
                            userAchievement == null
                                    ? null
                                    : userAchievement.getUnlockedAt()
                    );
                })
                .toList();
    }

    private XpEvent toXpEvent(
            User user,
            Lesson lesson,
            LessonProgress progress
    ) {
        XpEvent xpEvent = new XpEvent();
        xpEvent.setUser(user);
        xpEvent.setEventType(XpEventType.LESSON_COMPLETED);
        xpEvent.setSourceId(progress.getId());
        xpEvent.setXp(progress.getXpEarned());
        xpEvent.setDescription("Completed lesson: " + lesson.getTitle());
        return xpEvent;
    }

    private LessonProgressResponse toLessonProgressResponse(
            LessonProgress progress
    ) {
        Lesson lesson = progress.getLesson();

        return new LessonProgressResponse(
                lesson.getId(),
                lesson.getModule().getCourse().getId(),
                lesson.getModule().getId(),
                lesson.getTitle(),
                progress.isCompleted(),
                progress.getCompletedAt(),
                progress.getScore(),
                progress.getXpEarned()
        );
    }

    private int getDailyGoalMinutes(UUID userId) {
        return userProfileRepository.findByUser_IdAndDeletedAtIsNull(userId)
                .map(UserProfile::getDailyGoalMinutes)
                .filter(minutes -> minutes != null)
                .orElse(DEFAULT_DAILY_GOAL_MINUTES);
    }
}
