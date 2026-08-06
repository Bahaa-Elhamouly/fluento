export const SUPPORTED_LANGUAGE_CODES = ["de", "es", "fr"] as const;

export const SUPPORTED_LANGUAGES = [
  {
    code: "de",
    name: "German",
    nativeName: "Deutsch",
  },
  {
    code: "es",
    name: "Spanish",
    nativeName: "Espanol",
  },
  {
    code: "fr",
    name: "French",
    nativeName: "Francais",
  },
] as const;

export const LEARNING_LEVELS = [
  "BEGINNER",
  "ELEMENTARY",
  "INTERMEDIATE",
  "UPPER_INTERMEDIATE",
  "ADVANCED",
] as const;

export const DEFAULT_DAILY_GOAL_MINUTES = 10;
export const XP_PER_DAILY_GOAL_MINUTE = 5;
