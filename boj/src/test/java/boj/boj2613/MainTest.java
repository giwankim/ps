package boj.boj2613;

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
 * BOJ 2613 숫자구슬 (Number Beads) -- split a fixed row of {@code N} numbered beads into {@code M}
 * contiguous, non-empty groups so that the largest group sum is as small as possible.
 *
 * <p>{@code N} beads are threaded on a rod and can be neither removed nor reordered. Dividing them
 * into {@code M} groups of consecutive beads -- every group must hold at least one bead -- minimize
 * the maximum of the group sums. Print that minimum on the first line and, on the second line, the
 * number of beads in each group from left to right separated by single spaces. When several
 * groupings attain the optimum, any one of them is accepted (special judge).
 *
 * <p>Input: line 1 is {@code "N M"} ({@code 1 <= M <= N <= 300}); line 2 holds the {@code N} bead
 * values, natural numbers at most 100, so every sum fits comfortably in an {@code int} (total at
 * most 30,000). Output: line 1 the minimized maximum group sum, line 2 the {@code M} group sizes.
 *
 * <p>The special judge shapes the fixtures. The first line is unique but the size line need not be,
 * so {@link #assertOptimalGrouping} re-judges the output instead of string-matching it: exactly
 * {@code M} positive sizes that cover all {@code N} beads with every group sum at most the claimed
 * optimum. That check is sufficient -- no grouping's maximum can fall below the true optimum, so a
 * grouping whose sums all stay within it attains it. Every fixture is judged this way, even when
 * the optimal grouping happens to be unique.
 *
 * <p>The fixtures pin the things a hasty solution gets wrong:
 *
 * <ul>
 *   <li><b>The official sample.</b> Splitting 4|2|2 attains the optimum of 17
 *       ({@link #officialSampleMinimizesLargestGroupSumToSeventeen}).
 *   <li><b>The ends of the answer's range.</b> {@code M = 1} makes the answer the total sum
 *       ({@link #singleGroupSwallowsEveryBeadAndAnswersTheTotal}), {@code M = N} makes it the
 *       largest bead ({@link #oneGroupPerBeadAnswersTheLargestBead}), and a bead that dwarfs the
 *       rest floors the answer at that bead ({@link #answerNeverDropsBelowTheLargestBead}) -- a
 *       binary search seeded below the largest bead, or a feasibility check that "fits" an
 *       oversized bead anyway, converges on an impossible cap. The one-bead instance pins the
 *       degenerate read/print path ({@link #singleBeadSingleGroupEchoesTheBeadValue}).
 *   <li><b>Exactly {@code M} non-empty groups.</b> A greedy that packs early groups to the cap
 *       strands the tail on {@code [1 1 1 9]} into three groups; beads must be reserved for the
 *       remaining groups ({@link #everyGroupStillGetsABeadWhenGreedyWouldStarveTheTail}). Equal
 *       beads must spread evenly ({@link #equalBeadsSplitEvenlyAcrossAllGroups}).
 *   <li><b>The special judge.</b> Mirrored beads admit two optimal groupings and either passes
 *       ({@link #anyOneOfSeveralOptimalGroupingsIsAccepted}).
 *   <li><b>Scale and bulk correctness.</b> 300 beads of 100 into 150 groups answer 200 instantly,
 *       where enumerating splits would never finish
 *       ({@link #maxSizeInputIsSolvedWithoutEnumeratingSplits}); 500 random instances must agree
 *       with an interval-DP oracle that shares no logic with the usual parametric search
 *       ({@link #randomizedInputsMatchIntervalDpOracle}).
 * </ul>
 */
class MainTest {

  // --- Official sample: 8 beads (5 4 2 6 9 3 8 7) into 3 groups. Splitting 4|2|2 gives sums 17,
  // 12, 15 -> maximum 17, and no 3-way split does better: with a cap of 16 the first two groups
  // carry at most [5 4 2] and [6 9], stranding [3 8 7] = 18. ---

  @Test
  @StdIo({"8 3", "5 4 2 6 9 3 8 7"})
  void officialSampleMinimizesLargestGroupSumToSeventeen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertOptimalGrouping(out.capturedString().trim(), new int[] {5, 4, 2, 6, 9, 3, 8, 7}, 3, 17);
  }

  // --- The smallest instance: one bead, one group. The answer is the bead itself and the lone
  // group size is 1. Pins the degenerate read/print path before any real partitioning exists. ---

  @Test
  @StdIo({"1 1", "7"})
  void singleBeadSingleGroupEchoesTheBeadValue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertOptimalGrouping(out.capturedString().trim(), new int[] {7}, 1, 7);
  }

  // --- M = 1: the only grouping is all N beads in one group, so the answer is the total sum and
  // the size line is just N. Pins the upper end of the answer's search range (the sum, reached
  // exactly). ---

  @Test
  @StdIo({"4 1", "3 1 4 1"})
  void singleGroupSwallowsEveryBeadAndAnswersTheTotal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertOptimalGrouping(out.capturedString().trim(), new int[] {3, 1, 4, 1}, 1, 9);
  }

  // --- M = N: every bead stands alone, so the answer is the largest bead and the size line is N
  // ones. Pins the lower end of the search range and that exactly M sizes are printed even when no
  // choice exists. ---

  @Test
  @StdIo({"5 5", "1 2 100 2 1"})
  void oneGroupPerBeadAnswersTheLargestBead(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertOptimalGrouping(out.capturedString().trim(), new int[] {1, 2, 100, 2, 1}, 5, 100);
  }

  // --- A bead that dwarfs everything: [100 1 1] into 2 groups. The 100 must sit in some group, so
  // no cap below 100 is feasible; splitting 1|2 gives sums 100 and 2 while 2|1 gives 101 and 1. A
  // binary search that starts below the largest bead, or a feasibility check that "fits" an
  // oversized bead anyway, lands on a smaller, impossible answer. ---

  @Test
  @StdIo({"3 2", "100 1 1"})
  void answerNeverDropsBelowTheLargestBead(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertOptimalGrouping(out.capturedString().trim(), new int[] {100, 1, 1}, 2, 100);
  }

  // --- Six equal beads into three groups: a cap of 4 admits at most two beads per group, and
  // three groups of two cover all six beads exactly, so the answer is 4 with every group holding
  // two beads. Pins the even split and the ceil(total / M) floor of the answer. ---

  @Test
  @StdIo({"6 3", "2 2 2 2 2 2"})
  void equalBeadsSplitEvenlyAcrossAllGroups(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertOptimalGrouping(out.capturedString().trim(), new int[] {2, 2, 2, 2, 2, 2}, 3, 4);
  }

  // --- The starvation trap: [1 1 1 9] into 3 groups, optimum 9 (the 9 must stand alone: 1|2|1 or
  // 2|1|1; grouping it with a 1 costs 10). A greedy that packs the first group to the cap takes
  // [1 1 1], leaves [9] as the second group, and runs out of beads for the third -- output
  // construction must reserve one bead per remaining group. Two groupings are optimal, so the
  // output is re-judged, not string-matched. ---

  @Test
  @StdIo({"4 3", "1 1 1 9"})
  void everyGroupStillGetsABeadWhenGreedyWouldStarveTheTail(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertOptimalGrouping(out.capturedString().trim(), new int[] {1, 1, 1, 9}, 3, 9);
  }

  // --- A mirrored instance with two optima: [1 2 3 1 2] into 2 groups splits as 3|2 (sums 6, 3)
  // or 2|3 (sums 3, 6), both with maximum 6. The special judge accepts either, so the fixture
  // re-judges the grouping rather than guessing which one the solution prints. ---

  @Test
  @StdIo({"5 2", "1 2 3 1 2"})
  void anyOneOfSeveralOptimalGroupingsIsAccepted(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertOptimalGrouping(out.capturedString().trim(), new int[] {1, 2, 3, 1, 2}, 2, 6);
  }

  // --- Upper bound of the input: 300 beads of 100 into 150 groups. A cap of 200 holds at most two
  // beads per group and 150 groups of two cover all 300, so the answer is 30000 / 150 = 200 with
  // every group size exactly 2. Enumerating the C(299, 149) ways to cut would never finish; a
  // polynomial solution returns at once. ---

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maxSizeInputIsSolvedWithoutEnumeratingSplits() throws IOException {
    int[] beads = new int[300];
    Arrays.fill(beads, 100);
    assertOptimalGrouping(runMain(beadInput(150, beads)), beads, 150, 200);
  }

  // --- Small random instances, every group count from 1 to N, cross-checked against an
  // interval-DP oracle that derives the optimum straight from the definition -- no binary search,
  // no greedy packing, so it shares no bugs with the usual parametric-search solution. The
  // grouping itself is re-judged like the special judge would. This is where off-by-one caps, lost
  // beads, and starved groups surface in bulk. ---

  @Test
  void randomizedInputsMatchIntervalDpOracle() throws IOException {
    Random rnd = new Random(2613);
    for (int trial = 0; trial < 500; trial++) {
      int n = 1 + rnd.nextInt(10);
      int m = 1 + rnd.nextInt(n);
      int[] beads = new int[n];
      for (int i = 0; i < n; i++) {
        beads[i] = 1 + rnd.nextInt(9);
      }
      assertOptimalGrouping(runMain(beadInput(m, beads)), beads, m, minimaxGroupSum(beads, m));
    }
  }

  /**
   * Re-judges the printed output the way BOJ's special judge does: line 1 must claim the known
   * optimum, and line 2 must list exactly {@code groups} positive sizes that cover every bead with
   * each group's sum within that optimum. Any such grouping is optimal -- no grouping's maximum can
   * fall below the true optimum -- so no exact size string is demanded.
   */
  private static void assertOptimalGrouping(
      String output, int[] beads, int groups, int expectedMax) {
    String instance = "groups=" + groups + " beads=" + Arrays.toString(beads);
    String[] lines = output.split("\n");
    assertThat(lines).as("%s: an optimum line then a size line", instance).hasSize(2);
    assertThat(Integer.parseInt(lines[0].trim()))
        .as("%s: minimized maximum group sum", instance)
        .isEqualTo(expectedMax);
    int[] sizes =
        Arrays.stream(lines[1].trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
    assertThat(sizes).as("%s: one size per group", instance).hasSize(groups);
    for (int size : sizes) {
      assertThat(size).as("%s: every group holds at least one bead", instance).isPositive();
    }
    assertThat(Arrays.stream(sizes).sum())
        .as("%s: sizes cover every bead exactly once", instance)
        .isEqualTo(beads.length);
    int bead = 0;
    for (int size : sizes) {
      int sum = 0;
      for (int i = 0; i < size; i++) {
        sum += beads[bead++];
      }
      assertThat(sum)
          .as("%s: every group sum stays within the optimum", instance)
          .isLessThanOrEqualTo(expectedMax);
    }
  }

  /**
   * Independent oracle: the minimum possible value of the maximum group sum when the beads are cut
   * into exactly {@code groups} contiguous, non-empty groups. Interval DP straight from the
   * definition -- {@code dp[i][j]} is the best split of the first {@code i} beads into {@code j}
   * groups, extended by trying every length for the last group.
   *
   * @implNote {@code O(n^2 m)} time and {@code O(n m)} space, where {@code n} is
   *     {@code beads.length} and {@code m} is {@code groups} -- fine for the tiny randomized
   *     instances and the single max-size fixture.
   */
  private static int minimaxGroupSum(int[] beads, int groups) {
    int n = beads.length;
    int[] prefix = new int[n + 1];
    for (int i = 0; i < n; i++) {
      prefix[i + 1] = prefix[i] + beads[i];
    }
    int[][] dp = new int[n + 1][groups + 1];
    for (int[] row : dp) {
      Arrays.fill(row, Integer.MAX_VALUE);
    }
    dp[0][0] = 0;
    for (int j = 1; j <= groups; j++) {
      for (int i = j; i <= n; i++) {
        for (int k = j - 1; k < i; k++) {
          dp[i][j] = Math.min(dp[i][j], Math.max(dp[k][j - 1], prefix[i] - prefix[k]));
        }
      }
    }
    return dp[n][groups];
  }

  /** Renders BOJ 2613 input: {@code "N M"} on line 1, then the bead values on line 2. */
  private static String beadInput(int groups, int[] beads) {
    StringBuilder sb = new StringBuilder();
    sb.append(beads.length).append(' ').append(groups).append('\n');
    for (int i = 0; i < beads.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }
      sb.append(beads[i]);
    }
    return sb.append('\n').toString();
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
