package algospot.jumpgame;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * Algospot JUMPGAME -- decide whether the bottom-right cell of an {@code n x n} board is reachable
 * from the top-left one by hopping exactly the number written on the current cell, always moving
 * right or down and never leaving the board.
 *
 * <p><b>I/O contract.</b> Line 1 is {@code C} ({@code C <= 50}), the number of test cases. Each
 * test case is a line with {@code n} ({@code 2 <= n <= 100}) followed by {@code n} lines of
 * {@code n} digits, each 1 to 9 except the bottom-right cell, which is 0. Each test case prints one
 * line, {@code "YES"} if the end is reachable and {@code "NO"} otherwise.
 *
 * <p><b>TDD ladder.</b> Work down the file one rung at a time. Each rung is the smallest input that
 * fails against the minimal code satisfying every rung above it, so the first failure is always the
 * next thing to write. The staircase was verified by building those intermediate implementations
 * and checking each rung breaks exactly the one below it.
 *
 * <ol>
 *   <li>2x2, every hop leaves the board &rarr; print {@code NO}
 *   <li>2x2, goal reachable &rarr; something must depend on the board
 *   <li>3x3, first cell is 1 but the run dies &rarr; a real walk, not a peek at one cell
 *   <li>3x3, going right first dead-ends &rarr; backtracking
 *   <li>100x100 parity wall &rarr; memoization (exponential without it)
 *   <li>two cases &rarr; the loop over {@code C}, and with it re-reading {@code n} per case
 *   <li>two cases, second answer differs &rarr; the memo must be cleared per case
 * </ol>
 *
 * <p>Rungs 1-7 are load-bearing: each one was observed failing against the implementation that
 * satisfies its predecessor. The GUARD tests after them pin the judge's published sample, a varying
 * board size, and the mirror image of rung 4; no partial implementation fails those, and they are
 * labelled so nobody mistakes them for steps.
 */
class JumpGameTest {

