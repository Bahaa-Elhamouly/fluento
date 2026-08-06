# Fluento Test Cases

## Authentication

| ID | Scenario | Expected Result |
| --- | --- | --- |
| AUTH-001 | Register with valid data | User is created and token response is returned |
| AUTH-002 | Register with duplicate email | API returns `EMAIL_ALREADY_EXISTS` |
| AUTH-003 | Login with valid credentials | API returns Bearer access token |
| AUTH-004 | Login with wrong password | API returns `UNAUTHORIZED` |
| AUTH-005 | Access `/users/me` without token | API returns unauthorized |
| AUTH-006 | Access `/users/me` with token | API returns current user |
| AUTH-007 | Refresh token | New access token is returned and old refresh token is invalidated |
| AUTH-008 | Reset password | Password changes and old sessions are revoked |

## Learning

| ID | Scenario | Expected Result |
| --- | --- | --- |
| LEARN-001 | Select German course | German course modules are shown |
| LEARN-002 | Start lesson | Lesson content loads |
| LEARN-003 | Complete lesson | Progress updates |
| LEARN-004 | Resume lesson | Last progress position is restored |

## Vocabulary

| ID | Scenario | Expected Result |
| --- | --- | --- |
| VOCAB-001 | Search word | Matching words are shown |
| VOCAB-002 | Favorite word | Word appears in favorites |
| VOCAB-003 | Review flashcard | Review attempt is saved |

## Quiz

| ID | Scenario | Expected Result |
| --- | --- | --- |
| QUIZ-001 | Submit multiple choice quiz | Score and feedback are returned |
| QUIZ-002 | Submit fill-in-blank quiz | Accepted answer variants are evaluated |
| QUIZ-003 | Submit matching quiz | Matching score is calculated |

## Admin

| ID | Scenario | Expected Result |
| --- | --- | --- |
| ADMIN-001 | Student opens admin route | Access denied |
| ADMIN-002 | Admin creates course | Course is available |
| ADMIN-003 | Admin edits lesson | Updated lesson is visible |
| ADMIN-004 | Admin deletes vocabulary item | Item is removed or soft deleted |
