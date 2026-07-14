---
name: domain
description: Use this skill when working in domain/ — aggregates, business rules, domain
  errors and domain events. Triggers include "aggregate", "business rule", "domain model",
  "domain event", "invariant", or working in the domain/ directory.
---

# domain Skill

The business core: aggregates enforce invariants and raise domain events. One package per
feature, plural (`com.example.domain.examples`), aggregate singular (`Example`).

## Overview
| Property | Value |
|----------|-------|
| Location | domain/ |
| Package | `com.example.domain.<feature>` |
| Depends on | shared-kernel |
| Allowed extra API | `jakarta.persistence` annotations ONLY (documented trade-off below) |

## Architecture & Patterns
- Aggregates extend `AggregateRoot`; factory methods (`Example.create(...)`) and state
  changes return `Result`/`Result<T>` instead of throwing.
- Each feature package holds: the aggregate, `<Aggregate>Errors` (static Error catalog,
  e.g. `ExampleErrors.notFound(id)`), and `<Something>DomainEvent` records.
- Events are raised inside the aggregate (`raise(new ExampleCreatedDomainEvent(...))`)
  and dispatched after commit by infrastructure — never dispatched from here.

## Key Rules & Gotchas
- NO Spring, NO Hibernate, NO Jackson imports. The single pragmatic exception is
  `jakarta.persistence-api` mapping annotations (mirrors the .NET template allowing EF
  abstractions in Application) — it avoids maintaining a duplicate persistence model.
  Hibernate itself stays in infrastructure.
- No public setters; mutate through intention-revealing methods that guard invariants.
- Business failures return `Result` with an Error from the aggregate's error catalog —
  exceptions are reserved for programmer errors.

## How to Test
- Pure unit tests, no Spring context: `./mvnw -pl domain test`

## Related Skills
- Builds on `shared-kernel`; persistence adapters in `infrastructure`; use cases in `application`.
