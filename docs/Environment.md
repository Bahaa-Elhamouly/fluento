# Environment Configuration

## Profiles

The backend uses Spring profiles:

- `dev`: local development.
- `test`: automated tests.
- `prod`: production deployment.

The active profile is controlled by:

```text
SPRING_PROFILES_ACTIVE
```

## Backend Environment Variables

| Variable | Purpose | Default |
| --- | --- | --- |
| `SERVER_PORT` | Backend port | `8080` |
| `SPRING_PROFILES_ACTIVE` | Spring profile | `dev` |
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/fluento` |
| `DB_USERNAME` | PostgreSQL username | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | `postgres` for local only |
| `JWT_SECRET` | Base64 HMAC signing secret | local development default |
| `JWT_EXPIRATION_MS` | Access token lifetime | `86400000` |

## Frontend Environment Variables

Planned:

| Variable | Purpose |
| --- | --- |
| `NEXT_PUBLIC_API_BASE_URL` | Browser-accessible API base URL |
| `NEXT_PUBLIC_APP_ENV` | Visible app environment label |

## Rules

- Production secrets must not be committed.
- Local defaults must be safe and documented.
- Every new environment variable must be added to this document.
- CI/CD must inject production values through a secret store.
