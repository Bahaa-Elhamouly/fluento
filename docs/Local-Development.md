# Local Development Guide

## Prerequisites

- Node.js 18 or newer.
- npm.
- Java 25.
- PostgreSQL.
- Git.

## Frontend

```sh
npm install
npm run dev --workspace web
```

Default URL:

```text
http://localhost:3000
```

## Backend

```sh
cd backend/fluento-api
./mvnw spring-boot:run
```

Windows:

```powershell
cd backend\fluento-api
.\mvnw.cmd spring-boot:run
```

Default URL:

```text
http://localhost:8080
```

## Database

Create a local PostgreSQL database named `fluento`, or override `DB_URL`.

## Verification

```sh
npm run lint
npm run check-types
npm audit --audit-level=high
```

Backend:

```sh
cd backend/fluento-api
./mvnw test
```
