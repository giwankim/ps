package boj.boj30464;

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
 * BOJ 30464 시간낭비 ("Wasting Time") -- maximize the minute Geondeok first reaches school.
 *
 * <p>The road is {@code N} cells laid left to right, numbered {@code 1..N}. Geondeok starts on cell
 * {@code 1} and the school is cell {@code N}; he begins facing the school (rightward, toward higher
 * numbers). Every minute he jumps from his current cell by exactly the value written on it,
 * {@code a_i}, in the direction he faces. A jump may never leave the road, so from cell {@code i} a
 * rightward jump is legal only when {@code i + a_i <= N} and a leftward jump only when {@code i -
 * a_i >= 1}; he can therefore never overshoot the school, only land on it exactly. He may reverse
 * his facing direction <b>at most twice</b> (reversing is free and instantaneous). Wanting to
 * dawdle, he maximizes the minute at which he <em>first</em> lands on cell {@code N}; print that
 * minute, or {@code -1} if no sequence of moves can ever reach the school.
 *
 * <p>Constraints: {@code 3 <= N <= 200,000} and {@code 0 <= a_i <= 200,000}. Input is {@code N} on
 * the first line and the {@code N} cell values on the second; output is a single integer.
 *
 * <p><b>Model.</b> "At most two reversals" with a fixed rightward start means the walk has at most
 * three monotone phases: right, then left, then right. Encode a state as {@code (pos, rev)} with
 * {@code rev} the number of reversals already spent ({@code 0, 1, 2}); the facing direction is
 * rightward when {@code rev} is even and leftward when it is odd. From {@code (pos, rev)} the mover
 * may spend {@code r in [0, 2 - rev]} further reversals and then jump, landing on {@code pos + dir
 * * a_pos} for the post-reversal direction. Because {@code rev} never decreases and, within a fixed
 * direction, a positive {@code a_pos} moves strictly toward one end, the state graph is a
 * <b>DAG</b>; the answer is the <em>longest</em> walk (counting jumps) from {@code (1, 0)} to any
 * state sitting on cell {@code N}. Two structural facts force {@code -1}: a cell with {@code a_i =
 * 0} is an inescapable pit -- every jump is a zero-length self-loop -- and a cell whose every legal
 * jump (in either reachable direction) runs off the road strands the mover. Either way the school
 * is unreachable.
 *
 * <p><b>What the official samples do and do not pin down.</b> All four statement samples are
 * reproduced verbatim below. They anchor the four outcome shapes -- a genuine three-phase zig-zag
 * ({@link #statementSampleOneZigZagsThroughBothReversals(StdOut)} = 5), a forced single jump with
 * no room to dawdle ({@link #statementSampleTwoForcesAnImmediateArrival(StdOut)} = 1), an
 * unreachable board ({@link #statementSampleThreeIsUnreachable(StdOut)} = -1), and a longer mixed
 * walk ({@link #statementSampleFourTakesTwelveMinutes(StdOut)} = 12) -- but, as the problem's own
 * editorial notes, they can be passed even by a solver that conflates the three phases. The hand
 * cases below isolate each rule, and the randomized oracle sweeps exercise the {@code <= 2}
 * reversal cap across hundreds of shapes.
 *
 * <p><b>Oracles.</b> Expected answers for generated boards come from two independently ordered
 * dynamic programs over the same recurrence: a top-down memoized search ({@link #topDownAnswer(int,
 * int[])}) and a bottom-up sweep that resolves the layers {@code rev = 2, 1, 0} in turn
 * ({@link #bottomUpAnswer(int, int[])}). The tiny sweep pins the two against each other and against
 * {@link Main} ({@link #randomTinyBoardsMatchBothOracles()}); the larger and maximal sweeps trust
 * the depth-safe bottom-up oracle ({@link #randomModerateBoardsMatchTheBottomUpOracle()},
 * {@link #deepUniformChainIsResolvedWithinTheTimeLimit()},
 * {@link #maximalRandomBoardIsResolvedWithinTheTimeLimit()}).
 */
class MainTest {

  // --- The four statement samples, reproduced verbatim. ---

  // N = 5, a = [3,1,2,1,1]. The greedy 1->4->5 arrives in 2 minutes, but the late route burns 5:
  // right 1->4, reverse left 4->3->1, reverse right 1->4->5 -- two reversals, all three phases
  // used.
  @Test
  @StdIo({"5", "3 1 2 1 1"})
  void statementSampleOneZigZagsThroughBothReversals(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // N = 5, a = [4,1,2,1,1]. From cell 1 the only legal first jump is 1->5 (a leftward reversal runs
  // off the road), so Geondeok is forced onto the school at once and cannot dawdle: the answer is
  // 1.
  @Test
  @StdIo({"5", "4 1 2 1 1"})
  void statementSampleTwoForcesAnImmediateArrival(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // N = 3, a = [1,2,1]. Cell 1 forces 1->2; from cell 2 every jump (2+2=4 overshoots, 2-2=0
  // underflows) leaves the road in every direction, so he is stranded and the school is
  // unreachable.
  @Test
  @StdIo({"3", "1 2 1"})
  void statementSampleThreeIsUnreachable(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // N = 9, a = [1,2,2,4,2,1,1,1,1]. A longer mixed walk whose latest first-arrival is 12 minutes.
  @Test
  @StdIo({"9", "1 2 2 4 2 1 1 1 1"})
  void statementSampleFourTakesTwelveMinutes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- Smallest boards: a forced single jump pins the lower bound of a reachable answer. ---

  // N = 3, a = [2,1,1]. Cell 1 jumps 1->3 straight onto the school; a leftward reversal underflows,
  // so there is no detour. The minimum non-trivial answer, 1, with zero reversals spent.
  @Test
  @StdIo({"3", "2 1 1"})
  void minimumBoardReachesSchoolInOneMinute(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Family: a zero-valued cell is an inescapable pit, forcing -1. ---

  // N = 3, a = [0,1,1]. The start cell itself is a pit: every jump from cell 1 has length 0 and
  // never leaves it, so the school is never reached. Guards the a_1 = 0 boundary.
  @Test
  @StdIo({"3", "0 1 1"})
  void aZeroOnTheStartCellTrapsGeondeokImmediately(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // N = 4, a = [1,0,1,1]. Cell 1 forces 1->2, and cell 2 is a pit; since 1->2 is the only legal
  // opening, he is funneled into the pit and stranded. A zero strictly between start and school.
  @Test
  @StdIo({"4", "1 0 1 1"})
  void aZeroMidRouteSwallowsTheOnlyPathToSchool(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- Family: every legal jump running off the road strands the mover, forcing -1. ---

  // N = 4, a = [1,5,1,1]. Cell 1 forces 1->2; from cell 2 the value 5 overshoots the road in both
  // directions (2+5=7 > 4, 2-5 = -3 < 1), so no reversal can rescue him. An over-large value, not a
  // zero, does the stranding here -- distinct from the pit cases above.
  @Test
  @StdIo({"4", "1 5 1 1"})
  void anOverLargeValueOffTheRoadInBothDirectionsStrands(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- A clean three-phase zig-zag whose answer depends on the reversal cap. ---

  // N = 4, a = [1,1,1,1]. On a uniform unit board the latest arrival is the full zig-zag
  // 1->2->3->2->1->2->3->4 -- two reversals, 7 minutes. Continuing straight at any step would
  // arrive
  // sooner, so the maximum genuinely uses both reversals across all three phases.
  @Test
  @StdIo({"4", "1 1 1 1"})
  void uniformUnitBoardZigZagsForSevenMinutes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // --- Randomized cross-checks against the independent oracles. ---

  // Tiny boards cross-checked against both oracles and Main. Small N with values drawn across
  // [0, N] packs in pits, off-road overshoots, forced jumps, and full three-phase zig-zags, so this
  // sweep alone exercises the reversal cap, the per-jump road bound, the zero-pit dead end, and the
  // reachable/unreachable split across hundreds of shapes. Agreement of the two differently ordered
  // oracles licenses the bottom-up oracle for the larger boards below.
  @Test
  void randomTinyBoardsMatchBothOracles() throws IOException {
    Random rng = new Random(30464L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 500; trial++) {
      int n = 3 + rng.nextInt(7); // 3..9
      int[] a = randomBoard(n, n, rng); // values in [0, n]: traps, overshoots, and real moves mix
      String expected = bottomUpAnswer(n, a);
      assertThat(topDownAnswer(n, a))
          .as("oracle disagreement: n=%d a=%s", n, Arrays.toString(slice(a, n)))
          .isEqualTo(expected);
      assertThat(runMain(buildInput(n, a)))
          .as("n=%d a=%s", n, Arrays.toString(slice(a, n)))
          .isEqualTo(expected);
    }
  }

  // Larger boards cross-checked against the bottom-up sweep, a different evaluation order from the
  // intended top-down recursion. Trials alternate between small values (dense, branchy, mostly
  // reachable walks) and large values (frequent off-road jumps and dead ends), reaching the scale
  // where an O(N) layered DP is required while still exercising the road bound and the reversal
  // cap.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomModerateBoardsMatchTheBottomUpOracle() throws IOException {
    Random rng = new Random(3046400L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 200; trial++) {
      int n = 50 + rng.nextInt(1951); // 50..2000
      int maxVal = (trial % 2 == 0) ? 3 : n; // alternate dense small values and sparse large ones
      int[] a = randomBoard(n, maxVal, rng);
      assertThat(runMain(buildInput(n, a)))
          .as("n=%d maxVal=%d", n, maxVal)
          .isEqualTo(bottomUpAnswer(n, a));
    }
  }

  // A maximal uniform unit chain, N = 200,000. Every cell holds 1, so the zig-zag walk is on the
  // order of 3N jumps long and the reachable state DAG spans 3N nodes. A top-down recursion would
  // recurse that deep and overflow a default stack; the bottom-up oracle resolves it iteratively.
  // Forces an O(N), depth-safe solution.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void deepUniformChainIsResolvedWithinTheTimeLimit() throws IOException {
    int n = 200_000;
    int[] a = new int[n + 1];
    Arrays.fill(a, 1, n + 1, 1);
    assertThat(runMain(buildInput(n, a))).isEqualTo(bottomUpAnswer(n, a));
  }

  // A maximal board with values spread across the full [0, 200,000] range at N = 200,000, mixing
  // pits, far jumps, and off-road overshoots at scale. Any solver worse than linear per layer
  // cannot
  // finish in time; the winner is computed by the bottom-up oracle.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximalRandomBoardIsResolvedWithinTheTimeLimit() throws IOException {
    Random rng = new Random(30464200000L); // fixed seed -> deterministic across JVMs.
    int n = 200_000;
    int[] a = randomBoard(n, 200_000, rng);
    assertThat(runMain(buildInput(n, a))).isEqualTo(bottomUpAnswer(n, a));
  }

  /**
   * Sentinel for "the school cannot be reached from this state"; far enough below 0 to survive +1.
   */
  private static final int UNREACHABLE = Integer.MIN_VALUE / 2;

  /**
   * Top-down memoized oracle: the latest first-arrival minute, or {@code "-1"} if unreachable.
   * Directly transcribes the problem's recurrence over states {@code (pos, rev)} -- spend {@code r}
   * of the remaining reversals, then jump in the resulting direction -- so it is transparently
   * correct, and serves as the reference the bottom-up oracle is checked against.
   *
   * @implNote {@code O(N)} states each scanning a constant number of reversal choices; recursion
   *     depth is on the order of the walk length, up to {@code 3N}, so it is reserved for small
   *     {@code N} where the forked test JVM's enlarged stack comfortably holds it.
   */
  private static String topDownAnswer(int n, int[] a) {
    int[][] memo = new int[n + 1][3];
    boolean[][] done = new boolean[n + 1][3];
    int best = solveTopDown(1, 0, n, a, memo, done);
    return best == UNREACHABLE ? "-1" : Integer.toString(best);
  }

  private static int solveTopDown(
      int pos, int rev, int n, int[] a, int[][] memo, boolean[][] done) {
    if (pos == n) {
      return 0; // already standing on the school
    }
    if (a[pos] == 0) {
      return UNREACHABLE; // a zero cell is an inescapable pit
    }
    if (done[pos][rev]) {
      return memo[pos][rev];
    }
    int best = UNREACHABLE;
    for (int r = 0; rev + r <= 2; r++) {
      int nrev = rev + r;
      int dir = (nrev % 2 == 0) ? 1 : -1; // rightward after an even number of reversals
      int npos = pos + dir * a[pos];
      if (npos < 1 || npos > n) {
        continue; // this jump would leave the road
      }
      if (npos == n) {
        best = Math.max(best, 1); // landing exactly on the school ends the walk
        continue;
      }
      int sub = solveTopDown(npos, nrev, n, a, memo, done);
      if (sub != UNREACHABLE) {
        best = Math.max(best, 1 + sub);
      }
    }
    memo[pos][rev] = best;
    done[pos][rev] = true;
    return best;
  }

  /**
   * Bottom-up oracle: the same recurrence resolved by descending reversal layer {@code rev = 2, 1,
   * 0}. Within each layer the cells are swept in the direction that makes every same-layer
   * dependency already final -- descending position for the rightward layers ({@code rev = 0, 2})
   * and ascending position for the leftward layer ({@code rev = 1}) -- while cross-layer jumps look
   * only at the already-completed higher layers. Iterative, so depth-safe at the constraint
   * ceiling, and a different evaluation order from the top-down reference, which makes their
   * agreement real evidence.
   *
   * @implNote {@code O(N)} time and space, scanning a constant number of reversal choices per cell.
   */
  private static String bottomUpAnswer(int n, int[] a) {
    int[][] dp = new int[n + 1][3];
    for (int[] row : dp) {
      Arrays.fill(row, UNREACHABLE);
    }
    for (int pos = n - 1; pos >= 1; pos--) { // rev = 2: rightward, depends on larger positions
      dp[pos][2] = bestFrom(pos, 2, n, a, dp);
    }
    for (int pos = 1; pos <= n - 1; pos++) { // rev = 1: leftward, depends on smaller positions
      dp[pos][1] = bestFrom(pos, 1, n, a, dp);
    }
    for (int pos = n - 1; pos >= 1; pos--) { // rev = 0: rightward, depends on larger positions
      dp[pos][0] = bestFrom(pos, 0, n, a, dp);
    }
    int best = dp[1][0];
    return best == UNREACHABLE ? "-1" : Integer.toString(best);
  }

  private static int bestFrom(int pos, int rev, int n, int[] a, int[][] dp) {
    if (a[pos] == 0) {
      return UNREACHABLE; // a zero cell is an inescapable pit
    }
    int best = UNREACHABLE;
    for (int r = 0; rev + r <= 2; r++) {
      int nrev = rev + r;
      int dir = (nrev % 2 == 0) ? 1 : -1;
      int npos = pos + dir * a[pos];
      if (npos < 1 || npos > n) {
        continue;
      }
      if (npos == n) {
        best = Math.max(best, 1);
        continue;
      }
      if (dp[npos][nrev] != UNREACHABLE) {
        best = Math.max(best, 1 + dp[npos][nrev]);
      }
    }
    return best;
  }

  /**
   * A random board with {@code a[1..n]} drawn uniformly from {@code [0, maxVal]} (cell 0 unused).
   */
  private static int[] randomBoard(int n, int maxVal, Random rng) {
    int[] a = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      a[i] = rng.nextInt(maxVal + 1);
    }
    return a;
  }

  /** The used cells {@code a[1..n]} as a 0-based array, for diagnostic {@code as(...)} messages. */
  private static int[] slice(int[] a, int n) {
    return Arrays.copyOfRange(a, 1, n + 1);
  }

  /** Builds BOJ 30464 input: {@code N} on the first line, then {@code a_1 .. a_N} on the second. */
  private static String buildInput(int n, int[] a) {
    StringBuilder sb = new StringBuilder();
    sb.append(n).append('\n');
    for (int i = 1; i <= n; i++) {
      if (i > 1) {
        sb.append(' ');
      }
      sb.append(a[i]);
    }
    sb.append('\n');
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
