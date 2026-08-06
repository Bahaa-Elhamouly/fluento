# Release Notes

## Unreleased

### Added

- Documentation baseline for Fluento product vision, roadmap, architecture, API, database, security, deployment, environment, coding standards, Git workflow, and QA.
- Web homepage direction aligned with Fluento product positioning.
- Backend authentication foundation documented as current implementation.
- Refresh-token persistence, rotation, and logout revocation.
- Backend unit tests for auth service and refresh-token service behavior.
- Public learning catalog foundation for languages, courses, and course modules.
- Public lesson catalog foundation with seeded beginner lessons.
- Public vocabulary catalog foundation with seeded German, Spanish, and French beginner terms.
- Public grammar catalog foundation with seeded beginner rules and examples.
- Public quiz catalog foundation with seeded beginner lesson checks.
- Authenticated lesson completion and progress summary foundation with XP events.
- Configurable learner daily goal minutes with derived progress-summary XP targets.
- Authenticated user profile read/update foundation for learner preferences.
- Authenticated quiz attempt submission with scoring, per-question feedback, stored answers, and pass XP events.
- Authenticated grammar practice exercises with persisted attempts, feedback, and XP events.
- Authenticated notification feed and read-state foundation.
- Achievement definitions, unlock persistence, and achievement state in progress summaries.
- Persisted learner streaks with current and longest streak values in progress summaries.
- Subscription plan catalog, user entitlement endpoint, admin activation/cancellation foundation, and payment event schema.
- Email verification token flow and forgot/reset password token flow.
- Admin user management foundation with list, detail, update, and soft-delete operations.
- Admin course, module, lesson, vocabulary, grammar, and quiz content CRUD foundation.
- Admin analytics overview endpoint for user, content, learning, quiz, and subscription KPI totals.
- AI tutor session and message persistence foundation with deterministic local tutor replies.
- User vocabulary state with favorites, mastery level, and review timestamp.
- Seed data for German, Spanish, and French foundation courses.
- Shared TypeScript contracts for API, user, course, lesson, vocabulary, grammar, quiz, progress, notification, subscription, admin, and AI domains.

### Changed

- Next.js upgraded to `16.3.0`.
- Development database configuration moved behind environment variables.
- Workspace lockfile corrected for `@repo/eslint-config`.

### Known Gaps

- Email delivery integration, external AI model provider integration, richer quiz analytics time-series reports, notification delivery jobs, and payment provider webhooks are planned but not fully implemented.
- Backend test coverage exists for the auth foundation but must be expanded into controller, integration, repository, and API tests.
- Playwright test suite is not yet implemented.
