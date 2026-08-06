import type { SupportedLanguageCode } from "./User";

export type VocabularyTerm = {
  id: string;
  languageCode: SupportedLanguageCode;
  term: string;
  translation: string;
  pronunciation: string | null;
  example: string | null;
  tags: string[];
};

export type UserVocabularyState = {
  termId: string;
  languageCode: SupportedLanguageCode;
  term: string;
  translation: string;
  favorite: boolean;
  masteryLevel: number;
  lastReviewedAt?: string | null;
};

export type UpdateUserVocabularyRequest = {
  favorite?: boolean | null;
  masteryLevel?: number | null;
  markReviewed?: boolean | null;
};
