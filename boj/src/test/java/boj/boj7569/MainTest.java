package boj.boj7569;

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
 * BOJ 7569 토마토 ("Tomatoes") -- the days until every tomato in a stacked warehouse ripens.
 *
 * <p>A warehouse stores {@code H} boxes stacked bottom to top, each an {@code N}-row by
 * {@code M}-column grid whose cells hold a ripe tomato ({@code 1}), an unripe tomato ({@code 0}),
 * or nothing ({@code -1}), within {@code 2 <= M, N <= 100} and {@code 1 <= H <= 100}. Each day
 * every unripe tomato with a ripe tomato in one of the six axis-adjacent cells (left, right, front,
 * back, directly above, directly below) ripens; diagonal contact has no effect and no tomato ripens
 * on its own. The input is {@code M N H} followed by {@code H * N} rows of {@code M} values, bottom
 * box first, and at least one cell holds a tomato. Print the minimum number of days until no unripe
 * tomato remains, {@code 0} if none is unripe to begin with, and {@code -1} if some tomato can
 * never ripen. All ripe tomatoes spread at once, so the answer is the largest multi-source
 * unweighted shortest-path distance over the tomato cells.
 *
 * <ul>
 *   <li><b>The statement samples.</b> The three published examples cover all three verdict shapes:
 *       a tomato sealed behind empty cells ({@code -1}), a four-day spread from the center of the
 *       upper box ({@code 4}), and a warehouse that starts fully ripe ({@code 0})
 *       ({@link #statementSampleSealedTomatoNeverRipens(StdOut)},
 *       {@link #statementSampleRipensInFourDays(StdOut)},
 *       {@link #statementSampleAlreadyRipeStackNeedsNoDays(StdOut)}).
 *   <li><b>Boards that need no days.</b> No unripe tomato means zero days, even when the box is
 *       mostly empty cells. Guards solvers that charge a first day unconditionally or mishandle a
 *       search that ends where it started ({@link #smallestAllRipeBoxNeedsNoDays(StdOut)},
 *       {@link #aLoneRipeTomatoAmongEmptyCellsNeedsNoDays(StdOut)}).
 *   <li><b>Boards that can never finish.</b> A box with unripe tomatoes but no ripe source, unripe
 *       tomatoes touching a ripe one only diagonally -- within a layer and across the space
 *       diagonal, which 8-, 18-, and 26-connectivity solvers wrongly ripen -- and a stack whose
 *       middle layer is entirely empty ({@link #noRipeSourceLeavesUnripeTomatoesForever(StdOut)},
 *       {@link #inLayerDiagonalContactDoesNotRipen(StdOut)},
 *       {@link #spaceDiagonalContactDoesNotRipen(StdOut)},
 *       {@link #anAllEmptyLayerCutsTheStackInTwo(StdOut)}).
 *   <li><b>Day counting within one layer.</b> A single adjacent pair takes exactly one day, a
 *       corner source sweeps an obstacle-free layer in Manhattan-distance days, and a ripe tomato
 *       sealed off by itself must not trigger {@code -1} -- only unripe cells need reaching
 *       ({@link #oneAdjacentTomatoRipensInOneDay(StdOut)},
 *       {@link #cornerSourceSweepsASingleLayer(StdOut)},
 *       {@link #anIsolatedRipeTomatoNeedsNoRescue(StdOut)}).
 *   <li><b>Ripening crosses stacked layers.</b> A tomato directly above a ripe one ripens in one
 *       day and a ripe floor ripens the whole stack -- a four-direction two-dimensional solver
 *       reports {@code -1} on both -- and a rectangular stack pins the parse order of {@code H}
 *       blocks of {@code N} rows ({@link #ripenessClimbsStraightUpTheStack(StdOut)},
 *       {@link #aRipeFloorRipensTheWholeStack(StdOut)},
 *       {@link #rectangularStackReadsWholeLayersInOrder(StdOut)}).
 *   <li><b>Parsing the box shape.</b> The first line is width {@code M} before row count {@code N};
 *       on this rectangular layer the correct reading answers {@code 5} while the transposed
 *       reading answers {@code 4} ({@link #rectangularLayerReadsWidthThenRowCount(StdOut)}).
 *   <li><b>Simultaneous sources.</b> Every ripe tomato enters the spread at day zero, so two
 *       opposite corner sources meet in the middle. A solver that finishes one source's search
 *       before starting the next overcounts ({@link #allRipeSourcesSpreadOnTheSameDay(StdOut)}).
 *   <li><b>Full-size boards.</b> The 100 x 100 x 100 box corner to corner (an obstacle-free
 *       breadth-first distance collapses to the Manhattan distance, 297), fully ripe (zero days
 *       from a million-entry starting front), and with one sealed corner (the whole box must be
 *       explored before concluding {@code -1}). A linear-size search finishes instantly; a per-day
 *       full rescan or an unmemoized recursion does not ({@link #maximumBoxRipensCornerToCorner()},
 *       {@link #maximumBoxAlreadyRipeNeedsNoDays()},
 *       {@link #maximumBoxWithASealedCornerNeverFinishes()}).
 * </ul>
 *
 * <p>The small hand-picked answers are verified by the day-by-day spread quoted in each test
 * comment, and the full-size anchors are obstacle-free boxes whose breadth-first distance is the
 * Manhattan distance. Everything else is cross-checked against a transparently coded simulation
 * oracle ({@link #simulateDays(int[][][])}) that rescans the whole box once per day exactly as the
 * statement is worded -- no queue and no distances, so agreement with a breadth-first solver is
 * real evidence: exhaustively for every possible 2 x 2 x 1 and 2 x 2 x 2 box
 * ({@link #everyTinyBoxMatchesTheSimulationOracle()}) and on randomized boxes of mixed shapes and
 * densities ({@link #randomBoxesMatchTheSimulationOracle()}).
 */
class MainTest {

  // --- The statement samples. ---

  // The published first example: the tomato at the top-left corner touches only empty cells
  // (right and below are -1, and H = 1 leaves no vertical neighbors), so it can never ripen.
  @Test
  @StdIo({"5 3 1", "0 -1 0 0 0", "-1 -1 0 1 1", "0 0 0 1 1"})
  void statementSampleSealedTomatoNeverRipens(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // The published second example: the only ripe tomato sits at the center of the upper box, and
  // the farthest tomatoes are the bottom box's corners at distance 1 + 1 + 2 = 4 days.
  @Test
  @StdIo({"5 3 2", "0 0 0 0 0", "0 0 0 0 0", "0 0 0 0 0", "0 0 0 0 0", "0 0 1 0 0", "0 0 0 0 0"})
  void statementSampleRipensInFourDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  // The published third example: every tomato is already ripe (the rest is empty cells), so zero
  // days pass. Guards a solver that counts a day for the initial front or trips over -1 cells.
  @Test
  @StdIo({"4 3 2", "1 1 1 1", "1 1 1 1", "1 1 1 1", "1 1 1 1", "-1 -1 -1 -1", "1 1 1 -1"})
  void statementSampleAlreadyRipeStackNeedsNoDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Boards that need no days. ---

  // The smallest legal box, fully ripe. The classic bug is counting the breadth-first level that
  // drains the initial queue as a day, which reports 1 here.
  @Test
  @StdIo({"2 2 1", "1 1", "1 1"})
  void smallestAllRipeBoxNeedsNoDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // One ripe tomato and nothing else: no unripe tomato exists, so the answer is 0 even though the
  // ripe tomato has nowhere to spread.
  @Test
  @StdIo({"2 2 1", "1 -1", "-1 -1"})
  void aLoneRipeTomatoAmongEmptyCellsNeedsNoDays(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- Boards that can never finish. ---

  // The statement guarantees at least one tomato, not one ripe tomato. With no ripe source the
  // unripe tomatoes stay unripe forever; a solver that reads an empty queue as "done" prints 0.
  @Test
  @StdIo({"2 2 1", "0 0", "0 0"})
  void noRipeSourceLeavesUnripeTomatoesForever(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // The ripe and unripe tomatoes touch only across the layer's diagonal; both axis neighbors are
  // empty. An 8-connectivity solver ripens it in one day, but the correct answer is never.
  @Test
  @StdIo({"2 2 1", "1 -1", "-1 0"})
  void inLayerDiagonalContactDoesNotRipen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // The only two tomatoes sit on opposite corners of a 2 x 2 x 2 cube -- the space diagonal. Any
  // solver with 18- or 26-cell connectivity ripens it; six-direction adjacency never does.
  @Test
  @StdIo({"2 2 2", "1 -1", "-1 -1", "-1 -1", "-1 0"})
  void spaceDiagonalContactDoesNotRipen(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // A fully empty middle layer separates the ripe floor from the unripe top: vertical spread must
  // pass through cells, not jump layers, so the top layer never ripens.
  @Test
  @StdIo({"2 2 3", "1 1", "1 1", "-1 -1", "-1 -1", "0 0", "0 0"})
  void anAllEmptyLayerCutsTheStackInTwo(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- Day counting within one layer. ---

  // Exactly one unripe tomato, adjacent to ripe neighbors: one day. Pins the smallest nonzero
  // answer against off-by-one day counting in either direction.
  @Test
  @StdIo({"2 2 1", "1 1", "1 0"})
  void oneAdjacentTomatoRipensInOneDay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A corner source in an obstacle-free 3 x 2 layer: the farthest tomato is the opposite corner
  // at Manhattan distance 2 + 1 = 3 days (day 1 ripens the two neighbors, day 2 the diagonal
  // middle and far top, day 3 the far corner).
  @Test
  @StdIo({"3 2 1", "1 0 0", "0 0 0"})
  void cornerSourceSweepsASingleLayer(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // The left ripe tomato is walled in by empty cells, but only unripe tomatoes must be reached:
  // the right source ripens its column in day 1 and the last tomato in day 2. Guards a solver
  // that demands every tomato be connected instead of every unripe tomato be covered.
  @Test
  @StdIo({"4 2 1", "1 -1 0 1", "-1 -1 0 0"})
  void anIsolatedRipeTomatoNeedsNoRescue(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Ripening crosses stacked layers. ---

  // The only unripe tomato sits directly above the only ripe one; every lateral neighbor is
  // empty. The answer is one day through the vertical axis alone -- a four-direction
  // two-dimensional solver reports -1 here.
  @Test
  @StdIo({"2 2 2", "1 -1", "-1 -1", "0 -1", "-1 -1"})
  void ripenessClimbsStraightUpTheStack(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // A fully ripe bottom box under two unripe boxes: the middle layer ripens on day 1 and the top
  // on day 2. The two-dimensional solver again reports -1, and a solver that stacks boxes on the
  // wrong axis miscounts.
  @Test
  @StdIo({"2 2 3", "1 1", "1 1", "0 0", "0 0", "0 0", "0 0"})
  void aRipeFloorRipensTheWholeStack(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // H = 2 boxes of N = 3 rows with a source in the bottom box's first row and an empty cell in
  // the top box's last row: the farthest tomatoes sit at distance 3 (for example bottom (2,1) and
  // top (1,1)). Reading the H * N rows in any other nesting scrambles which cells are stacked.
  @Test
  @StdIo({"2 3 2", "1 0", "0 0", "0 0", "0 0", "0 0", "0 -1"})
  void rectangularStackReadsWholeLayersInOrder(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  // --- Parsing the box shape. ---

  // M = 4 columns by N = 2 rows with an empty cell beside the source: the spread must detour
  // through the second row, so the far corner ripens on day 5. A solver that swaps M and N (or
  // fills the grid transposed) computes 4 instead.
  @Test
  @StdIo({"4 2 1", "1 -1 0 0", "0 0 0 0"})
  void rectangularLayerReadsWidthThenRowCount(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Simultaneous sources. ---

  // Ripe tomatoes on opposite corners spread toward each other and meet after 2 days -- every
  // cell is within distance 2 of its nearer source. Searching from one source at a time (or
  // seeding later sources at a positive day) reports up to 4.
  @Test
  @StdIo({"5 2 1", "1 0 0 0 0", "0 0 0 0 1"})
  void allRipeSourcesSpreadOnTheSameDay(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- Full-size boards. ---

  // The largest legal box, all unripe except one corner: with no obstacles the breadth-first
  // distance is the Manhattan distance, so the opposite corner ripens on day 99 + 99 + 99 = 297.
  // A million cells force a linear-size search; a per-day full rescan does about 300 million cell
  // visits per surviving day and cannot finish in time.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumBoxRipensCornerToCorner() throws IOException {
    int[][][] box = new int[100][100][100];
    box[0][0][0] = 1;
    assertThat(runMain(boxToInput(100, 100, 100, box))).isEqualTo("297");
  }

  // The largest legal box, fully ripe: the starting front holds a million tomatoes and the answer
  // is still 0. Exercises bulk seeding of the initial queue and the zero-day short circuit.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumBoxAlreadyRipeNeedsNoDays() throws IOException {
    int[][][] box = new int[100][100][100];
    for (int[][] layer : box) {
      for (int[] row : layer) {
        Arrays.fill(row, 1);
      }
    }
    assertThat(runMain(boxToInput(100, 100, 100, box))).isEqualTo("0");
  }

  // The corner opposite the source is sealed behind its three axis neighbors: the search must
  // sweep the entire million-cell box before concluding that one tomato never ripens.
  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void maximumBoxWithASealedCornerNeverFinishes() throws IOException {
    int[][][] box = new int[100][100][100];
    box[0][0][0] = 1;
    box[99][99][98] = -1;
    box[99][98][99] = -1;
    box[98][99][99] = -1;
    assertThat(runMain(boxToInput(100, 100, 100, box))).isEqualTo("-1");
  }

  // --- Sweeps against the simulation oracle. ---

  // Every possible 2 x 2 x 1 and 2 x 2 x 2 box -- 3^4 + 3^8 cell assignments, minus the two
  // all-empty boards the statement rules out. Dense in every verdict: already ripe, unreachable,
  // no source, and every small day count.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void everyTinyBoxMatchesTheSimulationOracle() throws IOException {
    for (int h = 1; h <= 2; h++) {
      int cells = 4 * h;
      int total = 1;
      for (int i = 0; i < cells; i++) {
        total *= 3;
      }
      for (int code = 0; code < total; code++) {
        int[][][] box = new int[h][2][2];
        int rest = code;
        boolean hasTomato = false;
        for (int z = 0; z < h; z++) {
          for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
              box[z][y][x] = rest % 3 - 1;
              rest /= 3;
              hasTomato |= box[z][y][x] >= 0;
            }
          }
        }
        if (!hasTomato) {
          continue; // the statement guarantees at least one tomato.
        }
        assertThat(runMain(boxToInput(2, 2, h, box)))
            .as("h=%d code=%d", h, code)
            .isEqualTo(Integer.toString(simulateDays(box)));
      }
    }
  }

  // Random boxes across mixed shapes (up to 7 x 7 x 5) and densities: each trial draws its own
  // empty-cell and ripe-tomato biases, so the sample spans sparse mazes, source-free boards, and
  // near-solid boxes.
  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void randomBoxesMatchTheSimulationOracle() throws IOException {
    Random rng = new Random(7569L); // fixed seed -> deterministic across JVMs.
    for (int trial = 0; trial < 200; trial++) {
      int m = 2 + rng.nextInt(6);
      int n = 2 + rng.nextInt(6);
      int h = 1 + rng.nextInt(5);
      double emptyBias = rng.nextDouble() * 0.5;
      double ripeBias = rng.nextDouble() * 0.3;
      int[][][] box = new int[h][n][m];
      boolean hasTomato = false;
      for (int z = 0; z < h; z++) {
        for (int y = 0; y < n; y++) {
          for (int x = 0; x < m; x++) {
            if (rng.nextDouble() < emptyBias) {
              box[z][y][x] = -1;
            } else {
              box[z][y][x] = rng.nextDouble() < ripeBias ? 1 : 0;
              hasTomato = true;
            }
          }
        }
      }
      if (!hasTomato) {
        box[0][0][0] = 0; // the statement guarantees at least one tomato.
      }
      assertThat(runMain(boxToInput(m, n, h, box)))
          .as("trial=%d m=%d n=%d h=%d", trial, m, n, h)
          .isEqualTo(Integer.toString(simulateDays(box)));
    }
  }

  /**
   * Transparent oracle: a day-by-day transcription of the statement. Each day it collects every
   * unripe tomato with a ripe axis neighbor and ripens them all at once -- collect-then-apply, so
   * ripeness moves exactly one cell per day -- until a day changes nothing. No queue and no
   * distances, so agreement with a breadth-first solver is real evidence.
   *
   * @implNote {@code O(D * V)} time -- where {@code D} is the number of days until the box
   *     stabilizes and {@code V} is the cell count {@code M * N * H}; each day rescans every cell
   *     against its six neighbors.
   */
  private static int simulateDays(int[][][] box) {
    int h = box.length;
    int n = box[0].length;
    int m = box[0][0].length;
    int[][][] grid = new int[h][n][m];
    for (int z = 0; z < h; z++) {
      for (int y = 0; y < n; y++) {
        grid[z][y] = box[z][y].clone();
      }
    }
    int[][] dirs = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};
    int days = 0;
    while (true) {
      List<int[]> ripening = new ArrayList<>();
      for (int z = 0; z < h; z++) {
        for (int y = 0; y < n; y++) {
          for (int x = 0; x < m; x++) {
            if (grid[z][y][x] != 0) {
              continue;
            }
            for (int[] d : dirs) {
              int nz = z + d[0];
              int ny = y + d[1];
              int nx = x + d[2];
              if (nz >= 0
                  && nz < h
                  && ny >= 0
                  && ny < n
                  && nx >= 0
                  && nx < m
                  && grid[nz][ny][nx] == 1) {
                ripening.add(new int[] {z, y, x});
                break;
              }
            }
          }
        }
      }
      if (ripening.isEmpty()) {
        break;
      }
      for (int[] cell : ripening) {
        grid[cell[0]][cell[1]][cell[2]] = 1;
      }
      days++;
    }
    for (int[][] layer : grid) {
      for (int[] row : layer) {
        for (int cell : row) {
          if (cell == 0) {
            return -1;
          }
        }
      }
    }
    return days;
  }

  private static String boxToInput(int m, int n, int h, int[][][] box) {
    StringBuilder sb = new StringBuilder();
    sb.append(m).append(' ').append(n).append(' ').append(h).append('\n');
    for (int z = 0; z < h; z++) {
      for (int y = 0; y < n; y++) {
        for (int x = 0; x < m; x++) {
          if (x > 0) {
            sb.append(' ');
          }
          sb.append(box[z][y][x]);
        }
        sb.append('\n');
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
