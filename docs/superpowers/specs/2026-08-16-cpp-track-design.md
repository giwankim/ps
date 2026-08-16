# C++ Competitive-Programming Track Design

Date: 2026-08-16
Module: `cpp/` (new, outside the Gradle build)

## Goal

Add a C++ solving track alongside the existing Java/Kotlin Gradle modules. The
track is independent: it solves whatever problems you choose to solve in C++,
with no expectation of parity with the Java modules and no shared build.

It must serve two problem shapes that the Java side also serves:

- **stdin/stdout judges** (BOJ, Codeforces, CSES, USACO, UVa, Jungol) — a
  self-contained `main` reading stdin and writing stdout;
- **function-signature judges** (LeetCode, Programmers) — a class with a method
  to fill in and no stdin at all.

BOJ and Codeforces are the primary drivers, so local behavior must match a GNU
g++ / libstdc++ judge rather than macOS defaults.

## Decisions

| Topic | Decision |
| --- | --- |
| Role | Independent track; no mirroring of the Java modules, no sync obligation |
| Location | Top-level `cpp/`, standalone. Not wired into Gradle |
| Build system | CMake + Ninja, one `CMakeLists.txt`, glob-driven discovery |
| CMake version | `cmake_minimum_required(VERSION 3.24)` — needed by `FIND_PACKAGE_ARGS` |
| Test driver | CTest for every problem shape |
| Test framework | GoogleTest (+ gmock matchers), function-signature problems only |
| Layout | Flat: `cpp/<judge>/<problem>.cpp`, data in `cpp/<judge>/tests/<problem>/` |
| Compiler | Homebrew `g++-16` (`/opt/homebrew/bin/g++-16`), pinned in presets |
| Standard | C++20, `CMAKE_CXX_EXTENSIONS OFF` (`-std=c++20`, matching judge flags) |
| Scoping | `PS_SCOPE` cache variable; active-problem by default, `all` preset for sweeps |
| Formatting | clang-format, Google style, `ColumnLimit: 100` |
| Lint gate | Extend the existing `.githooks/pre-commit`, plus its regression suite |
| Index | Separate `cpp/README.md`; root `README.md` gets one pointer line |

### Rejected alternatives

- **Gradle native C++ plugin.** Its component model fits hundreds of
  independent `main`s poorly, and it adds Gradle indirection to the loop run
  most often. Rejected as inverting the track's stated independence.
- **Apple clang as the default compiler.** `<bits/stdc++.h>` and `__gnu_pbds`
  are libstdc++-only, so judge-idiomatic code would not compile locally.
- **In-process testing** (linking the solution and swapping `std::cin.rdbuf`).
  Rejected in favor of subprocess execution — see "Why subprocess" below.
- **A `cpp/tools/ps.py` CLI** (`new`, `stress`, `test` verbs). Deferred to a
  later phase; see "Deferred".

## Architecture

```
cpp/
├── CMakeLists.txt            # the only build file; globs everything below
├── CMakePresets.json         # dev / all / judge; g++-16 and flags pinned
├── CMakeUserPresets.json     # gitignored; your machine-local active problem
├── .clang-format             # Google style, ColumnLimit 100
├── README.md                 # index of problems solved in C++
├── cmake/
│   └── RunCase.cmake         # runs a binary on a .in, compares against .out
├── boj/
│   ├── 1000.cpp              # self-contained, has main(), paste-able as-is
│   └── tests/
│       └── 1000/
│           ├── sample-1.in / sample-1.out
│           └── max-values.in / max-values.out
├── codeforces/
│   ├── 1woA.cpp
│   └── tests/1woA/ ...
└── leetcode/
    ├── MinimumPathSum.hpp    # Solution class, methods defined inline, no main
    └── tests/
        └── MinimumPathSum_test.cpp
```

### Discovery rules

All globs use `CONFIGURE_DEPENDS`, so dropping in a new file re-triggers
configuration at build time. Discovery is keyed to **filenames**, never to file
contents — "does this file define `main`?" is not decidable at configure time,
whereas an extension is.

