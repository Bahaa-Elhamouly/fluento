# Backend Architecture

## Stack

- Spring Boot 3.x.
- Java 25.
- Spring Security.
- Spring Data JPA.
- PostgreSQL.
- Flyway.
- OpenAPI/Swagger.
- Maven.

## Package Standard

```text
com.fluento
  common
    config
    controller
    entity
    enums
    exception
    response
  features
    auth
      controller
      dto
      security
      service
    user
      controller
      entity
      enums
      repository
```

Future features should follow the same pattern.

## Layer Rules

- Controller: HTTP mapping, validation entry point, response envelope.
- DTO: API contract.
- Service: use cases, transactions, business rules.
- Repository: database access.
- Entity: persistence state.
- Mapper: entity/DTO mapping when complexity requires it.

## Planned Feature Modules

- `course`
- `lesson`
- `vocabulary`
- `grammar`
- `quiz`
- `progress`
- `notification`
- `subscription`
- `admin`
- `ai`
- `analytics`

## Enterprise Standards

- No entity exposure from controllers.
- Stable error codes.
- Transactional service methods.
- Flyway migrations for every schema change.
- Integration tests for repositories and security.
- API tests for each endpoint group.
