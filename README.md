# java-clean-architect

[![CI](https://github.com/datnm555/java-clean-architect/actions/workflows/ci.yml/badge.svg)](https://github.com/datnm555/java-clean-architect/actions/workflows/ci.yml)

Clean Architecture + DDD + Vertical Slice (CQRS-lite) template for **Java 21 / Spring Boot 3.5**,
built as a Maven multi-module project with PostgreSQL. Deliberately **thin**: the
architectural skeleton plus two reference slices (`products`, `orders`) — cross-cutting
patterns (outbox, caching, auth, telemetry) are added only when a real problem earns them.

## Architecture

```
            ┌───────────────────────── api ──────────────────────────┐
            │  REST controllers · Result → RFC 7807 · config · main  │
            └────────────────────────────┬────────────────────────────┘
            ┌──────────────────── infrastructure ─────────────────────┐
            │  JPA adapters · Flyway migrations · event dispatch      │
            └────────────────────────────┬────────────────────────────┘
            ┌────────────────────── application ──────────────────────┐
            │  use cases (one class each) · ports · command/query     │
            └────────────────────────────┬────────────────────────────┘
            ┌──────────────────────── domain ─────────────────────────┐
            │  aggregates · invariants · domain errors · events       │
            └────────────────────────────┬────────────────────────────┘
            ┌────────────────────── shared-kernel ────────────────────┐
            │  Result · Error · Entity · AggregateRoot · ValueObject  │
            └──────────────────────────────────────────────────────────┘
```

**The dependency rule** — each module depends only on modules below it:

```
shared-kernel  →  domain  →  application  →  infrastructure  →  api
```

Enforced twice: at **compile time** by Maven module boundaries (a violation cannot build)
and at **test time** by ArchUnit (naming + package conventions modules cannot express).

| Concern | Choice |
|---|---|
| Runtime | Java 21 LTS, Spring Boot 3.5.x |
| Application style | Use-case-per-class (CQRS-lite): command/query records, one `@Service` per use case, single public `handle` — no mediator, no decorator chain; cross-cutting via `@Valid`, `@Transactional`, AOP |
| Errors | Result pattern — business failures are `Result` + `Error` values (never exceptions), mapped to RFC 7807 ProblemDetail at the edge |
| Persistence | Spring Data JPA + PostgreSQL, schema changes only via Flyway |
| Domain events | Raised on aggregates, dispatched **after commit** (`@TransactionalEventListener`) |
| Tests | JUnit 5 · AssertJ · Mockito · ArchUnit · Testcontainers PostgreSQL |

## Quick start

Prerequisites: JDK 21+, Docker (for integration tests and the local database).

```bash
# Full build: unit + ArchUnit + Testcontainers integration tests
./mvnw verify

# Run the app (expects PostgreSQL at localhost:5432/cleanarch, postgres/postgres)
docker run -d --name cleanarch-db -p 5432:5432 \
  -e POSTGRES_DB=cleanarch -e POSTGRES_PASSWORD=postgres postgres:17-alpine
./mvnw -pl api spring-boot:run
```

### Try it

```bash
# Create a product
curl -s -X POST localhost:8080/products \
  -H 'Content-Type: application/json' \
  -d '{"name": "Keyboard", "price": 49.90}'
# → 201 {"id":"<product-id>"}

# Fetch it
curl -s localhost:8080/products/<product-id>

# Place an order (prices come from the catalog, not the client)
curl -s -X POST localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerId": "11111111-1111-1111-1111-111111111111",
       "lines": [{"productId": "<product-id>", "quantity": 2}]}'
# → 201 {"id":"<order-id>"}

# Cancel it (second cancel returns 409 Conflict — guarded state transition)
curl -s -X POST localhost:8080/orders/<order-id>/cancel

# Business failures are RFC 7807 problem details
curl -s localhost:8080/products/00000000-0000-0000-0000-000000000000
# → 404 {"title":"Not found","code":"product.not_found",...}
```

## Project structure

```
pom.xml               parent — Spring Boot BOM, Java 21, version management
shared-kernel/        framework-free primitives (JDK only)
domain/               aggregates per feature: products/, orders/
application/          use cases + ports per feature
infrastructure/       JPA adapters, Flyway migrations (db/migration/)
api/                  Spring Boot app, controllers, error mapping, test suites
.claude/              agent skills (architecture contract) + workflow commands
agent_docs/           skill routing tables
```

## Adding a feature

A feature is a **vertical slice** across all four layers — plural package, singular
aggregate. Use the `products` slice as the simple reference and `orders` for richer
aggregate behavior (value objects, state transitions, events). With Claude Code, run
`/new-slice <feature> <Aggregate>` for the full recipe.

Every change lands via a pull request (`/create-pr`): branch → `./mvnw verify` →
PR (What / Why / How tested) → CI green. No direct commits to `main`.

## For AI agents

`CLAUDE.md` holds the global rules. Each module has a skill in `.claude/skills/` that is
the **contract** for that module — if code and skill diverge, one of them is wrong and
must be fixed in the same PR. `make doctor` validates the setup.

`claude-starter-kit/` is the vendored source kit the agent setup was adapted from.
