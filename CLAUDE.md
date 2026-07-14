# java-clean-architect — Claude Code Instructions

Clean Architecture + DDD + Vertical Slice (CQRS-lite) template for **Java 21 / Spring Boot 3.5**,
Maven multi-module (`com.example`), PostgreSQL. Java mirror of the .NET
`clean-architect-template`. Deliberately thin: architectural skeleton + one example slice;
cross-cutting patterns (outbox, caching, auth, telemetry) are added only when a real
problem earns them.

## How to work here
1. Think before coding; state assumptions; ask when ambiguous.
2. Simplicity first; surgical changes only.
3. Turn vague asks into verifiable success criteria; verify before claiming done.

## Skill system
Each Maven module has a skill at `.claude/skills/<module>/SKILL.md`; the whole-system map
is the `platform` skill. The routing contract (topic→skill, directory→skill) is
`agent_docs/skill-routing.md`. **Always load the matching skill BEFORE responding** on
its topic — don't answer from general knowledge.

Module skills are the contract for their module: if an implementation diverges from the
skill, fix the code or update the skill **in the same PR** — never let them drift.

## Mandatory PR workflow
**Every feature or implementation change lands via a pull request — no direct commits to
`main`.** The mandatory path is `/create-pr`: branch → verify locally (`./mvnw verify`)
→ push → open a clear PR (What / Why / How tested) → monitor CI until green.
A PR is done when CI is green, not when it is opened.

Workflow commands in `.claude/commands/`:
- `/create-pr` — the mandatory path for landing every change.
- `/pr-review` — review a PR against the owning modules' skills.
- `/new-slice` — scaffold a vertical feature slice across all four layers.
- `/status-check` — working-tree / branch / open-PR overview.

## Architecture — the dependency rule
```
shared-kernel  →  domain  →  application  →  infrastructure  →  api
```
Arrows read "is depended on by": each module may depend only on modules to its left.
Enforced at compile time by Maven module boundaries and at test time by ArchUnit.
Details live in the `platform` skill — load it before architectural decisions.

## Quick commands
```bash
./mvnw verify                    # full build: unit + ArchUnit (integration needs Docker)
./mvnw -pl api spring-boot:run   # run the app (expects local PostgreSQL)
make doctor                      # validate agent structure (skills, commands, routing)
```

## Production safety
Read-only by default. Mutating cloud/DB/infra calls require explicit permission and a
verified account/region. Never DROP/DELETE/destroy without confirmation.
