---
name: application
description: Use this skill when working in application/ — CQRS commands, queries, handlers,
  ports and validation. Triggers include "use case", "command handler", "query handler",
  "CQRS", "port", "repository interface", "validation", or working in the application/
  directory.
---

# application Skill

Use-case orchestration: one command/query + handler per operation (vertical slice), and
ports for everything the outside world provides.

## Overview
| Property | Value |
|----------|-------|
| Location | application/ |
| Package | `com.example.application.<feature>` |
| Depends on | domain (+ shared-kernel transitively) |
| Allowed Spring | `spring-context` (DI annotations), `spring-tx` (`@Transactional`) — NOT spring-web, NOT spring-data |

## Architecture & Patterns
- Commands/queries are records; handlers implement `CommandHandler<C, R>` /
  `QueryHandler<Q, R>` (defined in `com.example.application.abstractions`) and return
  `Result<R>`.
- Ports (e.g. `ExampleRepository`) are interfaces defined here, implemented in
  infrastructure. Ports speak domain language — no JPA/HTTP types in signatures.
- Handlers are constructed by DI only; keep them package-private where possible.
- Cross-cutting handler behavior (validation, logging) is decoration around the handler
  interfaces — mirroring the .NET Scrutor decorators. Runtime order:
  HTTP → controller → logging → validation → handler.

## Key Rules & Gotchas
- No HTTP types, no JPA entity leakage — a slice's response DTO lives with the slice,
  not in a global `dto` package.
- Naming is enforced by ArchUnit: `*CommandHandler` must implement `CommandHandler`,
  `*QueryHandler` must implement `QueryHandler`. Fix the type, don't loosen the test.
- Handlers return `Result` — never throw for business failures.

## How to Test
- Mockito for ports, no Spring context: `./mvnw -pl application test`

## Related Skills
- Orchestrates `domain`; ports implemented in `infrastructure`; called from `api` controllers.
