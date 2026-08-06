# Acceptance Criteria

## Authentication

- Registration creates a user with normalized email and hashed password.
- Duplicate active email registration returns a conflict error.
- Login returns a valid Bearer token for correct credentials.
- Invalid login returns a generic unauthorized error.
- Refresh token rotation invalidates the previous refresh token.
- Email verification marks the account as verified only once.
- Password reset tokens expire and cannot be reused.

## Learning

- A student can select German, Spanish, or French.
- A student can browse courses and modules for a selected language.
- A student can open a lesson and submit exercises.
- Lesson completion updates progress.
- Vocabulary favorites are user-specific.
- Grammar exercises return instant feedback.

## Quiz

- Quiz questions support multiple choice, fill in the blank, and matching.
- Submission calculates score deterministically.
- Feedback identifies correct and incorrect answers.
- Attempts are persisted for progress reporting.

## Progress

- XP is awarded through defined XP events.
- Streaks update based on daily activity rules.
- Achievements unlock only once per user.
- Dashboard displays current course progress, weekly activity, XP, streak, and recent activity.

## Admin

- Admin users can create, update, and delete managed content.
- Student users cannot access admin endpoints.
- Admin changes are audit logged.

## Documentation

- API changes update `API.md` and OpenAPI.
- Schema changes update `Database.md`.
- New workflows update QA test cases.
