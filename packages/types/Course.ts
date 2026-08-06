import type { LearningLevel, SupportedLanguageCode } from "./User";

export type CourseStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";

export type Course = {
  id: string;
  languageCode: SupportedLanguageCode;
  languageName: string;
  title: string;
  slug: string;
  description: string;
  level: LearningLevel;
  status: CourseStatus;
  position: number;
};

export type CourseModule = {
  id: string;
  courseId: string;
  title: string;
  description?: string;
  position: number;
  lessonCount?: number;
};
