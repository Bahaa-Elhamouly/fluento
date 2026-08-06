# Security Testing Checklist

- Missing token cannot access protected API.
- Invalid token cannot access protected API.
- Expired token cannot access protected API.
- Student cannot access admin API.
- Duplicate registration does not reveal sensitive user state beyond documented conflict.
- Login failure message is generic.
- Password reset token expires.
- Password reset token cannot be reused.
- Refresh token rotates.
- Logout revokes refresh token.
- CORS blocks unknown production origins.
- Validation rejects malformed payloads.
- API does not return stack traces.
- Logs do not include passwords or tokens.
- Admin actions are audit logged.
