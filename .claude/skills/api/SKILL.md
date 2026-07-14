---
name: api
description: Use this skill when working in api/ — the Spring Boot application. REST
  controllers, Result→HTTP mapping, global exception handling, configuration, and the
  whole-app test suites (ArchUnit + Testcontainers integration). Triggers include
  "controller", "endpoint", "REST", "HTTP", "ProblemDetail", "exception handler",
  "application.yml", "run the app", or working in the api/ directory.
---

# api Skill

The composition root: Spring Boot main class, REST controllers, HTTP error mapping,
configuration, and the whole-app test suites.

## Overview
| Property | Value |
|----------|-------|
| Location | api/ |
| Package | `com.example.api` |
| Depends on | infrastructure (+ everything transitively) |
| Port | 8080 |
| Database | PostgreSQL (connection in `application.yml`; Testcontainers in tests) |

## Quick Commands
```bash
./mvnw verify                          # full build: unit + ArchUnit (integration needs Docker)
./mvnw -pl api spring-boot:run         # run the app (expects local PostgreSQL)
./mvnw -pl api test -Dtest=LayerTests  # just the architecture rules
```

## Architecture & Patterns
- Controllers are thin: bind request (`@Valid`) → build command/query → call the
  injected use case → map the `Result` to HTTP. Failures become RFC 7807
  `ProblemDetail` with status from
  `ErrorType`: VALIDATION→400, PROBLEM→400, NOT_FOUND→404, CONFLICT→409, FAILURE→500.
- `GlobalExceptionHandler` (`@RestControllerAdvice`) catches the unexpected → 500
  ProblemDetail. Business failures never travel as exceptions.
- ArchUnit tests in this module guard layer + naming rules for ALL modules.
- Integration tests: Testcontainers PostgreSQL + the real Spring context, hitting real
  HTTP endpoints.

## Key Rules & Gotchas
- No business logic in controllers — a controller branching on domain state is a smell;
  move the rule inward.
- New config keys get an entry in `application.yml` AND a note in the PR body.
- A red integration test usually means Docker isn't running — check before debugging.

## How to Test
- `./mvnw -pl api test` (unit + ArchUnit); `./mvnw -pl api verify` with Docker running
  for the Testcontainers suite.

## Related Skills
- Maps errors defined in `shared-kernel`; calls use cases from `application`.
