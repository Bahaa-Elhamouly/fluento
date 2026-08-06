# Requirements

## Functional Requirements

### Authentication

- Register.
- Login.
- JWT access tokens.
- Refresh tokens.
- Email verification.
- Forgot password.
- Reset password.
- Logout and token revocation.

### User Management

- User profile.
- Avatar.
- Language preferences.
- Learning level.
- Account status.

### Learning Content

- Language courses for German, Spanish, and French.
- Modules.
- Lessons.
- Reading, listening, vocabulary, grammar, and exercise lesson types.

### Vocabulary

- Flashcards.
- Word lists.
- Favorites.
- Search.
- Examples and translations.

### Grammar

- Grammar rules.
- Examples.
- Practice exercises.

### Quiz System

- Multiple choice.
- Fill in the blank.
- Matching.
- Instant feedback.
- Score calculation.

### Progress

- XP system.
- Streaks.
- Achievements.
- Daily goals.
- Course progress.
- Weekly progress.
- Recent activity.

### Notifications

- Daily reminders.
- Lesson reminders.
- Achievement notifications.

### Admin Panel

- User CRUD.
- Course CRUD.
- Lesson CRUD.
- Vocabulary CRUD.
- Grammar CRUD.
- Quiz CRUD.
- Analytics dashboard.
- Subscription management.
- Payment monitoring.
- AI configuration.
- Reports.

## Non-Functional Requirements

- Secure authentication and authorization.
- PostgreSQL-backed persistence.
- Flyway-managed schema.
- Modular monolith suitable for later extraction.
- API response consistency.
- OpenAPI documentation.
- Environment-specific configuration.
- Automated tests for critical flows.
- CI/CD-ready commands.
- Observability through logs, metrics, and health checks.
- Multilingual extensibility.
- Accessibility-conscious frontend.

## Traceability Rule

Each requirement must have:

- Architecture coverage.
- API or UI design coverage.
- Database model coverage where applicable.
- Automated or manual QA coverage.