1. **Judges** are the immediate subdirectories of `cpp/`, excluding `cmake`,
   `build`, and `_deps`. Adding `cpp/usaco/` requires no build-file edit.
2. `cpp/<judge>/*.cpp` → one executable target, `<judge>_<problem>`, where
   `<problem>` is the filename stem. Stems containing characters CMake
   disallows in target names are sanitized to `_`; the CTest test name keeps
   the original stem.
3. `cpp/<judge>/tests/<problem>/*.in` → one CTest test per file, named
   `<judge>/<problem>/<stem>`, running that executable through
   `cmake/RunCase.cmake`.
4. `cpp/<judge>/tests/*_test.cpp` → all such files in a judge compile into one
   GoogleTest binary, `<judge>_tests`, with `cpp/<judge>/` on the include path.
   `gtest_discover_tests()` registers each `TEST()` as its own CTest test.

The two mechanisms cannot overlap: `*.cpp` at a judge's top level is always an
executable, `*.hpp` is always a header linked into gtest.

### Why subprocess, not in-process

Stdin/stdout solutions are launched as separate processes rather than linked
into a test binary. This is a fidelity decision, not a compromise:

- **Fresh process per case.** CP solutions routinely declare `int a[200005];`
  at namespace scope. In-process execution leaks case 1's globals into case 2;
  the judge never does.
- **Captures C stdio.** Solutions using `printf`/`scanf` for speed are not
  captured by a `std::cout.rdbuf()` swap.
- **Survives `exit()`.** A solution calling `exit(0)` would kill an in-process
  test binary.
- **No `main` collision**, so no `#ifdef PS_TEST` guard or `-Dmain=solve` trick
  in the source. Solution files stay pristine and paste-able.

Function-signature solutions have no `main` and are linked directly, since none
of the above applies.

### `cmake/RunCase.cmake`

Invoked as a CMake script with `EXE`, `IN`, and `EXPECTED` defined:

```cmake
add_test(NAME boj/1000/max-values COMMAND ${CMAKE_COMMAND}
  -DEXE=$<TARGET_FILE:boj_1000>
  -DIN=${CMAKE_SOURCE_DIR}/boj/tests/1000/max-values.in
  -DEXPECTED=${CMAKE_SOURCE_DIR}/boj/tests/1000/max-values.out
  -P ${CMAKE_SOURCE_DIR}/cmake/RunCase.cmake)
```

Behavior:

- Runs `EXE` with `INPUT_FILE ${IN}`, capturing stdout and stderr.
- **Non-zero exit is a failure** reporting the exit code and stderr. This is
  the runtime-error signal; under the sanitizer build an out-of-bounds write
  fails here rather than producing mystery output.
- **Normalizes trailing whitespace only** on both sides: per-line trailing
  spaces/tabs/CR, and trailing blank lines. Leading whitespace is deliberately
  *not* normalized — judges ignore trailing whitespace, but a bug emitting a
  stray leading space must still fail. Same rule as the `cses` Java harness.
- On mismatch, reports the **first differing line** with expected and actual,
  not a dump of both files.

Implementation trap to handle: CMake lists are semicolon-separated, so output
containing `;` corrupts a naive `string(REPLACE "\n" ";" ...)` line split.
Escape semicolons before splitting.

A per-test `TIMEOUT` property (default 5s, overridable via a `PS_TIMEOUT` cache
variable) turns an infinite loop into a failing test instead of a hung
terminal. No test framework offers per-test timeouts; this is a CTest-specific
capability and a large part of why CTest drives everything.

`RunCase.cmake` accepts an optional `COMPARATOR` argument defaulting to exact
match, so special judges (any-valid-answer, floating-point tolerance) can be
added later without reworking the harness. Not implemented now.

### Fail-fast guards

An empty glob produces zero tests, which passes green — the exact failure mode
the `cses` spec calls out. Two guards:

1. A `.in` with no sibling `.out` → configure-time `FATAL_ERROR` naming the
   file.
2. An executable target with no `tests/<problem>/` directory → configure-time
   `WARNING` listing them. A warning, not an error, so a problem you have just
   started is not blocked.

