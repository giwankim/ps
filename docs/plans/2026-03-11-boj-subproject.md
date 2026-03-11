# BOJ Subproject Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Move the existing `boj` Java sources and tests into a dedicated Gradle subproject `:boj` without changing Java package names or behavior.

**Architecture:** The root build remains responsible for the existing root `src` tree except for the `boj` package, which moves into a standard Java subproject under `boj/`. Gradle project wiring is updated in `settings.gradle.kts`, and the new subproject uses the existing shared `ps.java-conventions` plugin so it behaves like the other Java-only modules.

**Tech Stack:** Gradle Kotlin DSL, Java, JUnit 5, AssertJ, existing `buildSrc` convention plugins

---

## Preconditions

- Run this plan in a dedicated clean worktree as required by `@writing-plans`.
- Apply `@test-driven-development` for each behavior change and `@verification-before-completion` before claiming success.
- Do not touch unrelated root packages such as `apple`, `codeforces`, `cses`, `google`, `learn`, `meta`, `misc`, `naver`, or `support`.

### Task 1: Establish the Failing Build Case for `:boj`

**Files:**
- Modify: `settings.gradle.kts`
- Create: `boj/build.gradle.kts`

**Step 1: Verify the new subproject does not exist yet**

Run: `./gradlew :boj:test --tests boj.boj2557.MainTest`
Expected: FAIL with a Gradle error indicating project `:boj` is not found.

**Step 2: Add the new Gradle project wiring**

Create `boj/build.gradle.kts`:

```kotlin
plugins {
    id("ps.java-conventions")
}
```

Modify `settings.gradle.kts` to include:

```kotlin
include("boj")
```

Place the new include alongside the existing subproject includes.

**Step 3: Verify Gradle now recognizes `:boj`**

Run: `./gradlew :boj:test --tests boj.boj2557.MainTest`
Expected: FAIL differently than Step 1 because the project now exists but the BOJ sources and tests have not been moved yet.

**Step 4: Commit the wiring change**

```bash
git add settings.gradle.kts boj/build.gradle.kts
git commit -m "build(boj): add Gradle subproject"
```

### Task 2: Move BOJ Production Sources Into the New Subproject

**Files:**
- Move: `src/main/java/boj/boj10699/Main.java` -> `boj/src/main/java/boj/boj10699/Main.java`
- Move: `src/main/java/boj/boj15552/Main.java` -> `boj/src/main/java/boj/boj15552/Main.java`
- Move: `src/main/java/boj/boj2557/Main.java` -> `boj/src/main/java/boj/boj2557/Main.java`

**Step 1: Verify compilation is still incomplete**

Run: `./gradlew :boj:test --tests boj.boj2557.MainTest`
Expected: FAIL because the test classes and/or main classes are not fully available in `:boj`.

**Step 2: Move the production sources**

Move the three `Main.java` files into the matching `boj/src/main/java/boj/...` directories.

**Step 3: Verify the subproject can now compile production code**

Run: `./gradlew :boj:compileJava`
Expected: PASS.

**Step 4: Commit the production-source move**

```bash
git add boj/src/main/java/boj src/main/java/boj
git commit -m "refactor(boj): move BOJ sources to subproject"
```

### Task 3: Move BOJ Tests Into the New Subproject

**Files:**
- Move: `src/test/java/boj/boj10699/MainTest.java` -> `boj/src/test/java/boj/boj10699/MainTest.java`
- Move: `src/test/java/boj/boj15552/MainTest.java` -> `boj/src/test/java/boj/boj15552/MainTest.java`
- Move: `src/test/java/boj/boj2557/MainTest.java` -> `boj/src/test/java/boj/boj2557/MainTest.java`

**Step 1: Verify the BOJ test target still fails before the move**

Run: `./gradlew :boj:test --tests boj.boj2557.MainTest`
Expected: FAIL because the BOJ test sources have not been moved into `:boj` yet.

**Step 2: Move the BOJ tests**

Move the three test files into the matching `boj/src/test/java/boj/...` directories.

**Step 3: Run the focused regression tests**

Run: `./gradlew :boj:test --tests boj.boj2557.MainTest --tests boj.boj15552.MainTest --tests boj.boj10699.MainTest`
Expected: PASS.

**Step 4: Commit the test-source move**

```bash
git add boj/src/test/java/boj src/test/java/boj
git commit -m "test(boj): move BOJ tests to subproject"
```

### Task 4: Remove Empty Legacy Directories and Verify Root Build Separation

**Files:**
- Delete if empty: `src/main/java/boj`
- Delete if empty: `src/test/java/boj`

**Step 1: Remove the old directories if they are empty after the moves**

Delete only the empty legacy `boj` directories from the root source tree.

**Step 2: Verify `:boj` owns the BOJ tests**

Run: `./gradlew :boj:test`
Expected: PASS with the three BOJ tests running from the new subproject.

**Step 3: Verify the root project still builds without BOJ in its source tree**

Run: `./gradlew test`
Expected: PASS in the clean worktree, confirming the root project no longer depends on root-level `boj` sources.

**Step 4: Commit the cleanup**

```bash
git add boj src/main/java src/test/java
git commit -m "refactor(boj): finalize subproject move"
```

### Task 5: Final Verification and Review

**Files:**
- Review: `settings.gradle.kts`
- Review: `boj/build.gradle.kts`
- Review: `boj/src/main/java/boj/**`
- Review: `boj/src/test/java/boj/**`

**Step 1: Run the full verification sequence**

Run: `./gradlew :boj:test test`
Expected: PASS in the clean worktree.

**Step 2: Inspect the final moved file set**

Run: `find boj/src -type f | sort`
Expected: The three BOJ production files and three BOJ test files appear under `boj/src/...`.

**Step 3: Inspect git status**

Run: `git status --short`
Expected: Clean worktree.

**Step 4: Prepare for integration**

If the worktree is clean and verification passed, use `@finishing-a-development-branch` to decide whether to keep the branch, merge, or push.
