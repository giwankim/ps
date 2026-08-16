#!/usr/bin/env bash
#
# Regression tests for the pre-commit hook (.githooks/pre-commit).
#
# Each test builds a throwaway git repo with a FAKE ./gradlew and a FAKE
# bin/clang-format -- no real Gradle, JVM, or clang-format runs -- then drives
# the hook through a real `git commit` and asserts its contract. The fake
# gradlew models a whole-tree linter: it fails iff some *.java on disk contains
# the marker "BADFORMAT". The fake clang-format models a per-file linter: it
# fails iff the file it's given contains that marker (or, to catch a dispatch
# bug, if it's ever handed a *.java file at all). Together they let the tests
# prove WHICH content the hook puts on disk (the staged snapshot) at lint time,
# WHICH tool runs for a given staged language, and that the hook restores the
# working tree afterward.
#
# Usage: .githooks/pre-commit.test.sh [path-to-hook]   (defaults to ./pre-commit)
set -uo pipefail

HOOK=${1:-"$(cd "$(dirname "$0")" && pwd)/pre-commit"}
ORIG=$(pwd)
PASS=0
FAIL=0

# PATH the hook runs under. Tests prepend their sandbox bin/ so the fake
# clang-format is found; T16 points it somewhere without one.
PS_TEST_PATH="$PATH"

# Fake gradlew: a whole-tree linter that fails on any on-disk *.java with BADFORMAT.
FAKE_GRADLEW='#!/usr/bin/env bash
if grep -rq --include="*.java" --exclude-dir=.git BADFORMAT . 2>/dev/null; then
  echo "fake-lint: BADFORMAT present"; exit 1
fi
echo "fake-lint: clean"; exit 0
'

# Fake clang-format: a per-file checker that fails on any argument containing
# BADFORMAT, or on any *.java argument (clang-format has a Java mode, so
# feeding it Java files -- e.g. by dispatching on $staged instead of
# $cpp_staged -- would be a real bug, not just a test artifact). Mirrors
# FAKE_GRADLEW so tests can prove WHICH tool ran, and WHICH files it received.
FAKE_CLANG_FORMAT='#!/usr/bin/env bash
for f in "$@"; do
  case "$f" in --*) continue;; esac
  case "$f" in *.java) echo "fake-clang-format: should never see $f" >&2; exit 1;; esac
  if [ -f "$f" ] && grep -q BADFORMAT "$f"; then
    echo "fake-clang-format: $f needs formatting" >&2
    exit 1
  fi
done
exit 0
'

# Build a throwaway repo; echo its path. Setup runs under `set -e` inside a
# group whose stdout goes to stderr, because this function's stdout IS its
# return value: one stray line -- git printing "nothing to commit" when setup
# half-fails -- gets captured into the caller's $d. The bare `cd "$d"` this
# suite used to do then failed, and with no `set -e` at top level the run
# carried on and aimed its `git commit`s at the REAL repository. Not
# hypothetical: it landed test fixtures on a live branch and overwrote the
# tracked gradlew with the fake. `enter` below is the second half of the guard,
# needed because `exit` here only leaves the subshell that command substitution
# creates.
new_sandbox() {
  local d rc
  d=$(mktemp -d) || return 1
  # Kept out of any `if`/`||` condition on purpose: bash disables errexit inside
  # a subshell used as a condition, so `set -e` there would not abort on the
  # first failure and one missing file would print a cascade of follow-on errors.
  ( set -e
    git -C "$d" init -q
    git -C "$d" config user.email t@t.dev
    git -C "$d" config user.name tester
    git -C "$d" config core.hooksPath .hooks
    mkdir -p "$d/.hooks"
    cp "$HOOK" "$d/.hooks/pre-commit"
    chmod +x "$d/.hooks/pre-commit"
    printf '%s' "$FAKE_GRADLEW" >"$d/gradlew"
    chmod +x "$d/gradlew"
    mkdir -p "$d/bin"
    printf '%s' "$FAKE_CLANG_FORMAT" >"$d/bin/clang-format"
    chmod +x "$d/bin/clang-format"
    # Track gradlew + the hook + the fake clang-format (as in the real repo) so
    # --include-untracked never stashes them; commit with --no-verify so this
    # setup commit skips the hook.
    git -C "$d" add gradlew .hooks/pre-commit bin/clang-format
    git -C "$d" commit -q --no-verify -m baseline
  ) >&2
  rc=$?
  if [ "$rc" -ne 0 ]; then
    echo "FATAL: sandbox setup failed (hook: $HOOK)" >&2
    return 1
  fi
  printf '%s' "$d"
}

