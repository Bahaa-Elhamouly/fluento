import type { LearningLevel, SupportedLanguageCode } from "./User";

export type GrammarRuleSummary = {
  id: string;
  languageCode: SupportedLanguageCode;
  title: string;
  slug: string;
  level: LearningLevel;
  position: number;
};

export type GrammarRuleDetail = GrammarRuleSummary & {
  explanation: string;
  examples: GrammarExample[];
};

export type GrammarRule = {
  id: string;
  languageCode: SupportedLanguageCode;
  title: string;
  slug: string;
  explanation: string;
  level: LearningLevel;
  position: number;
  examples: GrammarExample[];
};

export type GrammarRuleQuery = {
  language: SupportedLanguageCode;
  level?: LearningLevel;
};

export type GrammarExample = {
  sourceText: string;
  translation: string;
  note?: string | null;
  position: number;
};

export type GrammarExerciseType =
  | "MULTIPLE_CHOICE"
  | "FILL_IN_THE_BLANK"
  | "MATCHING";

export type GrammarExercise = {
  id: string;
  ruleId: string;
  type: GrammarExerciseType;
  prompt: string;
  options: string[];
  explanation?: string | null;
  level: LearningLevel;
  points: number;
  position: number;
};

export type GrammarPracticeAnswer = {
  exerciseId: string;
  answer: string;
};

export type GrammarPracticeRequest = {
  answers: GrammarPracticeAnswer[];
};

export type GrammarPracticeFeedback = {
  exerciseId: string;
  correct: boolean;
  pointsAwarded: number;
  maxPoints: number;
  message: string;
};

export type GrammarPracticeResult = {
  attemptId: string;
  ruleId: string;
  score: number;
  maxScore: number;
  percentage: number;
  xpEarned: number;
  feedback: GrammarPracticeFeedback[];
};
