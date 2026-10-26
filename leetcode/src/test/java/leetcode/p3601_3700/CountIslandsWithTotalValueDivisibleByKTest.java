package leetcode.p3601_3700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CountIslandsWithTotalValueDivisibleByKTest {
  CountIslandsWithTotalValueDivisibleByK sut = new CountIslandsWithTotalValueDivisibleByK();

  // ===========================================================================================
  // The floor: a 1 x 1 grid (Steps 1-3).
  // ===========================================================================================

  // Step 1: water totals 0, and 0 is divisible by every k. Only positive cells are land, so a
  //         solution that flood-fills the zeros as a region of their own, or reads land as
  //         grid >= 0, answers 1
  @Test
  void singleWaterCellIsNotAnIsland() {
    assertThat(sut.countIslands(new int[][] {{0}}, 1)).isZero();
  }

  // Step 2: the smallest island. A total equal to k divides evenly
  @Test
  void singleLandCellEqualToKIsCounted() {
    assertThat(sut.countIslands(new int[][] {{5}}, 5)).isEqualTo(1);
  }

  // Step 3: the same cell one below k. The divisibility filter is real: returning the plain
  //         island count answers 1
  @Test
  void singleLandCellBelowKIsNotCounted() {
    assertThat(sut.countIslands(new int[][] {{4}}, 5)).isZero();
  }

  // ===========================================================================================
  // Which cells join an island (Steps 4-8).
  // ===========================================================================================

  // Step 4: neither 2 nor 3 is divisible by 5, but their island totals 5. Testing cells one at
  //         a time answers 0
  @Test
  void horizontalNeighborsShareOneTotal() {
    assertThat(sut.countIslands(new int[][] {{2, 3}}, 5)).isEqualTo(1);
  }

  // Step 5: the mirror of Step 4 on a 2 x 1 grid, the first with m != n. A solution that bounds
  //         columns by grid.length reads past the end of each one-cell row
  @Test
  void verticalNeighborsShareOneTotal() {
    assertThat(sut.countIslands(new int[][] {{2}, {3}}, 5)).isEqualTo(1);
  }

  // Step 6: cells that touch only at a corner are separate islands totaling 2 and 3. Treating
  //         all eight neighbors as connected merges them into 5 and answers 1
  @Test
  void diagonalNeighborsAreSeparateIslands() {
    assertThat(sut.countIslands(new int[][] {{2, 0}, {0, 3}}, 5)).isZero();
  }

  // Step 7: the 2 ends row 0 and the 3 starts row 1. Flattening to index r * n + c and stepping
  //         by +-1 without a row-boundary check joins them into 5 and answers 1
  @Test
  void rowEndDoesNotTouchNextRowStart() {
    assertThat(sut.countIslands(new int[][] {{0, 0, 2}, {3, 0, 0}}, 5)).isZero();
  }

  // Step 8: the zero between the two 3s is water, so these are two islands of 3, neither
  //         divisible by 6. Reading a zero cell as land bridges them into 6 and answers 1
  @Test
  void zeroCellSplitsIslands() {
    assertThat(sut.countIslands(new int[][] {{3, 0, 3}}, 6)).isZero();
  }

  // ===========================================================================================
  // Whole-island totals (Steps 9-12).
  // ===========================================================================================

  // Step 9: one island totaling 10. The answer counts islands, not cells, so counting the cells
  //         divisible by 5 answers 2
  @Test
  void islandIsCountedOnceNotPerCell() {
    assertThat(sut.countIslands(new int[][] {{5, 5}}, 5)).isEqualTo(1);
  }

  // Step 10: the 5 is divisible but its island totals 6. Qualifying an island on any one of its
  //          cells answers 1
  @Test
  void oneDivisibleCellDoesNotQualifyItsIsland() {
    assertThat(sut.countIslands(new int[][] {{5, 1}}, 5)).isZero();
  }

  // Step 11: k = 1 divides every total, so the answer is the island count: 4 islands totaling
  //          2, 14, 9 and 4. Counting land cells answers 6, and so does also counting the two
  //          water regions, each totaling 0
  @Test
  void k1CountsEveryIslandAndNoWater() {
    int[][] grid = {
      {1, 1, 0, 7},
      {0, 0, 0, 7},
      {9, 0, 4, 0}
    };
    assertThat(sut.countIslands(grid, 1)).isEqualTo(4);
  }

  // Step 12: the smallest island with a cycle totals 4. A BFS that marks a cell visited when it
  //          is dequeued rather than enqueued queues the bottom-right cell twice, sums 5 and
  //          answers 0
  @Test
  void squareBlockSumsEachCellOnce() {
    assertThat(sut.countIslands(new int[][] {{1, 1}, {1, 1}}, 4)).isEqualTo(1);
  }

  // ===========================================================================================
  // LeetCode examples (Steps 13-14).
  // ===========================================================================================

  // Step 13: four islands totaling 8, 5, 15 and 8. The 15 is five cells, none divisible by 5,
  //          so only the whole-island total qualifies it. Corner contact would merge 8 + 15 + 8
  //          into 31 and answer 1
  @Test
  void leetCodeExample1() {
    int[][] grid = {
      {0, 2, 1, 0, 0},
      {0, 5, 0, 0, 5},
      {0, 0, 1, 0, 0},
      {0, 1, 4, 7, 0},
      {0, 2, 0, 0, 8}
    };
    assertThat(sut.countIslands(grid, 5)).isEqualTo(2);
  }

  // Step 14: six lone 3s that touch only at corners, each its own island. Eight-way adjacency
  //          joins them into one island of 18 and answers 1, as does testing the grid total
  @Test
  void leetCodeExample2() {
    int[][] grid = {
      {3, 0, 3, 0},
      {0, 3, 0, 3},
      {3, 0, 3, 0}
    };
    assertThat(sut.countIslands(grid, 3)).isEqualTo(6);
  }

  // ===========================================================================================
  // Value range (Step 15).
  // ===========================================================================================

  // Step 15: a 3 x 1000 block of 1e6 totals 3e9, past Integer.MAX_VALUE. An int accumulator
  //          wraps to -1,294,967,296, leaves remainder -967,296 and answers 0. Accumulate in a
  //          long, or reduce modulo k as you go
  @Test
  void islandTotalOverflowsInt() {
    assertThat(sut.countIslands(filled(3, 1000, 1_000_000), 1_000_000)).isEqualTo(1);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 16-17). m * n reaches 1e5: a traversal that settles each cell once
  // finishes in milliseconds. Re-flooding an island from each of its cells costs cells x island
  // size = 1e10 on the full grid, and clearing an m x n visited array per island costs
  // 5e4 x 1e5 = 5e9 on the checkerboard.
  // ===========================================================================================

  // Step 16: every bound at once on a wide 100 x 1000 grid: one island of 1e5 cells totaling
  //          1e11. An int accumulator wraps to 1,215,752,192, leaves remainder 752,192 and
  //          answers 0. A recursive DFS can nest about 1e5 deep here, which the build's -Xss64m
  //          admits, so recursion alone does not fail this step
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxGridOfMaxValuesIsOneIsland() {
    assertThat(sut.countIslands(filled(100, 1000, 1_000_000), 1_000_000)).isEqualTo(1);
  }

  // Step 17: the most islands a grid can hold, on a tall 1000 x 100 grid: 50,000 single cells
  //          valued 1 to 1e5 with no repeats (see checkerboard), of which 7,142 are multiples
  //          of 7
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void checkerboardOfFiftyThousandSingleCellIslands() {
    assertThat(sut.countIslands(checkerboard(), 7)).isEqualTo(7142);
  }

  // ===========================================================================================
  // Hygiene (Step 18).
  // ===========================================================================================

  // Step 18: one instance answers grids of different sizes, the largest in the middle. A
  //          visited array cached on the instance and sized by the first 3 x 4 grid overruns
  //          the 3 x 1000 one, and a count field never reset answers 6, 7, 7, 9
  @Test
  void oneInstanceAnswersGridsOfDifferentSizes() {
    int[][] example2 = {
      {3, 0, 3, 0},
      {0, 3, 0, 3},
      {3, 0, 3, 0}
    };
    int[][] example1 = {
      {0, 2, 1, 0, 0},
      {0, 5, 0, 0, 5},
      {0, 0, 1, 0, 0},
      {0, 1, 4, 7, 0},
      {0, 2, 0, 0, 8}
    };
    assertThat(sut.countIslands(example2, 3)).isEqualTo(6);
    assertThat(sut.countIslands(filled(3, 1000, 1_000_000), 1_000_000)).isEqualTo(1);
    assertThat(sut.countIslands(new int[][] {{0}}, 1)).isZero();
    assertThat(sut.countIslands(example1, 5)).isEqualTo(2);
  }

  /** An m x n grid with every cell set to {@code value}. */
  private static int[][] filled(int m, int n, int value) {
    int[][] grid = new int[m][n];
    for (int[] row : grid) {
      Arrays.fill(row, value);
    }
    return grid;
  }

  /**
   * A 1000 x 100 checkerboard: land where row + column is even, holding row * 100 + column + 1.
   * That makes 50,000 single-cell islands with distinct values from 1 to 1e5.
   */
  private static int[][] checkerboard() {
    int[][] grid = new int[1000][100];
    for (int r = 0; r < 1000; r++) {
      for (int c = r % 2; c < 100; c += 2) {
        grid[r][c] = r * 100 + c + 1;
      }
    }
    return grid;
  }
}
