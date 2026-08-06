# Testing Strategy

## Goals

Testing must protect Fluento's learning workflows, authentication, subscriptions, admin operations, and AI-assisted practice from regressions.

## Test Layers

- Unit tests: business rules, services, validators, scoring, XP, streaks, entitlement checks.
- Integration tests: repositories, Flyway migrations, transactional services, security filters.
- API tests: request validation, response envelopes, auth flows, authorization, admin endpoints.
- Playwright E2E tests: registration, login, lesson completion, quiz submission, progress dashboard, admin CRUD.
- Manual tests: exploratory checks, release smoke tests, regression suites.
- Security tests: auth hardening, access control, token lifecycle, input validation.
- Performance tests: dashboard, course browsing, quiz submission, AI endpoints.

## Required MVP Coverage

- Register.
- Login.
- Refresh token.
- Email verification.
- Forgot/reset password.
- User profile update.
- Course/module/lesson browsing.
- Lesson completion.
- Vocabulary favorite/search.
- Grammar practice.
- Quiz attempt and score calculation.
- XP/streak/achievement updates.
- Notifications.
- Admin CRUD.

## Automation Rule

Critical paths must have automated tests. Manual testing is used to supplement automation, not replace it.

## Current Status

Backend test command passes but no test sources exist yet. Frontend lint and type-check pass. Playwright tests are planned but not yet implemented.
