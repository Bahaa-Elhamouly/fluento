export type AiMessageRole = "USER" | "TUTOR" | "SYSTEM";

export type CreateAiSessionRequest = {
  languageCode: string;
  title?: string | null;
};

export type SendAiMessageRequest = {
  message: string;
};

export type AiMessage = {
  id: string;
  role: AiMessageRole;
  content: string;
  provider: string;
  model: string;
  createdAt: string;
};

export type AiSession = {
  id: string;
  languageCode: string;
  title: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
  messages: AiMessage[];
};
