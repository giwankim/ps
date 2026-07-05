package boj.boj1697;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 1697 숨바꼭질 ("Hide and Seek") -- the fastest time for Subin to reach his brother on a number
 * line.
 *
 * <p>Subin stands at point {@code N} and his brother stands still at point {@code K}, both within
 * {@code 0 <= N, K <= 100,000}. Every second Subin moves from his current position {@code X} to
 * {@code X - 1}, {@code X + 1}, or {@code 2 * X}. The input is a single line holding {@code N} and
 * {@code K}; print the minimum number of seconds until Subin's position equals {@code K}. All three
 * moves cost the same one second, so the answer is the unweighted shortest-path distance from
 * {@code N} to {@code K}.
 *
 * <ul>
 *   <li><b>The statement sample and standing still.</b> The published example, and the {@code N ==
 *       K} case whose answer is zero seconds. A solver that charges at least one move, or that
 *       tests positions only as it dequeues their neighbors, breaks on the equal pairs
 *       ({@link #statementSampleFindsTheBrotherInFourSeconds(StdOut)},
 *       {@link #samePositionNeedsNoTime(StdOut)}, {@link #samePositionAtOriginNeedsNoTime(StdOut)},
 *       {@link #samePositionAtTheUpperBoundNeedsNoTime(StdOut)}).
 *   <li><b>A brother behind can only be walked to.</b> When {@code K < N} both {@code X + 1} and
 *       {@code 2 * X} move away, so the answer is exactly {@code N - K}. Guards solvers that assume
 *       {@code N <= K} or only ever double
 *       ({@link #brotherBehindIsReachedByWalkingDownOnly(StdOut)},
 *       {@link #brotherOneStepBehindTakesOneSecond(StdOut)}).
 *   <li><b>Doubling and mixed strategies.</b> The shortest path freely mixes the three moves:
 *       stepping <em>away</em> from the brother before doubling can win, and greedy always-doubling
 *       overshoots ({@link #adjacentBrotherAheadTakesOneSecond(StdOut)},
 *       {@link #doublingLandsExactlyOnTheBrother(StdOut)},
 *       {@link #steppingBackThenDoublingBeatsWalkingForward(StdOut)},
 *       {@link #doublingTwiceThenSteppingDownBeatsWalking(StdOut)},
 *       {@link #alwaysDoublingOvershootsTheBrother(StdOut)}).
 *   <li><b>The origin self-loop.</b> {@code 2 * 0 = 0}, so doubling at the origin is a no-op. A
 *       solver that treats it as progress loops forever or counts a phantom move
 *       ({@link #originDoublingIsANoOpSoTheFirstMoveStepsUp(StdOut)},
 *       {@link #originSelfLoopMustNotStallTheSearch(StdOut)}).
 *   <li><b>The upper boundary.</b> Position {@code 100,000} is legal (an off-by-one in a visited
 *       array sized {@code 100,000} fails), and doubling from just above {@code 50,000} lands past
 *       the board -- the optimal move steps down first and then doubles
 *       ({@link #oneBelowTheUpperBoundStepsUp(StdOut)},
 *       {@link #theUpperBoundStepsDownToTheBrother(StdOut)},
 *       {@link #doublingLandsExactlyOnTheUpperBound(StdOut)},
 *       {@link #steppingDownFirstAvoidsDoublingPastTheUpperBound(StdOut)}).
 *   <li><b>Full-board maxima.</b> The largest possible answer (walking the whole board down), and
 *       the deep doubling ladders out of the origin and out of position one. An exponential
 *       recursive search without a visited check cannot finish these within the timeout
 *       ({@link #maximumWalkFromTheUpperBoundToTheOrigin(StdOut)},
 *       {@link #climbFromTheOriginToTheUpperBound(StdOut)},
 *       {@link #doublingLadderFromOneToTheUpperBound(StdOut)}).
 * </ul>
 *
 * <p>The small hand-picked answers are verified by the move sequences quoted in each test comment.
 * The full-board anchors and the sweeps are cross-checked by two independent oracles: a
 * transparently coded breadth-first sweep over the whole board ({@link #bfsDistances(int)}), and a
 * backward recursion on the brother's position that never runs the forward search at all
 * ({@link #backwardGreedyMinSeconds(int, int)}). The exhaustive sweep
 * ({@link #everySmallBoardPairMatchesTheBfsOracle()}) covers all pairs of a small board corner, and
 * the randomized sweep ({@link #randomFullRangePairsMatchTheBackwardGreedyOracle()}) samples the
 * full coordinate range.
 */
class MainTest {

  // --- The statement sample and standing still. ---

  // The published example: 5 -> 10 -> 9 -> 18 -> 17 (or 5 -> 4 -> 8 -> 16 -> 17) reaches the
  // brother in 4 seconds, and no 3-move sequence can.
  @Test
  @StdIo("5 17")
  void statementSampleFindsTheBrotherInFourSeconds(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // Subin starts on top of his brother, so zero seconds pass. Guards a solver that charges at
  // least one move or only tests positions as it generates neighbors.
  @Test
  @StdIo("17 17")
  void samePositionNeedsNoTime(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The equal pair at the smallest legal coordinate. Combined with the origin's 2*0 = 0 self-loop,
  // a solver that expands moves before checking the start can double-count the origin.
  @Test
  @StdIo("0 0")
  void samePositionAtOriginNeedsNoTime(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // The equal pair at the largest legal coordinate. Position 100,000 must be representable, so a
  // visited array sized 100,000 instead of 100,001 fails here before any move is made.
  @Test
  @StdIo("100000 100000")
  void samePositionAtTheUpperBoundNeedsNoTime(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- A brother behind can only be walked to. ---

  // K < N: stepping up and doubling both move away from the brother, so the only useful move is
  // X - 1 and the answer is exactly N - K = 7.
  @Test
  @StdIo("10 3")
  void brotherBehindIsReachedByWalkingDownOnly(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  // The smallest behind case: one step down. Guards an off-by-one in the N > K direction.
  @Test
  @StdIo("5 4")
  void brotherOneStepBehindTakesOneSecond(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Doubling and mixed strategies. ---

  // From 1 both X + 1 and 2 * X land on 2; either way a single second suffices.
  @Test
  @StdIo("1 2")
  void adjacentBrotherAheadTakesOneSecond(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // 2 -> 4 by teleport: doubling alone is the entire shortest path.
  @Test
  @StdIo("2 4")
  void doublingLandsExactlyOnTheBrother(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // 5 -> 4 -> 8 takes 2 seconds while walking forward (5 -> 6 -> 7 -> 8) takes 3. The optimal
  // first move steps AWAY from the brother, so a greedy toward-the-target search miscounts.
  @Test
  @StdIo("5 8")
  void steppingBackThenDoublingBeatsWalkingForward(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // 4 -> 8 -> 16 -> 15: two teleports overshoot by one on purpose, then a single step down
  // finishes in 3 seconds. Doubling only while it undershoots (4 -> 8 then walking) needs 8.
  @Test
  @StdIo("4 15")
  void doublingTwiceThenSteppingDownBeatsWalking(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // 3 -> 4 -> 5 -> 10 (or 3 -> 6 -> 5 -> 10) takes 3 seconds. Doubling whenever possible
  // (3 -> 6 -> 12 -> 11 -> 10) takes 4, so a greedy always-double strategy overshoots and loses.
  @Test
  @StdIo("3 10")
  void alwaysDoublingOvershootsTheBrother(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- The origin self-loop. ---

  // 2 * 0 = 0, so the teleport at the origin goes nowhere and the first real move is 0 -> 1. A
  // solver that counts the no-op teleport as progress reports the wrong time or loops.
  @Test
  @StdIo("0 1")
  void originDoublingIsANoOpSoTheFirstMoveStepsUp(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // 0 -> 1 -> 2 takes 2 seconds. Without a visited check the origin re-enqueues itself through
  // 2 * 0 = 0 forever, so this pins both the answer and termination.
  @Test
  @StdIo("0 2")
  void originSelfLoopMustNotStallTheSearch(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- The upper boundary. ---

  // 99,999 -> 100,000: one step up onto the last legal position. Doubling from 99,999 (199,998)
  // must be discarded as off the board, not indexed into an array.
  @Test
  @StdIo("99999 100000")
  void oneBelowTheUpperBoundStepsUp(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // 100,000 -> 99,999: from the top of the board both X + 1 and 2 * X leave it, so the single
  // step down is the whole path.
  @Test
  @StdIo("100000 99999")
  void theUpperBoundStepsDownToTheBrother(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // 50,000 -> 100,000: one teleport lands exactly on the last legal position, exercising the
  // largest in-bounds doubling.
  @Test
  @StdIo("50000 100000")
  void doublingLandsExactlyOnTheUpperBound(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // From 50,001 the teleport lands on 100,002 -- past the board -- so the optimal path steps down
  // to 50,000 first and then doubles: 2 seconds. Solvers that index 2 * X without a bounds check
  // crash here, and solvers that never move away from the brother walk 49,999 steps.
  @Test
  @StdIo("50001 100000")
  void steppingDownFirstAvoidsDoublingPastTheUpperBound(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Full-board maxima. ---

  // 100,000 -> 0 is the largest possible answer: every one of the 100,000 down-steps is forced.
  // The search must visit the entire board, so an exponential recursion without a visited check
  // cannot finish and a linear-size search finishes instantly.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  @StdIo("100000 0")
  void maximumWalkFromTheUpperBoundToTheOrigin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("100000");
  }

  // 0 -> 100,000 in 22 seconds (oracle-derived, cross-checked by both oracles): the forced 0 -> 1
  // step, then a ladder of doublings with corrective walk-steps in between.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  @StdIo("0 100000")
  void climbFromTheOriginToTheUpperBound(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("22");
  }

  // 1 -> 100,000 in 21 seconds (oracle-derived): the deepest useful doubling ladder, one second
  // shorter than the climb from the origin because the forced 0 -> 1 step is already done.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  @StdIo("1 100000")
  void doublingLadderFromOneToTheUpperBound(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("21");
  }

  // --- Sweeps against the two independent oracles. ---

  // Every ordered pair (n, k) with both coordinates in 0..60 -- all 3,721 of them -- checked
  // against the transparent breadth-first oracle. The window is dense in walk-only, double-heavy,
  // and mixed answers, and includes both origin corners.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void everySmallBoardPairMatchesTheBfsOracle() throws IOException {
    for (int n = 0; n <= 60; n++) {
      int[] dist = bfsDistances(n);
      for (int k = 0; k <= 60; k++) {
        assertThat(runMain(n + " " + k + "\n"))
            .as("n=%d k=%d", n, k)
            .isEqualTo(Integer.toString(dist[k]));
      }
    }
  }

  // Random pairs across the full 0..100,000 range, checked against the backward recursion -- a
  // formulation that shares no code and no search direction with a forward shortest-path solver.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomFullRangePairsMatchTheBackwardGreedyOracle() throws IOException {
    Random rng = new Random(1697L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 300; trial++) {
      int n = rng.nextInt(100_001);
      int k = rng.nextInt(100_001);
      assertThat(runMain(n + " " + k + "\n"))
          .as("n=%d k=%d", n, k)
          .isEqualTo(Integer.toString(backwardGreedyMinSeconds(n, k)));
    }
  }

  /**
   * Transparent oracle: a plain breadth-first sweep from {@code start} over the whole board,
   * returning the distance to every position at once. Coded independently of any solver, with the
   * board bound and the origin self-loop handled by the same visited array.
   *
   * @implNote {@code O(P)} time and space -- where {@code P} is the board size (100,001 positions).
   */
  private static int[] bfsDistances(int start) {
    int[] dist = new int[100_001];
    Arrays.fill(dist, -1);
    ArrayDeque<Integer> q = new ArrayDeque<>();
    dist[start] = 0;
    q.add(start);
    while (!q.isEmpty()) {
      int x = q.poll();
      for (int next : new int[] {x - 1, x + 1, 2 * x}) {
        if (next >= 0 && next <= 100_000 && dist[next] < 0) {
          dist[next] = dist[x] + 1;
          q.add(next);
        }
      }
    }
    return dist;
  }

  /**
   * Independent oracle built on a different formulation: recurse backward from the brother's
   * position. A {@code k} at or below {@code n} is only reachable by walking down ({@code n - k}
   * seconds); an even {@code k} was reached by the final teleport from {@code k / 2} or by pure
   * walking; an odd {@code k} was reached by a final step from {@code k - 1} or {@code k + 1}.
   * Descending before doubling is captured naturally, because {@code k / 2} may fall below
   * {@code n} and hit the walk-down base case. Agreement with a forward breadth-first search across
   * many random instances is real evidence, since this never runs a search at all.
   *
   * @implNote {@code O(K)} time in the worst case -- where {@code K} is the brother's position
   *     {@code k}: the recursion halves {@code k} every other level but branches twice on odd
   *     values, and it is left unmemoized for transparency.
   */
  private static int backwardGreedyMinSeconds(int n, int k) {
    if (k <= n) {
      return n - k;
    }
    if (k == 1) { // only reachable here with n == 0: the single forced step 0 -> 1.
      return 1;
    }
    if (k % 2 == 1) {
      return 1 + Math.min(backwardGreedyMinSeconds(n, k - 1), backwardGreedyMinSeconds(n, k + 1));
    }
    return Math.min(k - n, 1 + backwardGreedyMinSeconds(n, k / 2));
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
