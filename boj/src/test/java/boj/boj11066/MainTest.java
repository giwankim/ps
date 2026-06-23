package boj.boj11066;

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
 * BOJ 11066 파일 합치기 ("File Merging") -- the canonical optimal interval-merge DP, a 1-D cousin of
 * matrix-chain multiplication.
 *
 * <p>Novelist Kim Daejeon writes a novel as K chapters, each in its own file, then combines them
 * into one final file. A combine step takes <b>two adjacent</b> files (an original chapter file or
 * a previously combined temporary file) and merges them into one, at a cost equal to the sum of the
 * two files' sizes; the chapters must stay in their original order, so only neighbors may be
 * merged. Given the K chapter file sizes, compute the minimum total cost to combine everything into
 * a single file.
 *
 * <p><b>I/O contract.</b> Line 1 is {@code T}, the number of test data sets. Each test data set is
 * two lines: a line with {@code K} ({@code 3 <= K <= 500}), then a line with {@code K}
 * space-separated positive file sizes (each at most 10,000). The program prints exactly {@code T}
 * lines, the i-th holding the minimum combine cost for the i-th test data set, in input order.
 *
 * <p><b>Why this is not Huffman / greedy.</b> If <em>any</em> two files could be merged, the
 * optimum would be the greedy "always merge the two smallest" (Huffman). The adjacency constraint
 * breaks that: the two globally smallest files may be non-adjacent and therefore un-mergeable
 * directly. {@link #huffmanWouldUndercutTheAdjacentOptimum(StdOut)} and
 * {@link #localGreedyMergingIsNotOptimal(StdOut)} are built specifically to fail any greedy
 * implementation.
 *
 * <p><b>Range of the answer.</b> With {@code K <= 500} and sizes {@code <= 10,000} the minimum cost
 * stays within a signed 32-bit {@code int} (an optimal merge tree has height {@code O(log K)}, so
 * the total is roughly {@code 5,000,000 * log2(500)}, far below {@code 2^31}). The oracles below
 * nonetheless accumulate in {@code long} so the cross-checks themselves never overflow regardless
 * of the shape under test.
 *
 * <p><b>Oracles.</b> Small hand-pickable cases are verified against
 * {@link #bruteForceMinCost(int[], int, int)}, an exponential enumeration of every order-preserving
 * merge tree that recomputes each range sum by direct iteration -- algorithmically independent of
 * the tabulated DP, so agreement is genuine evidence. The maximum-size case is cross-checked
 * against {@link #dpMinCost(int[])}, an independently written prefix-sum DP, since exhaustive
 * enumeration is infeasible there; a separate all-equal closed-form case anchors a large instance
 * to pure arithmetic.
 */
class MainTest {

  // --- Base case: the smallest legal input (K = 3), where the merge tree is fixed in shape. ---

  // Three equal files [7, 7, 7]. With only three files the tree is always (two merged) + (the
  // third): one inner merge of cost 14, then the final merge of the whole range, cost 21, for 35.
  // Both split points cost the same here because the files are equal, so this isolates the
  // range-sum re-charge from any split-choice logic: dp = 14 + 21 = 35.
  @Test
  @StdIo({"1", "3", "7 7 7"})
  void smallestEqualInputChargesEachLevelOnce(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("35");
  }

  // --- Split choice: at K = 3 the optimum merges the side that excludes the larger end first. ---

  // [10, 20, 30]. The two candidate trees are (10·20)·30 = 30 + 60 = 90 and 10·(20·30) = 50 + 60 =
  // 110; they differ only by (10+20) vs (20+30), i.e. by the excluded end. The optimum picks the
  // cheaper inner merge -> 90. A solver that always splits at a fixed position (say always leftmost
  // or always the midpoint) rather than minimizing over p would mis-handle one orientation.
  @Test
  @StdIo({"1", "3", "10 20 30"})
  void splitChoosesTheCheaperInnerMerge(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("90");
  }

  // --- Adjacency vs Huffman: the two smallest files are at opposite ends and cannot be merged. ---

  // [1, 100, 100, 1]. The two size-1 files are the cheapest pair but sit at positions 0 and 3, so a
  // Huffman solver would (illegally) merge them first and report 1+1 -> 2, then 2+100 -> 102, then
  // +200 -> a total of 306. The adjacency-respecting optimum instead merges each [1, 100] end pair
  // (101 + 101 = 202) and joins them (+202), for 404. The correct answer is the LARGER number here,
  // so an accidental Huffman implementation surfaces as an under-count.
  @Test
  @StdIo({"1", "4", "1 100 100 1"})
  void huffmanWouldUndercutTheAdjacentOptimum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("404");
  }

  // --- Local greed fails: merging the cheapest adjacent pair at each step is sub-optimal. ---

  // [6, 4, 4, 6]. Greedily merging the cheapest adjacent pair first picks the middle (4·4 = 8),
  // leaving [6, 8, 6], whose remaining merges cost 14 then 20 -> 42. The global optimum instead
  // splits down the middle, merging [6, 4] and [4, 6] (10 + 10) before the final join (+20) -> 40.
  // Any step-greedy solver prints 42 instead of 40.
  @Test
  @StdIo({"1", "4", "6 4 4 6"})
  void localGreedyMergingIsNotOptimal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("40");
  }

  // --- A balanced tree beats a linear one for equal files. ---

  // [10, 10, 10, 10]. Pairing neighbors (10·10, 10·10 -> two 20s, cost 40) then joining the two
  // 20s (+40) costs 80, while a left-leaning chain 20 -> 30 -> 40 costs 90. The DP must discover
  // the
  // balanced tree -> 80. Guards against a solver hard-wired to fold files one-at-a-time from an
  // end.
  @Test
  @StdIo({"1", "4", "10 10 10 10"})
  void equalFilesPreferTheBalancedMergeTree(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("80");
  }

  // --- The judge's official sample, both test data sets answered line for line. ---

  // Test data set 1 [40, 30, 30, 50] -> 300 (verified by hand: dp[0..1] + dp[2..3] + 150 = 70 + 80
  // + 150). Test data set 2, the 15-file array, -> 864 (the published sample output). Feeding both
  // in one run pins the per-line, in-order output contract: a solver that printed only the first
  // answer, swapped the two, or emitted a header would fail here.
  @Test
  @StdIo({"2", "4", "40 30 30 50", "15", "1 21 3 4 5 35 5 4 3 5 98 21 14 17 32"})
  void officialSampleIsAnsweredLineForLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("300", "864");
  }

  // --- Several test data sets in one run must each be answered independently, in input order. ---

  // Three sets with the deliberately distinctive answer shape 35, 90, 40 (reusing the equal-triple,
  // the cheaper-split triple, and the greedy-trap quartet above). The order would be scrambled by
  // any run that sorted or grouped answers, and the values would drift if state (a dp table or
  // prefix array) leaked from one set into the next instead of being reset per set.
  @Test
  @StdIo({"3", "3", "7 7 7", "3", "10 20 30", "4", "6 4 4 6"})
  void eachTestDataSetGetsItsOwnLineInInputOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("35", "90", "40");
  }

  // --- The same test data set repeated must produce the same answer on every line. ---

  // The greedy-trap quartet [6, 4, 4, 6] supplied four times. Each repetition is an independent set
  // and must re-emit 40 -- no deduplication, and no corruption from a memo table carried across
  // sets. This is the multi-set analogue that specifically targets a missing per-set reset.
  @Test
  @StdIo({"4", "4", "6 4 4 6", "4", "6 4 4 6", "4", "6 4 4 6", "4", "6 4 4 6"})
  void repeatedTestDataSetsEachGetTheirOwnLine(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedLines()).containsExactly("40", "40", "40", "40");
  }

  // --- A strictly increasing run, to exercise an asymmetric optimal tree. ---

  // [1, 2, 3, 4, 5], sum 15. The optimum is dp[0][4] = 33 (one valid optimal tree merges 1·2 = 3,
  // then 3·3 = 6, then 4·5 = 9, then joins the 6-file and 9-file blocks, +15). Cross-checked by the
  // brute-force oracle in the sweep below; pinned here as a concrete five-file value distinct from
  // all the symmetric cases.
  @Test
  @StdIo({"1", "5", "1 2 3 4 5"})
  void increasingRunHasAnAsymmetricOptimum(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("33");
  }

  // --- Randomized cross-check: many small inputs against the independent brute-force oracle. ---

  // Random sets of K in 3..9 files with random sizes are each answered and compared to a brute
  // force
  // that enumerates every order-preserving merge tree and sums the real merge costs. The small K
  // keeps the Catalan-number enumeration cheap while still mixing every split orientation, so this
  // single sweep exercises the min-over-p choice, the range-sum re-charge, and the base case across
  // hundreds of shapes. A fixed seed keeps it reproducible across JVMs.
  @Test
  void randomSmallInputsMatchTheBruteForceOracle() throws IOException {
    Random rng = new Random(11066L);
    for (int trial = 0; trial < 300; trial++) {
      int k = 3 + rng.nextInt(7); // 3..9
      int[] sizes = new int[k];
      for (int i = 0; i < k; i++) {
        sizes[i] = 1 + rng.nextInt(10000); // 1..10000, matching the size cap
      }
      String expected = Long.toString(bruteForceMinCost(sizes, 0, k - 1));
      assertThat(runMain(buildInput(sizes)))
          .as("sizes=%s", Arrays.toString(sizes))
          .isEqualTo(expected);
    }
  }

  // --- Several random test data sets in one run, each compared to the oracle. ---

  // Six random sets concatenated into a single T = 6 input and run once; every output line is
  // checked against the brute-force oracle. This is the multi-set analogue of the sweep above and
  // guards the per-set reset under varied K within one process.
  @Test
  void multipleRandomTestDataSetsInOneRunAreAnsweredIndependently() throws IOException {
    Random rng = new Random(110660L);
    int t = 6;
    int[][] sets = new int[t][];
    String[] expected = new String[t];
    for (int i = 0; i < t; i++) {
      int k = 3 + rng.nextInt(7); // 3..9
      int[] sizes = new int[k];
      for (int j = 0; j < k; j++) {
        sizes[j] = 1 + rng.nextInt(10000);
      }
      sets[i] = sizes;
      expected[i] = Long.toString(bruteForceMinCost(sizes, 0, k - 1));
    }
    assertThat(runMainLines(buildInput(sets))).containsExactly(expected);
  }

  // --- A large all-equal instance anchored to a closed form. ---

  // 256 files each of size 1. With equal weights the optimal tree is perfectly balanced: 8 rounds
  // of
  // pairwise neighbor merges, each round costing 256, for 8 * 256 = 2048. The power-of-two count
  // keeps every round even, so the closed form is exact and independent of the DP. Larger than the
  // hand cases, it stresses the inner loops while staying pure arithmetic to verify.
  @Test
  void largeAllEqualInstanceMatchesTheBalancedClosedForm() throws IOException {
    int k = 256;
    int[] sizes = new int[k];
    Arrays.fill(sizes, 1);
    assertThat(runMain(buildInput(sizes))).isEqualTo("2048");
  }

  // --- The maximum number of chapters is parsed and solved correctly within the time limit. ---

  // A full K = 500 set of deterministic pseudo-random sizes, cross-checked against the independent
  // prefix-sum DP oracle (exhaustive enumeration is infeasible at this size). The naive recurrence
  // is O(K^3) ~ 1.25e8 basic operations, comfortably inside the limit; the @Timeout guards against
  // an accidentally exponential or otherwise pathological implementation, and the oracle guards
  // correctness at the largest legal size.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void maximumNumberOfChaptersIsHandledWithinTheTimeLimit() throws IOException {
    int k = 500;
    int[] sizes = new int[k];
    Random rng = new Random(500500L); // fixed seed -> reproducible
    for (int i = 0; i < k; i++) {
      sizes[i] = 1 + rng.nextInt(10000);
    }
    String expected = Long.toString(dpMinCost(sizes));
    assertThat(runMain(buildInput(sizes))).isEqualTo(expected);
  }

  /**
   * Independent brute-force oracle: returns the minimum cost to combine the 0-indexed inclusive
   * range {@code [lo, hi]} of {@code sizes} by trying <em>every</em> split point and recursing on
   * each side, recomputing the range sum by direct iteration each call. It mirrors the physical
   * merge process (the final join always costs the whole range sum) rather than the tabulated DP,
   * so agreement with {@link Main} is a genuine cross-check rather than a re-run of the same code.
   *
   * @implNote Exponential -- the number of order-preserving merge trees is the Catalan number
   *     {@code C(n-1)} where {@code n = hi - lo + 1} is the range length -- so callers must keep
   *     the range small (here at most nine files). Returns 0 for a single file, which performs no
   *     merge.
   */
  private static long bruteForceMinCost(int[] sizes, int lo, int hi) {
    if (lo == hi) {
      return 0L;
    }
    long rangeSum = 0L;
    for (int i = lo; i <= hi; i++) {
      rangeSum += sizes[i];
    }
    long best = Long.MAX_VALUE;
    for (int p = lo; p < hi; p++) {
      long cost = bruteForceMinCost(sizes, lo, p) + bruteForceMinCost(sizes, p + 1, hi) + rangeSum;
      best = Math.min(best, cost);
    }
    return best;
  }

  /**
   * Independent prefix-sum DP oracle used where exhaustive enumeration is infeasible: tabulates
   * {@code dp[i][j]} = minimum cost to combine files {@code i..j} bottom-up by range length, taking
   * the range sum from a prefix array. Written separately from {@link Main} so it can stand in as
   * an expected-value source at the maximum input size.
   *
   * @implNote {@code O(K^3)} time and {@code O(K^2)} space, where {@code K = sizes.length} is the
   *     number of files; accumulates in {@code long} so the oracle cannot overflow.
   */
  private static long dpMinCost(int[] sizes) {
    int k = sizes.length;
    long[] prefix = new long[k + 1];
    for (int i = 0; i < k; i++) {
      prefix[i + 1] = prefix[i] + sizes[i];
    }
    long[][] dp = new long[k][k];
    for (int len = 2; len <= k; len++) {
      for (int i = 0; i + len - 1 < k; i++) {
        int j = i + len - 1;
        long rangeSum = prefix[j + 1] - prefix[i];
        long best = Long.MAX_VALUE;
        for (int p = i; p < j; p++) {
          best = Math.min(best, dp[i][p] + dp[p + 1][j] + rangeSum);
        }
        dp[i][j] = best;
      }
    }
    return dp[0][k - 1];
  }

  /** Builds BOJ 11066 input for a single test data set: {@code "1"}, then {@code K}, then sizes. */
  private static String buildInput(int[] sizes) {
    return buildInput(new int[][] {sizes});
  }

  /**
   * Builds BOJ 11066 input: {@code T} on the first line, then for each test data set a line with
   * {@code K} followed by a line of the {@code K} space-separated file sizes.
   */
  private static String buildInput(int[][] sets) {
    StringBuilder sb = new StringBuilder();
    sb.append(sets.length).append('\n');
    for (int[] sizes : sets) {
      sb.append(sizes.length).append('\n');
      for (int i = 0; i < sizes.length; i++) {
        if (i > 0) {
          sb.append(' ');
        }
        sb.append(sizes[i]);
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  /** Runs {@link Main} on {@code input}, returning stdout trimmed of trailing whitespace. */
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

  /** Runs {@link Main} on {@code input}, returning stdout split into trimmed non-empty lines. */
  private static String[] runMainLines(String input) throws IOException {
    String captured = runMain(input);
    return captured.isEmpty() ? new String[0] : captured.split("\\R");
  }
}
