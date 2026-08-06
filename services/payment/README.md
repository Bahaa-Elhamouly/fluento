# Payment Service Integration

The backend currently stores subscription plans, user subscription state, and payment events.

Production payment integration must provide:

- Checkout session creation
- Customer portal integration
- Signed webhook verification
- Idempotent payment event processing
- Subscription reconciliation jobs
