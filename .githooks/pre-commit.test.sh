#!/usr/bin/env bash
#
# Regression tests for the pre-commit hook (.githooks/pre-commit).
#
# Each test builds a throwaway git repo with a FAKE ./gradlew -- no real Gradle or
# JVM runs -- then drives the hook through a real `git commit` and asserts its
# contract. The fake gradlew models a whole-tree linter: it fails iff some *.java
# on disk contains the marker "BADFORMAT". That lets the tests prove WHICH content
# the hook puts on disk (the staged snapshot) at lint time, and that it restores
# the working tree afterward.
#
# Usage: .githooks/pre-commit.test.sh [path-to-hook]   (defaults to ./pre-commit)
set -uo pipefail

HOOK=${1:-"$(cd "$(dirname "$0")" && pwd)/pre-commit"}
ORIG=$(pwd)
PASS=0
FAIL=0

# Fake gradlew: a whole-tree linter that fails on any on-disk *.java with BADFORMAT.
FAKE_GRADLEW='#!/usr/bin/env bash
if grep -rq --include="*.java" --exclude-dir=.git BADFORMAT . 2>/dev/null; then
  echo "fake-lint: BADFORMAT present"; exit 1
fi
echo "fake-lint: clean"; exit 0
'

new_sandbox() {
  local d
  d=$(mktemp -d)
  git -C "$d" init -q
  git -C "$d" config user.email t@t.dev
  git -C "$d" config user.name tester
  git -C "$d" config core.hooksPath .hooks
  mkdir -p "$d/.hooks"
  cp "$HOOK" "$d/.hooks/pre-commit"
  chmod +x "$d/.hooks/pre-commit"
  printf '%s' "$FAKE_GRADLEW" >"$d/gradlew"
  chmod +x "$d/gradlew"
  # Track gradlew + the hook (as in the real repo) so --include-untracked never
  # stashes them; commit with --no-verify so this setup commit skips the hook.
  git -C "$d" add gradlew .hooks/pre-commit
  git -C "$d" commit -q --no-verify -m baseline
  printf '%s' "$d"
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
try_commit() { if git commit -qm "$1" >hook.log 2>&1; then printf yes; else printf no; fi; }
stash_count() { git stash list | grep -c . | tr -d ' '; }

echo "Testing hook: $HOOK"

# T1 -- clean staged Java commits.
d=$(new_sandbox)
cd "$d"
printf 'class A {}\n' >A.java
git add A.java
assert "T1 clean staged commits" "$(try_commit one)" "yes"
cd "$ORIG"

# T2 -- a violation in the staged content blocks the commit.
d=$(new_sandbox)
cd "$d"
printf 'class A {}\n// BADFORMAT\n' >A.java
git add A.java
assert "T2 dirty staged blocks" "$(try_commit two)" "no"
cd "$ORIG"

# T4 -- an untracked dirty file must NOT block, and must be restored.
d=$(new_sandbox)
cd "$d"
printf 'class A {}\n' >A.java
git add A.java                       # clean, staged
printf 'class B {}\n// BADFORMAT\n' >B.java   # untracked, dirty
assert "T4 untracked ignored -> commits" "$(try_commit four)" "yes"
assert "T4 untracked file restored" "$([ -f B.java ] && echo yes || echo no)" "yes"
cd "$ORIG"

# T5 -- lints the STAGED snapshot, not the worktree; unstaged edit is restored.
d=$(new_sandbox)
cd "$d"
printf 'class A {\n}\n' >A.java
git add A.java                       # staged: clean
printf 'class A {\n}\n// BADFORMAT\n' >A.java # unstaged: + violation (other line)
assert "T5 staged-clean commits despite dirty worktree" "$(try_commit five)" "yes"
assert "T5 unstaged edit restored" "$(grep -c BADFORMAT A.java)" "1"
cd "$ORIG"

# T6 -- same-line partial staging (both clean): commit succeeds, tree restored
#       with NO conflict markers and NO leaked stash.
d=$(new_sandbox)
cd "$d"
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
cd "$d"
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
cd "$d"
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

echo "----"
echo "PASS=$PASS FAIL=$FAIL"
[ "$FAIL" -eq 0 ]
