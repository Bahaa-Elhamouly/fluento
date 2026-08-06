# Authentication Flow

## Implemented

- Registration.
- Login.
- JWT access token generation.
- Refresh token persistence.
- Refresh token rotation.
- Logout refresh-token revocation.
- JWT request filter.
- Authenticated current-user endpoint.

## MVP Additions

- Email verification token.
- Forgot password request.
- Reset password token.
- Admin role authorization.

## Token Types

### Access Token

- Short lived.
- Sent as `Authorization: Bearer <token>`.
- Contains subject as user email.

### Refresh Token

- Long lived.
- Stored server-side as a hashed token.
- Rotated on use.
- Revoked on logout, password reset, or suspicious activity.

## Password Reset

1. User requests reset by email.
2. Backend creates a short-lived reset token.
3. Notification provider sends reset link.
4. User submits token and new password.
5. Backend hashes password and invalidates active refresh tokens.

## Email Verification

1. Registration creates verification token.
2. User opens verification link.
3. Backend marks `email_verified = true`.
4. Reusing token returns a safe already-used or invalid response.
