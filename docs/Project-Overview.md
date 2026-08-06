# Fluento Project Overview

## Vision

Fluento is an AI-powered multilingual language learning platform for web and mobile. The product is intended to become a complete language learning ecosystem, combining structured learning paths, vocabulary, grammar, speaking, listening, reading, writing, AI tutoring, progress tracking, gamification, subscriptions, and administration.

The platform starts with German, Spanish, and French. Language support must be data-driven so additional languages can be added without changing core application architecture.

## Product Goals

- Deliver a reliable self-paced learning experience for individual learners.
- Provide AI-assisted speaking, writing, and conversation practice.
- Track learner progress through XP, streaks, achievements, goals, course progress, and recent activity.
- Support premium subscriptions and payment monitoring.
- Provide an admin platform for managing users, content, subscriptions, analytics, notifications, and AI configuration.
- Keep the backend and frontend architecture ready for future teachers, schools, companies, organizations, and enterprise customers.

## Primary Users

### Student

The student is the primary MVP user. Students can register, log in, manage a profile, choose target languages, complete lessons, take quizzes, build vocabulary, practice with AI, track progress, receive notifications, and manage subscriptions.

### Admin

Admins manage the platform. MVP admin responsibilities include user CRUD, course CRUD, lesson CRUD, vocabulary CRUD, grammar CRUD, quiz CRUD, analytics, subscription monitoring, notifications, AI configuration, and reports.

### Future Roles

Teacher, school, company, organization, and enterprise roles are future expansions. The domain model and access-control strategy should allow role expansion without rewriting authentication, authorization, course ownership, or reporting foundations.

## MVP Scope

The MVP includes authentication, user profile management, language preferences, learning level, courses, modules, lessons, vocabulary, grammar, quizzes, progress, notifications, dashboard, admin CRUD, analytics, and complete project documentation.

AI tutor, speaking assessment, payments, and richer analytics are included in the product direction. They should be designed into the architecture early, but implementation can be phased after the core learning workflow is stable.

## Current Codebase Status

- Backend foundation exists in `backend/fluento-api`.
- Backend stack: Spring Boot 3.x, Java 25, Spring Security, JWT, JPA, PostgreSQL, Flyway, OpenAPI, validation, global exception handling, unified API response.
- Current implemented backend capability includes health/status, registration, login, JWT access token generation, authenticated `/api/v1/users/me`, user entity, and first user migration.
- Frontend foundation exists in `apps/web`.
- Frontend stack: Next.js 16, React 19, TypeScript, Turborepo workspaces, shared config packages.
- Documentation files are maintained in `docs/` and manual QA files in `testing/manual/`.

## Product Principles

- Architecture first: avoid shortcuts that create large refactors later.
- Multilingual by design: languages, levels, localized content, and examples must be stored as domain data.
- AI as an assistive layer: AI features must have traceability, moderation, auditability, and deterministic fallback behavior.
- Learning progress is a first-class domain: XP, streaks, attempts, scores, achievements, and goals should be modeled explicitly.
- Admin operations must be observable, auditable, and permission-controlled.
- Documentation, tests, and implementation must evolve together.
