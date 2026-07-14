---
description: Mandatory path for landing every feature/implementation — branch, verify locally, open a clear PR, monitor CI until green. No direct commits to main.
argument-hint: [context]
---

# Create PR

Target: $ARGUMENTS

**Every feature or implementation change in this repo lands via a PR.**
A PR is NOT done when it's opened — it's done when CI is green.

## Steps

1. **Context.** Load the skills of the modules the change touches
   (`agent_docs/skill-routing.md` has the directory→skill table).
2. **Branch.** Never commit to `main`. If on `main`, create
   `feat|fix|chore/<short-description>` first.
3. **Verify locally.** `./mvnw verify` (use `-pl <module>` while iterating, but run the
   full build before pushing; integration tests need Docker). A red local build never
   becomes a PR. Record what you ran and the result for the PR body.
4. **Review the diff.** `git diff` — every changed line must trace to the request; drop
   unrelated/accidental changes (formatting sweeps, files you didn't mean to touch).
   If the implementation diverged from a module skill, update the skill in the same PR.
5. **Push + open the PR.** Target `main`. Body: **What** changed, **Why**,
   **How tested** (exact commands + results — never claim something was tested if it
   wasn't). Link the ticket if there is one.
6. **Monitor CI until green.** `gh pr checks <n> --watch`.
   - Red check caused by your change → fix it, push to the same branch, back to step 3.
   - Red check pre-existing/unrelated → prove it (e.g. link the same failure on `main`)
     and note it in a PR comment.
   Only report "PR ready" when checks are green or every red is proven unrelated.

Updating an existing PR (pushing a fix) follows the same steps from 3.
