# CI/CD Documentation

## Required Pipeline Stages

1. Install dependencies.
2. Frontend lint.
3. Frontend type-check.
4. Frontend build.
5. Backend test.
6. Backend package.
7. API tests.
8. Playwright E2E tests.
9. Dependency audit.
10. Docker image build.
11. Deployment.
12. Post-deploy smoke test.

## Current Commands

Frontend and workspace:

```sh
npm run lint
npm run check-types
npm run build
npm audit --audit-level=high
```

Backend:

```sh
cd backend/fluento-api
./mvnw test
./mvnw clean package
```

## Deployment Gates

- No high-severity dependency findings.
- No failing tests.
- No failed database migrations.
- Required environment variables present.
- Release notes updated.
- Smoke checklist completed.
