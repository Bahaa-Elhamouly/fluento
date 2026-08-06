export type LessonType =
  | "READING"
  | "LISTENING"
  | "VOCABULARY"
  | "GRAMMAR"
  | "EXERCISE";

export type LessonStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";

export type Lesson = {
  id: string;
  moduleId: string;
  courseId?: string;
  title: string;
  description?: string;
  type: LessonType;
  status: LessonStatus;
  content?: string;
  estimatedMinutes: number;
  position: number;
};

export type LessonProgress = {
  lessonId: string;
  completed: boolean;
  completedAt?: string;
  score?: number;
  xpEarned: number;
};
