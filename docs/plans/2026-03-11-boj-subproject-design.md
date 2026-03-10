# BOJ Subproject Design

**Date:** 2026-03-11

## Goal

Move the existing `boj` Java sources and tests out of the root project into a dedicated Gradle subproject named `:boj`, while preserving the existing Java package structure and test behavior.

## Current State

- The root project currently includes multiple competitive-programming packages under `src/main/java` and `src/test/java`.
- Only the `boj` package should be split out in this change.
- Existing `boj` sources live under `src/main/java/boj/**`.
- Existing `boj` tests live under `src/test/java/boj/**`.
- The root project already contains other packages such as `apple`, `codeforces`, `cses`, `google`, `learn`, `meta`, `misc`, and `naver`, plus shared test utilities under `src/test/java/support`.

## Chosen Approach

Create a standard Gradle subproject `:boj` and physically move the `boj` source trees into that subproject:

- `src/main/java/boj/**` -> `boj/src/main/java/boj/**`
- `src/test/java/boj/**` -> `boj/src/test/java/boj/**`

The package declarations remain unchanged, so classes such as `boj.boj2557.Main` keep the same fully qualified names.

## Why This Approach

- It matches the repository's existing subproject structure such as `:leetcode` and `:usaco`.
- It gives `boj` clear ownership in Gradle instead of relying on custom `sourceSets` that overlap with the root project.
- It keeps the refactor scoped to the requested package instead of broadening into a full root-project breakup.

## Build Changes

- Add `include("boj")` to `settings.gradle.kts`.
- Create `boj/build.gradle.kts`.
- Apply `ps.java-conventions` in the new subproject, matching the Java-only subprojects already in the repo.
- Leave the root project build unchanged apart from no longer compiling the `boj` sources from its own `src` tree.

## File Moves

- Move all production files under `src/main/java/boj/**` into `boj/src/main/java/boj/**`.
- Move all test files under `src/test/java/boj/**` into `boj/src/test/java/boj/**`.
- Remove the now-empty root `src/main/java/boj` and `src/test/java/boj` directories.

The root `src/test/java/support` package remains in the root project because it is not part of the requested move.

## Testing Strategy

Use the existing BOJ tests as the regression suite:

- Verify the current root tests for `boj` are runnable before the move.
- After the move, verify the same tests via `:boj:test`.
- Run a root test task to confirm the root project still builds without the `boj` sources present.

## Risks

- Forgetting to include the new subproject in `settings.gradle.kts` will leave moved code disconnected from the build.
- Accidentally moving non-`boj` sources would expand scope and could break the root project layout.
- If any root code implicitly depends on `boj`, the move could expose that coupling. The current package layout suggests low risk, but verification should confirm it.

## Non-Goals

- Splitting the rest of the root source tree into additional subprojects.
- Renaming Java packages.
- Moving shared root test utilities such as `support.ResourceUtils`.
