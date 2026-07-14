# Skill routing & platform reference (java-clean-architect)

Referenced, not auto-loaded. Always load the matching `.claude/skills/<name>/SKILL.md`
before responding on its topic. Whole-system map: the `platform` skill.

## Directory -> skill (path-based)

| Working directory | Skill |
|---|---|
| `shared-kernel/` | `shared-kernel` |
| `domain/` | `domain` |
| `application/` | `application` |
| `infrastructure/` | `infrastructure` |
| `api/` | `api` |

## Topic -> skill (keyword-based)

| User says... | Skill |
|---|---|
| architecture, system overview, dependency rule, module map | `platform` |
| Result, Error, base entity, value object, domain event interface | `shared-kernel` |
| aggregate, business rule, invariant, domain event | `domain` |
| use case, command, query, handler, port, validation | `application` |
| repository implementation, JPA, migration, Flyway, event dispatch, auditing | `infrastructure` |
| controller, endpoint, HTTP, ProblemDetail, config, run the app | `api` |

## Workflow commands

| Task | Command |
|---|---|
| Land ANY feature/implementation — mandatory (verify locally, monitor CI) | `/create-pr` |
| Review a PR | `/pr-review` |
| Scaffold a vertical feature slice | `/new-slice` |
| Working tree / branch / open-PR overview | `/status-check` |
