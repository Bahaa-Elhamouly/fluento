# Git Workflow

## Branch Strategy

- `main`: production-ready code.
- `develop`: integration branch before release, if used by the team.
- `feature/<short-name>`: feature work.
- `fix/<short-name>`: bug fixes.
- `docs/<short-name>`: documentation-only changes.
- `release/<version>`: release stabilization.

## Commit Rules

Commits should be small, reviewable, and scoped to one purpose. Suggested prefixes:

- `feat`: user-facing feature.
- `fix`: bug fix.
- `docs`: documentation.
- `test`: tests.
- `refactor`: behavior-preserving code change.
- `chore`: tooling or maintenance.
- `security`: security fix.

## Pull Request Requirements

- Describe product impact.
- Describe technical changes.
- Link requirements or user stories.
- Include test evidence.
- Note migrations, environment variables, and deployment risks.
- Update docs when behavior changes.

## Release Rule

No release should ship without passing lint, type-check, backend tests, API tests, E2E tests for critical flows, migration validation, and smoke testing.