### GoogleTest acquisition

```cmake
FetchContent_Declare(googletest
  GIT_REPOSITORY https://github.com/google/googletest.git
  GIT_TAG        v1.17.0   # confirm latest stable at implementation time
  FIND_PACKAGE_ARGS NAMES GTest)
```

`FIND_PACKAGE_ARGS` (CMake 3.24+) uses a Homebrew `googletest` when installed
and otherwise downloads and builds it once. Nothing is vendored into git.

The declaration is **conditional on at least one `*_test.cpp` being in scope**,
so configuring a BOJ-only scope never requires network access.

## Build profiles

`PS_SCOPE` is a plain cache variable, so it is overridable per configure with
no file editing:

```bash
cmake --preset dev -DPS_SCOPE=boj/1000     # command-line -D overrides the preset
```

Each preset uses its own `binaryDir`, so profiles coexist instead of forcing a
reconfigure when switching between "the problem I am solving" and "everything."

| Preset | `binaryDir` | `PS_SCOPE` | Flags |
| --- | --- | --- | --- |
| `dev` | `build/dev` | `all` (overridden per problem) | `-g -O0`, sanitizers, `-D_GLIBCXX_ASSERTIONS` |
| `all` | `build/all` | `all` | same as `dev` |
| `judge` | `build/judge` | `all` | `-O2`, no sanitizers — BOJ/Codeforces parity |

All presets pin `CMAKE_CXX_COMPILER=/opt/homebrew/bin/g++-16`, generator
`Ninja`, and `CMAKE_CXX_STANDARD=20` with extensions off.

Your machine-local active problem lives in a gitignored `CMakeUserPresets.json`
inheriting from `dev`. In CLion the equivalent switch is the profile's "CMake
options" field (`-DPS_SCOPE=boj/1000`), which reconfigures on edit.

Scoping exists because one-target-per-problem otherwise gives CLion a run
configuration per problem. It is the inherent cost of "hundreds of independent
programs" under the flat layout, and a version-controlled cache variable beats
any IDE-only workaround because it applies equally to the CLI.

### Judge-environment parity

Local fidelity to the judge is a recurring concern in this repo, not a one-off:
`boj/build.gradle.kts` carries `-Xss128m` and `ps.test-conventions` carries
`-Xss64m`, both for exactly this reason. The C++ instances:

- **Stack size.** macOS gives the main thread 8 MB; a DFS at depth 10⁶ can
  segfault locally and pass on the judge. All presets link with
  `-Wl,-stack_size,0x4000000` (64 MB), guarded by `if(APPLE)`.
- **libstdc++.** Homebrew g++-16 provides `<bits/stdc++.h>` and `__gnu_pbds`;
  Apple clang's libc++ does not.
- **Flags.** The `judge` preset uses `-O2 -std=c++20`, matching how BOJ and
  Codeforces compile.

Remaining known divergence: macOS vs Linux (`__int128` formatting, `long
double` width). A Docker profile pinning the judge's exact GCC and Linux
libstdc++ was considered and rejected as too slow a loop for the value; revisit
only if a divergence actually bites.

## Formatting and the pre-commit hook

`cpp/.clang-format`: Google style with `ColumnLimit: 100`, mirroring the Java
side (`palantirJavaFormat().style("GOOGLE")`, and Google Java Style is 100
columns). CLion reads `.clang-format` natively, so the IDE and CLI agree.

Accepted consequence: a formatted file is not byte-identical to what was pasted
into the judge. In practice you format, then paste.

`.githooks/pre-commit` is **extended, not duplicated** — its stash-and-restore
logic manipulates the working tree and must remain a single boundary. Two
edits:

1. Widen the staged-file gate from `\.(java|kts?)$` to also match
   `\.(cpp|hpp)$`.
2. Run each checker only when its language is staged: `./gradlew spotlessCheck
   ktlintCheck` for Java/Kotlin, `clang-format --dry-run --Werror` on the
   staged C++ paths. `--dry-run --Werror` is clang-format's check mode; it must
   never rewrite files, since the hook lints a stashed snapshot.