  // RUNG 1 -- forces: printing NO.
  // Fails on: an empty main. Both hops from the corner (9 right, 9 down) leave a 2x2 board, so
  // this is the shortest possible "unreachable" instance.
  @Test
  @StdIo({"1", "2", "9 9", "9 0"})
  void everyHopLeavingTheBoardIsUnreachable(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  // RUNG 2 -- forces: the answer to depend on the board at all.
  // Fails on: rung-1 code that always prints NO. Right-then-down (or down-then-right) reaches the
  // corner, which is the shortest possible "reachable" instance.
  @Test
  @StdIo({"1", "2", "1 1", "1 0"})
  void goalIsReachableOnTheSmallestBoard(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  // RUNG 3 -- forces: actually walking the board instead of inspecting the starting cell.
  // Fails on: rung-2 code faked as `board[0][0] == 1 ? YES : NO`. The corner is 1 here too, but
  // both of its neighbors are 9s that hop straight off the board, so the run dies immediately.
  @Test
  @StdIo({"1", "3", "1 9 9", "9 9 9", "9 9 0"})
  void reachableFirstHopIsNotEnoughToReachTheGoal(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  // RUNG 4 -- forces: backtracking, i.e. trying the other direction after one dead-ends.
  // Fails on: rung-3 code that walks a single greedy path preferring right. Going right from the
  // corner lands on a 9 and dies; the only route is down, down, right, right.
  @Test
  @StdIo({"1", "3", "1 9 9", "1 9 9", "1 1 0"})
  void deadEndOnTheFirstDirectionMustBeBackedOutOf(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  // RUNG 5 -- forces: memoization.
  // Fails on: rung-4 code, a correct but un-memoized DFS, which does not finish. Every cell is a
  // 2, so only even coordinates are reachable and the odd corner (99, 99) is not -- but proving
  // that means exhausting the search, and the 50x50 lattice of even cells holds on the order of
  // C(98, 49) distinct paths. Memoized it is 10,000 states and returns instantly.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  @StdIo
  void parityWallIsUnreachableAndMustNotBeSearchedPathByPath(StdOut out) throws IOException {
    // LADDER-GEN: jumpgame_parity_wall
    runOn(parityWall(100));

    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  // RUNG 6 -- forces: the loop over C, and with it re-reading n inside that loop.
  // Fails on: rung-5 code that solves one board and stops, printing a single line. Both cases
  // answer YES, so this rung isolates the loop from anything to do with per-case state. Note that
  // the loop and the per-case n cannot be separated into two rungs: the format repeats n before
  // every case, so a solver that reads n once derails on the second case whatever its size.
  @Test
  @StdIo({"2", "2", "1 1", "1 0", "2", "1 1", "1 0"})
  void everyTestCaseGetsItsOwnLine(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("YES", "YES");
  }

  // RUNG 7 -- forces: clearing the memo between test cases.
  // Fails on: rung-6 code that hoists the cache out of the loop (a static field, say) and fills it
  // once. Both boards are 2x2, so the second case reads the first case's verdict for cell (0, 0)
  // straight out of the stale cache and answers YES instead of NO.
  @Test
  @StdIo({"2", "2", "1 1", "1 0", "2", "9 9", "9 0"})
  void verdictsMustNotLeakFromOneTestCaseToTheNext(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("YES", "NO");
  }

  // GUARD -- test cases of different sizes in one run.
  // Not a rung: rung 6 already forces n to be re-read per case, because the second case's n line
  // sits in the stream whether or not the sizes differ. Kept because it is the readable statement
  // of that contract -- rung 6 proves n is re-read, this shows a genuinely varying board size.
  @Test
  @StdIo({"2", "2", "1 1", "1 0", "3", "1 9 9", "1 9 9", "1 1 0"})
  void boardSizeMayChangeBetweenTestCases(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("YES", "YES");
  }

  // GUARD -- figure (a) from the statement, the reachable half of the judge's sample.
  // Not a rung: any implementation clearing rung 4 clears this too. It is here as the published
  // instance, so a rewrite can be checked against the exact board the problem illustrates.
  @Test
  @StdIo({
    "1",
    "7",
    "2 5 1 6 1 4 1",
    "6 1 1 2 2 9 3",
    "7 2 3 2 1 3 1",
    "1 1 3 1 7 1 2",
    "4 1 2 3 4 1 2",
    "3 3 1 2 3 4 1",
    "1 5 2 9 4 7 0"
  })
  void officialSampleBoardAIsReachable(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  // GUARD -- figure (b): board (a) with the last cell of row 4 changed from 2 to 3, which
  // overshoots the edge and severs the only route to the corner. Not a rung, same reason.
  @Test
  @StdIo({
    "1",
    "7",
    "2 5 1 6 1 4 1",
    "6 1 1 2 2 9 3",
    "7 2 3 2 1 3 1",
    "1 1 3 1 7 1 2",
    "4 1 2 3 4 1 3",
    "3 3 1 2 3 4 1",
    "1 5 2 9 4 7 0"
  })
  void officialSampleBoardBIsUnreachable(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("NO");
  }

  // GUARD -- the official sample verbatim, both boards in one run, exactly as
  // problems/JUMPGAME/samples/sample-01 gives it. The end-to-end characterization test.
  @Test
  @StdIo({
    "2",
    "7",
    "2 5 1 6 1 4 1",
    "6 1 1 2 2 9 3",
    "7 2 3 2 1 3 1",
    "1 1 3 1 7 1 2",
    "4 1 2 3 4 1 2",
    "3 3 1 2 3 4 1",
    "1 5 2 9 4 7 0",
    "7",
    "2 5 1 6 1 4 1",
    "6 1 1 2 2 9 3",
    "7 2 3 2 1 3 1",
    "1 1 3 1 7 1 2",
    "4 1 2 3 4 1 3",
    "3 3 1 2 3 4 1",
    "1 5 2 9 4 7 0"
  })
  void officialSampleIsReproducedExactly(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedLines()).containsExactly("YES", "NO");
  }

  // GUARD -- rung 4 mirrored: here it is the downward first step that dead-ends and the rightward
  // one that survives. Not a rung, because the backtracking rung 4 forced is direction-agnostic;
  // it is kept so a future rewrite cannot pass rung 4 by merely flipping its preferred direction.
  @Test
  @StdIo({"1", "3", "1 1 1", "9 9 1", "9 9 0"})
  void deadEndOnTheOtherDirectionMustAlsoBeBackedOutOf(StdOut out) throws IOException {
    Main.main(new String[0]);

    assertThat(out.capturedString().trim()).isEqualTo("YES");
  }

  /**
   * Builds an {@code n x n} board of 2s with the mandated 0 in the bottom-right corner. Hops of two
   * from an even coordinate stay even, so the odd corner is unreachable -- but only an exhaustive
   * search proves it, which is what makes this the memoization rung.
   */
  private static String parityWall(int n) {
    StringBuilder sb = new StringBuilder();
    sb.append("1\n").append(n).append('\n');
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (j > 0) {
          sb.append(' ');
        }
        sb.append(i == n - 1 && j == n - 1 ? '0' : '2');
      }
      sb.append('\n');
    }
    return sb.toString();
  }

  /**
   * Feeds generated input to {@link Main}. {@code @StdIo} only accepts compile-time constants, so a
   * board too large to spell out as literals is piped in here instead; {@code System.in} is
   * restored afterwards because a bare {@code @StdIo} captures stdout only and leaves stdin alone.
   */
  private static void runOn(String input) throws IOException {
    InputStream original = System.in;
    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      Main.main(new String[0]);
    } finally {
      System.setIn(original);
    }
  }
}
