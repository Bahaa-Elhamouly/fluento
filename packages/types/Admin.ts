import type { CourseStatus, CourseModule } from "./Course";
import type { GrammarExample, GrammarRule } from "./Grammar";
import type { LessonStatus, LessonType } from "./Lesson";
import type { QuizQuestionType, QuizStatus } from "./Quiz";
import type { LearningLevel, SupportedLanguageCode, UserRole } from "./User";

export type AdminUser = {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  role: UserRole;
  enabled: boolean;
  emailVerified: boolean;
  createdAt: string;
  updatedAt: string;
};

export type AdminUpdateUserRequest = {
  firstName?: string | null;
  lastName?: string | null;
  role?: UserRole | null;
  enabled?: boolean | null;
  emailVerified?: boolean | null;
};

export type AdminCourseRequest = {
  languageCode: SupportedLanguageCode;
  title: string;
  slug: string;
  description: string;
  level: LearningLevel;
  status: CourseStatus;
  position: number;
};

export type AdminCourseModuleRequest = Pick<
  CourseModule,
  "title" | "description" | "position"
>;

export type AdminLessonRequest = {
  title: string;
  description?: string | null;
  type: LessonType;
  status: LessonStatus;
  content: string;
  estimatedMinutes: number;
  position: number;
};

export type AdminVocabularyTermRequest = {
  languageCode: SupportedLanguageCode;
  term: string;
  translation: string;
  pronunciation?: string | null;
  example?: string | null;
  tags: string[];
  enabled: boolean;
};

export type AdminGrammarExampleRequest = Pick<
  GrammarExample,
  "sourceText" | "translation" | "note" | "position"
>;

export type AdminGrammarRuleRequest = Pick<
  GrammarRule,
  "languageCode" | "title" | "slug" | "explanation" | "level" | "position"
> & {
  enabled: boolean;
  examples?: AdminGrammarExampleRequest[];
};

export type AdminQuizQuestionRequest = {
  type: QuizQuestionType;
  prompt: string;
  options?: string[];
  correctAnswer: string;
  explanation?: string | null;
  points: number;
  position: number;
};

export type AdminQuizRequest = {
  title: string;
  description?: string | null;
  status: QuizStatus;
  passingScore: number;
  position: number;
  questions?: AdminQuizQuestionRequest[];
};

export type AdminAnalyticsOverview = {
  totalUsers: number;
  activeUsers: number;
  activeSubscriptions: number;
  enabledLanguages: number;
  totalCourses: number;
  publishedCourses: number;
  totalModules: number;
  totalLessons: number;
  publishedLessons: number;
  vocabularyTerms: number;
  grammarRules: number;
  totalQuizzes: number;
  publishedQuizzes: number;
  completedLessons: number;
  totalXpAwarded: number;
  quizAttempts: number;
  passedQuizAttempts: number;
  averageQuizPercentage: number;
};
