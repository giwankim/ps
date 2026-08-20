# AGENTS.md

Repo-level instructions for coding agents. See
[docs/dependency-updates.md](docs/dependency-updates.md) for the
dependency-update workflow.

acmicpc.net is down. Recover BOJ problem statements, constraints, and samples
from the local archive rather than by triangulating mirrors — see
[docs/boj-problem-archive.md](docs/boj-problem-archive.md).

The `cpp/` directory is a standalone C++ competitive-programming track built
with CMake, outside the Gradle build. See [docs/cpp-track.md](docs/cpp-track.md)
for layout, presets, and the per-problem workflow.

Solutions in `leetcode/` are grouped into numeric range packages keyed on the
LeetCode problem number, and each class carries a javadoc link to its problem.
See [docs/leetcode-package-layout.md](docs/leetcode-package-layout.md) before
adding a solution.
