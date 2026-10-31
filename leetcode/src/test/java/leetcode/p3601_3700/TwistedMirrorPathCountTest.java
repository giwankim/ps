package leetcode.p3601_3700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class TwistedMirrorPathCountTest {
  TwistedMirrorPathCount sut = new TwistedMirrorPathCount();

  // ===========================================================================================
  // The floor: 2 x 2 grids (Steps 1-3).
  // ===========================================================================================

  // Step 1: the smallest grid the constraints allow, with a mirror right of the start. Moving
  //         right into it turns the robot down onto the target, so a mirror redirects a path
  //         instead of blocking it. Reading mirrors as obstacles, as in Unique Paths II,
  //         answers 1
  @Test
  void mirrorRightOfStartReflectsDownOntoTarget() {
    assertThat(sut.uniquePaths(new int[][] {{0, 1}, {0, 0}})).isEqualTo(2);
  }

  // Step 2: the mirror of Step 1. Moving down into a mirror turns the robot right, again onto
  //         the target. A solution that turns every reflection down sends this one off the
  //         bottom edge and answers 1, as does the obstacle reading
  @Test
  void mirrorBelowStartReflectsRightOntoTarget() {
    assertThat(sut.uniquePaths(new int[][] {{0, 0}, {1, 0}})).isEqualTo(2);
  }

  // Step 3: both moves reflect onto the target. They are still two paths, because they bounce
  //         off different mirrors. Collecting the landing cells into a set before recursing
  //         answers 1, and the obstacle reading answers 0
  @Test
  void twoMirrorsReflectingOntoOneCellAreTwoPaths() {
    assertThat(sut.uniquePaths(new int[][] {{0, 1}, {1, 0}})).isEqualTo(2);
  }

  // ===========================================================================================
  // Reflection off the edge (Steps 4-5).
  // ===========================================================================================

  // Step 4: a mirror on the bottom row. From row 0, column 1, moving down into it turns the
  //         robot right onto the target. From row 1, column 0, moving right into it turns the
  //         robot down and off the grid, which voids that path. Reading mirrors as empty
  //         answers 3, the plain 2 x 3 count, and the obstacle reading answers 1
  @Test
  void reflectionOffTheBottomEdgeVoidsThePath() {
    assertThat(sut.uniquePaths(new int[][] {{0, 0, 0}, {0, 1, 0}})).isEqualTo(2);
  }

  // Step 5: the mirror of Step 4 on a 3 x 2 grid: moving down into the right-column mirror
  //         turns the robot off the right edge. Steps 4 and 5 together fail a solution that
  //         mixes up m and n in its bounds checks, whichever way round
  @Test
  void reflectionOffTheRightEdgeVoidsThePath() {
    assertThat(sut.uniquePaths(new int[][] {{0, 0}, {0, 1}, {0, 0}})).isEqualTo(2);
  }

  // ===========================================================================================
  // Chained reflections, the Note (Steps 6-9). A mirror turns the robot by the direction it
  // entered that mirror, not by the move it first attempted. The same mirror can therefore send
  // two arrivals to different cells, so the count at a mirror depends on the entry direction.
  // A memo keyed on the cell alone fails each of these four steps, whichever move it tries
  // first.
  // ===========================================================================================

  // Step 6: right into the top mirror turns the robot down into the bottom one. It entered that
  //         one moving down, so it turns right onto the target. The other first move reaches
  //         row 1, column 0, and from there moving right into the same bottom mirror turns it
  //         down and off the grid. Stopping after one reflection answers 0, and so does sliding
  //         on down through the second mirror
  @Test
  void secondReflectionTurnsRightOntoTarget() {
    assertThat(sut.uniquePaths(new int[][] {{0, 1, 0}, {0, 1, 0}})).isEqualTo(1);
  }

  // Step 7: the transpose of Step 6: down into the left mirror, right into the other, down onto
  //         the target. Swapping right and down maps paths to paths, so the count cannot change.
  //         A solution that answers Step 6 but not this one has a row and column mixed up in its
  //         chain walk
  @Test
  void secondReflectionTurnsDownOntoTarget() {
    assertThat(sut.uniquePaths(new int[][] {{0, 0}, {1, 1}, {0, 0}})).isEqualTo(1);
  }

  // Step 8: a chain can still leave the grid. Down into the left mirror turns the robot right
  //         into the middle one, entered moving right, so it turns down and off the bottom
  //         edge. Only the two paths through row 0, column 1 survive. Stopping on the second
  //         mirror as if it were empty, or sliding on right through it, reaches the target and
  //         answers 3
  @Test
  void secondReflectionOffTheBottomEdgeVoidsThePath() {
    assertThat(sut.uniquePaths(new int[][] {{0, 0, 0}, {1, 1, 0}})).isEqualTo(2);
  }

  // Step 9: the transpose of Step 8: right into the top mirror, down into the one below it,
  //         then right and off the edge. The same misreadings answer 3
  @Test
  void secondReflectionOffTheRightEdgeVoidsThePath() {
    assertThat(sut.uniquePaths(new int[][] {{0, 1}, {0, 1}, {0, 0}})).isEqualTo(2);
  }

  // ===========================================================================================
  // Whole-grid patterns (Steps 10-11).
  // ===========================================================================================

  // Step 10: every cell but the two corners is a mirror. Each first move starts a chain that
  //          zigzags down a two-cell-wide staircase, five reflections long, onto the target. So
  //          the two first moves are the two paths. One reflection per move answers 0 when the
  //          next mirror voids the path and 8 when the robot stops on it. Sliding through
  //          mirrors answers 0, and a set of landing cells answers 1, as both chains end on the
  //          same cell
  @Test
  void staircaseOfMirrorsCarriesBothMovesCornerToCorner() {
    int[][] grid = {
      {0, 1, 1, 1},
      {1, 1, 1, 1},
      {1, 1, 1, 1},
      {1, 1, 1, 0}
    };
    assertThat(sut.uniquePaths(grid)).isEqualTo(2);
  }

  // Step 11: mirrors wherever row + column is odd. From each empty diagonal cell, both moves
  //          reflect onto the next diagonal cell, so each of the 3 diagonal steps doubles the
  //          count: 2^3 = 8. A set of landing cells answers 1, the obstacle reading answers 0,
  //          and the empty reading answers 20
  @Test
  void checkerboardDoublesTheCountAtEveryDiagonalStep() {
    int[][] grid = {
      {0, 1, 0, 1},
      {1, 0, 1, 0},
      {0, 1, 0, 1},
      {1, 0, 1, 0}
    };
    assertThat(sut.uniquePaths(grid)).isEqualTo(8);
  }

  // ===========================================================================================
  // LeetCode examples (Steps 12-14).
  // ===========================================================================================

  // Step 12: the robot never stands on a mirror, so nothing passes the one at row 0, column 1
  //          to reach the rest of row 0. That leaves 5 paths where the empty reading counts 6.
  //          Path 5 enters the bottom-left mirror moving down and turns right along the bottom
  //          row. The obstacle reading answers 1
  @Test
  void leetCodeExample1() {
    assertThat(sut.uniquePaths(new int[][] {{0, 1, 0}, {0, 0, 1}, {1, 0, 0}})).isEqualTo(5);
  }

  // Step 13: the baseline. With no mirrors the count is plain Unique Paths, C(2, 1) = 2
  @Test
  void leetCodeExample2() {
    assertThat(sut.uniquePaths(new int[][] {{0, 0}, {0, 0}})).isEqualTo(2);
  }

  // Step 14: each first move starts a two-mirror chain. The right one ends on the target, and
  //          the down one turns down off the bottom edge. Voiding a path at its second mirror
  //          answers 0, and stopping on that mirror as if it were empty answers 2
  @Test
  void leetCodeExample3() {
    assertThat(sut.uniquePaths(new int[][] {{0, 1, 1}, {1, 1, 0}})).isEqualTo(1);
  }

  // ===========================================================================================
  // The modulus (Step 15).
  // ===========================================================================================

  // Step 15: an empty 18 x 18 grid has C(34, 17) = 2,333,606,220 paths. That makes it the
  //          smallest square grid whose count passes both the modulus and Integer.MAX_VALUE.
  //          The 17 x 17 grid has 601,080,390. An int count with no modulus wraps to
  //          -1,961,361,076
  @Test
  void empty18x18PassesTheModulus() {
    assertThat(sut.uniquePaths(new int[18][18])).isEqualTo(333_606_206);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 16-19). A 500 x 500 grid has 250,000 cells: a DP that settles each
  // cell, and each entry direction into a mirror, once finishes in milliseconds. Enumerating
  // paths one at a time never finishes: the empty grid has a 299-digit count and the
  // checkerboard has 2^499 paths.
  // ===========================================================================================

  // Step 16: an empty grid at the size bound has C(998, 499) paths. A long count reduced only
  //          at the end wraps, and Java's % then answers 930,140,548. An int count with no
  //          modulus answers -611,624,576
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void empty500x500() {
    assertThat(sut.uniquePaths(new int[500][500])).isEqualTo(264_223_182);
  }

  // Step 17: Step 11 at the size bound: 2^499 paths, reduced modulo 1e9 + 7. A set of landing
  //          cells answers 1, and the obstacle reading answers 0
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void checkerboard500x500() {
    assertThat(sut.uniquePaths(checkerboard(500))).isEqualTo(695_241_507);
  }

  // Step 18: Step 10 at the size bound: each of the two paths is one move followed by 997
  //          reflections. Stopping on the second mirror as if it were empty answers 695,241,507,
  //          the count from Step 17
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void staircase500x500() {
    assertThat(sut.uniquePaths(staircase(500))).isEqualTo(2);
  }

  // Step 19: 99,999 mirrors in a fixed pattern (see patterned), so plain moves, single
  //          reflections, two-mirror chains and reflections off the edge all mix at full size.
  //          Each misreading from Steps 1-11 answers something else. The obstacle reading
  //          answers 0, and the empty reading answers 264,223,182, the count from Step 16.
  //          Voiding a path at its second mirror answers 50,392,498, and sliding through
  //          mirrors answers 684,464,072
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void patterned500x500() {
    assertThat(sut.uniquePaths(patterned(500, 500))).isEqualTo(703_722_020);
  }

  // ===========================================================================================
  // Hygiene (Step 20).
  // ===========================================================================================

  // Step 20: one instance answers grids of different sizes, the largest in the middle. A table
  //          cached on the instance and sized by the first 2 x 3 grid overruns the 500 x 500
  //          one. A memo map kept across calls returns the first grid's count and answers 1
  //          all four times
  @Test
  void oneInstanceAnswersGridsOfDifferentSizes() {
    assertThat(sut.uniquePaths(new int[][] {{0, 1, 1}, {1, 1, 0}})).isEqualTo(1);
    assertThat(sut.uniquePaths(checkerboard(500))).isEqualTo(695_241_507);
    assertThat(sut.uniquePaths(new int[][] {{0, 1}, {1, 0}})).isEqualTo(2);
    assertThat(sut.uniquePaths(new int[][] {{0, 1, 0}, {0, 0, 1}, {1, 0, 0}})).isEqualTo(5);
  }

  /** A size x size grid with a mirror wherever row + column is odd. */
  private static int[][] checkerboard(int size) {
    int[][] grid = new int[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        grid[i][j] = (i + j) % 2;
      }
    }
    return grid;
  }

  /** A size x size grid of mirrors everywhere except the start and target corners. */
  private static int[][] staircase(int size) {
    int[][] grid = new int[size][size];
    for (int[] row : grid) {
      Arrays.fill(row, 1);
    }
    grid[0][0] = 0;
    grid[size - 1][size - 1] = 0;
    return grid;
  }

  /**
   * An m x n grid with a mirror wherever (row + 2 * column) % 5 < 2, except at the two corners.
   * Moving right into such a mirror from a cell where that value is 3 bounces off a second mirror
   * before landing, so two-mirror chains occur throughout.
   */
  private static int[][] patterned(int m, int n) {
    int[][] grid = new int[m][n];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        grid[i][j] = (i + 2 * j) % 5 < 2 ? 1 : 0;
      }
    }
    grid[0][0] = 0;
    grid[m - 1][n - 1] = 0;
    return grid;
  }
}
