package com.fluento.features.progress.service;

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
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserProfileRepository;
import com.fluento.features.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
class ProgressServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LessonProgressRepository lessonProgressRepository;

    @Mock
    private XpEventRepository xpEventRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private UserAchievementRepository userAchievementRepository;

    @Mock
    private UserStreakRepository userStreakRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private ProgressService progressService;

    @Test
    void completeLessonCreatesProgressAndXpEvent() {
        User user = user();
        Lesson lesson = lesson();

        when(lessonProgressRepository
                .findByUser_IdAndLesson_IdAndDeletedAtIsNull(
                        user.getId(),
                        lesson.getId()
                ))
                .thenReturn(Optional.empty());
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(lessonRepository.findByIdAndStatusAndDeletedAtIsNull(
                lesson.getId(),
                LessonStatus.PUBLISHED
        ))
                .thenReturn(Optional.of(lesson));
        when(lessonProgressRepository.save(any(LessonProgress.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(lessonProgressRepository
                .countByUser_IdAndCompletedTrueAndDeletedAtIsNull(user.getId()))
                .thenReturn(1L);
        when(achievementRepository
                .findByTypeAndEnabledTrueAndDeletedAtIsNull(
                        AchievementType.FIRST_LESSON
                ))
                .thenReturn(Optional.empty());
        when(userStreakRepository
                .findByUser_IdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.empty());
        when(userStreakRepository.save(any(UserStreak.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LessonProgressResponse response = progressService.completeLesson(
                user.getId(),
                lesson.getId(),
                new CompleteLessonRequest(90)
        );

        assertThat(response.lessonId()).isEqualTo(lesson.getId());
        assertThat(response.completed()).isTrue();
        assertThat(response.score()).isEqualTo(90);
        assertThat(response.xpEarned()).isEqualTo(80);

        ArgumentCaptor<XpEvent> xpEvent = ArgumentCaptor
                .forClass(XpEvent.class);
        verify(xpEventRepository).save(xpEvent.capture());
        assertThat(xpEvent.getValue().getXp()).isEqualTo(80);
        assertThat(xpEvent.getValue().getDescription())
                .contains("German greetings");
    }

    @Test
    void completeLessonDoesNotAwardDuplicateXpWhenAlreadyCompleted() {
        User user = user();
        LessonProgress existing = completedProgress(user, lesson());

        when(lessonProgressRepository
                .findByUser_IdAndLesson_IdAndDeletedAtIsNull(
                        user.getId(),
                        existing.getLesson().getId()
                ))
                .thenReturn(Optional.of(existing));

        LessonProgressResponse response = progressService.completeLesson(
                user.getId(),
                existing.getLesson().getId(),
                new CompleteLessonRequest(100)
        );

        assertThat(response.xpEarned()).isEqualTo(80);
        verify(lessonProgressRepository, never()).save(any());
        verify(xpEventRepository, never()).save(any());
    }

    @Test
    void completeLessonThrowsWhenUserIsMissing() {
        UUID userId = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();

        when(lessonProgressRepository
                .findByUser_IdAndLesson_IdAndDeletedAtIsNull(userId, lessonId))
                .thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> progressService.completeLesson(
                        userId,
                        lessonId,
                        new CompleteLessonRequest(null)
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void completeLessonThrowsWhenLessonIsMissing() {
        User user = user();
        UUID lessonId = UUID.randomUUID();

        when(lessonProgressRepository
                .findByUser_IdAndLesson_IdAndDeletedAtIsNull(
                        user.getId(),
                        lessonId
                ))
                .thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(lessonRepository.findByIdAndStatusAndDeletedAtIsNull(
                lessonId,
                LessonStatus.PUBLISHED
        ))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> progressService.completeLesson(
                        user.getId(),
                        lessonId,
                        new CompleteLessonRequest(null)
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getSummaryReturnsUserProgressOverview() {
        User user = user();
        LessonProgress progress = completedProgress(user, lesson());

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(lessonProgressRepository.sumXpByUserId(user.getId()))
                .thenReturn(80L);
        when(lessonProgressRepository
                .countByUser_IdAndCompletedTrueAndDeletedAtIsNull(user.getId()))
                .thenReturn(1L);
        when(lessonProgressRepository
                .findByUser_IdAndCompletedTrueAndCompletedAtGreaterThanEqualAndDeletedAtIsNull(
                        any(UUID.class),
                        any(LocalDateTime.class)
                ))
                .thenReturn(List.of(progress));
        when(lessonProgressRepository
                .findTop5ByUser_IdAndCompletedTrueAndDeletedAtIsNullOrderByCompletedAtDesc(
                        user.getId()
                ))
                .thenReturn(List.of(progress));
        when(userStreakRepository.findByUser_IdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.of(streak(user, 3, 5)));
        when(userProfileRepository.findByUser_IdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.empty());
        when(userAchievementRepository
                .findByUser_IdAndDeletedAtIsNullOrderByUnlockedAtDesc(
                        user.getId()
                ))
                .thenReturn(List.of());
        when(achievementRepository
                .findByEnabledTrueAndDeletedAtIsNullOrderByTitleAsc())
                .thenReturn(List.of());

        ProgressSummaryResponse response =
                progressService.getSummary(user.getId());

        assertThat(response.totalXp()).isEqualTo(80);
        assertThat(response.completedLessons()).isEqualTo(1);
        assertThat(response.activeDaysLastSevenDays()).isEqualTo(1);
        assertThat(response.currentStreakDays()).isEqualTo(3);
        assertThat(response.longestStreakDays()).isEqualTo(5);
        assertThat(response.dailyGoalMinutes()).isEqualTo(10);
        assertThat(response.dailyGoalXp()).isEqualTo(50);
        assertThat(response.achievements()).isEmpty();
        assertThat(response.recentLessons()).hasSize(1);
    }

    @Test
    void recordLearningActivityIncrementsConsecutiveStreak() {
        User user = user();
        UserStreak streak = streak(user, 2, 4);
        streak.setLastActivityDate(java.time.LocalDate.now().minusDays(1));

        when(userStreakRepository.findByUser_IdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.of(streak));

        progressService.recordLearningActivity(user);

        assertThat(streak.getCurrentStreakDays()).isEqualTo(3);
        assertThat(streak.getLongestStreakDays()).isEqualTo(4);
        verify(userStreakRepository).save(streak);
    }

    @Test
    void evaluateAchievementsUnlocksFirstLessonAchievement() {
        User user = user();
        Achievement achievement = achievement(AchievementType.FIRST_LESSON);

        when(lessonProgressRepository
                .countByUser_IdAndCompletedTrueAndDeletedAtIsNull(user.getId()))
                .thenReturn(1L);
        when(lessonProgressRepository.sumXpByUserId(user.getId()))
                .thenReturn(80L);
        when(xpEventRepository
                .countByUser_IdAndEventTypeAndDeletedAtIsNull(
                        user.getId(),
                        XpEventType.QUIZ_PASSED
                ))
                .thenReturn(0L);
        when(achievementRepository
                .findByTypeAndEnabledTrueAndDeletedAtIsNull(
                        AchievementType.FIRST_LESSON
                ))
                .thenReturn(Optional.of(achievement));
        when(userAchievementRepository
                .existsByUser_IdAndAchievement_IdAndDeletedAtIsNull(
                        user.getId(),
                        achievement.getId()
                ))
                .thenReturn(false);
        when(userAchievementRepository.save(any(UserAchievement.class)))
                .thenAnswer(invocation -> {
                    UserAchievement userAchievement = invocation.getArgument(0);
                    userAchievement.setId(UUID.randomUUID());
                    return userAchievement;
                });

        progressService.evaluateAchievements(user);

        verify(userAchievementRepository).save(any(UserAchievement.class));
        verify(xpEventRepository).save(any(XpEvent.class));
    }

    private LessonProgress completedProgress(User user, Lesson lesson) {
        LessonProgress progress = new LessonProgress();
        progress.setId(UUID.randomUUID());
        progress.setUser(user);
        progress.setLesson(lesson);
        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        progress.setScore(90);
        progress.setXpEarned(80);
        return progress;
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

    private Achievement achievement(AchievementType type) {
        Achievement achievement = new Achievement();
        achievement.setId(UUID.randomUUID());
        achievement.setType(type);
        achievement.setTitle("First Lesson");
        achievement.setDescription("Complete your first lesson.");
        achievement.setXpReward(25);
        achievement.setEnabled(true);
        return achievement;
    }

    private UserStreak streak(
            User user,
            int currentStreakDays,
            int longestStreakDays
    ) {
        UserStreak streak = new UserStreak();
        streak.setId(UUID.randomUUID());
        streak.setUser(user);
        streak.setCurrentStreakDays(currentStreakDays);
        streak.setLongestStreakDays(longestStreakDays);
        streak.setLastActivityDate(java.time.LocalDate.now());
        return streak;
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
