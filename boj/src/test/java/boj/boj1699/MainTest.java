package boj.boj1699;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1699 제곱수의 합 ("Sum of Squares") -- the minimum number of perfect squares that sum to N.
 *
 * <p>A single natural number {@code N} is given. Write it as a sum of squares {@code 1^2, 2^2, 3^2,
 * ...} (repeats allowed) and print the <b>fewest</b> terms any such sum can use. For example
 * {@code 11 = 3^2 + 1^2 + 1^2} uses 3 terms, and no shorter sum reaches 11, so the answer is 3.
 *
 * <p>Constraints: {@code 1 <= N <= 100,000}. By Lagrange's four-square theorem every natural number
 * is a sum of at most four squares, so the answer is always one of {@code 1, 2, 3, 4} -- a tiny
 * range that hides how easy the recurrence is to get subtly wrong.
 *
 * <p>The intended solution is the bottom-up DP {@code dp[i] = min over j>=1 of dp[i - j*j] + 1}
 * with {@code dp[0] = 0}; the answer is {@code dp[N]}. Four families of mistakes drive these
 * fixtures:
 *
 * <ul>
 *   <li><b>Greedy "subtract the largest square".</b> The signature wrong answer. Repeatedly peeling
 *       off the biggest square {@code <= i} is locally appealing but globally suboptimal:
 *       {@code 12} greedily becomes {@code 9 + 1 + 1 + 1} (4 terms) while the optimum is {@code 4 +
 *       4 + 4} (3 terms) ({@link #twelveDefeatsTheLargestSquareGreedy(StdOut)}). The gap can be
 *       large -- {@code 32} is {@code 16 + 16} (2) but greedy spends 5
 *       ({@link #thirtyTwoIsTwoSquaresButGreedySpendsFive(StdOut)}).
 *   <li><b>The {@code j*j <= i} boundary.</b> A perfect square must cost exactly 1 term, which only
 *       happens if the inner loop lets {@code j} reach the point where {@code j*j == i} (so
 *       {@code dp[i] = dp[0] + 1}). An off-by-one writing {@code j*j < i} silently misses it and
 *       reports a bloated count for every square ({@link #fourIsASingleSquare(StdOut)},
 *       {@link #oneHundredIsASingleSquare(StdOut)}).
 *   <li><b>The {@code dp[0] = 0} base case.</b> If the empty sum is not zero-cost, every perfect
 *       square inherits the error. {@code N = 1} pins it: {@code dp[1] = dp[0] + 1} must equal 1
 *       ({@link #oneIsASingleSquare(StdOut)}).
 *   <li><b>Collapsing distinct answers.</b> The four output classes (1, 2, 3, 4) must all be
 *       reachable. Representatives anchor each: squares give 1, sums of two squares give 2, the
 *       residual case gives 3, and {@code 4^a(8b+7)} numbers force 4
 *       ({@link #sevenNeedsAllFourSquares(StdOut)}, {@link #fifteenNeedsAllFourSquares(StdOut)}).
 * </ul>
 *
 * <p>The hand-picked cases are cross-checked by an independent number-theoretic oracle
 * ({@link #closedFormOracle(int)}) built from Lagrange's four-square theorem and Legendre's
 * three-square theorem -- a completely different algorithm from the DP, so the two agreeing is real
 * evidence. The oracle drives an exhaustive sweep of every {@code N} in {@code 1..3000}
 * ({@link #everyValueUpToThreeThousandMatchesTheClosedFormOracle()}) and a full-scale random fuzz
 * ({@link #largeRandomInputsMatchTheClosedFormOracle()}); the {@code @Timeout} max-size cases force
 * a polynomial solution at {@code N = 100,000}.
 */
class MainTest {

  // --- The official sample from the statement. ---

  // 7 = 2^2 + 1^2 + 1^2 + 1^2 (4 + 1 + 1 + 1) needs four terms. 7 is not a perfect square, is not a
  // sum of two squares (no a^2 + b^2 = 7), and 7 = 4^0 * (8*0 + 7), so by Legendre it genuinely
  // requires the full four squares. This is the worst case for the smallest input that exhibits it.
  @Test
  @StdIo({"7"})
  void officialSampleSevenNeedsFourSquares(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // The canonical example from the statement: 11 = 3^2 + 1^2 + 1^2 (9 + 1 + 1), three terms, and no
  // shorter sum reaches 11. Note the greedy here happens to agree (9 + 1 + 1 is also what greedy
  // picks), so this guards the "three" output class rather than the greedy trap.
  @Test
  @StdIo({"11"})
  void elevenIsThreeSquares(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The smallest inputs: the dp[0] base case and the first non-square. ---

  // N = 1 = 1^2 is a single square. This pins dp[0] = 0, because dp[1] is computed as dp[1 - 1] + 1
  // = dp[0] + 1 and must equal 1. The minimal input.
  @Test
  @StdIo({"1"})
  void oneIsASingleSquare(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // N = 2 = 1^2 + 1^2 is the smallest number that is not itself a square, so the answer steps up to
  // 2. Confirms the recurrence does not accidentally report 1 for non-squares.
  @Test
  @StdIo({"2"})
  void twoIsTwoSquares(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family 1: the largest-square greedy is wrong. ---

  // The cleanest greedy trap. Greedy subtracts the largest square <= 12, namely 9, leaving 3 =
  // 1 + 1 + 1, for 9 + 1 + 1 + 1 = four terms. The optimum is 4 + 4 + 4 = three terms. A solution
  // that peels off the biggest square prints 4 and fails here.
  @Test
  @StdIo({"12"})
  void twelveDefeatsTheLargestSquareGreedy(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // 18 = 9 + 9, two terms. Greedy grabs 16 first, leaving 2 = 1 + 1, for 16 + 1 + 1 = three terms.
  // The DP must look past the largest square to find the pair of 3^2.
  @Test
  @StdIo({"18"})
  void eighteenIsTwoSquaresButGreedyTakesThree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // The widest gap in this suite. 32 = 16 + 16, two terms. Greedy takes 25, then 4, then 1 + 1 + 1
  // -- 25 + 4 + 1 + 1 + 1 = five terms. Two versus five cleanly separates the DP from any
  // largest-first heuristic.
  @Test
  @StdIo({"32"})
  void thirtyTwoIsTwoSquaresButGreedySpendsFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family 2: perfect squares must cost exactly one term (the j*j <= i boundary). ---

  // 4 = 2^2. The inner loop must allow j = 2 where j*j == i so that dp[4] = dp[0] + 1 = 1. An
  // off-by-one bound of j*j < i misses this and reports dp[3] + 1 = 4 instead.
  @Test
  @StdIo({"4"})
  void fourIsASingleSquare(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // 100 = 10^2, a perfect square well away from the tiny inputs, so the j = 10 term must be reached
  // for the answer to be 1. Guards the boundary at a larger scale than 4 does.
  @Test
  @StdIo({"100"})
  void oneHundredIsASingleSquare(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Family 3: sums of exactly two squares (the "two" output class). ---

  // 5 = 2^2 + 1^2. Not a square, but a sum of two, so the answer is 2.
  @Test
  @StdIo({"5"})
  void fiveIsTwoSquares(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // 13 = 3^2 + 2^2 (9 + 4). Here the two squares are distinct and neither is 1, exercising a
  // genuinely two-term optimum that is not "a square plus ones".
  @Test
  @StdIo({"13"})
  void thirteenIsTwoSquares(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Family 4: numbers that genuinely need all four squares (Lagrange's upper bound). ---

  // 15 = 3^2 + 2^2 + 1^2 + 1^2. Like 7 it is of the form 4^a(8b+7) (here 8*1 + 7), so by Legendre's
  // three-square theorem it cannot be done in three, and four is forced.
  @Test
  @StdIo({"15"})
  void fifteenNeedsAllFourSquares(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // 23 = 3^2 + 3^2 + 2^2 + 1^2 (9 + 9 + 4 + 1). 23 = 8*2 + 7 is again a four-square number, and the
  // greedy 16 + 4 + 1 + 1 + 1 would spend five, so this case is both a Lagrange-bound and a greedy
  // check.
  @Test
  @StdIo({"23"})
  void twentyThreeNeedsAllFourSquares(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // The "four" class restated under the name the class javadoc references, alongside 7 and 15.
  @Test
  @StdIo({"7"})
  void sevenNeedsAllFourSquares(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Max-size inputs (N up to 100,000): a polynomial DP clears the limit; @Timeout guards
  // against an accidentally exponential search. ---

  // 99,856 = 316^2 is the largest perfect square <= 100,000, so its answer is 1. This forces the
  // inner loop to reach j = 316 (j*j == i) at the very top of the range -- the boundary check from
  // family 2, but at full scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largestPerfectSquareUnderTheCapIsOneSquare() throws IOException {
    assertThat(runMain("99856")).isEqualTo("1");
  }

  // 99,999 = 8 * 12,499 + 7 is of the form 4^0(8b+7), so by Legendre it needs the full four
  // squares. The heaviest possible answer (4) occurring near the top of the input range.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void aFourSquareNumberNearTheCapNeedsFour() throws IOException {
    assertThat(runMain("99999")).isEqualTo("4");
  }

  // 100,000 itself, the exact upper bound, exercises the full dp array end to end. Cross-checked
  // against the independent oracle rather than a hand-computed constant.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void theCapValueMatchesTheClosedFormOracle() throws IOException {
    assertThat(runMain("100000")).isEqualTo(closedFormOracle(100_000));
  }

  // --- Exhaustive and randomized cross-checks against the independent number-theoretic oracle. ---

  // Every N from 1 to 3,000 must agree with the closed-form oracle. A single sweep that, in one
  // shot, pins all four output classes, the greedy trap, the perfect-square boundary, and the base
  // case across a dense contiguous range.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void everyValueUpToThreeThousandMatchesTheClosedFormOracle() throws IOException {
    for (int n = 1; n <= 3_000; n++) {
      assertThat(runMain(Integer.toString(n))).as("n=%d", n).isEqualTo(closedFormOracle(n));
    }
  }

  // A pseudo-random sample drawn from across the full [1, 100,000] range, cross-checked against the
  // oracle. Fixed seed -> deterministic across JVMs. Exercises parsing, I/O, and the recurrence at
  // the judge's scale where the dense small-N sweep cannot reach.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void largeRandomInputsMatchTheClosedFormOracle() throws IOException {
    Random rng = new Random(1699L);
    for (int trial = 0; trial < 200; trial++) {
      int n = 1 + rng.nextInt(100_000); // 1..100000
      assertThat(runMain(Integer.toString(n))).as("n=%d", n).isEqualTo(closedFormOracle(n));
    }
  }

  /**
   * Independent closed-form oracle for the minimum number of squares, built from classical number
   * theory rather than the DP, so agreement is a real cross-check. By Lagrange every {@code n >= 1}
   * needs at most four squares, and the exact count follows three tests applied in order:
   *
   * <ol>
   *   <li>1 if {@code n} is a perfect square;
   *   <li>2 if {@code n = a^2 + b^2} for some {@code a, b >= 1};
   *   <li>4 if {@code n = 4^a(8b + 7)} (Legendre's three-square theorem says exactly these numbers
   *       cannot be written as three squares);
   *   <li>3 otherwise.
   * </ol>
   *
   * @return the count as a string, matching the program's printed form.
   * @implNote {@code O(sqrt(N))} time -- where {@code N} is the input value {@code n} -- dominated
   *     by the two-squares scan over candidate {@code a}.
   */
  private static String closedFormOracle(int n) {
    if (isPerfectSquare(n)) {
      return "1";
    }
    for (int a = 1; (long) a * a <= n; a++) {
      if (isPerfectSquare(n - a * a)) {
        return "2";
      }
    }
    int m = n;
    while (m % 4 == 0) {
      m /= 4;
    }
    if (m % 8 == 7) {
      return "4";
    }
    return "3";
  }

  /** Whether {@code x} is a non-negative perfect square, using an integer-sqrt guard. */
  private static boolean isPerfectSquare(int x) {
    if (x < 0) {
      return false;
    }
    int r = (int) Math.sqrt(x);
    while ((long) (r + 1) * (r + 1) <= x) {
      r++;
    }
    while ((long) r * r > x) {
      r--;
    }
    return r * r == x;
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
