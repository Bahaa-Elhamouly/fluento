# Frontend Architecture

## Stack

- Next.js 16.
- React 19.
- TypeScript.
- CSS Modules.
- Turborepo workspaces.

## Current Structure

```text
apps/web
  app
    layout.tsx
    page.tsx
    globals.css
```

## Planned Structure

```text
apps/web
  app
  components
  features
    auth
    dashboard
    courses
    lessons
    vocabulary
    grammar
    quizzes
    progress
    admin
  lib
    api
    auth
    config
  styles
```

## Frontend Rules

- Use server components by default where practical.
- Use client components only for interactivity.
- Use `next/link` for internal navigation.
- Keep API calls behind a typed client boundary.
- Keep authentication/session handling centralized.
- Keep student and admin areas separated by route and authorization checks.
- Maintain accessible forms, buttons, labels, and focus states.

## MVP Screens

- Register.
- Login.
- Forgot password.
- Reset password.
- Email verification result.
- Student dashboard.
- Course catalog.
- Course detail.
- Lesson player.
- Vocabulary review.
- Grammar practice.
- Quiz attempt.
- Progress dashboard.
- Notifications.
- Admin dashboard.
- Admin CRUD screens.
