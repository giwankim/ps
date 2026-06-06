package boj.boj13423;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 13423 Three Dots -- count the equally-spaced triples (3-term arithmetic progressions) among a
 * set of distinct points on a line.
 *
 * <p>On a line sit {@code N} distinct points; point {@code i} is at position {@code Xi}. Picking
 * three of them and naming the leftmost {@code a}, the middle {@code b}, and the rightmost
 * {@code c}, the trio is "equally spaced" when {@code Xb - Xa == Xc - Xb} -- i.e. {@code b} is the
 * arithmetic mean of {@code a} and {@code c}. The task is to count, over all {@code C(N, 3)} trios,
 * how many are equally spaced. Worked example for {@code N = 5} at positions {@code -4 -1 0 2 4}:
 * the equally spaced trios are {@code (-4,-1,2)}, {@code (-4,0,4)}, and {@code (0,2,4)} -- so the
 * answer is {@code 3} (the trio {@code (-4,-1,0)} does not count, since {@code 3 != 1}).
 *
 * <p>Input: line 1 is the number of test cases {@code T} (a natural number). Each test case is two
 * lines -- the point count {@code N} ({@code 3 <= N <= 1,000}), then {@code N} space-separated
 * integer positions, all distinct and given in arbitrary order (not necessarily sorted). Output:
 * for each test case, on its own line, the count of equally spaced trios.
 *
 * <p><b>Coordinate range -- the spec understates it.</b> Blog mirrors of the statement quote
 * {@code [-100,000,000, 100,000,000]}, but the live judge data carries coordinates around
 * {@code 1e9}: a 32-bit {@code int} solution is judged wrong, while reading positions as
 * {@code long} is accepted. The trap is {@code 2*Xb - Xa}, whose magnitude reaches {@code 3 *
 * |coord|}; once {@code |coord| >= 2^30 (~1.07e9)} that probe overflows {@code int}. Critically the
 * overflow never <em>under</em>-counts -- two's-complement arithmetic is modular, so a genuine
 * third point {@code R = 2M - L} (always within range, hence within {@code int}) is computed
 * exactly even through an intermediate overflow. The damage is a phantom <em>over</em>-count: a
 * non-progression pair whose true target lands outside {@code int} can wrap onto a real point and
 * be miscounted as a trio ({@link #overflowOfTheMidpointProbeIsNotCounted},
 * {@link #realTripleAtOverflowScaleIsCountedExactlyOnce}).
 *
 * <p>The fixtures pin the things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>Equal spacing, not mere collinearity.</b> Every trio lies on the line, so the only test
 *       is {@code Xb - Xa == Xc - Xb}; an unequal-gap trio must not count
 *       ({@link #nonArithmeticTripleIsNotCounted}), and the bare minimum {@code N = 3} either
 *       yields one or zero ({@link #singleArithmeticTripleIsCounted},
 *       {@link #minimumThreePointsNotEquallySpacedYieldZero}).
 *   <li><b>Every qualifying trio counts -- overlaps included.</b> The answer is the number of
 *       trios, not a partition into disjoint ones; shared endpoints and middles each count, and a
 *       single point set contributes trios at several common differences
 *       ({@link #overlappingTriplesAreEachCounted}).
 *   <li><b>The middle is the arithmetic mean -- the third point must actually exist.</b> A trio
 *       qualifies only when the implied point {@code 2*Xb - Xa} is itself one of the given points;
 *       a gap in the set silently kills a would-be progression
 *       ({@link #missingMiddlePointBreaksWouldBeTriple}).
 *   <li><b>Position, not input order, defines {@code a}/{@code b}/{@code c}.</b> Points arrive
 *       unsorted, so a solution must sort (or otherwise order by value) first -- shuffled and
 *       fully-descending inputs must give the same counts as their sorted forms
 *       ({@link #inputIsSortedByPositionNotByInputOrder},
 *       {@link #descendingMinimumInputIsCountedAfterSorting}).
 *   <li><b>Negative coordinates and zero-straddling progressions.</b> A progression can straddle
 *       zero with large opposite-signed extremes as its endpoints
 *       ({@link #negativeAndExtremeCoordinatesFormTriple}).
 *   <li><b>The midpoint probe needs 64 bits.</b> {@code 2*Xb - Xa} overflows 32-bit {@code int}
 *       once coordinates reach {@code ~1e9}; the wrapped value can collide with a real point and
 *       inflate the count, so the probe (and the stored positions) must be {@code long}
 *       ({@link #overflowOfTheMidpointProbeIsNotCounted},
 *       {@link #realTripleAtOverflowScaleIsCountedExactlyOnce}).
 *   <li><b>{@code T} test cases, one count per line.</b> The driver loops {@code T} times and
 *       prints each answer on its own line, including a plain {@code 0}
 *       ({@link #multipleTestCasesEachPrintedOnItsOwnLine}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeMultiCaseInputStaysWithinTimeLimit} pins the per-case {@code O(N^2
 * log N)} (sort, then for each of the {@code O(N^2)} (left, middle) pairs a membership probe for
 * the implied third point) against the {@code O(N^3)} brute force over every trio -- where
 * {@code N} is the points in a test case and {@code T} the number of test cases. With {@code T}
 * cases of {@code N = 1,000}, the brute force does {@code T * C(1000,3) ~ T * 1.66e8} trio checks
 * and blows the limit, while the intended solution does {@code ~T * 1e6} pair probes and finishes
 * comfortably. {@link #randomizedBatchesMatchBruteForceTripleCount} cross-checks the judged output
 * against an independent {@code O(N^3)} brute-force count on small random batches -- the brute
 * force shares no logic with a hash/binary-search solution, so the two agree only when both are
 * right, and the random batches also exercise the {@code T}-loop and unsorted input. The overflow
 * regression cases additionally guard against a reversion to 32-bit {@code int}, which silently
 * over-counts on coordinates near {@code 1e9}.
 */
class MainTest {

  // --- The official sample: five points -4 -1 0 2 4 with three equally spaced trios -- (-4,-1,2),
  // (-4,0,4), (0,2,4). End-to-end smoke test, and already enough to pin that adjacency is not the
  // rule: (-4,-1,0) is three consecutive *given* points but is NOT equally spaced (gaps 3 and 1),
  // so
  // a solution that counts consecutive sorted triples would over-count. ---

  @Test
  @StdIo({"1", "5", "-4 -1 0 2 4"})
  void officialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The minimal yes-case: three points 1 2 3 form exactly one equally spaced trio (gaps 1 and
  // 1). Pins the core predicate Xb - Xa == Xc - Xb on the smallest possible input. ---

  @Test
  @StdIo({"1", "3", "1 2 3"})
  void singleArithmeticTripleIsCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The minimal no-case: 1 2 4 has gaps 1 and 2, so the lone trio is not equally spaced. Pins
  // that an unequal-gap trio contributes nothing -- the answer is 0, and 0 must still be printed.
  // ---

  @Test
  @StdIo({"1", "3", "1 2 4"})
  void nonArithmeticTripleIsNotCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- A second minimal no-case framed as the N=3 lower boundary: 2 5 6 (gaps 3 and 1). Together
  // with singleArithmeticTripleIsCounted this brackets the smallest legal N at both outcomes. ---

  @Test
  @StdIo({"1", "3", "2 5 6"})
  void minimumThreePointsNotEquallySpacedYieldZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Overlaps all count, at multiple common differences. Five consecutive points 1 2 3 4 5
  // yield:
  // difference 1 -> (1,2,3),(2,3,4),(3,4,5) = 3 trios that share endpoints/middles; difference 2 ->
  // (1,3,5) = 1. Total 4. A solution that stops at the first trio through a point, or that only
  // partitions into disjoint trios, under-counts. ---

  @Test
  @StdIo({"1", "5", "1 2 3 4 5"})
  void overlappingTriplesAreEachCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- The implied middle must be a *given* point. 1 2 3 5 has (1,2,3) [d=1] and (1,3,5) [d=2] =
  // 2,
  // but the would-be (3,4,5) is killed because 4 is absent from the set. Pins that qualifying on
  // 2*Xb - Xa requires that value to actually exist among the points, not merely be an integer. ---

  @Test
  @StdIo({"1", "4", "1 2 3 5"})
  void missingMiddlePointBreaksWouldBeTriple(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Position, not input order, names a/b/c. This is the official sample's point set shuffled to
  // 4 0 2 -4 -1; the answer must stay 3. A solution that trusts input order (treating the first of
  // three as the "left" point) instead of sorting by value misprints. ---

  @Test
  @StdIo({"1", "5", "4 0 2 -4 -1"})
  void inputIsSortedByPositionNotByInputOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Sorting at the N=3 boundary: 5 3 1 arrives fully descending and sorts to 1 3 5, the single
  // trio (1,3,5) with gaps 2 and 2. Answer 1. Pins that even the smallest input is ordered by value
  // before the gap test. ---

  @Test
  @StdIo({"1", "3", "5 3 1"})
  void descendingMinimumInputIsCountedAfterSorting(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The full coordinate range, negatives included. The extremes -100000000 and 100000000 with 0
  // as the midpoint form one progression (gaps 1e8 and 1e8). Pins handling of negative positions
  // and
  // the boundary magnitudes of the stated range, with the mean landing exactly on a given point.
  // ---

  @Test
  @StdIo({"1", "3", "-100000000 0 100000000"})
  void negativeAndExtremeCoordinatesFormTriple(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Two test cases in one input: 1 2 3 -> 1, then 1 2 4 -> 0. Pins that the driver honors T,
  // processes each case independently, and prints each answer on its own line (including the 0).
  // ---

  @Test
  @StdIo({"2", "3", "1 2 3", "3", "1 2 4"})
  void multipleTestCasesEachPrintedOnItsOwnLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1\n0");
  }

  // --- The 32-bit overflow trap, with NO real progression to find. Points -2^30, 5, 2^30
  // (-1073741824, 5, 1073741824): the only pair that probes a phantom third point is the extremes
  // (-2^30, 2^30), whose true target 2*(2^30) - (-2^30) = 3*2^30 = 3,221,225,472 overflows int and
  // wraps to exactly -2^30 -- a point in the set. A long solution computes 3,221,225,472 honestly,
  // finds no such point, and answers 0; an int solution miscounts the wrap as a trio and answers 1.
  // This is the case the local suite was missing when the judge rejected the int submission. ---

  @Test
  @StdIo({"1", "3", "-1073741824 5 1073741824"})
  void overflowOfTheMidpointProbeIsNotCounted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- A genuine progression living at overflow scale, alongside the same phantom pair. Points
  // -2^30, 0, 5, 2^30: (-2^30, 0, 2^30) is a real equally spaced trio (gap 2^30 on each side), so
  // the
  // answer is exactly 1. The extremes pair (-2^30, 2^30) still wraps onto -2^30 under int, so an
  // int
  // solution answers 2 -- the real trio plus one phantom. This pins that 64-bit arithmetic both
  // keeps
  // the legitimate large-coordinate trio (overflow never under-counts -- the true target 2^30 is in
  // range and computed exactly) and rejects the phantom (overflow's only failure is over-counting).
  // ---

  @Test
  @StdIo({"1", "4", "-1073741824 0 5 1073741824"})
  void realTripleAtOverflowScaleIsCountedExactlyOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Small random batches checked against an independent O(N^3) brute force. Coordinates are
  // drawn
  // from a narrow window so progressions are common (a wide range would make most answers a trivial
  // 0), and each batch bundles 1..4 test cases so the T-loop and per-line output are exercised
  // alongside the count. The brute force enumerates every trio and shares no logic with a
  // hash/binary-search judge solution, so the two agree only when both are correct. ---

  @Test
  void randomizedBatchesMatchBruteForceTripleCount() throws IOException {
    Random rnd = new Random(13423);
    for (int trial = 0; trial < 500; trial++) {
      int t = 1 + rnd.nextInt(4); // 1..4 test cases per input
      int[][] cases = new int[t][];
      StringBuilder expected = new StringBuilder();
      for (int c = 0; c < t; c++) {
        int n = 3 + rnd.nextInt(10); // N in [3, 12]
        cases[c] = distinctCoordinates(n, rnd);
        if (c > 0) {
          expected.append('\n');
        }
        expected.append(bruteForceTripleCount(cases[c]));
      }
      assertThat(runMain(multiCaseInput(cases)))
          .as("cases=%s", Arrays.deepToString(cases))
          .isEqualTo(expected.toString());
    }
  }

  // --- Upper bound on N across several test cases. Each of T = 25 cases is 1,000 consecutive
  // points
  // 1..1000, whose equally spaced count is the closed form sum over differences d of (1000 - 2d).
  // The intended O(N^2 log N) per case runs ~25 * 1e6 pair probes and finishes well within the
  // limit, whereas the O(N^3) brute force does ~25 * C(1000,3) ~ 4e9 trio checks and cannot. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeMultiCaseInputStaysWithinTimeLimit() throws IOException {
    int t = 25;
    int n = 1_000;
    int[] consecutive = new int[n];
    for (int i = 0; i < n; i++) {
      consecutive[i] = i + 1;
    }
    int[][] cases = new int[t][];
    Arrays.fill(cases, consecutive);

    // 3-term APs in {1..n}: for each difference d, the left point ranges over [1, n - 2d].
    long perCase = 0;
    for (int d = 1; n - 2 * d >= 1; d++) {
      perCase += n - 2 * d;
    }
    StringBuilder expected = new StringBuilder();
    for (int c = 0; c < t; c++) {
      if (c > 0) {
        expected.append('\n');
      }
      expected.append(perCase);
    }
    assertThat(runMain(multiCaseInput(cases))).isEqualTo(expected.toString());
  }

  /**
   * Independent oracle: count equally spaced trios by brute force over every {@code C(N, 3)} trio.
   * Sorts a copy, then for each {@code i < j < k} counts the trio when {@code Xj - Xi == Xk - Xj}.
   * Uses no hashing or binary search, so it shares no logic with a judge solution.
   *
   * @implNote {@code O(N^3)} time, where {@code N} is {@code xs.length} -- acceptable only because
   *     the randomized fixtures keep {@code N} small.
   */
  private static long bruteForceTripleCount(int[] xs) {
    int[] s = xs.clone();
    Arrays.sort(s);
    long count = 0;
    for (int i = 0; i < s.length; i++) {
      for (int j = i + 1; j < s.length; j++) {
        for (int k = j + 1; k < s.length; k++) {
          if ((long) s[j] - s[i] == (long) s[k] - s[j]) {
            count++;
          }
        }
      }
    }
    return count;
  }

  /**
   * Picks {@code n} distinct positions from the narrow window {@code [-10, 10]} in random order, so
   * progressions are common and the input is unsorted. The window's 21 candidates comfortably
   * exceed the largest {@code n} the randomized test requests.
   */
  private static int[] distinctCoordinates(int n, Random rnd) {
    List<Integer> pool = new ArrayList<>();
    for (int v = -10; v <= 10; v++) {
      pool.add(v);
    }
    Collections.shuffle(pool, rnd);
    int[] xs = new int[n];
    for (int i = 0; i < n; i++) {
      xs[i] = pool.get(i);
    }
    return xs;
  }

  /**
   * Renders test cases as BOJ 13423 input: {@code T} on line 1, then for each case {@code N} on its
   * own line followed by the {@code N} space-separated positions.
   */
  private static String multiCaseInput(int[][] cases) {
    StringBuilder sb = new StringBuilder();
    sb.append(cases.length).append('\n');
    for (int[] xs : cases) {
      sb.append(xs.length).append('\n');
      for (int i = 0; i < xs.length; i++) {
        if (i > 0) {
          sb.append(' ');
        }
        sb.append(xs[i]);
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
