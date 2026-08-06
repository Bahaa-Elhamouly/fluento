# Fluento

Fluento is an AI-powered multilingual language learning platform for web and mobile. The long-term product direction is a complete language learning ecosystem that combines structured courses, vocabulary training, grammar, speaking, listening, reading, writing, AI tutoring, progress tracking, gamification, subscriptions, and admin management.

Initial language targets:

- German
- Spanish
- French

The codebase is designed to grow beyond individual learners into future teacher, school, company, organization, and enterprise workflows.

## Current Status

Fluento is in foundation/MVP development.

Implemented today:

- Monorepo foundation with Turborepo.
- Next.js web app foundation.
- Spring Boot API foundation.
- PostgreSQL/Flyway persistence foundation.
- JWT registration and login foundation.
- Refresh-token persistence, rotation, and logout revocation.
- Authenticated current-user endpoint.
- Authenticated user profile read/update foundation.
- Public language, course, module, lesson, vocabulary, grammar, and quiz catalog endpoints.
- Authenticated lesson completion, XP events, and progress summary foundation.
- Authenticated quiz attempt scoring and feedback foundation.
- Authenticated grammar practice exercises, attempts, scoring, and XP events.
- Configurable learner daily goal minutes with derived progress-summary XP targets.
- Authenticated notification feed and read-state foundation.
- Achievement definitions, unlock persistence, and progress-summary achievement state.
- Persisted current and longest learning streaks.
- Subscription plan catalog, entitlement endpoint, and admin subscription activation/cancellation foundation.
- Email verification and forgot/reset password token flows.
- Admin user management foundation.
- Admin course, module, lesson, vocabulary, grammar, and quiz content CRUD foundation.
- AI tutor session and message persistence foundation.
- User vocabulary favorites, mastery level, and review state.
- Admin analytics overview for user, content, learning, quiz, and subscription KPIs.
- Unified API success/error envelopes.
- Global exception handling.
- OpenAPI/Swagger configuration.
- Product, architecture, DevOps, and QA documentation baseline.

Planned MVP work:

- Email delivery integration for verification and password reset.
- Richer user preferences and onboarding personalization.
- Flashcard UI and spaced-review scheduling.
- Daily goal completion tracking and reminder automation.
- Notification scheduling and delivery jobs.
- Richer admin analytics time-series reports.
- Payment provider checkout and webhook integration.
- External AI model provider integration.
- Expanded unit, integration, API, and Playwright E2E test suites.

## Repository Structure

```text
frontend/
  web/                         Next.js web application
backend/
  fluento-api/                 Spring Boot backend API
docs/                          Product, architecture, API, database, security, DevOps docs
packages/
  config/                      Shared ESLint config
  types/                       Shared TypeScript domain types
  typescript-config/           Shared TypeScript config
  ui/                          Shared React UI package
testing/
  manual/                      Manual QA plans, test cases, checklists, reports
```

## Tech Stack

Frontend:

- Next.js 16
- React 19
- TypeScript
- CSS Modules
- Turborepo

Backend:

- Spring Boot 3.x
- Java 25
- Spring Security
- JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- OpenAPI/Swagger
- Maven

## Local Development

### Prerequisites

- Node.js 18 or newer
- npm
- Java 25
- PostgreSQL

### Install Dependencies

```sh
npm install
```

### Configure Local Environment

Copy `.env.example` values into your local shell or IDE run configuration.
Keep real credentials out of git.

### Start Frontend Web App

```sh
npm run dev --workspace web
```

Default web URL:

```text
http://localhost:3000
```

### Start Backend API

Linux/macOS:

```sh
cd backend/fluento-api
./mvnw spring-boot:run
```

Windows:

```powershell
cd backend\fluento-api
.\mvnw.cmd spring-boot:run
```

Default API URL:

```text
http://localhost:8080
```

### Backend Environment

The `dev` profile reads these variables:

| Variable | Default |
| --- | --- |
| `SERVER_PORT` | `8080` |
| `SPRING_PROFILES_ACTIVE` | `dev` |
| `DB_URL` | `jdbc:postgresql://localhost:5432/fluento` |
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `postgres` |
| `JWT_SECRET` | development default |
| `JWT_EXPIRATION_MS` | `86400000` |

Create a local PostgreSQL database named `fluento`, or override `DB_URL`.

Optional local PostgreSQL with Docker:

```sh
docker compose -f docker/development/docker-compose.yml up -d
```

## Verification

Run frontend/workspace checks:

```sh
npm run lint
npm run check-types
npm audit --audit-level=high
npm run build --workspace web
```

Run backend tests:

```sh
cd backend/fluento-api
./mvnw test
```

Windows:

```powershell
cd backend\fluento-api
.\mvnw.cmd test
```

## API

Implemented endpoints:

- `GET /api/v1/public/status`
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh`
- `POST /api/v1/auth/logout`
- `POST /api/v1/auth/email-verification/request`
- `POST /api/v1/auth/verify-email`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`
- `GET /api/v1/users/me`
- `GET /api/v1/users/me/profile`
- `PATCH /api/v1/users/me/profile`
- `GET /api/v1/catalog/languages`
- `GET /api/v1/catalog/courses`
- `GET /api/v1/catalog/courses/{slug}`
- `GET /api/v1/catalog/courses/{courseId}/modules`
- `GET /api/v1/catalog/modules/{moduleId}/lessons`
- `GET /api/v1/catalog/lessons/{lessonId}`
- `GET /api/v1/catalog/vocabulary?language=de`
- `GET /api/v1/catalog/vocabulary?language=de&search=hal`
- `GET /api/v1/catalog/vocabulary/{termId}`
- `GET /api/v1/vocabulary`
- `PATCH /api/v1/vocabulary/{termId}`
- `GET /api/v1/catalog/grammar?language=de`
- `GET /api/v1/catalog/grammar?language=de&level=BEGINNER`
- `GET /api/v1/catalog/grammar/{slug}`
- `GET /api/v1/grammar/{ruleId}/exercises`
- `POST /api/v1/grammar/{ruleId}/practice-attempts`
- `GET /api/v1/catalog/lessons/{lessonId}/quiz`
- `GET /api/v1/catalog/quizzes/{quizId}`
- `POST /api/v1/quizzes/{quizId}/attempts`
- `POST /api/v1/lessons/{lessonId}/complete`
- `GET /api/v1/progress/me`
- `GET /api/v1/notifications`
- `PATCH /api/v1/notifications/{notificationId}/read`
- `GET /api/v1/subscriptions/plans`
- `GET /api/v1/subscriptions/me`
- `POST /api/v1/admin/subscriptions/activate`
- `DELETE /api/v1/admin/subscriptions/users/{userId}`
- `GET /api/v1/admin/users`
- `GET /api/v1/admin/users/{userId}`
- `PATCH /api/v1/admin/users/{userId}`
- `DELETE /api/v1/admin/users/{userId}`
- `GET /api/v1/admin/content/courses`
- `POST /api/v1/admin/content/courses`
- `PATCH /api/v1/admin/content/courses/{courseId}`
- `DELETE /api/v1/admin/content/courses/{courseId}`
- `GET /api/v1/admin/content/courses/{courseId}/modules`
- `POST /api/v1/admin/content/courses/{courseId}/modules`
- `PATCH /api/v1/admin/content/modules/{moduleId}`
- `DELETE /api/v1/admin/content/modules/{moduleId}`
- `GET /api/v1/admin/content/modules/{moduleId}/lessons`
- `POST /api/v1/admin/content/modules/{moduleId}/lessons`
- `PATCH /api/v1/admin/content/lessons/{lessonId}`
- `DELETE /api/v1/admin/content/lessons/{lessonId}`
- `GET /api/v1/admin/content/vocabulary`
- `POST /api/v1/admin/content/vocabulary`
- `PATCH /api/v1/admin/content/vocabulary/{termId}`
- `DELETE /api/v1/admin/content/vocabulary/{termId}`
- `GET /api/v1/admin/content/grammar`
- `POST /api/v1/admin/content/grammar`
- `PATCH /api/v1/admin/content/grammar/{ruleId}`
- `DELETE /api/v1/admin/content/grammar/{ruleId}`
- `GET /api/v1/admin/content/lessons/{lessonId}/quizzes`
- `POST /api/v1/admin/content/lessons/{lessonId}/quizzes`
- `PATCH /api/v1/admin/content/quizzes/{quizId}`
- `DELETE /api/v1/admin/content/quizzes/{quizId}`
- `GET /api/v1/admin/analytics/overview`
- `GET /api/v1/ai/tutor/sessions`
- `POST /api/v1/ai/tutor/sessions`
- `GET /api/v1/ai/tutor/sessions/{sessionId}`
- `POST /api/v1/ai/tutor/sessions/{sessionId}/messages`

Swagger/OpenAPI is available when the backend is running:

```text
http://localhost:8080/swagger-ui/index.html
```

## Documentation

Start here:

- `docs/Vision.md`
- `docs/Project-Overview.md`
- `docs/Requirements.md`
- `docs/Architecture.md`
- `docs/Implementation-Plan.md`
- `docs/API.md`
- `docs/Database.md`
- `docs/Security.md`
- `docs/Testing-Strategy.md`

QA artifacts are under:

```text
testing/manual
```

## Engineering Rules

- Backend feature work follows feature-based architecture.
- Controllers use DTOs and unified response envelopes.
- Services own business logic and transaction boundaries.
- Flyway migrations are required for schema changes.
- API changes must update `docs/API.md`.
- Database changes must update `docs/Database.md`.
- New user-facing workflows must update QA test cases.
- Critical flows must receive automated tests before production release.

## Product Direction

Fluento should be built as a production SaaS platform, not a prototype. Architecture decisions must support maintainability, multilingual expansion, AI integration, subscriptions, analytics, admin operations, and future organization/enterprise roles.
