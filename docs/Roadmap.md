# Fluento Roadmap

## Phase 0: Foundation

Status: In progress.

- Establish monorepo structure.
- Establish Spring Boot backend foundation.
- Establish Next.js frontend foundation.
- Implement JWT registration and login.
- Add unified API responses and global exception handling.
- Add PostgreSQL/Flyway database foundation.
- Add OpenAPI configuration.
- Create project documentation baseline.
- Fix workspace lint/type-check/audit health.

## Phase 1: MVP Learning Core

- Complete authentication: refresh tokens, email verification, forgot password, reset password, logout, token revocation.
- Build user profile: avatar, display name, native language, target language preferences, learning level, daily goal.
- Build course model for German, Spanish, and French.
- Build modules and lessons with reading, listening, vocabulary, grammar, and exercise lesson types.
- Build vocabulary lists, flashcards, favorites, and search.
- Build grammar rules, examples, and practice exercises.
- Build quiz system: multiple choice, fill in the blank, matching, instant feedback, score calculation.
- Build progress: XP, streaks, achievements, course progress, weekly progress, recent activity.
- Build dashboard for students.
- Build admin CRUD for users, courses, modules, lessons, vocabulary, grammar, and quizzes.
- Add unit, integration, API, manual, and Playwright E2E coverage.

## Phase 2: AI Practice And Personalization

- Add AI tutor conversation partner.
- Add writing practice with AI feedback.
- Add speaking practice using speech-to-text and pronunciation feedback.
- Add personalized learning plans.
- Add AI configuration controls in admin.
- Add safety controls, usage limits, prompt versioning, and content moderation.
- Add analytics for AI usage, learner outcomes, and practice quality.

## Phase 3: Subscription And Growth

- Add subscription plans and entitlements.
- Integrate payment provider.
- Add invoices, payment status, cancellation, grace periods, and premium feature gates.
- Add notification preferences and delivery providers.
- Add referral and lifecycle messaging.
- Add advanced reporting.

## Phase 4: Organizations And Enterprise

- Add teacher, school, company, and organization roles.
- Add classrooms, assignments, cohorts, and institutional analytics.
- Add organization billing.
- Add SSO and enterprise security controls.
- Add data export and compliance workflows.

## Release Rule

Every roadmap item must have matching requirements, architecture notes, API/database documentation, and QA coverage before it is considered production-ready.
