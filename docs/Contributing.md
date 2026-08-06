# Contributing

## Local Setup

1. Install Node.js 18 or newer.
2. Install Java 25.
3. Install PostgreSQL.
4. Run `npm install` from the repository root.
5. Configure backend environment variables as needed.
6. Start backend and frontend development servers.

## Quality Gates

Run before submitting changes:

```sh
npm run lint
npm run check-types
```

Backend:

```sh
cd backend/fluento-api
./mvnw test
```

## Documentation Requirement

Every meaningful product, API, database, security, deployment, or testing change must update the corresponding document in `docs/` or `testing/manual/`.

## Development Principles

- Respect existing feature boundaries.
- Add migrations for database changes.
- Add tests proportional to risk.
- Keep API contracts stable and documented.
- Prefer explicit business rules over implicit behavior.