# cd into a sandbox or abort the run. Never fall through into the real repo: a
# `git commit` there is not a failed test, it is a corrupted working tree.
enter() {
  if [ -z "$1" ] || [ ! -d "$1/.git" ] || ! cd "$1"; then
    printf 'FATAL: refusing to run tests outside a sandbox (got [%s])\n' "$1" >&2
    exit 1
  fi
}

assert() { # label  actual  expected
  if [ "$2" = "$3" ]; then
    PASS=$((PASS + 1))
    printf '  ok   %s\n' "$1"
  else
    FAIL=$((FAIL + 1))
    printf '  FAIL %s -- want [%s] got [%s]\n' "$1" "$3" "$2"
  fi
}

# Commit in the current dir; echo yes/no for success. Hook chatter -> hook.log.
try_commit() { if PATH="$PS_TEST_PATH" git commit -qm "$1" >hook.log 2>&1; then printf yes; else printf no; fi; }
stash_count() { git stash list | grep -c . | tr -d ' '; }

echo "Testing hook: $HOOK"

# T1 -- clean staged Java commits.
d=$(new_sandbox)
enter "$d"
printf 'class A {}\n' >A.java
git add A.java
assert "T1 clean staged commits" "$(try_commit one)" "yes"
cd "$ORIG"

# T2 -- a violation in the staged content blocks the commit.
d=$(new_sandbox)
enter "$d"
printf 'class A {}\n// BADFORMAT\n' >A.java
git add A.java
assert "T2 dirty staged blocks" "$(try_commit two)" "no"
cd "$ORIG"

# T4 -- an untracked dirty file must NOT block, and must be restored.
d=$(new_sandbox)
enter "$d"
printf 'class A {}\n' >A.java
git add A.java                       # clean, staged
printf 'class B {}\n// BADFORMAT\n' >B.java   # untracked, dirty
assert "T4 untracked ignored -> commits" "$(try_commit four)" "yes"
assert "T4 untracked file restored" "$([ -f B.java ] && echo yes || echo no)" "yes"
cd "$ORIG"

# T5 -- lints the STAGED snapshot, not the worktree; unstaged edit is restored.
d=$(new_sandbox)
enter "$d"
printf 'class A {\n}\n' >A.java
git add A.java                       # staged: clean
printf 'class A {\n}\n// BADFORMAT\n' >A.java # unstaged: + violation (other line)
assert "T5 staged-clean commits despite dirty worktree" "$(try_commit five)" "yes"
assert "T5 unstaged edit restored" "$(grep -c BADFORMAT A.java)" "1"
cd "$ORIG"

# T6 -- same-line partial staging (both clean): commit succeeds, tree restored
#       with NO conflict markers and NO leaked stash.
d=$(new_sandbox)
enter "$d"
printf 'a\nb\nc\n' >A.java
git add A.java
git commit -qm init >/dev/null 2>&1
printf 'a\nSTAGED\nc\n' >A.java
git add A.java                       # stage line 2
printf 'a\nUNSTAGED\nc\n' >A.java    # unstaged edit to the SAME line 2
assert "T6 same-line partial commits" "$(try_commit six)" "yes"
assert "T6 no conflict markers" "$(grep -c '<<<<<<<' A.java)" "0"
assert "T6 no leaked stash" "$(stash_count)" "0"
cd "$ORIG"

# T7 -- the commit captures the STAGED content (different lines: restore can't
#       conflict, isolating index correctness).
d=$(new_sandbox)
enter "$d"
printf 'class A {\n  int x;\n}\n' >A.java
git add A.java
git commit -qm init >/dev/null 2>&1
printf 'class A {\n  int STAGED;\n}\n' >A.java
git add A.java                       # staged S (line 2)
printf 'class A {\n  int STAGED;\n}\n// extra\n' >A.java # unstaged (line 4)
try_commit seven >/dev/null
assert "T7 commit has staged content" "$(git show HEAD:A.java | grep -c STAGED)" "1"
assert "T7 commit excludes unstaged" "$(git show HEAD:A.java | grep -c extra)" "0"
assert "T7 worktree keeps unstaged" "$(grep -c extra A.java)" "1"
cd "$ORIG"

