export type UserRole = "USER" | "ADMIN";

export type LearningLevel =
  | "BEGINNER"
  | "ELEMENTARY"
  | "INTERMEDIATE"
  | "UPPER_INTERMEDIATE"
  | "ADVANCED";

export type SupportedLanguageCode = "de" | "es" | "fr";

export type UserSummary = {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  role: UserRole;
  emailVerified: boolean;
};

export type UserProfile = UserSummary & {
  avatarUrl?: string | null;
  nativeLanguageCode?: string | null;
  targetLanguageCodes: SupportedLanguageCode[];
  learningLevel?: LearningLevel;
  dailyGoalMinutes?: number | null;
};

export type UpdateUserProfileRequest = {
  avatarUrl?: string | null;
  nativeLanguageCode?: string | null;
  targetLanguageCodes?: SupportedLanguageCode[];
  learningLevel?: LearningLevel | null;
  dailyGoalMinutes?: number | null;
};

export type AuthResponse = {
  accessToken: string;
  refreshToken: string;
  tokenType: "Bearer";
  expiresIn: number;
  user: UserSummary;
};

export type EmailRequest = {
  email: string;
};

export type TokenRequest = {
  token: string;
};

export type ResetPasswordRequest = {
  token: string;
  newPassword: string;
};

export type TokenIssuedResponse = {
  token: string;
  expiresAt: string;
};
