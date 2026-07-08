package boj.boj7576;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 7576 토마토 ("Tomato") -- the minimum number of days until every tomato in a grid box ripens.
 *
 * <p>A box is {@code M} cells wide and {@code N} cells tall ({@code 2 <= M, N <= 1,000}); the first
 * input line gives {@code M} then {@code N}, and the next {@code N} lines each hold {@code M}
 * integers: {@code 1} for a ripe tomato, {@code 0} for an unripe tomato, and {@code -1} for a cell
 * holding no tomato. Each day, every unripe tomato adjacent (left/right/up/down only -- diagonals
 * do not count) to a ripe one ripens; tomatoes never ripen on their own. Print the minimum number
 * of days until all tomatoes are ripe: {@code 0} if every stored tomato is already ripe, {@code -1}
 * if some tomato can never ripen. The input is guaranteed to contain at least one tomato. Limits: 1
 * second, 256 MB.
 *
 * <ul>
 *   <li><b>The official samples.</b> The five published examples: a single corner source flooding
 *       an open box ({@link #officialSampleOneRipensTheWholeBoxInEightDays}), a corner sealed off
 *       by empty cells ({@link #officialSampleTwoSealedCornerNeverRipens}), two sources meeting
 *       around walls ({@link #officialSampleThreeTwoSourcesMeetInSixDays}), a one-cell corridor
 *       ringing the box ({@link #officialSampleFourRingCorridorTakesFourteenDays}), and a box whose
 *       only tomatoes are already ripe ({@link #officialSampleFiveAllTomatoesAlreadyRipe}).
 *   <li><b>Base cases and day counting.</b> A fully ripe box needs zero days
 *       ({@link #fullyRipeBoxNeedsZeroDays}), a single unripe neighbor ripens in exactly one day
 *       ({@link #singleUnripeNeighborRipensInOneDay}), a box holding unripe tomatoes but no ripe
 *       source never ripens ({@link #boxWithNoRipeTomatoNeverRipens}), an unripe tomato walled in
 *       by empty cells never ripens ({@link #unripeTomatoSealedByEmptyCellsNeverRipens}), and empty
 *       cells themselves never need to ripen ({@link #emptyCellsDoNotNeedToRipen}).
 *   <li><b>The box is M wide and N tall.</b> The first input number is the width and the second the
 *       height -- the reverse of the usual rows-then-columns order. Two non-square boxes whose
 *       transposed readings yield a smaller answer isolate a swapped reader, which a token-based
 *       scanner survives without crashing ({@link #firstInputNumberIsTheWidthNotTheHeight},
 *       {@link #secondInputNumberIsTheHeightNotTheWidth}).
 *   <li><b>Ripening spreads from every source at once, around walls.</b> All ripe tomatoes spread
 *       simultaneously from day one, so a solver searching from only one source overcounts
 *       ({@link #allRipeSourcesSpreadSimultaneously}), and ripening must detour around empty cells
 *       rather than pass through them ({@link #ripeningDetoursAroundEmptyCells}).
 *   <li><b>Boundaries at M = N = 1,000.</b> The maximal open box ripens across the full diagonal in
 *       1,998 days ({@link #maximumBoxWithOneRipeCornerRipensAcrossTheFullDiagonal}), a maximal
 *       fully ripe box parses two million tokens and prints 0
 *       ({@link #maximumBoxAlreadyFullyRipeNeedsZeroDays}), a serpentine corridor produces the
 *       largest possible day count of 500,499 -- feasible only for a linear-time search
 *       ({@link #serpentineCorridorAcrossTheMaximumBoxRipensCellByCell}), and a maximal box whose
 *       lone unripe tomato is unreachable still answers -1
 *       ({@link #maximumBoxWithAnUnreachableTomatoReportsMinusOne}).
 *   <li><b>Randomized cross-check.</b> Random boxes across a spread of sizes, empty-cell densities,
 *       and ripe shares are compared against an independent day-by-day simulation oracle
 *       ({@link #randomBoxesMatchTheDayByDaySimulationOracle}).
 * </ul>
 *
 * <p>The hand-picked answers are verified by the walks quoted in each test comment. The oracle
 * ({@link #simulationOracle(int[][])}) replays the statement literally -- each day it rescans the
 * whole box and ripens every unripe tomato beside a ripe one, with no queue and no distances -- so
 * its day counts come from a genuinely different evaluation order than the multi-source
 * breadth-first search an intended solution would run; agreement across 300 random boxes is
 * independent evidence rather than the same algorithm checked against itself.
 */
class MainTest {

  // --- The official samples. ---

  // Official sample 1: one ripe corner, no empty cells. The farthest unripe tomato sits at the
  // opposite corner, a Manhattan distance of 5 + 3 = 8 -- eight days.
  @Test
  @StdIo({"6 4", "0 0 0 0 0 0", "0 0 0 0 0 0", "0 0 0 0 0 0", "0 0 0 0 0 1"})
  void officialSampleOneRipensTheWholeBoxInEightDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // Official sample 2: the top-left tomato's only neighbors are empty cells, so ripening can never
  // reach it and the answer is -1 no matter how the rest of the box fares.
  @Test
  @StdIo({"6 4", "0 -1 0 0 0 0", "-1 0 0 0 0 0", "0 0 0 0 0 0", "0 0 0 0 0 1"})
  void officialSampleTwoSealedCornerNeverRipens(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // Official sample 3: two ripe sources at opposite corners spread at once around two walls of
  // empty cells and meet after six days -- a single-source solver reports more.
  @Test
  @StdIo({"6 4", "1 -1 0 0 0 0", "0 -1 0 0 0 0", "0 0 0 0 -1 0", "0 0 0 0 -1 1"})
  void officialSampleThreeTwoSourcesMeetInSixDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // Official sample 4: the tomatoes form a one-cell corridor around a block of empty cells, so the
  // single source must ripen them strictly one per day -- fourteen days for fourteen tomatoes.
  @Test
  @StdIo({"5 5", "-1 1 0 0 0", "0 -1 -1 -1 0", "0 -1 -1 -1 0", "0 -1 -1 -1 0", "0 0 0 0 0"})
  void officialSampleFourRingCorridorTakesFourteenDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("14");
  }

  // Official sample 5: both stored tomatoes are already ripe, so zero days pass. Guards a solver
  // that counts the day the initial queue drains as a day of ripening.
  @Test
  @StdIo({"2 2", "1 -1", "-1 1"})
  void officialSampleFiveAllTomatoesAlreadyRipe(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Base cases and day counting. ---

  // Every cell holds a ripe tomato: zero days, with no empty cells involved at all.
  @Test
  @StdIo({"2 2", "1 1", "1 1"})
  void fullyRipeBoxNeedsZeroDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // Exactly one unripe tomato with two ripe neighbors: one day. Pins the smallest nonzero answer
  // against off-by-one day counting in either direction.
  @Test
  @StdIo({"2 2", "1 1", "1 0"})
  void singleUnripeNeighborRipensInOneDay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // Tomatoes are stored but none is ripe -- the statement only guarantees at least one tomato, not
  // a ripe one. Nothing ever ripens, so the answer is -1; a solver that assumes a nonempty
  // starting frontier misbehaves here.
  @Test
  @StdIo({"2 2", "0 0", "0 0"})
  void boxWithNoRipeTomatoNeverRipens(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // The smallest impossible box: the lone unripe tomato's only neighbors are empty cells, so the
  // ripe corner can never reach it.
  @Test
  @StdIo({"2 2", "1 -1", "-1 0"})
  void unripeTomatoSealedByEmptyCellsNeverRipens(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // The empty cell is unreachable, but only tomatoes must ripen: the answer is the one day the two
  // unripe tomatoes take. A solver demanding every CELL be reached reports -1 instead.
  @Test
  @StdIo({"2 2", "1 0", "0 -1"})
  void emptyCellsDoNotNeedToRipen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- The box is M wide and N tall. ---

  // M = 4 columns, N = 2 rows, source at the top-right: the farthest tomato is the bottom-left,
  // four days away. A reader that treats the first number as the row count parses this very token
  // stream as a 4x2 box whose answer is 3, so the wrong orientation yields a wrong number rather
  // than a crash.
  @Test
  @StdIo({"4 2", "0 0 0 1", "0 0 0 0"})
  void firstInputNumberIsTheWidthNotTheHeight(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // The mirror image: M = 2 columns, N = 4 rows, source at the top-right. Again four days, and
  // again the transposed reading (a 2x4 box with answer 3) is a clean parse of the same tokens --
  // together the pair fails any consistent axis swap.
  @Test
  @StdIo({"2 4", "0 1", "0 0", "0 0", "0 0"})
  void secondInputNumberIsTheHeightNotTheWidth(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // --- Ripening spreads from every source at once, around walls. ---

  // Ripe corners at both ends of the top row spread toward each other and finish in three days.
  // A solver that searches from only the first ripe tomato it finds needs five days to cross the
  // whole box and overcounts.
  @Test
  @StdIo({"5 2", "1 0 0 0 1", "0 0 0 0 0"})
  void allRipeSourcesSpreadSimultaneously(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // A wall of empty cells splits the top rows, so ripening from the top-left corner must walk down
  // and around: six days to the top-right tomato. Treating empty cells as passable (or diagonals
  // as adjacent) shortcuts the detour and undercounts.
  @Test
  @StdIo({"3 3", "1 -1 0", "0 -1 0", "0 0 0"})
  void ripeningDetoursAroundEmptyCells(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  // --- Boundaries at M = N = 1,000. ---

  // The maximal box with a single ripe corner and no empty cells: the opposite corner ripens on
  // day 999 + 999 = 1,998. A million tomatoes must pass through the search inside the judge's
  // 1-second budget, which a linear queue-based sweep does easily and a rescan-the-box-per-day
  // simulation does not.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumBoxWithOneRipeCornerRipensAcrossTheFullDiagonal() throws IOException {
    int[][] box = new int[1000][1000];
    box[0][0] = 1;
    assertThat(runMain(buildInput(box))).isEqualTo("1998");
  }

  // The maximal box with every cell already ripe: two million input tokens, a starting frontier of
  // one million tomatoes, and the answer 0. Pins parse speed and the zero-day case at scale.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumBoxAlreadyFullyRipeNeedsZeroDays() throws IOException {
    int[][] box = new int[1000][1000];
    for (int[] row : box) {
      Arrays.fill(row, 1);
    }
    assertThat(runMain(buildInput(box))).isEqualTo("0");
  }

  // A serpentine corridor of tomatoes across the maximal box: full even rows joined alternately at
  // the right and left ends by one tomato on each odd row, everything else empty. The path holds
  // 500 * 1000 + 500 = 500,500 tomatoes with the ripe one at its head, so the tail ripens on day
  // 500,499 -- the day count itself scales with the area, overwhelming any solver whose work grows
  // with days times cells.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void serpentineCorridorAcrossTheMaximumBoxRipensCellByCell() throws IOException {
    int[][] box = new int[1000][1000];
    for (int r = 1; r < 1000; r += 2) {
      Arrays.fill(box[r], -1);
      box[r][r % 4 == 1 ? 999 : 0] = 0;
    }
    box[0][0] = 1;
    assertThat(runMain(buildInput(box))).isEqualTo("500499");
  }

  // The maximal box holding only two tomatoes in opposite corners -- one ripe, one unripe -- with
  // nothing but empty cells between them. The spread dies immediately and only the full final scan
  // of a million cells discovers the survivor: -1.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumBoxWithAnUnreachableTomatoReportsMinusOne() throws IOException {
    int[][] box = new int[1000][1000];
    for (int[] row : box) {
      Arrays.fill(row, -1);
    }
    box[0][0] = 1;
    box[999][999] = 0;
    assertThat(runMain(buildInput(box))).isEqualTo("-1");
  }

  // --- Randomized cross-check against the day-by-day simulation oracle. ---

  // Random boxes with widths and heights across 2..12, empty-cell densities from open floors to
  // walled mazes, and ripe shares from a lone scattered source to mostly ripe, checked against the
  // simulation oracle. Sparse ripe shares stretch the day count, dense empty cells exercise the -1
  // detection, and non-square sizes keep the M/N orientation honest throughout.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomBoxesMatchTheDayByDaySimulationOracle() throws IOException {
    Random rng = new Random(7576L); // fixed seed -> deterministic across JVMs.
    double[] emptyChances = {0.0, 0.2, 0.45};
    double[] ripeChances = {0.05, 0.25, 0.6};
    for (int trial = 0; trial < 300; trial++) {
      int n = 2 + rng.nextInt(11);
      int m = 2 + rng.nextInt(11);
      double emptyChance = emptyChances[trial % emptyChances.length];
      double ripeChance = ripeChances[(trial / emptyChances.length) % ripeChances.length];
      int[][] box = randomBox(n, m, emptyChance, ripeChance, rng);
      assertThat(runMain(buildInput(box)))
          .as("box %dx%d empty=%.2f ripe=%.2f%n%s", m, n, emptyChance, ripeChance, buildInput(box))
          .isEqualTo(simulationOracle(box));
    }
  }

  /**
   * Transparent oracle: replays the statement day by day. Each day it rescans the whole box, ripens
   * every unripe tomato adjacent to a tomato that was ripe at dawn, and stops on the first day
   * nothing changes; any unripe survivor means -1. No queue and no distances, so its answers come
   * from a genuinely different evaluation order than a breadth-first solver.
   *
   * @implNote {@code O(D * N * M)} time and {@code O(N * M)} space -- where {@code N} and {@code M}
   *     are the box's height and width and {@code D} is the number of simulated days (at most
   *     {@code N * M}).
   */
  private static String simulationOracle(int[][] box) {
    int n = box.length;
    int m = box[0].length;
    int[][] cur = new int[n][];
    for (int r = 0; r < n; r++) {
      cur[r] = box[r].clone();
    }
    int days = 0;
    while (true) {
      List<int[]> ripening = new ArrayList<>();
      for (int r = 0; r < n; r++) {
        for (int c = 0; c < m; c++) {
          boolean besideRipe = (r > 0 && cur[r - 1][c] == 1)
              || (r + 1 < n && cur[r + 1][c] == 1)
              || (c > 0 && cur[r][c - 1] == 1)
              || (c + 1 < m && cur[r][c + 1] == 1);
          if (cur[r][c] == 0 && besideRipe) {
            ripening.add(new int[] {r, c});
          }
        }
      }
      if (ripening.isEmpty()) {
        break;
      }
      for (int[] cell : ripening) {
        cur[cell[0]][cell[1]] = 1;
      }
      days++;
    }
    for (int[] row : cur) {
      for (int v : row) {
        if (v == 0) {
          return "-1";
        }
      }
    }
    return Integer.toString(days);
  }

  /**
   * A random {@code n x m} box where each cell is empty with probability {@code emptyChance} and
   * each tomato is ripe with probability {@code ripeChance}, regenerated until at least one tomato
   * is present -- the statement guarantees the input holds one.
   */
  private static int[][] randomBox(
      int n, int m, double emptyChance, double ripeChance, Random rng) {
    while (true) {
      int[][] box = new int[n][m];
      boolean hasTomato = false;
      for (int r = 0; r < n; r++) {
        for (int c = 0; c < m; c++) {
          if (rng.nextDouble() < emptyChance) {
            box[r][c] = -1;
          } else {
            box[r][c] = rng.nextDouble() < ripeChance ? 1 : 0;
            hasTomato = true;
          }
        }
      }
      if (hasTomato) {
        return box;
      }
    }
  }

  /**
   * Builds BOJ 7576 input: the width {@code M} then the height {@code N} on the first line,
   * followed by {@code N} rows of {@code M} space-separated integers.
   */
  private static String buildInput(int[][] box) {
    StringBuilder sb = new StringBuilder();
    sb.append(box[0].length).append(' ').append(box.length).append('\n');
    for (int[] row : box) {
      for (int c = 0; c < row.length; c++) {
        sb.append(row[c]).append(c + 1 < row.length ? ' ' : '\n');
      }
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
