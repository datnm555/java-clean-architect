---
description: Review a pull request against the owning modules' skills — correctness, tests, architecture conventions, security.
argument-hint: <pr-number-or-url>
---

# PR Review

Target: $ARGUMENTS

1. **Load context.** Fetch the PR (description + full diff). Identify which modules the
   diff touches; load each module's skill — review against ITS conventions, not generic
   taste. For cross-module changes also load `platform`.
2. **Correctness first.** Trace the changed logic; hunt for bugs, broken edge cases, and
   behaviour changes not covered by the description.
3. **Architecture.** Does the change respect the dependency rule and the module's Key
   Rules (Result over exceptions, ports without JPA/HTTP leakage, migrations immutable,
   thin controllers)? Would ArchUnit catch a violation — and if it should but can't,
   flag the missing rule.
4. **Tests.** Does the diff include/update tests per the skill's "How to Test"? Would
   the tests fail without the change?
5. **Conventions & scope.** Naming/pattern rules from the skills; flag unrelated changes
   that snuck into the diff. Skills must not drift from code in the same PR.
6. **Security & safety.** Secrets in the diff, injection, authz gaps, unsafe migrations,
   destructive operations without a guard.
7. **Report.** Findings ordered by severity, each with `file:line` + a concrete
   suggestion. Verdict: approve / request changes. Don't invent nitpicks to seem thorough.
