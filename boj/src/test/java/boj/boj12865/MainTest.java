package boj.boj12865;

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
 * BOJ 12865 평범한 배낭 ("Ordinary Knapsack") -- the textbook 0/1 knapsack.
 *
 * <p>There are {@code N} items; item {@code i} has weight {@code W[i]} and value {@code V[i]}. The
 * knapsack holds at most weight {@code K}. Choose a subset whose total weight is {@code <= K} and
 * whose total value is as large as possible, and print that maximum value. Each item is available
 * exactly once -- this is the <b>0/1</b> knapsack, not the unbounded one where an item may be
 * reused.
 *
 * <p>Constraints: {@code 1 <= N <= 100}, {@code 1 <= K <= 100,000}, {@code 1 <= W[i] <= 100,000},
 * {@code 0 <= V[i] <= 1,000}; every input number is an integer. The richest possible answer takes
 * all {@code N} items at the value cap, {@code N * max(V) = 100 * 1,000 = 100,000}, which sits far
 * inside a signed 32-bit {@code int} -- unlike weight-sized billions, this answer never needs a
 * {@code long}.
 *
 * <p>Three families of mistakes drive these fixtures:
 *
 * <ul>
 *   <li><b>Reusing an item (unbounded instead of 0/1).</b> The signature bug is a 1D DP whose inner
 *       capacity loop runs <em>forward</em>, letting one item be packed several times. A lone
 *       light, valuable item exposes it: 0/1 takes it once, the unbounded bug stacks copies
 *       ({@link #anItemCannotBeTakenTwice(StdOut)}).
 *   <li><b>Mishandling the capacity boundary.</b> An item heavier than {@code K} can never be
 *       packed ({@link #singleItemHeavierThanCapacityIsLeftBehind(StdOut)}); an item weighing
 *       exactly {@code K} still fits ({@link #itemWeighingExactlyTheCapacityStillFits(StdOut)}). An
 *       off-by-one on {@code <= K} breaks one of these.
 *   <li><b>Greedy selection.</b> Grabbing the single highest-value item
 *       ({@link #greedilyTakingTheHighestValueItemIsSuboptimal(StdOut)}) or the highest
 *       value-per-weight item ({@link #aDenseLightItemBlockingAMoreValuableHeavyOneLoses(StdOut)})
 *       both lose; only an exhaustive optimum (DP) is correct.
 * </ul>
 *
 * <p>The hand-picked cases are cross-checked by two independent oracles: an exhaustive every-subset
 * enumeration for small inputs ({@link #randomizedSmallInputsMatchBruteForceOracle()}) and a
 * separately written reverse-iterating 1D DP for the max-scale fuzz
 * ({@link #largeRandomInputMatchesTheDpOracle()}). The {@code @Timeout} max-size cases force a
 * polynomial solution: an {@code O(2^N)} subset search is hopeless at {@code N = 100}.
 */
class MainTest {

  // --- The official sample from the statement. ---

  // K = 7 with items (W,V) = (6,13) (4,8) (3,6) (5,12). The best subset is items 2 and 3: weight
  // 4 + 3 = 7 (exactly the capacity), value 8 + 6 = 14. The single heaviest-value item (6,13) is
  // worth only 13, and (5,12) only 12, so 14 wins.
  @Test
  @StdIo({"4 7", "6 13", "4 8", "3 6", "5 12"})
  void officialSampleMaximizesToFourteen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("14");
  }

  // --- The smallest inputs: a single item, taken or rejected. ---

  // N = 1. The only item weighs 5 and the knapsack holds 10, so it fits and is taken for its full
  // value. The minimal input that yields a non-zero answer.
  @Test
  @StdIo({"1 10", "5 100"})
  void singleItemThatFitsIsTaken(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  // N = 1 but the item weighs 6 while the knapsack holds only 5, so it can never be packed and the
  // answer is 0. Pins the "too heavy to carry" rejection at the minimum size.
  @Test
  @StdIo({"1 5", "6 100"})
  void singleItemHeavierThanCapacityIsLeftBehind(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The decisive boundary case: the item weighs exactly the capacity (5 == 5), so it just fits and
  // is taken. A solution using the wrong bound (W < K instead of W <= K) wrongly rejects it and
  // prints 0.
  @Test
  @StdIo({"1 5", "5 100"})
  void itemWeighingExactlyTheCapacityStillFits(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100");
  }

  // --- Family 1: each item may be used at most once (0/1, not unbounded). ---

  // N = 1, capacity 10, one item of weight 2 and value 5. In 0/1 the item is packed once for 5.
  // The classic unbounded-knapsack bug -- a forward-iterating 1D capacity loop -- would pack five
  // copies (5 * 2 = 10 weight) for 25. This is the single most important test: it separates 0/1
  // from unbounded.
  @Test
  @StdIo({"1 10", "2 5"})
  void anItemCannotBeTakenTwice(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Family 2: greedy selection is wrong. ---

  // Capacity 10. The lone highest-value item (10,10) fills the bag for 10. Skipping it to take the
  // two (5,6) items (weight 5 + 5 = 10) yields 12 > 10. A greedy "grab the biggest value" picks 10
  // and loses.
  @Test
  @StdIo({"3 10", "10 10", "5 6", "5 6"})
  void greedilyTakingTheHighestValueItemIsSuboptimal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // Capacity 4. Item (1,3) has the best value-per-weight (3.0); item (4,10) is denser-looking only
  // by total value (2.5 per unit). A density-greedy grabs (1,3) first, then cannot fit the weight-4
  // item (1 + 4 = 5 > 4), settling for 3. The optimum simply takes (4,10) alone for 10. Catches the
  // fractional-knapsack greedy that does not survive the 0/1 integrality constraint.
  @Test
  @StdIo({"2 4", "1 3", "4 10"})
  void aDenseLightItemBlockingAMoreValuableHeavyOneLoses(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Edge cases. ---

  // When the capacity dwarfs the total weight, the constraint never bites and every item is taken:
  // values 1 + 2 + 3 = 6. Confirms the DP accumulates across all items instead of stopping early.
  @Test
  @StdIo({"3 100", "1 1", "2 2", "3 3"})
  void whenEverythingFitsEveryItemIsTaken(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // Value may be 0. Capacity 3 admits only one of the two weight-3 items; the value-0 decoy is
  // worthless, so the value-5 item is chosen for 5. Pins that V = 0 parses and that a zero-value
  // item is never preferred over a positive one.
  @Test
  @StdIo({"2 3", "3 0", "3 5"})
  void aZeroValueItemIsNeverPreferred(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Max-size inputs (N up to 100, K up to 100,000): a polynomial DP clears the limit; the
  // @Timeout guards against an exponential subset search. ---

  // 100 items each weighing 1,000 and worth the value cap 1,000, with capacity 100,000. Their total
  // weight 100 * 1,000 = 100,000 equals K exactly, so the whole set fits and is taken: value
  // 100 * 1,000 = 100,000 -- the heaviest legal answer, exercising the full capacity array and the
  // accumulator width.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxItemsFillingCapacityExactlySumToTheLargestValue() throws IOException {
    int n = 100;
    int k = 100_000;
    int[] w = new int[n];
    int[] v = new int[n];
    java.util.Arrays.fill(w, 1_000);
    java.util.Arrays.fill(v, 1_000);
    assertThat(runMain(buildInput(k, w, v))).isEqualTo("100000");
  }

  // A full-size pseudo-random instance cross-checked against an independently written 1D DP. Random
  // W in [1, 100000] and V in [1, 1000] mix items that fit and items too heavy to carry at the
  // judge's scale, exercising parsing, I/O, and the recurrence together under the time limit.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void largeRandomInputMatchesTheDpOracle() throws IOException {
    int n = 100;
    int k = 100_000;
    int[] w = new int[n];
    int[] v = new int[n];
    Random rng = new Random(12865L); // fixed seed -> deterministic across JVMs.
    for (int i = 0; i < n; i++) {
      w[i] = 1 + rng.nextInt(100_000); // 1..100000
      v[i] = 1 + rng.nextInt(1000); // 1..1000
    }
    assertThat(runMain(buildInput(k, w, v))).isEqualTo(Integer.toString(dpOracle(k, w, v)));
  }

  // --- Randomized cross-check against an exhaustive oracle on small inputs: the truly independent
  // check that catches reuse, boundary, and greedy mistakes the hand-picked cases might miss. ---

  @Test
  void randomizedSmallInputsMatchBruteForceOracle() throws IOException {
    Random rng = new Random(128650L);
    for (int trial = 0; trial < 500; trial++) {
      int n = 1 + rng.nextInt(14); // 1..14, small enough for the O(2^N) oracle
      int k = 1 + rng.nextInt(25); // tight capacity so the weight constraint actually bites
      int[] w = new int[n];
      int[] v = new int[n];
      for (int i = 0; i < n; i++) {
        w[i] = 1 + rng.nextInt(10); // small weights -> mix of fitting and overflowing subsets
        v[i] = rng.nextInt(1001); // 0..1000, including the legal zero value
      }
      String expected = Integer.toString(bruteForceOracle(k, w, v));
      assertThat(runMain(buildInput(k, w, v)))
          .as(
              "n=%d k=%d w=%s v=%s",
              n, k, java.util.Arrays.toString(w), java.util.Arrays.toString(v))
          .isEqualTo(expected);
    }
  }

  /**
   * Independent exhaustive oracle: the best total value over every subset of items whose total
   * weight is within {@code k}. It enumerates all {@code 2^N} subsets, so it is obviously correct
   * -- each item is independently in or out, capturing the 0/1 rule -- and trustworthy only for
   * tiny inputs.
   *
   * @implNote {@code O(2^N * N)} time -- where {@code N} is the number of items {@code w.length}.
   *     Callers must keep {@code N} small (here {@code <= 14}).
   */
  private static int bruteForceOracle(int k, int[] w, int[] v) {
    int n = w.length;
    int best = 0;
    for (int mask = 0; mask < (1 << n); mask++) {
      int weight = 0;
      int value = 0;
      for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {
          weight += w[i];
          value += v[i];
        }
      }
      if (weight <= k && value > best) {
        best = value;
      }
    }
    return best;
  }

  /**
   * Independent DP oracle: a separately written 1D 0/1 knapsack for cross-checking the solution at
   * full scale. {@code dp[c]} is the most value packable into capacity {@code c}; iterating the
   * capacity dimension <em>downward</em> for each item is what forbids reusing that item (the
   * defining difference from the unbounded knapsack). The answer is {@code dp[k]}.
   *
   * @implNote {@code O(N * K)} time and {@code O(K)} space -- where {@code N} is the number of
   *     items {@code w.length} and {@code K} is the capacity {@code k}.
   */
  private static int dpOracle(int k, int[] w, int[] v) {
    int[] dp = new int[k + 1];
    for (int i = 0; i < w.length; i++) {
      for (int c = k; c >= w[i]; c--) {
        int candidate = dp[c - w[i]] + v[i];
        if (candidate > dp[c]) {
          dp[c] = candidate;
        }
      }
    }
    return dp[k];
  }

  /** Builds BOJ 12865 input: {@code "N K"} on the first line, then {@code "W[i] V[i]"} rows. */
  private static String buildInput(int k, int[] w, int[] v) {
    int n = w.length;
    StringBuilder sb = new StringBuilder(n * 12 + 16);
    sb.append(n).append(' ').append(k).append('\n');
    for (int i = 0; i < n; i++) {
      sb.append(w[i]).append(' ').append(v[i]).append('\n');
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
