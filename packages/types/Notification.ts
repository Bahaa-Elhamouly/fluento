export type NotificationType =
  | "DAILY_REMINDER"
  | "LESSON_REMINDER"
  | "ACHIEVEMENT"
  | "SYSTEM";

export type Notification = {
  id: string;
  type: NotificationType;
  title: string;
  message: string;
  actionUrl?: string | null;
  read: boolean;
  readAt?: string | null;
  createdAt: string;
};

export type NotificationFeed = {
  unreadCount: number;
  notifications: Notification[];
};
