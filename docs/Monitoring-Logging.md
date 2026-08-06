# Monitoring And Logging

## Goals

Monitoring must make production issues visible across API health, user learning flows, subscriptions, notifications, and AI usage.

## Backend

Track:

- Health endpoint status.
- Request latency.
- Error rate by endpoint.
- Authentication failures.
- Database connection health.
- Flyway migration status.
- Notification delivery failures.
- Payment webhook failures.
- AI request failures and usage.

## Frontend

Track:

- Page load performance.
- Client-side errors.
- Failed API calls.
- Conversion through registration/login.
- Lesson and quiz flow completion.

## Logging Rules

- Logs must be structured.
- Include request id or correlation id.
- Do not log secrets, passwords, tokens, payment details, or sensitive AI inputs.
- Admin actions must be audit logged.

## Alerts

- API unavailable.
- High 5xx rate.
- Database unavailable.
- Payment webhook failures.
- Notification failure spike.
- AI provider failures.
