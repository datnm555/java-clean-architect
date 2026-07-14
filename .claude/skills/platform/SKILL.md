---
name: platform
description: Whole-system map for java-clean-architect — the Maven modules, the dependency
  rule, the stack, and which skill owns which area. Use when a question spans multiple
  modules, when deciding where code belongs, or when onboarding. Triggers include
  "architecture", "system overview", "which module", "dependency rule", "module map",
  "clean architecture".
---

# java-clean-architect Platform Skill — bird's-eye view

A from-scratch Clean Architecture + DDD + CQRS + Vertical Slice template for Java 21 /
Spring Boot 3.5.x, built as a Maven multi-module project (`com.example`), PostgreSQL via
Spring Data JPA. It is the Java mirror of the .NET `clean-architect-template`: only the
architectural skeleton plus one example vertical slice (`examples`), with cross-cutting
patterns deferred until a real problem earns them.

## Modules & owning skills

| Module | What it does | Skill |
|---|---|---|
| `shared-kernel/` | Framework-free primitives: Result, Error, Entity, AggregateRoot, ValueObject, DomainEvent | `shared-kernel` |
| `domain/` | Aggregates, business rules, domain errors, domain events | `domain` |
| `application/` | CQRS command/query handlers, ports (interfaces), validation | `application` |
| `infrastructure/` | JPA adapters, Spring Data repositories, Flyway migrations, event dispatch | `infrastructure` |
| `api/` | Spring Boot app: REST controllers, Result→HTTP mapping, config, test suites | `api` |

## The dependency rule

```
shared-kernel  →  domain  →  application  →  infrastructure  →  api
```

Arrows read "is depended on by": each module may depend only on modules to its left.
Enforced twice:
1. **Compile time** — Maven module dependencies; a violation cannot build.
2. **Test time** — ArchUnit rules in the `api` test suite (naming and package
   conventions that module boundaries cannot express).

## Stack

| Concern | Choice |
|---|---|
| Language / runtime | Java 21 LTS |
| Framework | Spring Boot 3.5.x |
| Build | Maven multi-module, wrapper committed (`./mvnw`), versions managed in the parent pom |
| Database | PostgreSQL, Spring Data JPA, Flyway migrations |
| Result/error model | Result pattern (shared-kernel); RFC 7807 ProblemDetail at the HTTP edge |
| Domain events | Raised on aggregates, dispatched AFTER COMMIT (`@TransactionalEventListener`) |
| Tests | JUnit 5, AssertJ, Mockito; ArchUnit; Testcontainers PostgreSQL |

## Anatomy of a vertical slice

Feature packages are **plural** (`examples`), the aggregate inside is **singular**
(`Example`). One slice spans four modules — `/new-slice` has the full recipe:

```
domain/…/examples/          Aggregate + errors + domain events
application/…/examples/     Commands/queries + handlers + ports
infrastructure/…/examples/  JPA repository adapter + persistence config + migration
api/…/examples/             REST controller + request/response records
```

## Skill navigation guide

- Business rules / aggregates / invariants → `domain`
- Use cases / handlers / ports / validation → `application`
- Persistence / migrations / event dispatch → `infrastructure`
- HTTP / controllers / error mapping / config / running the app → `api`
- Result, Error, base types → `shared-kernel`

> Keep this skill at MAP level: per-module detail lives in that module's skill —
> link to it, never restate it.
