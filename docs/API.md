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

Creates a student account and returns access and refresh tokens.

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
  "refreshToken": "opaque-refresh-token",
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

Authenticates a user and returns access and refresh tokens.

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

### User Profile

`GET /api/v1/users/me/profile`

Requires `Authorization: Bearer <accessToken>`.

Returns identity fields plus avatar, native language code, target language codes, learning level, and daily goal.

### Update User Profile

`PATCH /api/v1/users/me/profile`

Requires `Authorization: Bearer <accessToken>`.

Creates or updates the current user's profile preferences. Target language codes must exist in the learning catalog.

Request:

```json
{
  "avatarUrl": "https://example.com/avatar.png",
  "nativeLanguageCode": "ar",
  "targetLanguageCodes": ["de", "es"],
  "learningLevel": "BEGINNER",
  "dailyGoalMinutes": 20
}
```

### Refresh Token

`POST /api/v1/auth/refresh`

Rotates a refresh token and returns a new access/refresh token pair.

Request:

```json
{
  "refreshToken": "opaque-refresh-token"
}
```

### Logout

`POST /api/v1/auth/logout`

Revokes the submitted refresh token.

Request:

```json
{
  "refreshToken": "opaque-refresh-token"
}
```

### Request Email Verification

`POST /api/v1/auth/email-verification/request`

Issues an email verification token for a known user. Current development response returns the token so local flows can be tested before mail delivery is connected.

### Verify Email

`POST /api/v1/auth/verify-email`

Marks the user's email as verified when the submitted token is valid and unexpired.

### Forgot Password

`POST /api/v1/auth/forgot-password`

Issues a password reset token when the email exists. The response message is generic to avoid account enumeration.

### Reset Password

`POST /api/v1/auth/reset-password`

Updates the user's password when the submitted reset token is valid and unexpired.

### Languages

`GET /api/v1/catalog/languages`

Returns enabled learning languages. This is public so unauthenticated users can discover the catalog.

### Courses

`GET /api/v1/catalog/courses`

Returns published courses for all languages.

Optional query:

```text
language=de
```

### Course Detail

`GET /api/v1/catalog/courses/{slug}`

Returns a published course contract by slug.

### Course Modules

`GET /api/v1/catalog/courses/{courseId}/modules`

Returns modules for a course in display order.

### Module Lessons

`GET /api/v1/catalog/modules/{moduleId}/lessons`

Returns published lesson summaries for a module in display order.

### Lesson Detail

`GET /api/v1/catalog/lessons/{lessonId}`

Returns published lesson content and metadata.

### Vocabulary Terms

`GET /api/v1/catalog/vocabulary?language=de`

Returns enabled vocabulary terms for a language.

Optional query:

```text
search=hal
```

### Vocabulary Term Detail

`GET /api/v1/catalog/vocabulary/{termId}`

Returns one enabled vocabulary term.

### My Vocabulary

`GET /api/v1/vocabulary`

Requires `Authorization: Bearer <accessToken>`.

Returns the current user's saved vocabulary state.

Optional query:

```text
favoritesOnly=true
```

### Update My Vocabulary

`PATCH /api/v1/vocabulary/{termId}`

Requires `Authorization: Bearer <accessToken>`.

Creates or updates current-user vocabulary state for a term, including favorite state, mastery level, and review timestamp.

### Grammar Rules

`GET /api/v1/catalog/grammar?language=de`

Returns enabled grammar rule summaries for a language.

Optional query:

```text
level=BEGINNER
```

### Grammar Rule Detail

`GET /api/v1/catalog/grammar/{slug}`

Returns one enabled grammar rule with ordered examples.

### Grammar Practice

`GET /api/v1/grammar/{ruleId}/exercises`

Requires `Authorization: Bearer <accessToken>`. Returns enabled grammar practice exercises for one rule.

`POST /api/v1/grammar/{ruleId}/practice-attempts`

Requires `Authorization: Bearer <accessToken>`. Scores submitted grammar exercise answers, stores the attempt, and awards grammar practice XP when at least one answer is correct.

### Lesson Quiz

`GET /api/v1/catalog/lessons/{lessonId}/quiz`

Returns the first published quiz for a lesson with public question data. Correct answers are not included in this response.

### Quiz Detail

`GET /api/v1/catalog/quizzes/{quizId}`

Returns one published quiz with ordered public questions.

### Complete Lesson

`POST /api/v1/lessons/{lessonId}/complete`

Requires `Authorization: Bearer <accessToken>`.

Marks a published lesson as completed for the current user and awards deterministic lesson XP. The endpoint is idempotent; completing the same lesson again returns the existing progress without awarding duplicate XP.

Request:

```json
{
  "score": 90
}
```

### My Progress

`GET /api/v1/progress/me`

Requires `Authorization: Bearer <accessToken>`.

Returns total XP, completed lesson count, active learning days over the last seven days, current and longest streak, current daily XP goal, achievement states, and recent completed lessons.

### Submit Quiz Attempt

`POST /api/v1/quizzes/{quizId}/attempts`

Requires `Authorization: Bearer <accessToken>`.

Scores a published quiz attempt for the current user and stores both the attempt and submitted answers. Passing attempts award XP and create an XP event.

Request:

```json
{
  "answers": [
    {
      "questionId": "uuid",
      "answer": "Hello"
    }
  ]
}
```

Response data includes `score`, `maxScore`, `percentage`, `passed`, `xpEarned`, and per-question feedback.

### Notifications

`GET /api/v1/notifications`

Requires `Authorization: Bearer <accessToken>`.

Returns the current user's latest notification feed and unread count.

### Mark Notification Read

`PATCH /api/v1/notifications/{notificationId}/read`

