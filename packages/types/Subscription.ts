export type BillingInterval = "MONTHLY" | "YEARLY";

export type SubscriptionStatus =
  | "TRIALING"
  | "ACTIVE"
  | "PAST_DUE"
  | "CANCELED"
  | "EXPIRED";

export type SubscriptionPlan = {
  id: string;
  code: string;
  name: string;
  description: string;
  price: number;
  currency: string;
  billingInterval: BillingInterval;
  premium: boolean;
  position: number;
};

export type UserSubscription = {
  id: string;
  plan: SubscriptionPlan;
  status: SubscriptionStatus;
  premium: boolean;
  currentPeriodStart: string;
  currentPeriodEnd: string;
  cancelAtPeriodEnd: boolean;
};

export type SubscriptionEntitlement = {
  premium: boolean;
  subscription?: UserSubscription | null;
};

export type AdminActivateSubscriptionRequest = {
  userEmail: string;
  planCode: string;
  durationDays: number;
};
