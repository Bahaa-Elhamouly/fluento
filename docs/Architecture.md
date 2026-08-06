# System Architecture

## Overview

Fluento uses a modular monorepo with separate web, backend, shared package, documentation, and testing areas.

- `frontend/web`: Next.js web application.
- `backend/fluento-api`: Spring Boot API.
- `packages/ui`: shared React UI package.
- `packages/types`: shared TypeScript domain contracts.
- `packages/config`: shared lint configuration.
- `packages/typescript-config`: shared TypeScript configuration.
- `docs`: product, technical, DevOps, and QA documentation.
- `testing/manual`: manual QA artifacts.

## Backend Architecture

The backend is the official system foundation. It follows feature-based architecture with clean architecture boundaries:

- `common`: cross-cutting configuration, base entities, responses, exceptions, and shared enums.
- `features/<feature>`: each domain feature owns its controller, DTOs, service, repository, entity, enums, and security helpers where applicable.
- Controllers accept DTOs and return unified responses.
- Services contain business rules and transaction boundaries.
- Repositories handle persistence only.
- Entities model persisted domain state.
- DTOs protect API contracts from persistence internals.
- Flyway migrations are the source of truth for schema changes.

Current implemented features include auth, user profile, health/status, JWT authentication, refresh tokens, email verification tokens, password reset tokens, public learning catalog, lesson catalog, vocabulary catalog, user vocabulary state, grammar catalog, quiz catalog, quiz attempts, lesson progress, XP events, achievements, streaks, notification feed/read state, subscription entitlement, admin user management, admin course/module/lesson CRUD, AI tutor session persistence, and global error handling.

## Frontend Architecture

The web app uses Next.js App Router with TypeScript. The frontend should evolve around feature modules:

- `app`: route entry points and layouts.
- `features`: planned location for domain-specific UI and client/server actions.
- `components`: shared composed UI.
- `lib`: API client, auth session helpers, utilities.
- `styles`: global styles and tokens where needed.

Frontend code should consume backend APIs through typed contracts. Shared `packages/types` may define stable cross-platform DTOs once backend contracts are finalized.

## Data Architecture

PostgreSQL is the primary operational database. Flyway migrations must create and evolve schema. The data model must support:

- Users, roles, authentication tokens, verification tokens, password reset tokens.
- Languages, courses, modules, lessons, exercises.
- Vocabulary terms, translations, examples, tags, favorites.
- Grammar rules, examples, and practice items.
- Quiz questions, answers, attempts, and scoring.
- Progress, XP events, streaks, achievements, daily goals.
- Notifications and delivery status.
- Subscriptions, payments, invoices, and entitlements.
- Admin audit logs.
- AI conversation sessions, prompts, feedback, and moderation metadata.

## API Architecture

REST APIs use `/api/v1` versioning. Responses use a consistent envelope:

- Success: `success`, `message`, `data`, `timestamp`.
- Error: `success`, `status`, `error`, `code`, `message`, `path`, `validationErrors`, `timestamp`.

OpenAPI/Swagger must document all public contracts.

## Security Architecture

Security is stateless for API access and based on JWT access tokens. MVP security must add refresh tokens, email verification, password reset, token revocation, role-based authorization, CORS configuration, rate limiting for auth endpoints, and admin audit logging.

Secrets must come from environment variables or secret stores. Development defaults may exist only when safe and documented.

## AI Architecture

AI features should be isolated behind an AI service boundary. Prompts, model settings, moderation rules, usage limits, and evaluation criteria must be configurable and auditable. AI output should never directly mutate learning progress without deterministic validation.

## Scalability

The initial deployment can be a modular monolith. Architecture must keep feature boundaries strong enough to extract services later if needed. High-growth candidates include AI practice, notifications, payments, analytics, and media processing.
