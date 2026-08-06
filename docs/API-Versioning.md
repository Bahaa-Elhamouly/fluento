# API Versioning

## Current Version

The current API version is `v1` and all public contracts must be exposed under:

```text
/api/v1
```

## Compatibility Rules

- Backward-compatible changes can be added to the current version.
- Breaking changes require a new API version.
- Removing fields, renaming fields, changing field types, changing enum meanings, or changing authentication requirements are breaking changes.
- Adding optional response fields is allowed.
- Adding required request fields is breaking unless introduced behind a new endpoint or version.

## Deprecation Policy

When an endpoint is replaced:

- Mark it deprecated in OpenAPI.
- Document the replacement endpoint.
- Keep it available for at least one full minor release cycle.
- Add migration notes in `ReleaseNotes.md`.

## Planned Versions

- `v1`: MVP student/admin learning platform.
- `v2`: future organization, teacher, and enterprise capabilities if contract changes require version separation.
