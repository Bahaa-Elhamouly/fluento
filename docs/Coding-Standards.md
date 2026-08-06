# Coding Standards

## General

- Prefer clarity over cleverness.
- Keep feature boundaries explicit.
- Avoid large unrelated refactors in feature work.
- Keep documentation, tests, and implementation synchronized.

## Backend

- Use Spring Boot 3.x and Java 25.
- Use feature-based packages under `com.fluento.features`.
- Controllers must stay thin.
- Services own business logic and transaction boundaries.
- Repositories own persistence access.
- Use DTOs for API requests and responses.
- Use validation annotations on request DTOs.
- Use `BusinessException` and stable `ErrorCode` values for domain failures.
- Return `ApiResponse` or `ApiError`.
- Add Flyway migrations for schema changes.
- Do not expose entities directly from public APIs.

## Frontend

- Use TypeScript and React components with explicit props.
- Use Next.js `Link` for internal navigation.
- Keep route components focused on composition.
- Move domain logic into feature modules or libraries as the app grows.
- Prefer accessible semantic HTML.
- Keep layouts responsive and avoid text overlap.

## Testing

- Unit tests cover business rules.
- Integration tests cover database and service behavior.
- API tests cover request validation, authentication, authorization, and response envelopes.
- Playwright tests cover critical user flows.

## Formatting

- Use repository lint and type-check commands.
- Avoid committing generated build output.
- Keep docs in Markdown with concise headings and actionable content.
