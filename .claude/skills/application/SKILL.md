---
name: application
description: Use this skill when working in application/ — use cases (one class per use
  case), command/query records, ports and validation. Triggers include "use case",
  "command", "query", "CQRS", "port", "repository interface", "validation", or working
  in the application/ directory.
---

# application Skill

Use-case orchestration, one class per use case (vertical slice), and ports for
everything the outside world provides. Deliberately **no mediator and no hand-rolled
decorator chain** — cross-cutting concerns use native Spring mechanisms.

## Overview
| Property | Value |
|----------|-------|
| Location | application/ |
| Package | `com.example.application.<feature>` |
| Depends on | domain (+ shared-kernel transitively) |
| Allowed Spring | `spring-context` (DI annotations), `spring-tx` (`@Transactional`), `jakarta.validation` — NOT spring-web, NOT spring-data |

## Architecture & Patterns
- One use case = one class: `Create<Aggregate>UseCase` is a `@Service` with a single
  public `handle(command)` method returning `Result<R>`. Controllers inject the use case
  directly — no dispatcher in between.
- Inputs are records named for intent (CQRS-lite): `Create<Aggregate>Command`,
  `Get<Aggregate>Query`, carrying Bean Validation annotations (`@NotBlank`, ...) that
  the edge validates with `@Valid`.
- Cross-cutting via native Spring, not decorators: `@Transactional` on the use case,
  Bean Validation on input records, AOP (`@Aspect`) only if logging/metrics are needed.
- Ports (e.g. `ExampleRepository`) are interfaces defined here, implemented in
  infrastructure. Ports speak domain language — no JPA/HTTP types in signatures.

## Key Rules & Gotchas
- No HTTP types, no JPA entity leakage — a slice's response DTO lives with the slice,
  not in a global `dto` package.
- Naming is enforced by ArchUnit: `*UseCase` classes must live in
  `com.example.application.<feature>` and expose exactly one public method (`handle`).
  Fix the type, don't loosen the test.
- Use cases return `Result` — never throw for business failures.

## How to Test
- Mockito for ports, no Spring context — instantiate the use case with `new`:
  `./mvnw -pl application test`

## Related Skills
- Orchestrates `domain`; ports implemented in `infrastructure`; called from `api` controllers.
