export type CompleteLessonRequest = {
  score?: number | null;
};

export type LessonProgress = {
  lessonId: string;
  courseId: string;
  moduleId: string;
  lessonTitle: string;
  completed: boolean;
  completedAt?: string | null;
  score?: number | null;
  xpEarned: number;
};

export type ProgressSummary = {
  totalXp: number;
  completedLessons: number;
  activeDaysLastSevenDays: number;
  currentStreakDays: number;
  longestStreakDays: number;
  dailyGoalMinutes: number;
  dailyGoalXp: number;
  achievements: Achievement[];
  recentLessons: LessonProgress[];
};

export type XpEventType =
  | "LESSON_COMPLETED"
  | "QUIZ_PASSED"
  | "GRAMMAR_PRACTICE_COMPLETED"
  | "DAILY_GOAL_MET"
  | "ACHIEVEMENT_UNLOCKED";

export type AchievementType =
  | "FIRST_LESSON"
  | "FIVE_LESSONS"
  | "FIRST_QUIZ_PASS"
  | "XP_100";

export type Achievement = {
  id: string;
  type: AchievementType;
  title: string;
  description: string;
  xpReward: number;
  unlocked: boolean;
  unlockedAt?: string | null;
};
