export type QuizQuestionType =
  | "MULTIPLE_CHOICE"
  | "FILL_IN_THE_BLANK"
  | "MATCHING";

export type QuizStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";

export type Quiz = {
  id: string;
  lessonId: string;
  title: string;
  description?: string | null;
  passingScore: number;
  position: number;
  questions: QuizQuestion[];
};

export type QuizQuestion = {
  id: string;
  type: QuizQuestionType;
  prompt: string;
  options: string[];
  points: number;
  position: number;
};

export type QuizAttemptAnswer = {
  questionId: string;
  answer: string;
};

export type QuizAttemptRequest = {
  answers: QuizAttemptAnswer[];
};

export type QuizMatchingPair = {
  left: string;
  right: string;
};

export type QuizAttemptResult = {
  attemptId: string;
  quizId: string;
  score: number;
  maxScore: number;
  percentage: number;
  passed: boolean;
  xpEarned: number;
  feedback: QuizQuestionFeedback[];
};

export type QuizQuestionFeedback = {
  questionId: string;
  correct: boolean;
  pointsAwarded: number;
  maxPoints: number;
  message: string;
};
