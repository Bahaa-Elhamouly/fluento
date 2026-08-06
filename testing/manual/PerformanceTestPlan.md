# Performance Test Plan

## Goals

Establish baseline performance for core learning and admin workflows.

## MVP Targets

- API health response under 200 ms in normal conditions.
- Course catalog response under 500 ms for typical dataset.
- Lesson load response under 500 ms for typical lesson.
- Quiz submission under 700 ms excluding external AI calls.
- Dashboard under 800 ms for typical user progress.

## Scenarios

- Login burst.
- Course browsing.
- Lesson completion.
- Quiz submission.
- Dashboard loading.
- Admin content listing.
- AI practice endpoint load once implemented.

## Metrics

- p50, p95, p99 latency.
- Error rate.
- Throughput.
- Database query time.
- CPU and memory usage.
