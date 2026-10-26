package leetcode.p1201_1300;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class NumberOfClosedIslandsTest {
  private static final int MAX = 100;

  NumberOfClosedIslands sut = new NumberOfClosedIslands();

  // =============================================================================================
  // The floor, and the rule that the grid's edge is not water (Steps 1-5).
  // =============================================================================================

  // Step 1: the constraint floor, a 1 x 1 grid of land. That cell is an island, but it lies on
  //         all four edges at once, so nothing surrounds it. The Number of Islands habit of
  //         treating the outside of the grid as water counts every island and answers 1
  @Test
  void singleLandCellIsNotClosed() {
    assertThat(sut.closedIsland(new int[][] {{0}})).isZero();
  }

  // Step 2: the other 1 x 1 grid. Here 0 is land and 1 is water, the reverse of LeetCode 200.
  //         Carrying that problem's convention over counts the water cell as an island and
  //         answers 1
  @Test
  void singleWaterCellHasNoIsland() {
    assertThat(sut.closedIsland(new int[][] {{1}})).isZero();
  }

  // Step 3: the smallest closed island. It needs a ring of water on all four sides, so no grid
  //         narrower than three rows or three columns can hold one. Reading 1 as land and keeping
  //         the edge check answers 0 here, because the ring of 1s touches every edge
  @Test
  void landCellRingedByWaterIsClosed() {
    int[][] grid = {
      {1, 1, 1},
      {1, 0, 1},
      {1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(1);
  }

  // Step 4: one row. Each land cell has water to its left and right, but the grid's edge above
  //         and below it is not water, so neither island is closed. Counting every island, or
  //         excluding only islands that touch one of the four corners, answers 2
  @Test
  void singleRowHasNoClosedIsland() {
    assertThat(sut.closedIsland(new int[][] {{1, 0, 1, 0, 1}})).isZero();
  }

  // Step 5: the mirror of Step 4, one column. Water above and below each land cell, the grid's
  //         edge to its left and right. Besides the answer of 2 from counting every island, a
  //         solution that bounds the column loop by grid.length reads past the end of each
  //         one-cell row
  @Test
  void singleColumnHasNoClosedIsland() {
    assertThat(sut.closedIsland(new int[][] {{1}, {0}, {1}, {0}, {1}})).isZero();
  }

  // =============================================================================================
  // Each edge of the grid opens an island that touches it (Steps 6-9). Every grid holds one
  // closed single cell and one open two-cell bar that meets exactly one edge, away from the
  // corners, so skipping that edge answers 2.
  // =============================================================================================

  // Step 6: the bar meets the top edge in the middle of row 0. LeetCode's first hint says
  //         corners where it means the whole border, and excluding only islands that cover a
  //         corner answers 2 on all four of these grids
  @Test
  void islandTouchingTheTopEdgeIsOpen() {
    int[][] grid = {
      {1, 1, 0, 1, 1},
      {1, 1, 0, 1, 1},
      {1, 1, 1, 1, 1},
      {1, 1, 0, 1, 1},
      {1, 1, 1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(1);
  }

  // Step 7: the mirror of Step 6 at the bottom edge. The bar's first cell in row-major order,
  //         row 3, is interior, and only the cell below it is on the edge. Testing row m rather
  //         than row m - 1, or judging the island by the cell its search started from, answers 2
  @Test
  void islandTouchingTheBottomEdgeIsOpen() {
    int[][] grid = {
      {1, 1, 1, 1, 1},
      {1, 1, 0, 1, 1},
      {1, 1, 1, 1, 1},
      {1, 1, 0, 1, 1},
      {1, 1, 0, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(1);
  }

  // Step 8: the bar meets the left edge in the middle of column 0. A solution that checks the
  //         top and bottom rows but not the side columns answers 2
  @Test
  void islandTouchingTheLeftEdgeIsOpen() {
    int[][] grid = {
      {1, 1, 1, 1, 1},
      {1, 1, 1, 1, 1},
      {0, 0, 1, 0, 1},
      {1, 1, 1, 1, 1},
      {1, 1, 1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(1);
  }

  // Step 9: the mirror of Step 8 at the right edge. As in Step 7, the bar's first cell, column
  //         3, is interior. Testing column n rather than column n - 1, or judging the island by
  //         its first cell, answers 2
  @Test
  void islandTouchingTheRightEdgeIsOpen() {
    int[][] grid = {
      {1, 1, 1, 1, 1},
      {1, 1, 1, 1, 1},
      {1, 0, 1, 0, 0},
      {1, 1, 1, 1, 1},
      {1, 1, 1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(1);
  }

  // =============================================================================================
  // What one island is (Steps 10-14).
  // =============================================================================================

  // Step 10: a closed island of three cells in a wide grid counts once. Counting closed land
  //          cells answers 3. Bounding the column loop by grid.length stops at column 2, takes it
  //          for the right edge, and answers 0
  @Test
  void horizontalIslandInWideGridCountsOnce() {
    int[][] grid = {
      {1, 1, 1, 1, 1},
      {1, 0, 0, 0, 1},
      {1, 1, 1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(1);
  }

  // Step 11: the mirror of Step 10 in a tall grid, where the same grid.length mix-up reads past
  //          the end of each three-cell row instead of stopping short
  @Test
  void verticalIslandInTallGridCountsOnce() {
    int[][] grid = {
      {1, 1, 1},
      {1, 0, 1},
      {1, 0, 1},
      {1, 0, 1},
      {1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(1);
  }

  // Step 12: connection is 4-directional. The two land cells touch only at a corner, so they
  //          are two islands, each closed. Following diagonals merges them and answers 1
  @Test
  void cellsTouchingOnlyAtACornerAreSeparateIslands() {
    int[][] grid = {
      {1, 1, 1, 1},
      {1, 0, 1, 1},
      {1, 1, 0, 1},
      {1, 1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(2);
  }

  // Step 13: the other half of Step 12. The center cell touches the open corner island only
  //          diagonally, so it stays closed. Following diagonals pulls it into the open island
  //          and answers 0
  @Test
  void cornerContactWithAnOpenIslandLeavesAClosedOneClosed() {
    int[][] grid = {
      {0, 1, 1},
      {1, 0, 1},
      {1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(1);
  }

  // Step 14: an island is open as a whole, including the cells a search has not reached when it
  //          first meets the edge. Both islands here are open, and each has an interior pocket
  //          beside the edge cell where a row-order scan starts it, whose other land neighbor is
  //          also on the edge. A recursive search that joins its four calls with a short-circuit
  //          && stops at the first edge contact, so it clears the top pocket only if it tries
  //          down before right, and the left pocket only if it tries right before down. The
  //          leftover pocket is later found walled in by visited cells and counted as closed:
  //          every neighbor order answers 1 or 2, and a BFS that returns at the first edge cell
  //          it dequeues answers 2
  @Test
  void pocketsOfOpenIslandsAreNotClosedIslands() {
    int[][] grid = {
      {1, 0, 0, 1, 1},
      {1, 0, 1, 1, 1},
      {1, 1, 1, 1, 1},
      {0, 0, 1, 1, 1},
      {0, 1, 1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isZero();
  }

  // =============================================================================================
  // The official examples (Steps 15-17).
  // =============================================================================================

  // Step 15: LeetCode Example 1 — four islands, two of them closed: the large one on the left
  //          and the single cell at row 3, column 6. The other two, the top three cells of
  //          column 7 and the bottom-right corner cell, touch the right edge. Counting every
  //          island answers 4, and counting closed land cells answers 11
  @Test
  void leetCodeExample1() {
    int[][] grid = {
      {1, 1, 1, 1, 1, 1, 1, 0},
      {1, 0, 0, 0, 0, 1, 1, 0},
      {1, 0, 1, 0, 1, 1, 1, 0},
      {1, 0, 0, 0, 0, 1, 0, 1},
      {1, 1, 1, 1, 1, 1, 1, 0}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(2);
  }

  // Step 16: LeetCode Example 2 — the closed cell at row 1, column 2 sits in the embrace of two
  //          open islands that run down the left and right edges. Counting every island answers
  //          3. Following diagonals joins the closed cell to both open islands and answers 0
  @Test
  void leetCodeExample2() {
    int[][] grid = {
      {0, 0, 1, 0, 0},
      {0, 1, 0, 1, 0},
      {0, 1, 1, 1, 0}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(1);
  }

  // Step 17: LeetCode Example 3 — a closed ring of land around a lake, and a closed island in
  //          that lake. Nested islands count separately, so the answer is 2. Reading 1 as land
  //          with the edge check kept finds one closed water ring and answers 1
  @Test
  void leetCodeExample3() {
    int[][] grid = {
      {1, 1, 1, 1, 1, 1, 1},
      {1, 0, 0, 0, 0, 0, 1},
      {1, 0, 1, 1, 1, 0, 1},
      {1, 0, 1, 0, 1, 0, 1},
      {1, 0, 1, 1, 1, 0, 1},
      {1, 0, 0, 0, 0, 0, 1},
      {1, 1, 1, 1, 1, 1, 1}
    };
    assertThat(sut.closedIsland(grid)).isEqualTo(2);
  }

  // =============================================================================================
  // Constraint bounds, 1 <= m, n <= 100 (Steps 18-22). At most 10^4 cells, so a flood fill that
  // marks each cell once finishes in milliseconds, and the timeouts exist for searches that
  // never settle: a DFS that unmarks cells on the way back out enumerates every simple path
  // through the open interior, and a BFS that marks nothing re-enqueues cells around every
  // cycle. A recursive flood can nest up to 10^4 calls deep on these grids.
  // =============================================================================================

  // Step 18: the largest island possible, all 10^4 cells, and it covers every edge. Counting
  //          every island answers 1
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxGridOfAllLandIsOneOpenIsland() {
    assertThat(sut.closedIsland(filled(0))).isZero();
  }

  // Step 19: the largest closed island possible, the 98 x 98 interior inside a one-cell frame of
  //          water. Counting closed land cells answers 9604
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxGridFramedLandIsOneClosedIsland() {
    assertThat(sut.closedIsland(framed())).isEqualTo(1);
  }

  // Step 20: the most islands possible, a checkerboard with land wherever row + column is odd.
  //          Every land cell is its own island, and the 4802 off the edge are closed. Counting
  //          every island answers 5000. Following diagonals fuses the whole board into one island
  //          that touches the edge and answers 0
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxGridCheckerboardHas4802ClosedIslands() {
    assertThat(sut.closedIsland(checkerboard())).isEqualTo(4802);
  }

  // Step 21: a single 4850-cell serpentine path that sweeps 49 rows of the interior, joined by
  //          one-cell links that alternate between the right and left ends. Sealed by water on
  //          every side, it is one closed island
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxGridSealedSerpentineIsOneClosedIsland() {
    assertThat(sut.closedIsland(serpentine(false))).isEqualTo(1);
  }

  // Step 22: Step 21 with one more land cell, at row 97, column 99, where the path's far end
  //          breaks through the right edge. A search that starts from the path's top-left end
  //          meets the edge only at its last cell, 4850 cells later. Judging the island by the
  //          cell its search started from answers 1
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxGridSerpentineOpenOnlyAtItsFarEndIsNotClosed() {
    assertThat(sut.closedIsland(serpentine(true))).isZero();
  }

  // =============================================================================================
  // Hygiene (Step 23).
  // =============================================================================================

  // Step 23: one instance answers grids of several sizes, deliberately out of order with the
  //          largest in the middle. A visited array or dimension kept in an instance field and
  //          sized by the first call breaks on the second. Each call gets a fresh grid, so
  //          sinking visited land in place is allowed
  @Test
  void oneInstanceAnswersSeveralGridsInAnyOrder() {
    assertThat(sut.closedIsland(new int[][] {
          {1, 1, 1, 1, 1, 1, 1, 0},
          {1, 0, 0, 0, 0, 1, 1, 0},
          {1, 0, 1, 0, 1, 1, 1, 0},
          {1, 0, 0, 0, 0, 1, 0, 1},
          {1, 1, 1, 1, 1, 1, 1, 0}
        }))
        .isEqualTo(2);
    assertThat(sut.closedIsland(checkerboard())).isEqualTo(4802);
    assertThat(sut.closedIsland(new int[][] {{0}})).isZero();
    assertThat(sut.closedIsland(new int[][] {
          {1, 1, 1},
          {1, 0, 1},
          {1, 1, 1}
        }))
        .isEqualTo(1);
  }

  private static int[][] filled(int value) {
    int[][] grid = new int[MAX][MAX];
    for (int[] row : grid) {
      Arrays.fill(row, value);
    }
    return grid;
  }

  private static int[][] framed() {
    int[][] grid = filled(1);
    for (int r = 1; r < MAX - 1; r++) {
      Arrays.fill(grid[r], 1, MAX - 1, 0);
    }
    return grid;
  }

  private static int[][] checkerboard() {
    int[][] grid = new int[MAX][MAX];
    for (int r = 0; r < MAX; r++) {
      for (int c = 0; c < MAX; c++) {
        grid[r][c] = (r + c + 1) % 2;
      }
    }
    return grid;
  }

  // Land fills rows 1, 3, ..., 97 from column 1 to 98. Row 2k links them at column 98 for odd k
  // and at column 1 for even k, so the path runs from row 1, column 1 to row 97, column 98.
  private static int[][] serpentine(boolean openFarEnd) {
    int[][] grid = filled(1);
    for (int r = 1; r < MAX - 2; r += 2) {
      Arrays.fill(grid[r], 1, MAX - 1, 0);
    }
    for (int r = 2; r < MAX - 3; r += 2) {
      grid[r][(r / 2) % 2 == 1 ? MAX - 2 : 1] = 0;
    }
    if (openFarEnd) {
      grid[MAX - 3][MAX - 1] = 0;
    }
    return grid;
  }
}
