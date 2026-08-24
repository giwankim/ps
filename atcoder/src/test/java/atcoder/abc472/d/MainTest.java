package atcoder.abc472.d;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * AtCoder ABC 472 D -- Bomber Mad.
 *
 * <p>Line 1 holds H, W and K (1 &le; H, W &le; 5 x 10^5; H x W &le; 5 x 10^5; 0 &le; K &le; H x W -
 * 1); the next H lines hold the grid, {@code .} for an empty cell and {@code #} for a bomb cell. An
 * empty cell is <em>safe</em> when its whole row and its whole column are free of bombs. A move
 * steps to an orthogonally adjacent empty cell -- bomb cells are never entered. Print how many
 * empty cells can reach some safe empty cell in at most K moves.
 *
 * <p>The shape of the answer is a multi-source BFS seeded with every safe cell, counting the empty
 * cells whose distance is at most K. The tests below separate the ways that goes wrong.
 *
 * <ul>
 *   <li><b>Safety needs the row <em>and</em> the column.</b> A clean row alone does not do it
 *       ({@link #aCleanRowIsNotEnoughOnItsOwn}, {@link #safetyNeedsBothTheRowAndTheColumnClean}),
 *       and the bomb that spoils a row may sit at the far end of it rather than next door
 *       ({@link #aBombAtTheFarEndStillDirtiesItsRow}). When no row and column are both clean there
 *       is no source at all and the answer is 0 ({@link #aGridWithNoSafeCellAnswersZero}).
 *   <li><b>The search is a BFS through empty cells.</b> Bombs are walls, so a wall can push the
 *       true distance past the Manhattan distance
 *       ({@link #wallsForceADetourLongerThanTheManhattanDistance}), diagonal neighbors cost two
 *       moves ({@link #theDiagonalCellsJoinOnlyAtTwoMoves}), and a cell sealed in by bombs is never
 *       counted however large K grows
 *       ({@link #aCellSealedOffByBombsIsNeverCountedEvenAtTheLargestK} -- the trap for an unreached
 *       distance left sitting at its 0 default).
 *   <li><b>Every safe cell is a source.</b> Seeding the queue with one of them and walking outward
 *       measures the wrong distance ({@link #theNearestSafeCellWinsNotTheFirstOne}).
 *   <li><b>"At most K" is inclusive.</b> A cell exactly K moves out is counted
 *       ({@link #aCellExactlyKMovesAwayIsCounted}) and one move further is not
 *       ({@link #aCellOneMoveBeyondKIsNotCounted}); at K = 0 only the safe cells themselves survive
 *       ({@link #whenKIsZeroOnlyTheSafeCellsThemselvesCount}).
 *   <li><b>Only empty cells are counted.</b> A bomb is never part of the answer
 *       ({@link #bombCellsAreNeverCountedThemselves}).
 * </ul>
 *
 * <p>H and W each range up to 5 x 10^5 on their own and are only jointly bounded by H x W, so the
 * grid may arrive as one enormous row or as half a million one-character rows; both extremes are
 * driven through {@link #runMain(String)}, along with a quarter-million-cell corridor -- the
 * deepest distance chain the constraints allow -- that a per-cell search cannot finish. Randomized
 * grids are cross-checked against {@link #oracle}, which runs a separate depth-limited BFS out of
 * every empty cell instead of one sweep inward from the safe cells.
 */
class MainTest {

  // --- Official samples. ---

  @Test
  @StdIo({"3 3 1", "#..", "...", "..#"})
  void officialSampleOneCountsTheCrossAroundTheLoneSafeCell(StdOut out) throws IOException {
    // Row 2 and column 2 are the only bomb-free ones, so (2,2) is the single safe cell and the
    // four cells orthogonally around it join at one move.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"2 3 0", "...", "..."})
  void officialSampleTwoTreatsABombFreeGridAsEntirelySafe(StdOut out) throws IOException {
    // No bombs at all, so every row and every column is clean and all six cells are safe -- K = 0
    // still admits every one of them.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  @Test
  @StdIo({"5 7 2", "..#....", "..#....", ".......", "...#...", "...#..."})
  void officialSampleThreeHandlesTwoSeparateBombColumns(StdOut out) throws IOException {
    // Row 3 is the only clean row and columns 3 and 4 are the only dirty ones, leaving five safe
    // cells. Exactly two of the 31 empty cells -- (1,4) and (5,3) -- sit three moves out.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("29");
  }

  // --- Safety needs a clean row AND a clean column. ---

  @Test
  @StdIo({"2 2 0", "..", ".#"})
  void aCleanRowIsNotEnoughOnItsOwn(StdOut out) throws IOException {
    // Row 1 is clean but column 2 carries the bomb, so (1,2) is not safe; only (1,1) sits on a
    // clean row and a clean column. Reading the rule as "clean row or clean column" answers 3.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"3 3 0", "...", "..#", "..."})
  void safetyNeedsBothTheRowAndTheColumnClean(StdOut out) throws IOException {
    // One bomb spoils row 2 and column 3 at once, leaving rows {1,3} x columns {1,2} = four safe
    // cells. The "or" reading answers 8.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"3 5 0", "....#", ".....", "....."})
  void aBombAtTheFarEndStillDirtiesItsRow(StdOut out) throws IOException {
    // The bomb sits in the last column, four cells away from (1,1), and still rules out every
    // cell of row 1. Checking only a cell's neighbors would call (1,1) safe.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  @Test
  @StdIo({"2 2 3", "#.", ".#"})
  void aGridWithNoSafeCellAnswersZero(StdOut out) throws IOException {
    // Both rows carry a bomb, so the BFS has no source. The two empty cells are still empty and
    // still reachable from each other -- an unreached distance left at its 0 default counts them.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"2 2 3", "##", "##"})
  void aGridOfNothingButBombsAnswersZero(StdOut out) throws IOException {
    // Nothing to count and nothing to walk on.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  // --- "At most K" is inclusive, and K = 0 admits only the sources. ---

  @Test
  @StdIo({"3 3 0", "#..", "...", "..#"})
  void whenKIsZeroOnlyTheSafeCellsThemselvesCount(StdOut out) throws IOException {
    // Sample 1's grid with no moves allowed: the answer collapses to the number of safe cells.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"4 2 2", ".#", ".#", ".#", ".."})
  void aCellExactlyKMovesAwayIsCounted(StdOut out) throws IOException {
    // Column 1 is clean and row 4 is clean, so (4,1) is the only safe cell and column 1 is a
    // corridor climbing away from it: (3,1) at 1, (2,1) at 2, (1,1) at 3, with (4,2) at 1. At
    // K = 2 the four cells through distance 2 count, (2,1) landing exactly on the bound.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"4 2 1", ".#", ".#", ".#", ".."})
  void aCellOneMoveBeyondKIsNotCounted(StdOut out) throws IOException {
    // The same corridor one step tighter: (2,1) now sits one move past K and drops out.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("3");
  }

  @Test
  @StdIo({"4 2 3", ".#", ".#", ".#", ".."})
  void theWholeCorridorFitsOnceKReachesItsFarEnd(StdOut out) throws IOException {
    // K = 3 reaches (1,1), the deepest cell, so all five empty cells count.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  // --- Bombs are walls: the metric is BFS distance, not Manhattan distance. ---

  @Test
  @StdIo({"6 5 4", ".....", ".###.", ".#.#.", ".#.#.", ".#.#.", "....."})
  void wallsForceADetourLongerThanTheManhattanDistance(StdOut out) throws IOException {
    // Columns 1 and 5 and rows 1 and 6 are clean, giving four safe cells at the corners. The
    // cell (3,3) sits at the top of a blind shaft walled in on three sides: it must descend to
    // row 6 and turn, five moves, though the nearest corner is four away as the crow flies. At
    // K = 4 it is the one empty cell of 21 left out; a Manhattan test answers 21.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  @Test
  @StdIo({"6 5 5", ".....", ".###.", ".#.#.", ".#.#.", ".#.#.", "....."})
  void theSameDetourFitsOneMoveLater(StdOut out) throws IOException {
    // One more move buys the shaft's dead end, so every empty cell counts.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("21");
  }

  @Test
  @StdIo({"3 3 2", "#..", "...", "..#"})
  void theDiagonalCellsJoinOnlyAtTwoMoves(StdOut out) throws IOException {
    // Sample 1's grid with one more move. (1,3) and (3,1) are diagonal from the safe (2,2) and
    // cost two moves, not one -- they are exactly what sample 1 leaves out at K = 1.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({"5 5 24", ".....", "..#..", ".#.#.", "..#..", "....."})
  void aCellSealedOffByBombsIsNeverCountedEvenAtTheLargestK(StdOut out) throws IOException {
    // Four bombs box (3,3) in on all four sides. K is at its ceiling of H x W - 1, so every cell
    // the BFS can reach is counted -- 20 of the 21 empty cells. The sealed cell is unreachable
    // rather than distant, so a distance array left at 0 for unvisited cells answers 21, and a
    // Manhattan test answers 21 as well.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("20");
  }

  @Test
  @StdIo({"3 3 8", "...", ".#.", "..."})
  void bombCellsAreNeverCountedThemselves(StdOut out) throws IOException {
    // The eight cells of the ring are all reachable from the four safe corners, but the bomb at
    // the center is not one of them: 8, not 9.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("8");
  }

  // --- Every safe cell seeds the search. ---

  @Test
  @StdIo({"3 5 1", ".....", ".###.", "....."})
  void theNearestSafeCellWinsNotTheFirstOne(StdOut out) throws IOException {
    // Rows 1 and 3 and columns 1 and 5 are clean, so all four corners are safe. Ten of the 12
    // empty cells lie within one move of *some* corner; walking out from a single seed reaches
    // only 3.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  @StdIo({"3 5 2", ".....", ".###.", "....."})
  void theFrontiersFromEveryCornerMeetInTheMiddle(StdOut out) throws IOException {
    // One more move picks up (1,3) and (3,3), the two cells farthest from any corner.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("12");
  }

  // --- The 1 x 1 floor and the degenerate row and column shapes. ---

  @Test
  @StdIo({"1 1 0", "."})
  void theSmallestGridCountsItsOnlyEmptyCell(StdOut out) throws IOException {
    // H, W and K all at their floors.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1 1 0", "#"})
  void theSmallestGridCountsNothingWhenItHoldsABomb(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"1 5 0", "....."})
  void aSingleBombFreeRowIsEntirelySafe(StdOut out) throws IOException {
    // With one row, every column is a single cell: all five are clean and all five are safe.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"1 5 4", "..#.."})
  void aSingleRowWithABombHasNoSafeCellAtAll(StdOut out) throws IOException {
    // One bomb spoils the only row, so nothing is safe no matter that four empty cells remain
    // and K sits at its ceiling.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"5 1 0", ".", ".", ".", ".", "."})
  void aSingleBombFreeColumnIsEntirelySafe(StdOut out) throws IOException {
    // The transpose of the single-row case: W = 1 with every row clean.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({"5 1 4", ".", ".", "#", ".", "."})
  void aSingleColumnWithABombHasNoSafeCellAtAll(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"2 3 0", "...", "..#"})
  void theGridIsRectangularAndHMustNotBeReadAsW(StdOut out) throws IOException {
    // H = 2 rows of W = 3. Row 1 is clean, columns 1 and 2 are clean, so (1,1) and (1,2) are
    // safe. Swapping H and W reads a third grid line that is not there.
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  // --- H x W at its ceiling of 5 x 10^5, in each of the shapes the constraints allow. ---

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void theLargestSquareGridIsAnsweredWithinTheTimeLimit() throws IOException {
    // 707 x 707 = 499849 cells, no bombs, K = 0: every cell is safe. Deciding safety by
    // re-scanning a cell's row and column per cell is 7 x 10^8 character reads.
    int n = 707;
    String[] grid = new String[n];
    java.util.Arrays.fill(grid, ".".repeat(n));

    assertThat(runMain(buildInput(0, grid))).isEqualTo("499849");
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void theLongestCorridorIsWalkedWithoutTimingOut() throws IOException {
    // H = 2, W = 250000. Row 1 is clean and column 1 is clean, so (1,1) is the only safe cell,
    // and row 2 is a wall behind its first cell. Row 1 is therefore a corridor 250000 long whose
    // distances run 0, 1, 2, ...: the deepest chain the constraints allow, and the shape that
    // punishes searching outward from every empty cell in turn rather than inward once.
    // At K = 100000 the cells (1,1) through (1,100001) count, plus (2,1) one move down.
    int w = 250_000;
    String[] grid = {".".repeat(w), "." + "#".repeat(w - 1)};

    assertThat(runMain(buildInput(100_000, grid))).isEqualTo("100002");
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void theTallestGridReadsHalfAMillionSingleCharacterRows() throws IOException {
    // H = 5 x 10^5 with W = 1: the extreme the H bound allows on its own. No bombs, so every
    // cell is safe and K = 0 admits all of them.
    int h = 500_000;
    String[] grid = new String[h];
    java.util.Arrays.fill(grid, ".");

    assertThat(runMain(buildInput(0, grid))).isEqualTo("500000");
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void theWidestGridReadsOneHalfMillionCharacterRow() throws IOException {
    // The transpose: H = 1 with W = 5 x 10^5 arriving as a single line, which a tokenizer that
    // splits per character rather than per line will crawl through.
    String[] grid = {".".repeat(500_000)};

    assertThat(runMain(buildInput(0, grid))).isEqualTo("500000");
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void theWidestGridWithOneBombHasNoSafeCellAtAll() throws IOException {
    // A single bomb in the middle of that line spoils the only row, so the answer is 0 even with
    // K at its ceiling -- the search must not walk the whole row looking for a source it can
    // rule out up front.
    String[] grid = {".".repeat(249_999) + "#" + ".".repeat(250_000)};

    assertThat(runMain(buildInput(499_999, grid))).isEqualTo("0");
  }

  // --- Randomized cross-check against the definitional oracle. ---

  @Test
  void randomSmallGridsMatchTheOracle() throws IOException {
    assertRandomGridsMatchTheOracle(472_001L, 6, 0.25);
  }

  @Test
  void randomSparseGridsMatchTheOracle() throws IOException {
    // Few bombs, so clean rows and columns are common and most grids carry many safe cells.
    assertRandomGridsMatchTheOracle(472_002L, 7, 0.08);
  }

  @Test
  void randomDenseGridsMatchTheOracle() throws IOException {
    // Bombs on half the cells, so safe cells are rare, empty cells are often walled apart, and
    // many grids have no source at all.
    assertRandomGridsMatchTheOracle(472_003L, 6, 0.5);
  }

  /** Runs 400 random grids of side at most {@code maxSide} past {@link #oracle}. */
  private static void assertRandomGridsMatchTheOracle(long seed, int maxSide, double bombRate)
      throws IOException {
    Random rng = new Random(seed); // fixed seed -> reproducible
    for (int trial = 0; trial < 400; trial++) {
      int h = 1 + rng.nextInt(maxSide);
      int w = 1 + rng.nextInt(maxSide);
      String[] grid = new String[h];
      for (int i = 0; i < h; i++) {
        StringBuilder row = new StringBuilder(w);
        for (int j = 0; j < w; j++) {
          row.append(rng.nextDouble() < bombRate ? '#' : '.');
        }
        grid[i] = row.toString();
      }
      int k = rng.nextInt(h * w); // 0 <= K <= H * W - 1

      assertThat(runMain(buildInput(k, grid)))
          .as("H=%d W=%d K=%d grid=%s", h, w, k, String.join("/", grid))
          .isEqualTo(String.valueOf(oracle(k, grid)));
    }
  }

  /** Renders one grid in the input format: {@code H W K} on the first line, then the H rows. */
  private static String buildInput(int k, String[] grid) {
    StringBuilder sb = new StringBuilder();
    sb.append(grid.length)
        .append(' ')
        .append(grid[0].length())
        .append(' ')
        .append(k)
        .append('\n');
    for (String row : grid) {
      sb.append(row).append('\n');
    }
    return sb.toString();
  }

  /**
   * Independent oracle: replays the definition cell by cell. For each empty cell it runs its own
   * BFS outward, stopping after K rings, and asks whether any cell it has touched is safe --
   * deciding safety by rescanning that cell's row and column for a bomb. It never seeds a queue
   * with the safe cells and never sweeps inward, so agreement with {@link Main} is a genuine
   * cross-check of the multi-source distance rather than the same sweep run twice.
   *
   * @implNote {@code O((H * W)^2 * (H + W))} time and {@code O(H * W)} space, where {@code H =
   *     grid.length} is the number of rows and {@code W} the number of columns; callers must keep
   *     the grid tiny.
   */
  private static int oracle(int k, String[] grid) {
    int h = grid.length;
    int w = grid[0].length();
    int[] dr = {1, -1, 0, 0};
    int[] dc = {0, 0, 1, -1};
    int ans = 0;
    for (int sr = 0; sr < h; sr++) {
      for (int sc = 0; sc < w; sc++) {
        if (grid[sr].charAt(sc) != '.') {
          continue;
        }
        boolean[][] seen = new boolean[h][w];
        seen[sr][sc] = true;
        Deque<int[]> frontier = new ArrayDeque<>();
        frontier.add(new int[] {sr, sc});
        boolean found = isSafe(grid, sr, sc);
        for (int step = 0; !found && step < k && !frontier.isEmpty(); step++) {
          Deque<int[]> next = new ArrayDeque<>();
          for (int[] cell : frontier) {
            for (int d = 0; d < 4; d++) {
              int nr = cell[0] + dr[d];
              int nc = cell[1] + dc[d];
              if (nr < 0 || nr >= h || nc < 0 || nc >= w) {
                continue;
              }
              if (grid[nr].charAt(nc) != '.' || seen[nr][nc]) {
                continue;
              }
              seen[nr][nc] = true;
              next.add(new int[] {nr, nc});
              found |= isSafe(grid, nr, nc);
            }
          }
          frontier = next;
        }
        if (found) {
          ans++;
        }
      }
    }
    return ans;
  }

  /**
   * Safety straight from the definition: an empty cell whose whole row and column carry no bomb.
   */
  private static boolean isSafe(String[] grid, int r, int c) {
    if (grid[r].charAt(c) != '.') {
      return false;
    }
    if (grid[r].indexOf('#') >= 0) {
      return false;
    }
    for (String row : grid) {
      if (row.charAt(c) == '#') {
        return false;
      }
    }
    return true;
  }

  /** Drives {@link Main} over stdin/stdout for grids too large to spell out in {@code @StdIo}. */
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