# T8 -- staged content dirty though the worktree looks clean (same line):
#       commit blocked AND the clean worktree version is restored.
d=$(new_sandbox)
enter "$d"
printf 'a\nb\nc\n' >A.java
git add A.java
git commit -qm init >/dev/null 2>&1
printf 'a\nb // BADFORMAT\nc\n' >A.java
git add A.java                       # staged: dirty line 2
printf 'a\nb\nc\n' >A.java           # unstaged: clean (reverts line 2)
assert "T8 dirty-staged blocks even if worktree clean" "$(try_commit eight)" "no"
assert "T8 worktree restored to clean unstaged version" "$(grep -c BADFORMAT A.java)" "0"
assert "T8 no leaked stash after block" "$(stash_count)" "0"
cd "$ORIG"

# T9 -- a non-ASCII staged path must still trip the Java gate. git C-quotes such
#       paths unless core.quotePath=false, and the trailing quote made the gate's
#       '\.java$' miss -- so lint was skipped and this dirty commit sailed through.
d=$(new_sandbox)
enter "$d"
printf 'class A {}\n// BADFORMAT\n' >두_정수.java
git add 두_정수.java
assert "T9 non-ascii staged path trips java gate" "$(try_commit nine)" "no"
cd "$ORIG"

# T10 -- clean staged C++ commits (the gate must admit .cpp/.hpp).
d=$(new_sandbox); enter "$d"; PS_TEST_PATH="$d/bin:$PATH"
printf 'int main() { return 0; }\n' >a.cpp
git add a.cpp
assert "T10 clean staged cpp commits" "$(try_commit ten)" "yes"
cd "$ORIG"; PS_TEST_PATH="$PATH"

# T11 -- a violation in staged C++ blocks the commit.
d=$(new_sandbox); enter "$d"; PS_TEST_PATH="$d/bin:$PATH"
printf 'int main() { return 0; }\n// BADFORMAT\n' >a.cpp
git add a.cpp
assert "T11 dirty staged cpp blocks" "$(try_commit eleven)" "no"
cd "$ORIG"; PS_TEST_PATH="$PATH"

# T12 -- a mixed commit runs BOTH linters: clean Java must not excuse dirty C++.
d=$(new_sandbox); enter "$d"; PS_TEST_PATH="$d/bin:$PATH"
printf 'class A {}\n' >A.java
printf 'int main() { return 0; }\n// BADFORMAT\n' >a.cpp
git add A.java a.cpp
assert "T12 mixed commit lints cpp too" "$(try_commit twelve)" "no"
cd "$ORIG"; PS_TEST_PATH="$PATH"

# T13 -- a Java-only commit must NOT invoke clang-format. Proven with a fake
#        that always fails: if it ran, the commit would be blocked.
d=$(new_sandbox); enter "$d"; PS_TEST_PATH="$d/bin:$PATH"
printf '#!/usr/bin/env bash\nexit 1\n' >bin/clang-format
chmod +x bin/clang-format
# COMMIT the always-failing fake: an unstaged edit would be reverted by the
# hook's stash, restoring the permissive fake and making this test vacuous.
git add bin/clang-format
git commit -q --no-verify -m always-fail-clang-format
printf 'class A {}\n' >A.java
git add A.java
assert "T13 java-only skips clang-format" "$(try_commit thirteen)" "yes"
cd "$ORIG"; PS_TEST_PATH="$PATH"

# T14 -- a C++-only commit must NOT invoke Gradle (which is slow to start).
#        Proven with a gradlew that always fails.
d=$(new_sandbox); enter "$d"; PS_TEST_PATH="$d/bin:$PATH"
printf '#!/usr/bin/env bash\nexit 1\n' >gradlew
chmod +x gradlew
# COMMIT the always-failing fake, for the same reason as T13: gradlew is tracked,
# so an unstaged edit would be stashed away and the permissive fake restored.
git add gradlew
git commit -q --no-verify -m always-fail-gradlew
printf 'int main() { return 0; }\n' >a.cpp
git add a.cpp
assert "T14 cpp-only skips gradle" "$(try_commit fourteen)" "yes"
cd "$ORIG"; PS_TEST_PATH="$PATH"

