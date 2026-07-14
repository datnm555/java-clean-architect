---
description: Report working-tree state — current branch, uncommitted changes, unpushed commits, open PRs and their CI state.
---

# Status Check

1. `git status --short --branch` — current branch + uncommitted changes.
2. `git log --oneline @{u}..` — unpushed commits (if an upstream exists).
3. `gh pr list` — open PRs; `gh pr checks <n>` for their CI state.
4. Summarize: current branch, what's pending locally, which PRs await CI or review.
   Flag any work sitting directly on `main` — it must move to a branch (`/create-pr`).
