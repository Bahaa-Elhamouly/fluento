# Docker Setup

## Goal

Docker should provide repeatable local and production builds for Fluento.

## Planned Services

- `postgres`: PostgreSQL database.
- `fluento-api`: Spring Boot backend.
- `web`: Next.js frontend.

## Local Compose Requirements

- Expose PostgreSQL on `5432`.
- Expose API on `8080`.
- Expose web app on `3000`.
- Mount local source only for development profiles.
- Keep production images immutable.

## Environment

Docker configuration must use variables documented in `Environment.md`.

## Future Work

- Add `docker-compose.yml`.
- Add backend Dockerfile.
- Add frontend Dockerfile.
- Add database health checks.
- Add migration startup validation.
