---
description: Scaffold a vertical feature slice across all four layers (domain → application → infrastructure → api) plus tests, ending with /create-pr.
argument-hint: <feature-plural> <Aggregate> (e.g. "orders Order")
---

# New Vertical Slice

Target: $ARGUMENTS

Feature packages are **plural**, the aggregate **singular**. Use the `examples`/`Example`
slice as the living reference — copy its shape, not its code. Load the skill of each
module before writing code in it.

## Files to create

1. **domain** — `com.example.domain.<feature>/`
   - `<Aggregate>.java` — extends `AggregateRoot`; factory + behavior return `Result`
   - `<Aggregate>Errors.java` — static Error catalog
   - `<Aggregate>CreatedDomainEvent.java` (+ further events as behavior demands)
2. **application** — `com.example.application.<feature>/`
   - `Create<Aggregate>Command.java` + `Create<Aggregate>CommandHandler.java`
   - `Get<Aggregate>Query.java` + `Get<Aggregate>QueryHandler.java` + response record
   - `<Aggregate>Repository.java` (port)
3. **infrastructure** — `com.example.infrastructure.<feature>/`
   - `Jpa<Aggregate>Repository.java` (adapter) + package-private Spring Data interface
   - Flyway migration `V<next>__create_<feature>.sql`
4. **api** — `com.example.api.<feature>/`
   - `<Feature>Controller.java` + request records
5. **tests**
   - Aggregate unit tests (domain), handler unit tests (application, Mockito ports),
     endpoint integration test (api, Testcontainers)

## Checklist

- [ ] Handlers follow naming rules — ArchUnit will fail otherwise
- [ ] All business failures via `Result` + `<Aggregate>Errors` — no exceptions
- [ ] Migration added; `ddl-auto` stays `validate`
- [ ] `./mvnw verify` green locally
- [ ] Finish with `/create-pr` — a slice is not done without a PR
