# Development Docker

Development Docker assets should provide local PostgreSQL and supporting services for Fluento.

Minimum local services:

- PostgreSQL database named `fluento`
- Optional mail catcher for email verification testing
- Optional object storage emulator for avatar/media work

Start the local database:

```sh
docker compose -f docker/development/docker-compose.yml up -d
```

The default credentials match `backend/fluento-api/src/main/resources/application-dev.yml`.
