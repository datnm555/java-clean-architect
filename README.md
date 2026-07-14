# java-clean-architect

Clean Architecture + DDD + CQRS + Vertical Slice template for **Java 21 / Spring Boot 3.5**,
built as a Maven multi-module project with PostgreSQL. The Java mirror of the .NET
`clean-architect-template`: deliberately thin — the architectural skeleton plus one
example vertical slice, with cross-cutting patterns deferred until a real problem earns them.

## The dependency rule

```
shared-kernel  →  domain  →  application  →  infrastructure  →  api
```

Arrows read "is depended on by". Enforced at **compile time** by Maven module boundaries
and at **test time** by ArchUnit.

## Working in this repo

- Agent setup (skills, commands, routing): `CLAUDE.md`, `.claude/`, `agent_docs/`
- Whole-system map: `.claude/skills/platform/SKILL.md`
- **Every feature or implementation lands via a pull request** — the mandatory path is
  the `/create-pr` workflow command. No direct commits to `main`.
- `make doctor` validates the agent structure; `make help` lists build/test/run targets.

`claude-starter-kit/` is the vendored source kit this setup was adapted from
(multi-repo orchestrator → single-repo template).
