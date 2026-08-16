# C++ Track

A standalone competitive-programming track under `cpp/`, independent of the
Gradle build. Design: [docs/superpowers/specs/2026-08-16-cpp-track-design.md](superpowers/specs/2026-08-16-cpp-track-design.md).

## Setup

```bash
brew install cmake ninja clang-format
```

`g++-16` must be at `/opt/homebrew/bin/g++-16`; every preset pins it. Apple
clang is never used — `<bits/stdc++.h>` and `__gnu_pbds` are libstdc++-only, so
building with clang would diverge from the judges.

Open `cpp/` as its own CLion project. The repo root has no `CMakeLists.txt`
because it is a Gradle build. CLion run configurations have a "Redirect input
from" field — point it at a `.in` file to run one problem against one sample.

## Layout

```
cpp/<judge>/<problem>.cpp              solution, self-contained, has main()
cpp/<judge>/tests/<problem>/<stem>.in  input
cpp/<judge>/tests/<problem>/<stem>.out expected output
```

A judge is any subdirectory of `cpp/` except `cmake/` and `build/`. Adding one
requires no build-file edit.

## Presets

| Preset | Build dir | Purpose |
| --- | --- | --- |
| `dev` | `cpp/build/dev` | Everyday solving; sanitizers on. Override `PS_SCOPE` per problem |
| `all` | `cpp/build/all` | Full sweep across every judge |
| `judge` | `cpp/build/judge` | `-O2`, no sanitizers — matches how BOJ and Codeforces compile |

Separate build directories, so a scoped `dev` build and a full `all` sweep
coexist without reconfiguring each other.

Cache variables:

| Variable | Default | Meaning |
| --- | --- | --- |
| `PS_SCOPE` | `all` | `all`, `<judge>`, or `<judge>/<problem>` |
| `PS_STD` | `23` | C++ standard |
| `PS_TIMEOUT` | `5` | Per-test timeout in seconds |

Keep your active problem in `cpp/CMakeUserPresets.json` (gitignored), or
override per configure:

```bash
cd cpp
cmake --preset dev -DPS_SCOPE=boj/1000
cmake --build --preset dev
ctest --preset dev
```

## Adding a problem

1. Write `cpp/<judge>/<problem>.cpp` with a `main`.
2. Add samples under `cpp/<judge>/tests/<problem>/` with **descriptive** stems —
   `max-values.in`, not `3.in`. CTest names each test after the stem, so
   failures read `boj/1000/max-values`.
3. `cmake --preset dev -DPS_SCOPE=<judge>/<problem> && cmake --build --preset dev && ctest --preset dev`
4. Submit: `pbcopy < cpp/<judge>/<problem>.cpp`. Every solution is one
   self-contained file, so there is no bundling step.
5. Add the problem to [../cpp/README.md](../cpp/README.md).

## Judge-environment parity

Local fidelity to the judge is a recurring concern, not a one-off — the Java
side carries `-Xss128m` in `boj/build.gradle.kts` and `-Xss64m` in
`ps.test-conventions` for the same reason. The C++ instances:

- **Stack.** macOS gives the main thread 8 MB; judges give more. All presets
  link `-Wl,-stack_size,0x4000000` (64 MB), so a deep DFS does not segfault
  locally on code the judge accepts.
- **libstdc++.** g++-16 provides `<bits/stdc++.h>` and `__gnu_pbds`.
- **Compiler version.** This is the gap C++23 introduces. Local g++-16
  implements more of C++23 than the judges' GCC does, so newer library
  additions (`<print>`, `std::ranges::to`, `std::mdspan`, `<stdfloat>`) can
  compile here and fail on the judge. The fallback needs no file edit:
  `cmake --preset dev -DPS_STD=20`.

## Testing model

Each `.in` file becomes one CTest test that runs the binary as a **subprocess**
and compares stdout to the sibling `.out`. Subprocess rather than in-process
because it gives a fresh process per case (so a global `int a[200005];` cannot
leak between cases), captures `printf`/`scanf` output, and survives a solution
calling `exit()`.

Comparison normalizes **trailing** whitespace only — judges ignore it, but a
bug emitting a stray leading space must still fail.

Two configure-time guards exist because an empty glob yields zero tests, and
zero tests pass green:

- a `.in` with no matching `.out` is a fatal error;
- a solution with no test data is a warning.

Function-signature judges (LeetCode, Programmers) are designed for but not yet
built — see the spec's "Deferred" section.
