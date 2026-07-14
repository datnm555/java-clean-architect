---
name: shared-kernel
description: Use this skill when working in shared-kernel/ — the framework-free primitives
  every other module builds on. Covers Result, Error, ErrorType, ValidationError, Entity,
  AggregateRoot, ValueObject, DomainEvent, DateTimeProvider. Triggers include "Result
  pattern", "Error type", "base entity", "value object", "domain event interface", or
  working in the shared-kernel/ directory.
---

# shared-kernel Skill

Framework-free building blocks shared by every layer. This module is the innermost ring:
**zero dependencies** — no Spring, no JPA, no Jackson, no third-party libraries.

## Overview
| Property | Value |
|----------|-------|
| Location | shared-kernel/ |
| Package | `com.example.sharedkernel` |
| Dependencies | none (JDK only) |

## Contents
| Type | Role |
|---|---|
| `Result` / `Result<T>` | Success-or-failure return for use cases and domain operations — no exceptions for business outcomes |
| `Error` + `ErrorType` (FAILURE, VALIDATION, PROBLEM, NOT_FOUND, CONFLICT) | Failure payload; ErrorType maps to an HTTP status in `api` |
| `ValidationError` | Bundles many field-level errors into one Error with type VALIDATION |
| `Entity` | Identity + domain-event collection (raise / pull events) |
| `AggregateRoot` | Base for aggregates — the consistency boundary |
| `ValueObject` | Equality-by-components base |
| `DomainEvent` | Marker interface for domain events |
| `DateTimeProvider` | Clock port so time is injectable and testable |

## Key Rules & Gotchas
- NEVER add a dependency to this module's pom — if a type needs a framework, it belongs
  in a different module.
- Exceptions are for programmer errors; business failures return `Result` carrying an `Error`.
- Primitives only: a feature-specific type does NOT belong here.

## How to Test
- Plain JUnit 5 + AssertJ, no Spring context: `./mvnw -pl shared-kernel test`

## Related Skills
- Consumed by every module; the Error→HTTP mapping lives in `api`.
