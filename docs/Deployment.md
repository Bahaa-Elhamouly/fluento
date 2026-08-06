# Deployment Guide

## Target Architecture

MVP production deployment should include:

- Next.js web app.
- Spring Boot API.
- PostgreSQL database.
- Reverse proxy or managed ingress.
- TLS termination.
- Centralized logs.
- Environment-specific secrets.
- Database backup process.

## Local Development

Frontend:

```sh
npm install
npm run dev --workspace web
```

Backend:

```sh
cd backend/fluento-api
./mvnw spring-boot:run
```

Windows:

```powershell
cd backend\fluento-api
.\mvnw.cmd spring-boot:run
```

## Build

Frontend:

```sh
npm run build
```

Backend:

```sh
cd backend/fluento-api
./mvnw clean package
```

## Docker

Docker support should include:

- PostgreSQL service.
- Backend API image.
- Frontend web image.
- Local `docker compose` profile for development.
- Production images built from clean source and pinned dependency versions.

## Release Checklist

- Lint passes.
- Type-check passes.
- Backend tests pass.
- API tests pass.
- Playwright E2E tests pass.
- Flyway migrations run cleanly.
- OpenAPI docs are updated.
- Environment variables are present.
- Security checklist is complete.
- Smoke checklist is complete after deployment.
