package boj.boj12991;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 12991 홍준이의 행렬 (Hongjun's Matrix) -- the K-th smallest entry of the product matrix of two
 * sequences.
 *
 * <p>Hongjun has two sequences {@code A} and {@code B} of length {@code N}. He builds an
 * {@code N}-by-{@code N} matrix whose entry at row {@code i}, column {@code j} is {@code A[i] *
 * B[j]}, sorts all {@code N^2} entries, and asks for the value at position {@code K} (1-indexed).
 * Print that value.
 *
 * <p>Input: line 1 is {@code "N K"} ({@code 1 <= N <= 30,000}, {@code 1 <= K <= N^2}); line 2 holds
 * the {@code N} elements of {@code A} and line 3 the {@code N} elements of {@code B},
 * space-separated. Every element is a natural number between 1 and 1,000,000,000, in no particular
 * order. Output: the K-th smallest product.
 *
 * <p>The fixtures pin the traps a hasty solution falls into:
 *
 * <ul>
 *   <li><b>Rank semantics.</b> K is 1-indexed over a multiset: K=1 is the smallest product
 *       ({@link #kEqualsOneReturnsSmallestProduct}), K=N^2 the largest
 *       ({@link #kEqualsNSquaredReturnsLargestProduct}), and equal products occupy consecutive
 *       ranks rather than collapsing into one ({@link #duplicateProductsOccupyConsecutiveRanks}).
 *   <li><b>Input order.</b> The sequences arrive unsorted, so a counting strategy must sort first
 *       ({@link #middleRankIsFoundAcrossUnsortedSequences}).
 *   <li><b>64-bit arithmetic.</b> Elements reach 10^9, so products reach 10^18: both the
 *       binary-search bounds ({@link #smallestBillionScaleProductNeedsLongMath}) and the printed
 *       answer ({@link #largestProductReachesTenToTheEighteenth}) overflow int.
 *   <li><b>Degenerate sizes.</b> N=1 leaves a single 1-by-1 matrix entry
 *       ({@link #singleElementMatrixHasExactlyOneProduct}), and an all-equal matrix must not trap
 *       the search in an empty value range ({@link #allEqualProductsAnswerEveryRank}).
 * </ul>
 */
class MainTest {

  // --- Official sample: A = [2, 3], B = [3, 5] give the products {6, 10, 9, 15}, which sort to
  // 6, 9, 10, 15; the K=3rd is 10. Pins the core contract: products of all ordered pairs, ranked
  // ascending, 1-indexed K. ---

  @Test
  @StdIo({"2 3", "2 3", "3 5"})
  void officialSampleThirdSmallestOfFourProducts(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- N=1: the matrix is the single entry A[0] * B[0] = 7 * 6 = 42 and K can only be 1. Pins the
  // smallest legal input and that the answer is a product, not an element of either sequence. ---

  @Test
  @StdIo({"1 1", "7", "6"})
  void singleElementMatrixHasExactlyOneProduct(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("42");
  }

  // --- A = [5, 2, 4] and B = [9, 3, 7] sort to products 6, 12, 14, 15, 18, 28, 35, 36, 45. K=1
  // must return the global minimum 2*3 = 6, which pairs the smallest element of each sequence --
  // neither of which comes first in its input line. ---

  @Test
  @StdIo({"3 1", "5 2 4", "9 3 7"})
  void kEqualsOneReturnsSmallestProduct(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Same matrix, K = N^2 = 9: the global maximum 5*9 = 45. Pins the upper boundary rank; a
  // binary search whose upper bound forgets the largest A-times-largest-B product caps out below
  // the true answer. ---

  @Test
  @StdIo({"3 9", "5 2 4", "9 3 7"})
  void kEqualsNSquaredReturnsLargestProduct(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("45");
  }

  // --- A = [3, 1, 2] and B = [6, 4, 5] sort to products 4, 5, 6, 8, 10, 12, 12, 15, 18. K=5 lands
  // on 10, strictly inside the range, so an implementation that only handles the extremes or skips
  // sorting the shuffled input gets it wrong. ---

  @Test
  @StdIo({"3 5", "3 1 2", "6 4 5"})
  void middleRankIsFoundAcrossUnsortedSequences(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  // --- Same matrix, K=7: the product 12 arises twice (3*4 and 2*6) and fills ranks 6 and 7, so
  // K=7 is still 12, not 15. Pins multiset ranking -- the answer is the smallest value with at
  // least K products less than or equal to it, not the K-th distinct value. ---

  @Test
  @StdIo({"3 7", "3 1 2", "6 4 5"})
  void duplicateProductsOccupyConsecutiveRanks(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- Every product is 2*3 = 6, so every rank including K = N^2 = 4 answers 6. Pins the
  // degenerate case where the binary-search range collapses to a single value immediately; an
  // off-by-one in the loop bounds either spins forever or walks past the only candidate. ---

  @Test
  @StdIo({"2 4", "2 2", "3 3"})
  void allEqualProductsAnswerEveryRank(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Elements at the 10^9 ceiling, K=1: the smallest product is 999999999^2 =
  // 999999998000000001, far beyond int range. Pins that the very first counting probe -- and the
  // search's lower bound -- is computed in 64-bit math; with int arithmetic the product wraps
  // negative and the comparison logic inverts. ---

  @Test
  @StdIo({"2 1", "999999999 1000000000", "999999999 1000000000"})
  void smallestBillionScaleProductNeedsLongMath(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("999999998000000001");
  }

  // --- Same matrix, K = N^2 = 4: the maximum 10^9 * 10^9 = 10^18 exactly. Pins that the answer
  // itself is carried and printed as a long -- the largest value the problem can ever require. ---

  @Test
  @StdIo({"2 4", "999999999 1000000000", "999999999 1000000000"})
  void largestProductReachesTenToTheEighteenth(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1000000000000000000");
  }

  // --- Small random sequences drawn from [1, 12], so duplicate products are everywhere, with K
  // sweeping the full [1, N^2] range. Cross-checked against an oracle that enumerates all N^2
  // products and sorts them -- the strategy the real solution cannot afford, which shares no logic
  // with a counting binary search. Multiset-rank bugs surface here in bulk. ---

  @Test
  void randomizedSmallValueInputsMatchSortingOracle() throws IOException {
    Random rnd = new Random(12991);
    for (int trial = 0; trial < 300; trial++) {
      int n = 1 + rnd.nextInt(8);
      int k = 1 + rnd.nextInt(n * n);
      long[] a = randomSequence(rnd, n, 12);
      long[] b = randomSequence(rnd, n, 12);
      String expected = Long.toString(kthProductBySorting(a, b, k));
      assertThat(runMain(matrixInput(k, a, b)))
          .as("k=%d a=%s b=%s", k, Arrays.toString(a), Arrays.toString(b))
          .isEqualTo(expected);
    }
  }

  // --- The same oracle cross-check with elements drawn from the full [1, 10^9] range, where
  // nearly every product overflows int. A solution that does any intermediate comparison or bound
  // arithmetic in 32 bits diverges from the oracle almost immediately. ---

  @Test
  void randomizedBillionScaleInputsMatchSortingOracle() throws IOException {
    Random rnd = new Random(12992);
    for (int trial = 0; trial < 300; trial++) {
      int n = 1 + rnd.nextInt(8);
      int k = 1 + rnd.nextInt(n * n);
      long[] a = randomSequence(rnd, n, 1_000_000_000);
      long[] b = randomSequence(rnd, n, 1_000_000_000);
      String expected = Long.toString(kthProductBySorting(a, b, k));
      assertThat(runMain(matrixInput(k, a, b)))
          .as("k=%d a=%s b=%s", k, Arrays.toString(a), Arrays.toString(b))
          .isEqualTo(expected);
    }
  }

  // --- Upper bound on N: 30,000 elements per sequence, so the matrix has 9 * 10^8 entries --
  // materializing them (7.2 GB of longs) or sorting them is hopeless, which pins a
  // counting-by-value solution. A is all ones and B is a shuffled permutation of 1..30000, so each
  // value v in 1..30000 appears exactly N times among the products and the K-th smallest is
  // ceil(K / N) of the sorted B: ceil(450,000,015 / 30,000) = 15001 -- derived by counting, never
  // by running the algorithm under test. The shuffle also forces the solution to sort B before
  // counting. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputIsAnsweredByCountingNotEnumeration() throws IOException {
    int n = 30_000;
    long[] a = new long[n];
    Arrays.fill(a, 1L);
    long[] b = new long[n];
    for (int i = 0; i < n; i++) {
      b[i] = i + 1;
    }
    shuffle(b, new Random(42));
    assertThat(runMain(matrixInput(450_000_015L, a, b))).isEqualTo("15001");
  }

  /**
   * Independent oracle: the K-th smallest (1-indexed) of all pairwise products {@code a[i] * b[j]},
   * found by enumerating every product and sorting -- the definition itself, with no counting or
   * binary-search shortcut shared with the solution under test.
   *
   * @implNote {@code O(N^2 log N)} time and {@code O(N^2)} space, where {@code N} is the common
   *     length of {@code a} and {@code b} -- affordable only because the randomized fixtures keep
   *     {@code N} tiny.
   */
  private static long kthProductBySorting(long[] a, long[] b, int k) {
    long[] products = new long[a.length * b.length];
    int idx = 0;
    for (long x : a) {
      for (long y : b) {
        products[idx++] = x * y;
      }
    }
    Arrays.sort(products);
    return products[k - 1];
  }

  /** Samples {@code n} elements uniformly from {@code [1, maxValue]}; duplicates are welcome. */
  private static long[] randomSequence(Random rnd, int n, int maxValue) {
    long[] sequence = new long[n];
    for (int i = 0; i < n; i++) {
      sequence[i] = 1 + rnd.nextInt(maxValue);
    }
    return sequence;
  }

  /** Fisher-Yates shuffle, seeded by the caller for reproducibility. */
  private static void shuffle(long[] values, Random rnd) {
    for (int i = values.length - 1; i > 0; i--) {
      int j = rnd.nextInt(i + 1);
      long tmp = values[i];
      values[i] = values[j];
      values[j] = tmp;
    }
  }

  /**
   * Renders BOJ 12991 input: {@code "N K"} on line 1, then sequence A on line 2 and B on line 3.
   */
  private static String matrixInput(long k, long[] a, long[] b) {
    StringBuilder sb = new StringBuilder();
    sb.append(a.length).append(' ').append(k).append('\n');
    appendLine(sb, a);
    appendLine(sb, b);
    return sb.toString();
  }

  private static void appendLine(StringBuilder sb, long[] values) {
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(values[i]);
    }
    sb.append('\n');
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