Requires `Authorization: Bearer <accessToken>`.

Marks one notification owned by the current user as read. The endpoint is idempotent.

### Subscription Plans

`GET /api/v1/subscriptions/plans`

Returns enabled subscription plans. This endpoint is public so pricing can be shown before sign-in.

### My Subscription

`GET /api/v1/subscriptions/me`

Requires `Authorization: Bearer <accessToken>`.

Returns whether the current user has premium entitlement and the active/trialing subscription when present.

### Admin Activate Subscription

`POST /api/v1/admin/subscriptions/activate`

Requires admin role.

Activates a subscription for a user and cancels any existing active/past-due/trialing subscription.

### Admin Cancel Subscription

`DELETE /api/v1/admin/subscriptions/users/{userId}`

Requires admin role.

Cancels the user's current active/past-due/trialing subscription.

### Admin Users

`GET /api/v1/admin/users`

Requires admin role. Returns active non-deleted users.

`GET /api/v1/admin/users/{userId}`

Requires admin role. Returns one active user.

`PATCH /api/v1/admin/users/{userId}`

Requires admin role. Updates first name, last name, role, enabled state, or email verification state.

`DELETE /api/v1/admin/users/{userId}`

Requires admin role. Soft-deletes and disables a user.

### Admin Content

`GET /api/v1/admin/content/courses`

Requires admin role. Lists non-deleted courses.

`POST /api/v1/admin/content/courses`

Requires admin role. Creates a course.

`PATCH /api/v1/admin/content/courses/{courseId}`

Requires admin role. Updates a course.

`DELETE /api/v1/admin/content/courses/{courseId}`

Requires admin role. Soft-deletes a course.

`GET /api/v1/admin/content/courses/{courseId}/modules`

Requires admin role. Lists modules for a course.

`POST /api/v1/admin/content/courses/{courseId}/modules`

Requires admin role. Creates a module.

`PATCH /api/v1/admin/content/modules/{moduleId}`

Requires admin role. Updates a module.

`DELETE /api/v1/admin/content/modules/{moduleId}`

Requires admin role. Soft-deletes a module.

`GET /api/v1/admin/content/modules/{moduleId}/lessons`

Requires admin role. Lists lessons for a module.

`POST /api/v1/admin/content/modules/{moduleId}/lessons`

Requires admin role. Creates a lesson.

`PATCH /api/v1/admin/content/lessons/{lessonId}`

Requires admin role. Updates a lesson.

`DELETE /api/v1/admin/content/lessons/{lessonId}`

Requires admin role. Soft-deletes a lesson.

`GET /api/v1/admin/content/vocabulary`

Requires admin role. Lists non-deleted vocabulary terms, including disabled terms.

`POST /api/v1/admin/content/vocabulary`

Requires admin role. Creates a vocabulary term.

`PATCH /api/v1/admin/content/vocabulary/{termId}`

Requires admin role. Updates a vocabulary term.

`DELETE /api/v1/admin/content/vocabulary/{termId}`

Requires admin role. Soft-deletes a vocabulary term.

`GET /api/v1/admin/content/grammar`

Requires admin role. Lists non-deleted grammar rules, including disabled rules.

`POST /api/v1/admin/content/grammar`

Requires admin role. Creates a grammar rule and optional examples.

`PATCH /api/v1/admin/content/grammar/{ruleId}`

Requires admin role. Updates a grammar rule and replaces its active examples.

`DELETE /api/v1/admin/content/grammar/{ruleId}`

Requires admin role. Soft-deletes a grammar rule and its examples.

`GET /api/v1/admin/content/lessons/{lessonId}/quizzes`

Requires admin role. Lists non-deleted quizzes for a lesson, including draft quizzes.

`POST /api/v1/admin/content/lessons/{lessonId}/quizzes`

Requires admin role. Creates a quiz and optional questions for a lesson.

`PATCH /api/v1/admin/content/quizzes/{quizId}`

Requires admin role. Updates a quiz and replaces its active questions.

`DELETE /api/v1/admin/content/quizzes/{quizId}`

Requires admin role. Soft-deletes a quiz and its questions.

### Admin Analytics

`GET /api/v1/admin/analytics/overview`

Requires admin role. Returns user, subscription, content, learning-progress, and quiz KPI totals for the admin dashboard.

### AI Tutor Sessions

`GET /api/v1/ai/tutor/sessions`

Requires `Authorization: Bearer <accessToken>`.

Returns the current user's active AI tutor sessions.

`POST /api/v1/ai/tutor/sessions`

Requires `Authorization: Bearer <accessToken>`.

Creates an AI tutor session for a target language.

`GET /api/v1/ai/tutor/sessions/{sessionId}`

Requires `Authorization: Bearer <accessToken>`.

Returns one owned AI tutor session with message history.

`POST /api/v1/ai/tutor/sessions/{sessionId}/messages`

Requires `Authorization: Bearer <accessToken>`.

Stores a learner message and a tutor response. The current implementation uses a deterministic local tutor provider until external model integration is configured.

## Planned MVP Endpoints

- `GET/PATCH /api/v1/users/me/preferences`
- `GET /api/v1/courses`
- `GET /api/v1/courses/{courseId}`
- `GET /api/v1/courses/{courseId}/modules`
- `GET /api/v1/catalog/lessons/{lessonId}`
## API Rules

- Public endpoints must be explicitly allowlisted in security configuration.
- Authenticated endpoints require Bearer tokens.
- Admin endpoints require admin role authorization.
- Validation errors must use `VALIDATION_ERROR`.
- Business rule failures must use stable error codes.
- Pagination, filtering, and sorting must be standardized before list endpoints are finalized.