# T15 -- lints the STAGED snapshot: an unstaged dirty .cpp must not block.
d=$(new_sandbox); enter "$d"; PS_TEST_PATH="$d/bin:$PATH"
printf 'int main() {\n  return 0;\n}\n' >a.cpp
git add a.cpp
printf 'int main() {\n  return 0;\n}\n// BADFORMAT\n' >a.cpp
assert "T15 staged-clean cpp commits despite dirty worktree" "$(try_commit fifteen)" "yes"
assert "T15 unstaged cpp edit restored" "$(grep -c BADFORMAT a.cpp)" "1"
cd "$ORIG"; PS_TEST_PATH="$PATH"

# T16 -- staged C++ with no clang-format on PATH: blocked, and no leaked stash.
#        The PATH is an allowlist of symlinks, not "$d/nocf:/usr/bin:/bin": a
#        distro clang-format at /usr/bin/clang-format (Linux, CI images) would
#        satisfy the hook's guard and silently flip the first two assertions --
#        the commit would succeed and the suite would still look green here.
#        bash and grep are needed as well as git: the hook's shebang resolves
#        bash through PATH, and it greps the staged list before the guard. If it
#        ever gains another external dependency, the second assertion catches it
#        -- the commit still blocks, but the message no longer names clang-format.
d=$(new_sandbox); enter "$d"
mkdir -p "$d/nocf"
for tool in bash git grep; do
  ln -s "$(command -v "$tool")" "$d/nocf/$tool"
done
PS_TEST_PATH="$d/nocf"
printf 'int main() { return 0; }\n' >a.cpp
git add a.cpp
printf 'class B {}\n' >B.java          # untracked, so a stash would be taken
assert "T16 missing clang-format blocks" "$(try_commit sixteen)" "no"
assert "T16 message names clang-format" "$(grep -c clang-format hook.log)" "1"
assert "T16 no leaked stash" "$(stash_count)" "0"
cd "$ORIG"; PS_TEST_PATH="$PATH"

# T17 -- clang-format failing AFTER the stash must still restore the tree.
d=$(new_sandbox); enter "$d"; PS_TEST_PATH="$d/bin:$PATH"
printf 'int main() { return 0; }\n// BADFORMAT\n' >a.cpp
git add a.cpp
printf 'int main() { return 1; }\n// WIP-UNSTAGED\n' >a.cpp
printf 'scratch\n' >untracked.txt
assert "T17 dirty staged cpp blocks" "$(try_commit seventeen)" "no"
assert "T17 no leaked stash after cpp block" "$(stash_count)" "0"
assert "T17 unstaged cpp edit restored" "$(grep -c WIP-UNSTAGED a.cpp)" "1"
assert "T17 untracked file restored" "$(cat untracked.txt)" "scratch"
cd "$ORIG"; PS_TEST_PATH="$PATH"

# T18 -- a mixed commit runs Gradle too: clean C++ must not excuse dirty Java.
d=$(new_sandbox); enter "$d"; PS_TEST_PATH="$d/bin:$PATH"
printf 'class A {}\n// BADFORMAT\n' >A.java
printf 'int main() { return 0; }\n' >a.cpp
git add A.java a.cpp
assert "T18 mixed commit lints java too" "$(try_commit eighteen)" "no"
cd "$ORIG"; PS_TEST_PATH="$PATH"

# T19 -- a clean mixed commit succeeds. T12/T18 alone don't prove clang-format
#        receives C++ paths only ($cpp_staged, not $staged): both stage one
#        dirty file, so the commit is expected to block either way. Here
#        everything is clean, so if clang-format were handed the staged .java
#        too it would spuriously block (see FAKE_CLANG_FORMAT's *.java guard).
d=$(new_sandbox); enter "$d"; PS_TEST_PATH="$d/bin:$PATH"
printf 'class A {}\n' >A.java
printf 'int main() { return 0; }\n' >a.cpp
git add A.java a.cpp
assert "T19 clean mixed commit succeeds" "$(try_commit nineteen)" "yes"
cd "$ORIG"; PS_TEST_PATH="$PATH"

echo "----"
echo "PASS=$PASS FAIL=$FAIL"
[ "$FAIL" -eq 0 ]
