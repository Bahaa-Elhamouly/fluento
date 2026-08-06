import type { Course } from "../../../../packages/types/Course";
import type {
  GrammarExercise,
  GrammarPracticeRequest,
  GrammarPracticeResult,
  GrammarRuleSummary,
} from "../../../../packages/types/Grammar";
import type { Lesson } from "../../../../packages/types/Lesson";
import type { ProgressSummary } from "../../../../packages/types/Progress";
import type { Quiz, QuizAttemptRequest, QuizAttemptResult } from "../../../../packages/types/Quiz";
import type { VocabularyTerm } from "../../../../packages/types/Vocabulary";
import { apiRequest } from "./client";

export function getCourses(language?: string) {
  const query = language ? `?language=${encodeURIComponent(language)}` : "";
  return apiRequest<Course[]>(`/api/v1/catalog/courses${query}`);
}

export function getLesson(lessonId: string) {
  return apiRequest<Lesson>(`/api/v1/catalog/lessons/${lessonId}`);
}

export function getVocabulary(language: string, search?: string) {
  const params = new URLSearchParams({ language });
  if (search) {
    params.set("search", search);
  }
  return apiRequest<VocabularyTerm[]>(`/api/v1/catalog/vocabulary?${params}`);
}

export function getGrammar(language: string, level?: string) {
  const params = new URLSearchParams({ language });
  if (level) {
    params.set("level", level);
  }
  return apiRequest<GrammarRuleSummary[]>(`/api/v1/catalog/grammar?${params}`);
}

export function getGrammarExercises(ruleId: string) {
  return apiRequest<GrammarExercise[]>(`/api/v1/grammar/${ruleId}/exercises`);
}

export function submitGrammarPractice(
  ruleId: string,
  request: GrammarPracticeRequest,
  accessToken: string,
) {
  return apiRequest<GrammarPracticeResult>(
    `/api/v1/grammar/${ruleId}/practice-attempts`,
    {
      accessToken,
      body: JSON.stringify(request),
      method: "POST",
    },
  );
}

export function getQuizForLesson(lessonId: string) {
  return apiRequest<Quiz>(`/api/v1/catalog/lessons/${lessonId}/quiz`);
}

export function submitQuizAttempt(
  quizId: string,
  request: QuizAttemptRequest,
  accessToken: string,
) {
  return apiRequest<QuizAttemptResult>(`/api/v1/quizzes/${quizId}/attempts`, {
    accessToken,
    body: JSON.stringify(request),
    method: "POST",
  });
}

export function getProgress(accessToken: string) {
  return apiRequest<ProgressSummary>("/api/v1/progress/me", { accessToken });
}