`.githooks/pre-commit.test.sh` gains matching cases: a C++-only commit lints, a
mixed commit lints both, a Java-only commit skips clang-format.

## Per-problem workflow

**stdin/stdout problem:**

1. Create `cpp/<judge>/<problem>.cpp` — self-contained, with `main`.
2. Create `cpp/<judge>/tests/<problem>/` and add the statement's samples as
   `sample-1.in` / `sample-1.out`, plus reasoned edge cases under descriptive
   names (`max-values`, `single-element`, `all-negative`). CTest names tests
   after the file stem, so `ctest` output reads `boj/1000/max-values` — the
   descriptive-name convention the Java tests already follow, with no framework
   involved.
3. `cmake --preset dev -DPS_SCOPE=<judge>/<problem>` then `ctest --preset dev`.
4. Iterate to green, then `pbcopy < cpp/<judge>/<problem>.cpp` and submit.
5. Add the problem to `cpp/README.md`.

**function-signature problem:**

1. Create `cpp/<judge>/<Problem>.hpp` with the `Solution` class, methods
   defined inline.
2. Create `cpp/<judge>/tests/<Problem>_test.cpp` with descriptive `TEST()`
   cases. Use gmock matchers (`UnorderedElementsAreArray`) for
   order-independent answers.
3. `ctest --preset dev`, iterate to green, paste the class body, submit.
4. Add the problem to `cpp/README.md`.

Because no solution is split across a header and an implementation file,
submission is always a single copy with no bundler step.

## Prerequisites

```bash
brew install cmake ninja clang-format
```

`g++-16` is already installed. CLion bundles CMake and Ninja, so those two are
needed only for CLI use; `clang-format` is needed by the pre-commit hook
regardless.

Open `cpp/` as its own CLion project — the repo root has no `CMakeLists.txt`
because it is a Gradle build. CLion's per-target run configurations have a
"Redirect input from" field, which is the fastest way to run one problem
against one sample from the IDE.

## Documentation

- `docs/cpp-track.md` — layout, presets, adding a problem, submitting,
  judge-parity notes. Referenced from `AGENTS.md` (to which `CLAUDE.md` is a
  symlink), matching how `docs/boj-problem-archive.md` and
  `docs/dependency-updates.md` are already referenced.
- `cpp/README.md` — the C++ problem index, hand-maintained like the root one.
- Root `README.md` — one pointer line to `cpp/`.
- `.gitignore` — `cpp/build/` and `CMakeUserPresets.json`.

## Risks to validate during implementation

1. **AddressSanitizer under Homebrew GCC on macOS** has a history of being
   unreliable on Darwin. Validate early. If it does not work, the fallback is
   `-D_GLIBCXX_DEBUG -D_GLIBCXX_ASSERTIONS` (libstdc++'s own bounds checking,
   which needs no sanitizer runtime) plus `-fsanitize=undefined` alone. That
   combination catches out-of-bounds on `std::vector` and `std::array` but not
   on raw C arrays — an accepted reduction, not a silent one.
2. **BOJ's available C++ standards** could not be confirmed; acmicpc.net is
   unreachable (see `docs/boj-problem-archive.md`). C++20 is the assumption.
   Changing it is one line in the presets.
3. **Configure time** with several hundred targets under `PS_SCOPE=all`. Expected
   to stay in the low seconds with Ninja; if it does not, per-judge scoping
   already exists as the mitigation.

## Deferred

Explicitly out of scope for the initial implementation, to be revisited once
enough problems exist to know what the tooling should contain:

- A `cpp/tools/ps.py` CLI with `new` (scaffold a problem) and `stress`
  (generator vs. brute-force differential testing) verbs.
- A CLion External Tool bound to a shortcut that runs the current editor file's
  problem, bypassing the run-configuration dropdown entirely.
- Special-judge comparators (`RunCase.cmake` has the extension point).
- A generated rather than hand-maintained `cpp/README.md` index.
- CI. The repo has none today; the C++ track does not introduce the need for
  one.
