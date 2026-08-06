# Email Service Integration

Auth recovery currently issues verification/reset tokens through API responses for development.

Production email integration must provide:

- Transactional email provider credentials
- Verification email template
- Password reset email template
- Delivery failure logging
- Rate limiting and abuse monitoring
