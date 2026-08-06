# API Documentation

## Base URL

Local development:

```text
http://localhost:8080
```

All application APIs use:

```text
/api/v1
```

## Response Envelope

Successful responses:

```json
{
  "success": true,
  "message": "Request completed successfully",
  "data": {},
  "timestamp": "2026-08-06T00:00:00Z"
}
```

Error responses:

```json
{
  "success": false,
  "status": 400,
  "error": "Bad Request",
  "code": "VALIDATION_ERROR",
  "message": "Validation failed",
  "path": "/api/v1/auth/register",
  "validationErrors": {
    "email": "must be a well-formed email address"
  },
  "timestamp": "2026-08-06T00:00:00Z"
}
```

## Implemented Endpoints

### Public Status

`GET /api/v1/public/status`

Returns API health information.

### Register

`POST /api/v1/auth/register`

Creates a student account and returns an access token.

Request:

```json
{
  "firstName": "Sara",
  "lastName": "Ahmed",
  "email": "sara@example.com",
  "password": "StrongPassword123!"
}
```

Response data:

```json
{
  "accessToken": "jwt",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": "uuid",
    "firstName": "Sara",
    "lastName": "Ahmed",
    "email": "sara@example.com",
    "role": "USER",
    "emailVerified": false
  }
}
```

### Login

`POST /api/v1/auth/login`

Authenticates a user and returns an access token.

Request:

```json
{
  "email": "sara@example.com",
  "password": "StrongPassword123!"
}
```

### Current User

`GET /api/v1/users/me`

Requires `Authorization: Bearer <accessToken>`.

Returns the authenticated user's id, email, and authorities.

## Planned MVP Endpoints

- `POST /api/v1/auth/refresh`
- `POST /api/v1/auth/logout`
- `POST /api/v1/auth/verify-email`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`
- `GET/PATCH /api/v1/users/me/profile`
- `GET/PATCH /api/v1/users/me/preferences`
- `GET /api/v1/courses`
- `GET /api/v1/courses/{courseId}`
- `GET /api/v1/courses/{courseId}/modules`
- `GET /api/v1/lessons/{lessonId}`
- `POST /api/v1/lessons/{lessonId}/complete`
- `GET /api/v1/vocabulary`
- `POST/DELETE /api/v1/vocabulary/{wordId}/favorite`
- `GET /api/v1/grammar`
- `GET /api/v1/quizzes/{quizId}`
- `POST /api/v1/quizzes/{quizId}/attempts`
- `GET /api/v1/progress/me`
- `GET /api/v1/notifications`
- `PATCH /api/v1/notifications/{notificationId}/read`
- `GET/POST/PATCH/DELETE /api/v1/admin/*`

## API Rules

- Public endpoints must be explicitly allowlisted in security configuration.
- Authenticated endpoints require Bearer tokens.
- Admin endpoints require admin role authorization.
- Validation errors must use `VALIDATION_ERROR`.
- Business rule failures must use stable error codes.
- Pagination, filtering, and sorting must be standardized before list endpoints are finalized.
