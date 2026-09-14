# The house spec-suite style

Read this before writing the test file. It describes the form these suites have
settled into in `leetcode/src/test/java/`. The richest exemplars, worth opening
when the problem is hard:

- `leetcode/src/test/java/leetcode/p0101_0200/BestTimeToBuyAndSellStockIIITest.java`
- `leetcode/src/test/java/leetcode/p1501_1600/StoneGameVTest.java`
- `leetcode/src/test/java/leetcode/p1701_1800/MergeStringsAlternatelyTest.java`

## The skeleton

```java
package leetcode.p1401_1500;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KidsWithTheGreatestNumberOfCandiesTest {
  KidsWithTheGreatestNumberOfCandies sut = new KidsWithTheGreatestNumberOfCandies();

  // Step 1: ...
  @Test
  void descriptiveNameOfTheBehavior() {
    assertThat(sut.kidsWithCandies(new int[] {1, 2}, 1)).containsExactly(true, true);
  }
}
```

Package-private class, no `public`. One `sut` field, not a fresh `new` per test —
Step "one instance answers many inputs" below depends on the instance being
shared. JUnit 5 (`org.junit.jupiter.api.Test`) and AssertJ (`assertThat`);
never JUnit assertions, never Hamcrest.

One `@Test` per case with a name that says what behavior it pins down. No
`@ParameterizedTest` with a long argument list: when a spec goes red, the method
name is the diagnosis. When the problem is parameterized by a scalar, use the
terse `<param><value>` prefix — `n1ReturnsSinglePair`, `k3PicksThreeLargest` —
because JUnit sorts the report by name and that ordering then mirrors the
progression. Otherwise name the behavior, never spell numbers out in words.

## The Step comment is the point

Every test carries a `// Step N:` comment above it. A comment that only restates
the assertion is dead weight. It earns its place by naming **the wrong solution
this case kills**:

```java
  // Step 3: the mirror of Step 2. Every transaction is optional, so a falling market is worth 0 and
  //         never a loss. A solution that always trades, or that reports prices[n-1] - prices[0],
  //         answers -4
```

That is what makes the suite a spec rather than a regression net: the implementer
reads it as a list of the traps, in the order they are worth thinking about. When
the expected value is not obvious from the input, show the intermediate state the
same way the exemplars do (`altitudes = [0, 1, 3, 6] -> highest is 6`).

Continuation lines align under the text, not the `//`. Keep every line inside
100 columns — spotless will not rewrap comments for you.

## The progression

Order the steps so that each one adds a single new idea, and group them with
banner comments once the file passes roughly a dozen tests:

```java
  // ===========================================================================================
  // One transaction or two (Steps 6-9).
  // ===========================================================================================
```

1. **The floor.** The smallest input the constraints actually permit — read the
   constraint block, do not assume 0 or 1. If `2 <= n`, there is no single-element
   case and writing one is a bug in the spec.
2. **One rule per step.** Walk the statement's rules in order. When a rule is
   asymmetric (`word1` goes first, `i < j`), write its mirror as the next step;
   that pair kills the whole family of solutions that confuse the two operands.
3. **Interactions and tie-breaks.** Ties, duplicates, zeros, empty results,
   equal-valued candidates, the case the problem's Note sentence exists to
   disambiguate. This is usually where the problem's real difficulty lives.
4. **The official examples, verbatim.** Name them `leetCodeExample1`,
   `leetCodeExample2`, … and use the input and output exactly as printed. Their
   comment should say what reading each example rules out — LeetCode includes an
   Explanation precisely when the naive reading is wrong.
5. **The constraint bounds.** Maximum `n`, extreme values at both ends of the
   value range, and any combination that overflows `int` if the solver is
   careless. Build these with a deterministic generator — `Arrays.setAll`, a
   `%`-wrapped ramp, `"a".repeat(100)` — never `Random`, so a failure reproduces.
   Give each a timeout:

   ```java
   @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
   ```

   `SEPARATE_THREAD` matters: JUnit's default mode only compares elapsed time
   after the method returns, so against a genuinely too-slow solution a
   same-thread timeout hangs the build instead of reporting the failure it exists
   to report. Put a banner above this group explaining which complexity classes
   the timeouts separate (e.g. "10^5 elements: O(n^2) needs ~10^10 operations and
   cannot finish; O(n log n) finishes in milliseconds").
6. **Hygiene, last.** Two steps that catch defects no functional case will:
   - the input array or list is not modified — clone it, call, assert
     `containsExactly(original)`; sorting the caller's array in place is a
     tempting shortcut;
   - one `sut` answers several inputs of different sizes, deliberately out of
     order, largest in the middle — this catches state cached on the instance
     instead of reset per call.

For a design problem (a constructor plus methods), replace steps 1-3 with
operation sequences: each method alone, then the interleavings that make the
data structure interesting (eviction on overflow, re-insert of an existing key,
read of a key just evicted), and make the hygiene step a long mixed sequence.

## Assertion idioms by return type

| Return | Idiom |
| --- | --- |
| `int` / `long` | `assertThat(sut.f(x)).isEqualTo(6)`; `.isZero()` for 0 |
| `boolean` | `assertThat(sut.f(x)).isTrue()` / `.isFalse()` |
| `String` | `assertThat(sut.f(x)).isEqualTo("apbqcr")` |
| `int[]` / `List<T>`, order matters | `.containsExactly(1, 2, 3)` |
| result order unspecified | `.containsExactlyInAnyOrder(...)` — and say so in the comment |
| `List<List<Integer>>` | `.containsExactlyInAnyOrderElementsOf(List.of(List.of(1, 2), ...))` |
| in-place mutation with a returned length | mutate, then `assertThat(Arrays.copyOf(chars, length)).containsExactly(...)` |
| large-input size claims | chain: `.isEqualTo(expected).hasSize(200)` |

Extract a `private static` helper at the bottom of the class for repeated
large-input construction (`filled(100_000, 0)`), not a `@BeforeEach`.

## Getting the expected values right

A wrong expected value silently misdirects whoever implements this later, and it
is the one defect the RED run cannot catch — everything fails with
`UnsupportedOperationException` either way.

- Values printed in the official examples are ground truth. Copy them; do not
  "simplify" the input.
- Every value you derive by hand — every max-constraint step, every tie-break —
  gets confirmed by **two independent implementations** written in the scratchpad
  (never in the repo): a brute force, plus the intended algorithm, agreeing on
  all the small cases before you trust the brute force on the large ones.
- While you are there, compute what each *wrong* approach answers, so the claim
  in the Step comment ("a greedy that commits to the earliest profitable trade
  answers 10") is a checked number rather than a guess.

## Lint and format traps

- Prose comments can trip the "commented-out code" rule (S125). Avoid trailing
  semicolons inside comments, and rephrase coordinate tuples like `(1, 1)` as
  prose ("row 1, column 1").
- American spelling throughout.
- `./gradlew :leetcode:spotlessJavaCheck` is the arbiter of formatting. The
  worktree usually holds other work-in-progress leetcode files, so hand-apply the
  diff for your two files rather than running a module-wide `spotlessApply` that
  would reformat someone else's file.
