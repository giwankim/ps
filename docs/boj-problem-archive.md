# BOJ Problem Archive

acmicpc.net is unreachable, so BOJ problem statements, constraints, and sample
I/O have to come from an archive. [golbin/blj][blj] ships one: a SQLite catalog
of 33,828 BOJ problems including statement HTML, limits, tags, and samples.

`boj/tools/archive_problems.py` reads that catalog and writes
`boj/problems/<id>/`. The output is **gitignored** — statements are BOJ's
copyright, and the archive is reproducible from the script.

[blj]: https://github.com/golbin/blj

## blj is not a dependency

Nothing about blj enters `settings.gradle.kts`, `gradle/libs.versions.toml`, or
`boj/build.gradle.kts`. There is no artifact to resolve — blj is a Vue/Vite web
app, and its 141 MB catalog is Git LFS binary that has no business in this
repo's history.

You also never run blj. Its pnpm, Vite, and Docker sidecar toolchain exists to
serve a browsing UI and a local judge; this repo already judges through
`./gradlew :boj:test`. Because the catalog is a committed release artifact, the
script reads it directly and skips everything above it. The only tools required
are `git-lfs` and Python 3 — no Node, no Docker, no build.

## 1. One-time setup

Clone blj **outside** this repo and pull the catalog:

```bash
git clone --depth 1 https://github.com/golbin/blj ~/Development/blj
git -C ~/Development/blj lfs pull --include data/release/boj-catalog.sqlite
```

The pull fetches ~141 MB. Without it the file is a 134-byte LFS pointer, which
the script detects and reports rather than failing obscurely.

The script defaults to `~/Development/blj/data/release/boj-catalog.sqlite`.
Point it elsewhere with `--catalog PATH` or the `BLJ_CATALOG` environment
variable.

## 2. Archive problems

```bash
python3 boj/tools/archive_problems.py            # every solved problem not yet archived
python3 boj/tools/archive_problems.py 2015 1753  # specific ids, before solving them
python3 boj/tools/archive_problems.py --force    # re-archive, overwriting
```

With no ids, the script archives every `boj/src/main/java/boj/bojNNNN` package
that is not already present, and skips the rest. `boj/support` holds shared
helpers rather than a problem, so it is ignored.

Problems absent from the catalog, or present with metadata but no statement
text, are reported on stderr at the end of the run. Both lists are empty for
this repo's current set.

## 3. What lands on disk

```
boj/problems/2015/
  problem.json     # id, title, level, tags, limits, samples, provenance
  statement.md     # 문제 / 입력 / 출력 / 힌트 / 예제, converted from HTML
  source.html      # the archived fragments verbatim, if the conversion loses something
  samples/
    sample-01.in   sample-01.out
    sample-02.in   sample-02.out
```

The layout mirrors `algospot/problems/<SLUG>/`, which is committed rather than
ignored because algospot's archive was captured under different terms.

`problem.json` carries a `provenance` block through from the catalog —
`sourceUrl`, `mirrorUrl`, `archivedAt`, and a `contentHash`. That is what lets a
later reader tell a constraint read off the official statement from one
triangulated across mirrors.

## 4. Verify an archived problem

The samples are a differential test against the committed solution:

```bash
./gradlew :boj:classes -q
java -cp boj/build/classes/java/main boj.boj2015.Main < boj/problems/2015/samples/sample-01.in
```

Across all 138 solved problems (261 samples), 259 match exactly. The two that do
not are correct behavior, not defects, and are worth knowing about as a class:

- **10699** prints today's date, so its stored sample output can never match.
- **2467** is special-judged — its statement permits any tied pair — so a
  different valid answer is not a wrong answer.

A sample diff alone cannot distinguish these from a genuine WA. The statement
can, which is the point of archiving it.

## Caveats

**The snapshot is frozen.** It is dated 2026-04-24. blj's own crawler targets
acmicpc.net, so it cannot refresh while the site is down either. Re-pulling the
repo gets you a newer snapshot only once upstream can crawl again.

**Some images are dead.** Most image URLs were rewritten by blj's archiver to a
live mirror, but a handful resolve to `acmicpc.net` and will not load until BOJ
returns. The script resolves site-relative paths to absolute URLs so the target
is at least identifiable.

**Statements are Korean.** Levels come from solved.ac (`Gold IV`), and tags are
empty for many problems.

## Running the script's tests

```bash
python3 boj/tools/archive_problems_test.py
```

The suite covers the HTML-to-Markdown conversion, which is the only part with
enough logic to fail silently — a bad conversion does not crash, it produces a
statement that reads fine but states the wrong bound. The cases are the actual
tag and entity inventory observed across this repo's solved problems.
