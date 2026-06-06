package boj.boj1300;

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
 * BOJ 1300 K번째 수 (K-th Number) -- "binary search on the answer" where the array is never built.
 *
 * <p>Sejun forms an {@code N x N} array {@code A} with {@code A[i][j] = i * j} (1-indexed -- the
 * multiplication table). Flattening it into a one-dimensional array {@code B} of {@code N * N}
 * entries and sorting {@code B} ascending, find {@code B[k]} (1-indexed). For {@code N = 3} the
 * table flattens and sorts to {@code 1 2 2 3 3 4 6 6 9}, so {@code B[7] = 6}.
 *
 * <p>Input: line 1 is {@code N} ({@code 1 <= N <= 100,000}); line 2 is {@code k} ({@code 1 <= k <=
 * min(10^9, N^2)}). Output: the single value {@code B[k]}. (Spec triangulated across blog mirrors
 * of the statement while acmicpc.net was unreachable; the published sample {@code 3 / 7 -> 6}
 * corroborates it.)
 *
 * <p>The table has up to {@code 10^10} cells, so it cannot be materialized. The intended solution
 * binary-searches a candidate <em>value</em> {@code x} and counts how many table entries are
 * {@code <= x}: row {@code i} holds {@code i, 2i, ..., Ni}, contributing {@code min(N, x / i)}
 * entries, so {@code count(x) = sum over i in [1,N] of min(N, x / i)}. The answer is the
 * <em>smallest</em> {@code x} with {@code count(x) >= k}; that {@code x} is always a real table
 * value even though {@code count} plateaus across the gaps between table values.
 *
 * <p>The fixtures pin the things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>The answer is the smallest value with enough entries at or below it -- not the probed
 *       midpoint.</b> {@code count} is flat across values absent from the table, so a search that
 *       returns {@code mid} (or {@code lo + 1}) can land on a number that never appears.
 *       {@link #answerSkipsValuesAbsentFromTheTable} forces {@code k} into such a gap, and the
 *       official {@link #officialSampleProducesPublishedResult} already exercises the lower edge of
 *       a plateau.
 *   <li><b>A repeated value owns every position in its run.</b> Value {@code 4} fills three
 *       consecutive slots of the {@code N = 4} array, so consecutive {@code k} across that run all
 *       resolve to {@code 4} ({@link #repeatedValueClaimsTheFirstPositionOfItsRun},
 *       {@link #repeatedValueClaimsTheLastPositionOfItsRun}).
 *   <li><b>The search boundaries are real answers.</b> {@code k = 1} is always the table minimum
 *       {@code 1} ({@link #kEqualToOneReturnsTheTableMinimum}); {@code k = N^2} is always the table
 *       maximum {@code N * N} ({@link #kEqualToNSquaredReturnsTheTableMaximum}); the lone cell of
 *       {@code N = 1} is both ({@link #singleCellTableReturnsItsOnlyValue}).
 *   <li><b>Counting and the search range need 64 bits.</b> At {@code N = 50,000} the upper bound
 *       {@code N^2 = 2.5e9} overflows a signed 32-bit int (to a negative value, stalling the
 *       search) ({@link #searchBoundBeyondIntRangeStillResolves}); at {@code N = 100,000},
 *       {@code count(mid)} sums past {@code 2.1e9} mid-search even though the answer itself fits an
 *       int ({@link #maxSizeInputStaysWithinTimeLimit}).
 * </ul>
 *
 * <p>At scale, {@link #maxSizeInputStaysWithinTimeLimit} pins the {@code O(N log N^2)} search (~34
 * counting sweeps of {@code N} rows) against any attempt to enumerate the {@code 10^10}-cell table,
 * and {@link #randomizedInputsMatchSortedTableOracle} cross-checks the judged output against an
 * obviously-correct materialise-and-sort oracle. The {@code Main} under test is a stub that reads
 * {@code N} and {@code k} but prints nothing, so every assertion here is RED until the binary
 * search is implemented.
 */
class MainTest {

  // --- The official sample from the statement: the end-to-end smoke test. The sorted N=3 table is
  // 1 2 2 3 3 4 6 6 9, and B[7] = 6. It also pins the lower edge of a plateau: count(5) = 6 < 7 and
  // count(6) = 8 >= 7, so the search must climb to 6 (the value 5 is absent from the table). ---

  @Test
  @StdIo({"3", "7"})
  void officialSampleProducesPublishedResult(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Smallest input (N = 1): the array is the single cell 1*1 = 1, so the only valid query k = 1
  // returns 1. Pins that the degenerate one-element search terminates on the lone value. ---

  @Test
  @StdIo({"1", "1"})
  void singleCellTableReturnsItsOnlyValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Lower boundary of k: the first sorted element is always 1*1 = 1, whatever N. N=5, k=1 -> 1.
  // Pins that the search's low end reaches 1 and does not undershoot to 0. ---

  @Test
  @StdIo({"5", "1"})
  void kEqualToOneReturnsTheTableMinimum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Upper boundary of k: the last sorted element is always N*N, the lone largest cell. N=4,
  // k=16=N^2 -> 16. Pins that the search's high end reaches N*N and the answer can be the table
  // maximum (count(15) = 15 < 16, count(16) = 16). ---

  @Test
  @StdIo({"4", "16"})
  void kEqualToNSquaredReturnsTheTableMaximum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("16");
  }

  // --- A repeated value owns a run of consecutive positions. In N=4 the sorted array is
  // 1 2 2 3 3 4 4 4 6 6 8 8 9 12 12 16; value 4 fills positions 6, 7, 8 (cells 1*4, 2*2, 4*1).
  // k=6 lands on the first slot of that run -> 4. ---

  @Test
  @StdIo({"4", "6"})
  void repeatedValueClaimsTheFirstPositionOfItsRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- ...and k=8 lands on the last slot of the same run -> still 4. Together with the k=6 case
  // this pins that the whole plateau [6, 8] resolves to one value; a search that returns mid could
  // drift to 5 (absent) at the run's edges. ---

  @Test
  @StdIo({"4", "8"})
  void repeatedValueClaimsTheLastPositionOfItsRun(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- The crux: the answer is the smallest *table* value with count >= k, never a value absent
  // from the table. In N=4, count plateaus at 8 across x in {4, 5} (5 = i*j has no solution with
  // i,j <= 4) and jumps to 10 at x=6. k=9 falls in that gap, so the answer is 6, not 5. A search
  // that returns its midpoint -- or `lo` after the loop without checking membership -- misprints 5.
  // ---

  @Test
  @StdIo({"4", "9"})
  void answerSkipsValuesAbsentFromTheTable(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- An interior position at a slightly larger table, as a general sanity check away from the
  // boundaries. The sorted N=5 table is 1 2 2 3 3 4 5 5 6 6 8 8 9 10 10 12 12 15 15 16 20 20 25
  // (positions 1..25); k=13 -> 8 (the count of entries <= 8 is 14, and <= 6 is 12, so position 13
  // is the first 8). ---

  @Test
  @StdIo({"5", "13"})
  void interiorPositionResolvesToItsTableValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- 64-bit pin on the *search range*. At N=50,000 the upper bound N^2 = 2,500,000,000 overflows
  // a signed 32-bit int to a negative value; an `int hi = n * n` then leaves the search loop's
  // `lo <= hi` false from the start, so it prints garbage. With a long bound the smallest position
  // still resolves: k=2 -> 2 (the table minimum 1 occupies only position 1). A correct answer here
  // forces the bound to be 64-bit. ---

  @Test
  void searchBoundBeyondIntRangeStillResolves() throws IOException {
    assertThat(runMain(kthNumberInput(50_000, 2))).isEqualTo("2");
  }

  // --- k at its theoretical maximum, at scale. N=31,622 is the largest N with N^2 <= 10^9, so k
  // may reach N^2 = 999,950,884; the answer is then the table maximum N*N = 999,950,884 itself. The
  // count must accumulate all the way to N^2 without tripping, and the result -- just under 10^9 --
  // sits near the top of int range, pinning that a correct solution counts to the full table size.
  // ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void kEqualToNSquaredAtScaleReturnsTheTableMaximum() throws IOException {
    int n = 31_622;
    long k = (long) n * n; // 999,950,884 == N^2 <= 10^9
    assertThat(runMain(kthNumberInput(n, k))).isEqualTo("999950884");
  }

  // --- Upper bound on N (100,000) with k at its cap (10^9), the headline 64-bit-and-speed pin. The
  // table would hold 10^10 cells, so it cannot be built or sorted -- only the O(N log N^2) counting
  // search finishes. Mid-search, the bound N^2 = 10^10 and probe values reach into the billions,
  // and
  // count(mid) sums past 2.1e9, so `int` overflows everywhere internally even though the answer,
  // 204,535,821, fits an int. Expected value computed offline and cross-checked by a brute
  // materialize-and-sort oracle on small N (see randomizedInputsMatchSortedTableOracle). ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputStaysWithinTimeLimit() throws IOException {
    assertThat(runMain(kthNumberInput(100_000, 1_000_000_000L))).isEqualTo("204535821");
  }

  // --- Small random tables checked against an independent materialise-and-sort oracle. N stays
  // small (so N^2 entries are cheap to sort) but k ranges over the whole [1, N^2], so plateau
  // interiors, run edges, and absent-value gaps all get hit -- catching off-by-one boundary bugs
  // (`<` vs `<=` in the count test, returning `hi` vs `lo`) the hand-picked cases might slip past.
  // The oracle shares no logic with the counting search, so the two agree only when both are right.
  // ---

  @Test
  void randomizedInputsMatchSortedTableOracle() throws IOException {
    Random rnd = new Random(1300);
    for (int trial = 0; trial < 1000; trial++) {
      int n = 1 + rnd.nextInt(15); // N in [1, 15]
      int k = 1 + rnd.nextInt(n * n); // k in [1, N^2]
      String expected = Integer.toString(kthNumber(n, k));
      assertThat(runMain(kthNumberInput(n, k))).as("N=%d k=%d", n, k).isEqualTo(expected);
    }
  }

  /**
   * Independent oracle: materialise the whole {@code n x n} multiplication table, sort it, and read
   * off the {@code k}-th (1-indexed) entry. Uses no binary search or counting, so it shares no
   * logic with a judge solution and the two agree only when both are right.
   *
   * @implNote {@code O(n^2 log n)} time and {@code O(n^2)} space -- it builds the full table, which
   *     is acceptable only because the randomized fixtures keep {@code n} small.
   */
  private static int kthNumber(int n, int k) {
    int[] b = new int[n * n];
    int idx = 0;
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        b[idx++] = i * j;
      }
    }
    Arrays.sort(b);
    return b[k - 1];
  }

  /** Renders a query as BOJ 1300 input: {@code N} on line 1, {@code k} on line 2. */
  private static String kthNumberInput(int n, long k) {
    return n + "\n" + k + "\n";
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
