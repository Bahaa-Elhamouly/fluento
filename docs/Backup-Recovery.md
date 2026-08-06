# Backup And Recovery

## Scope

Backup and recovery covers PostgreSQL data, uploaded assets, generated content, and operational configuration.

## PostgreSQL

MVP production must define:

- Daily automated backups.
- Point-in-time recovery if supported by hosting provider.
- Backup encryption.
- Restore test schedule.
- Retention policy.

## Recovery Objectives

Initial targets:

- RPO: 24 hours for MVP.
- RTO: 4 hours for MVP.

These targets should be tightened before enterprise launch.

## Restore Test

At least once per release cycle:

1. Restore backup into isolated environment.
2. Run Flyway validation.
3. Run smoke tests.
4. Verify user, course, lesson, quiz, and progress data.

## Incident Records

Every recovery event must record:

- Incident timestamp.
- Cause.
- Data affected.
- Restore point.
- Verification evidence.
- Follow-up prevention tasks.
