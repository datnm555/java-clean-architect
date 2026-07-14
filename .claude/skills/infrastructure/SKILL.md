---
name: infrastructure
description: Use this skill when working in infrastructure/ — JPA adapters, Spring Data
  repositories, Flyway migrations, auditing, and after-commit domain event dispatch.
  Triggers include "repository implementation", "JPA", "migration", "Flyway", "database",
  "event dispatcher", "auditing", or working in the infrastructure/ directory.
---

# infrastructure Skill

Adapters for the ports application defines: persistence (Spring Data JPA + PostgreSQL),
time, and domain-event dispatch.

## Overview
| Property | Value |
|----------|-------|
| Location | infrastructure/ |
| Package | `com.example.infrastructure.<area>` |
| Depends on | application (+ inner modules transitively) |
| Tech | Spring Data JPA, Hibernate, Flyway, PostgreSQL |

## Architecture & Patterns
- Port implementations: `JpaExampleRepository implements ExampleRepository` — the Spring
  Data interface stays package-private behind the adapter.
- Flyway migrations in `src/main/resources/db/migration/V<N>__<desc>.sql`. Schema changes
  happen ONLY via migration — Hibernate `ddl-auto` stays `validate`.
- Domain events: after commit, events pulled from aggregates are published and handled by
  `@TransactionalEventListener(phase = AFTER_COMMIT)` listeners. Semantics are
  at-most-once-after-commit: a failing listener is logged, never aborts the others, and
  never rolls back the write (no Outbox until at-least-once delivery is actually needed).
- Auditing (`createdAt` / `modifiedAt`) via JPA auditing callbacks using `DateTimeProvider`.

## Key Rules & Gotchas
- NEVER import `api` types — this module implements inward-facing ports only.
- No business logic here: if an adapter starts growing rules, they belong in
  domain/application.
- Migration files are immutable once merged — fix mistakes with a NEW migration.

## How to Test
- Full-stack integration tests live in `api` (Testcontainers boots the app);
  adapter-level tests: `./mvnw -pl infrastructure test`

## Related Skills
- Implements ports from `application`; wired into the running app by `api`.
