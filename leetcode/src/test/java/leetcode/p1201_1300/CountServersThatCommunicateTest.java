package leetcode.p1201_1300;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import java.util.function.IntBinaryOperator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CountServersThatCommunicateTest {
  CountServersThatCommunicate sut = new CountServersThatCommunicate();

  // ===========================================================================================
  // The floor and the meaning of "communicate" (Steps 1-6).
  // ===========================================================================================

  // Step 1: smallest valid input (1 <= m, 1 <= n). A single empty cell holds no server, so there is
  //         nothing to count
  @Test
  void singleEmptyCellCountsNothing() {
    assertThat(sut.countServers(new int[][] {{0}})).isZero();
  }

  // Step 2: a single server has no one to talk to. A solution that counts every server, or whose
  //         threshold is "row or column holds at least one server" instead of "more than one",
  //         answers 1
  @Test
  void loneServerDoesNotCommunicate() {
    assertThat(sut.countServers(new int[][] {{1}})).isZero();
  }

  // Step 3: communication runs along the whole row, not just to neighbors. The two servers sit at
  //         opposite ends with empty cells between them, so the answer is 2. A flood fill over
  //         adjacent cells answers 0
  @Test
  void twoServersAtOppositeEndsOfARowCommunicate() {
    assertThat(sut.countServers(new int[][] {{1, 0, 0, 1}})).isEqualTo(2);
  }

  // Step 4: the mirror of Step 3 for a column. A solution that only checks rows answers 0 here,
  //         and a solution that only checks columns answers 0 for Step 3
  @Test
  void twoServersAtOppositeEndsOfAColumnCommunicate() {
    assertThat(sut.countServers(new int[][] {{1}, {0}, {0}, {1}})).isEqualTo(2);
  }

  // Step 5: the answer counts servers, not communicating pairs. Four servers in one row form six
  //         pairs, so a solution that counts pairs answers 6 instead of 4
  @Test
  void fullRowCountsServersNotPairs() {
    assertThat(sut.countServers(new int[][] {{1, 1, 1, 1}})).isEqualTo(4);
  }

  // Step 6: an empty grid larger than one cell. Every row and column count is 0
  @Test
  void allEmptyGridCountsNothing() {
    assertThat(sut.countServers(new int[][] {{0, 0, 0}, {0, 0, 0}})).isZero();
  }

  // ===========================================================================================
  // Rows and columns together (Steps 7-12).
  // ===========================================================================================

  // Step 7: a row pair plus a server that shares neither its row nor its column with anyone. Only
  //         the pair counts, so the answer is 2, not 3
  @Test
  void rowPairCountsButIsolatedServerDoesNot() {
    assertThat(sut.countServers(new int[][] {{1, 1, 0}, {0, 0, 0}, {0, 0, 1}})).isEqualTo(2);
  }

  // Step 8: the top-left server communicates along its row and along its column, and it is still
  //         one server. Adding up "servers in busy rows" (2) and "servers in busy columns" (2)
  //         counts it twice and answers 4 instead of 3
  @Test
  void serverBusyInBothRowAndColumnIsCountedOnce() {
    assertThat(sut.countServers(new int[][] {{1, 1}, {1, 0}})).isEqualTo(3);
  }

  // Step 9: a plus shape. The center row and center column each hold 3 servers and share the
  //         center cell. All 5 servers communicate. Adding up rows and columns counts the center
  //         twice and answers 6
  @Test
  void plusShapeCountsSharedCenterOnce() {
    assertThat(sut.countServers(new int[][] {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}})).isEqualTo(5);
  }

  // Step 10: a 2x2 block of servers plus a bottom-right server that is diagonally adjacent to the
  //          block but shares no row or column with it. The block counts (4). The corner server
  //          does not. An 8-direction neighbor check answers 5, and adding up rows and columns
  //          answers 8
  @Test
  void diagonalNeighborOfABlockStaysIsolated() {
    assertThat(sut.countServers(new int[][] {{1, 1, 0}, {1, 1, 0}, {0, 0, 1}})).isEqualTo(4);
  }

  // Step 11: a permutation grid. Every row and every column is occupied by exactly one server, so
  //          no one communicates. A solution that asks "is this row occupied" instead of "does
  //          this row hold another server" answers 4
  @Test
  void permutationGridHasNoCommunication() {
    int[][] grid = {{0, 1, 0, 0}, {0, 0, 0, 1}, {1, 0, 0, 0}, {0, 0, 1, 0}};
    assertThat(sut.countServers(grid)).isZero();
  }

  // Step 12: non-square grids, in both orientations. A solution that sizes its row counts by
  //          grid[0].length or its column counts by grid.length either throws
  //          ArrayIndexOutOfBoundsException or misses servers. Each grid has one pair, in a row in
  //          the 2x3 grid and in a column in the 3x2 grid, plus one isolated server, so both
  //          answer 2
  @Test
  void nonSquareGridsInBothOrientations() {
    assertThat(sut.countServers(new int[][] {{1, 0, 1}, {0, 1, 0}})).isEqualTo(2);
    assertThat(sut.countServers(new int[][] {{1, 0}, {0, 1}, {1, 0}})).isEqualTo(2);
  }

  // ===========================================================================================
  // The official examples (Steps 13-15).
  // ===========================================================================================

  // Step 13: LeetCode Example 1. The two servers are diagonal neighbors, which does not count as
  //          sharing a row or column. An 8-direction neighbor check answers 2
  @Test
  void leetCodeExample1() {
    assertThat(sut.countServers(new int[][] {{1, 0}, {0, 1}})).isZero();
  }

  // Step 14: LeetCode Example 2. The bottom-left server shares a column with the top server and a
  //          row with the bottom-right one. It is counted once. Adding up rows and columns
  //          answers 4
  @Test
  void leetCodeExample2() {
    assertThat(sut.countServers(new int[][] {{1, 0}, {1, 1}})).isEqualTo(3);
  }

  // Step 15: LeetCode Example 3. A row pair, a column pair, and an isolated bottom-right server
  //          that is diagonally adjacent to the column pair. An 8-direction neighbor check answers
  //          5. Note that adding up rows and columns happens to give 4 here too, because no server
  //          is busy in both directions. Steps 8, 9 and 14 catch that bug instead
  @Test
  void leetCodeExample3() {
    int[][] grid = {{1, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
    assertThat(sut.countServers(grid)).isEqualTo(4);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 16-21). The grid is at most 250x250 = 62,500 cells. Even rescanning
  // the full row and column for every server is about 3 * 10^7 steps and passes. The timeouts
  // reject all-pairs comparison of servers (about 2 * 10^9 pairs on a full grid). They also catch
  // a recursive DFS whose depth grows with component size: on the full grid it can go 62,500
  // frames deep.
  // ===========================================================================================

  // Step 16: every cell holds a server. Every row holds 250, so all 62,500 communicate
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void fullMaxGridAllCommunicate() {
    assertThat(sut.countServers(grid(250, 250, (i, j) -> 1))).isEqualTo(62_500);
  }

  // Step 17: the empty max grid
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void emptyMaxGridCountsNothing() {
    assertThat(sut.countServers(grid(250, 250, (i, j) -> 0))).isZero();
  }

  // Step 18: the 250x250 identity grid. This is the largest possible set of isolated servers,
  //          since each row and each column holds at most one
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void identityMaxGridHasNoCommunication() {
    assertThat(sut.countServers(grid(250, 250, (i, j) -> i == j ? 1 : 0))).isZero();
  }

  // Step 19: the identity grid plus one server in the top-right corner, row 0, column 249. That
  //          server shares row 0 with the top-left server and column 249 with the bottom-right
  //          server, so exactly those 3 communicate. Adding up rows and columns answers 4, and an
  //          adjacent-cells flood fill answers 0
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void identityPlusFarCornerLinksThreeServers() {
    int[][] grid = grid(250, 250, (i, j) -> i == j || (i == 0 && j == 249) ? 1 : 0);
    assertThat(sut.countServers(grid)).isEqualTo(3);
  }

  // Step 20: a 250x250 checkerboard. No two servers are 4-direction neighbors, but every row holds
  //          125, so all 31,250 communicate. An adjacent-cells flood fill answers 0, and adding up
  //          rows and columns answers 62,500
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void checkerboardMaxGridAllCommunicate() {
    assertThat(sut.countServers(grid(250, 250, (i, j) -> (i + j + 1) % 2))).isEqualTo(31_250);
  }

  // Step 21: the widest and tallest grids, 1x250 and 250x1, all servers. Each is a single row or
  //          a single column, so all 250 communicate
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void widestAndTallestFullGrids() {
    assertThat(sut.countServers(grid(1, 250, (i, j) -> 1))).isEqualTo(250);
    assertThat(sut.countServers(grid(250, 1, (i, j) -> 1))).isEqualTo(250);
  }

  // ===========================================================================================
  // Hygiene (Step 22).
  // ===========================================================================================

  // Step 22: one instance answers several grids of different shapes, deliberately out of order
  //          with the largest in the middle. This catches row or column counters kept on the
  //          instance and not reset per call
  @Test
  void oneInstanceAnswersManyGrids() {
    assertThat(sut.countServers(new int[][] {{1, 0}, {1, 1}})).isEqualTo(3);
    assertThat(sut.countServers(grid(250, 250, (i, j) -> 1))).isEqualTo(62_500);
    assertThat(sut.countServers(new int[][] {{1}})).isZero();
    assertThat(sut.countServers(new int[][] {{1, 0, 0, 1}})).isEqualTo(2);
    assertThat(sut.countServers(new int[][] {{1, 0}, {0, 1}})).isZero();
  }

  private static int[][] grid(int m, int n, IntBinaryOperator cell) {
    int[][] grid = new int[m][n];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        grid[i][j] = cell.applyAsInt(i, j);
      }
    }
    return grid;
  }
}
