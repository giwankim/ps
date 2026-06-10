package boj.boj2467;

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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/** BOJ 2467 용액 (Solution). */
class MainTest {

  // --- Minimum input N = 2: exactly one pair exists, so it must be printed no matter how far its
  // sum sits from zero. Guards reading N and emitting the only candidate rather than, say,
  // requiring
  // the sum to straddle zero. ---

  @Test
  @StdIo({"2", "5 9"})
  void twoSolutionsPrintTheOnlyPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5 9");
  }

  @Test
  @StdIo({"2", "-100 50"})
  void twoSolutionsAcrossZeroPrintTheOnlyPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-100 50");
  }

  // --- Minimum input where the only pair is also perfectly neutral: the initial best is already 0
  // before any pointer moves, and the sum == 0 short-circuit fires on the very first iteration. ---

  @Test
  @StdIo({"2", "-7 7"})
  void twoSolutionsSummingToZeroPrintTheOnlyPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-7 7");
  }

  // --- A pair whose sum is exactly 0, sitting in the interior (not at the extremes). The optimum
  // is
  // unique. Exercises the "perfectly neutral" branch many solutions short-circuit on, and proves
  // the
  // zero pair need not be the outermost two values. ---

  @Test
  @StdIo({"4", "-99 -2 2 7"})
  void interiorPairSummingToZeroIsOptimal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-2 2");
  }

  // --- All acidic (every value positive). Every sum is positive, so the closest to zero is the two
  // smallest values, i.e. the two leftmost. Catches a solution that assumes an acid/base pair must
  // exist. ---

  @Test
  @StdIo({"4", "2 5 7 9"})
  void allPositiveValuesPickTwoSmallest(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2 5");
  }

  // --- All alkaline (every value negative). Mirror of the all-positive case: every sum is
  // negative,
  // so the closest to zero is the two largest (rightmost) values. ---

  @Test
  @StdIo({"4", "-9 -7 -5 -2"})
  void allNegativeValuesPickTwoLargest(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-5 -2");
  }

  // --- Both signs present, yet the optimum is two negatives: (-4, -3) -> -7 beats the best
  // cross-zero pair (-4, 100) -> 96. Catches approaches that only ever consider pairs straddling
  // zero, such as a per-negative binary search for the closest positive. ---

  @Test
  @StdIo({"4", "-4 -3 100 200"})
  void negativePairBeatsCrossZeroPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-4 -3");
  }

  // --- Mirror with the optimum on the positive side: (3, 4) -> 7 beats the best cross-zero pair
  // (-100, 4) -> -96. ---

  @Test
  @StdIo({"4", "-200 -100 3 4"})
  void positivePairBeatsCrossZeroPairs(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3 4");
  }

  // --- Core two-pointer behavior: the optimum (-3, 2) -> -1 sits in the interior and is only found
  // after both pointers move inward several times. A naive "check the extremes" approach would
  // report
  // (-97, 100) -> 3 instead. ---

  @Test
  @StdIo({"5", "-97 -3 2 8 100"})
  void interiorOptimumFoundByMovingBothPointers(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-3 2");
  }

  // --- Both pointers walk inward to the innermost pair: each moves twice, and the running best
  // improves mid-walk (200 from the extremes, then -10 from (-40, 30), then 0 from (-1, 1)) while
  // a worse intermediate pair (-500, 30) -> -470 must be rejected along the way. Sits between the
  // one-step interior cases above and the full-array sweeps at maximum N. ---

  @Test
  @StdIo({"6", "-500 -40 -1 1 30 700"})
  void innermostPairFoundAfterBothPointersWalkInward(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1 1");
  }

  // --- The optimum IS the outermost pair: (-10, 11) -> 1 beats every interior pair. The very first
  // comparison is the answer, and every later pointer move must leave the running best untouched. A
  // solution that overwrites the best with the last-seen pair would fail. ---

  @Test
  @StdIo({"3", "-10 3 11"})
  void extremePairIsOptimalAndRetained(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-10 11");
  }

  // --- Two pairs tie for closest-to-zero: (-10, 7) and (-1, 4) both give |sum| = 3. The statement
  // permits either, so we assert validity (an optimal, in-bounds, ascending pair) rather than a
  // fixed
  // string. An over-strict equality test here would reject a correct solution. ---

  @Test
  @StdIo({"4", "-10 -1 4 7"})
  void tieIsResolvedToAnyOptimalPair(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertOptimalPair(new int[] {-10, -1, 4, 7}, out.capturedString());
  }

  // --- Largest magnitudes, N = 2: the sum 1,999,999,999 is near Integer.MAX_VALUE. Confirms the
  // sum
  // is computed without overflow and printed correctly. ---

  @Test
  @StdIo({"2", "999999999 1000000000"})
  void largePositiveValuesDoNotOverflow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("999999999 1000000000");
  }

  // --- Mirror of the above at the negative bound: the sum -1,999,999,999 is near
  // Integer.MIN_VALUE.
  // Guards an abs() that would mishandle large negative sums. ---

  @Test
  @StdIo({"2", "-1000000000 -999999999"})
  void largeNegativeValuesDoNotOverflow(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1000000000 -999999999");
  }

  // --- The extreme values cancel exactly: (-1,000,000,000, 1,000,000,000) -> 0 is the unique
  // optimum. Combines the maximum spread with an exact-zero result at the array ends. ---

  @Test
  @StdIo({"3", "-1000000000 1 1000000000"})
  void extremeValuesCancelToZero(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1000000000 1000000000");
  }

  // --- Performance, maximum N, all acidic: 1..100,000 must return "1 2". An O(n^2) scan (~10^10
  // pairs) blows the time limit; the intended O(n) two-pointer (right pointer walking the whole
  // array) is instant. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAllPositiveRunsWithoutBruteForce() throws IOException {
    assertThat(runMain(inputFor(ascendingRun(1, 100_000)))).isEqualTo("1 2");
  }

  // --- Performance, maximum N, all alkaline: -100,000..-1 must return "-2 -1". Mirror of the
  // all-positive case, driving the left pointer across the whole array instead. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeAllNegativeRunsWithoutBruteForce() throws IOException {
    assertThat(runMain(inputFor(ascendingRun(-100_000, 100_000)))).isEqualTo("-2 -1");
  }

  // --- Performance, maximum N, single neutralizing pair: [-3, 1, 2, ..., 99999]. The only sum
  // that reaches zero is (-3, 3): other pairs involving -3 miss zero by at least 1, and every pair
  // without -3 sums to at least 3. The right pointer must walk nearly the entire array before it
  // lands on the match, so this both stresses scale and pins a unique interior optimum. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeMixedWithUniqueZeroPair() throws IOException {
    int[] values = new int[100_000];
    values[0] = -3;
    for (int i = 1; i < values.length; i++) {
      values[i] = i; // 1, 2, ..., 99999
    }
    assertThat(runMain(inputFor(values))).isEqualTo("-3 3");
  }

  // --- Randomized cross-check against an independent O(n^2) oracle. Small nonzero ranges and short
  // arrays make ties, exact-zero hits, all-acidic and all-alkaline shapes all frequent, with N
  // drawn
  // across 2..40. Because ties are legal, the output is checked for optimality (correct minimum
  // |sum|, both values from the input, ascending order) rather than string equality. ---

  @Test
  void randomizedSmallInputsAreOptimal() throws IOException {
    Random rnd = new Random(2467);
    for (int trial = 0; trial < 500; trial++) {
      int n = 2 + rnd.nextInt(39); // 2..40
      int[] values = randomSortedDistinctNonZero(n, 25, rnd);
      String input = inputFor(values);
      assertOptimalPair(values, runMainRaw(input), "input=%n%s".formatted(input));
    }
  }

  // --- Randomized cross-check at full magnitude: values span the whole +/-10^9 domain and N runs
  // to 200. The small-range oracle above never exercises large-magnitude arithmetic in random
  // shapes, and the fixed overflow tests only do so at N = 2. Near-negations are injected so sums
  // still land close to zero; ties remain legal, so the validity check is reused. ---

  @Test
  void randomizedLargeMagnitudeInputsAreOptimal() throws IOException {
    Random rnd = new Random(24672);
    for (int trial = 0; trial < 200; trial++) {
      int n = 2 + rnd.nextInt(199); // 2..200
      int[] values = randomSortedDistinctNonZeroLargeMagnitude(n, rnd);
      String input = inputFor(values);
      assertOptimalPair(values, runMainRaw(input), "input=%n%s".formatted(input));
    }
  }

  // Asserts the printed line is *an* optimal answer: two distinct input values in ascending order
  // whose summed magnitude equals the true minimum over all pairs. Tolerates the statement's "print
  // any one" tie rule that a fixed-string assertion cannot.
  private static void assertOptimalPair(int[] values, String output) {
    assertOptimalPair(values, output, "output=<%s>".formatted(output));
  }

  private static void assertOptimalPair(int[] values, String output, String description) {
    String[] parts = output.trim().split("\\s+");
    assertThat(parts).as(description).hasSize(2);
    long a = Long.parseLong(parts[0]);
    long b = Long.parseLong(parts[1]);
    assertThat(a).as("%s%nvalues must be in ascending order", description).isLessThan(b);
    assertThat(contains(values, a))
        .as("%s%nfirst value %d must be an input value", description, a)
        .isTrue();
    assertThat(contains(values, b))
        .as("%s%nsecond value %d must be an input value", description, b)
        .isTrue();
    long best = bruteForceMinAbsSum(values);
    assertThat(Math.abs(a + b))
        .as(
            "%s%npair (%d, %d) must be closest to zero (optimal |sum| = %d)",
            description, a, b, best)
        .isEqualTo(best);
  }

  // Reference O(n^2) minimum |sum| over all distinct pairs. Obviously correct but too slow for the
  // judge; trustworthy only for the tiny inputs used by the oracle and the tie case.
  private static long bruteForceMinAbsSum(int[] values) {
    long best = Long.MAX_VALUE;
    for (int i = 0; i < values.length; i++) {
      for (int j = i + 1; j < values.length; j++) {
        best = Math.min(best, Math.abs((long) values[i] + values[j]));
      }
    }
    return best;
  }

  private static boolean contains(int[] values, long target) {
    for (int v : values) {
      if (v == target) {
        return true;
      }
    }
    return false;
  }

  private static int[] randomSortedDistinctNonZero(int n, int range, Random rnd) {
    List<Integer> pool = new ArrayList<>();
    for (int v = -range; v <= range; v++) {
      if (v != 0) { // characteristic values are strictly acidic (+) or alkaline (-), never zero
        pool.add(v);
      }
    }
    Collections.shuffle(pool, rnd);
    int[] values = new int[n];
    for (int i = 0; i < n; i++) {
      values[i] = pool.get(i);
    }
    Arrays.sort(values);
    return values;
  }

  // Draws n distinct nonzero values across the full +/-1,000,000,000 domain. Uniform draws at this
  // scale almost never sum near zero, so on average every other value is a near-negation (jitter
  // within +/-2) of the previous one, keeping near-cancelling cross-zero pairs frequent.
  private static int[] randomSortedDistinctNonZeroLargeMagnitude(int n, Random rnd) {
    Set<Integer> seen = new HashSet<>();
    int[] values = new int[n];
    int count = 0;
    while (count < n) {
      int v;
      if (count > 0 && rnd.nextBoolean()) {
        long mirrored = -(long) values[count - 1] + rnd.nextInt(5) - 2;
        v = (int) Math.max(-1_000_000_000L, Math.min(1_000_000_000L, mirrored));
      } else {
        v = rnd.nextInt(2_000_000_001) - 1_000_000_000;
      }
      if (v != 0 && seen.add(v)) {
        values[count++] = v;
      }
    }
    Arrays.sort(values);
    return values;
  }

  private static int[] ascendingRun(int start, int count) {
    int[] values = new int[count];
    for (int i = 0; i < count; i++) {
      values[i] = start + i;
    }
    return values;
  }

  private static String inputFor(int[] values) {
    StringBuilder sb = new StringBuilder();
    sb.append(values.length).append('\n');
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(values[i]);
    }
    return sb.append('\n').toString();
  }

  private static String runMain(String input) throws IOException {
    return runMainRaw(input).trim();
  }

  private static String runMainRaw(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return out.toString(StandardCharsets.UTF_8);
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
