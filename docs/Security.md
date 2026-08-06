# Security Design

## Current State

The backend uses Spring Security with stateless JWT access token authentication. Public endpoints include health/status, Swagger/OpenAPI, auth routes, and public routes. Other routes require authentication.

Passwords are hashed with BCrypt. User lookup excludes soft-deleted users. Refresh tokens are persisted as SHA-256 hashes, have an expiration timestamp, can be revoked, and are rotated on refresh.

## MVP Requirements

- JWT access tokens.
- Refresh tokens with persistence, rotation, expiration, and revocation. Implemented foundation exists.
- Email verification before sensitive user actions.
- Forgot-password and reset-password flow using short-lived tokens.
- Role-based authorization for `USER` and `ADMIN`.
- Admin audit logs for privileged actions.
- Rate limiting for login, registration, password reset, and email verification endpoints.
- CORS restricted by environment.
- Secrets managed through environment variables or deployment secret store.
- Structured security error responses without leaking credential validity.

## Authentication Flow

1. User registers with email and password.
2. Backend normalizes email, hashes password, creates user, and issues verification token.
3. Backend returns access/refresh token pair where appropriate.
4. User logs in with email/password.
5. Backend validates credentials and returns access/refresh token pair.
6. Client sends `Authorization: Bearer <accessToken>` for protected API calls.
7. Client uses refresh token to obtain a new access token.
8. Logout revokes the refresh token.

## Authorization

- Student endpoints require authenticated user access.
- Admin endpoints require `ADMIN`.
- Future teacher, school, company, and enterprise roles must be added through explicit authorities and policies.

## Data Protection

- Never store plaintext passwords.
- Never log secrets, tokens, passwords, payment payloads, or AI prompts containing sensitive personal data.
- Use HTTPS in production.
- Store payment data through the payment provider; do not store card data.
- Keep AI conversation records governed by retention, moderation, and privacy rules.

## Security Testing Checklist

- Invalid JWT returns unauthorized.
- Expired JWT returns unauthorized.
- Missing JWT cannot access protected endpoints.
- Student cannot access admin endpoints.
- Soft-deleted user cannot authenticate.
- Password reset token can be used only once.
- Refresh token rotation invalidates the previous token.
- Auth endpoints are rate limited.
- CORS blocks unknown origins in production.
