# Automation Test Suite

This folder contains the planned Playwright end-to-end coverage for the Fluento web application.

Run target after Playwright is installed:

```sh
npx playwright test
```

Primary smoke paths:

- Learner can open the dashboard and navigate to courses, lessons, practice, progress, and admin shell pages.
- Public catalog pages render without API credentials.
- Authenticated flows are covered through seeded or API-created test users once the E2E environment is connected.
