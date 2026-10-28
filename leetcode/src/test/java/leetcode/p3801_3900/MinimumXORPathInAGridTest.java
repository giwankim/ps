package leetcode.p3801_3900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumXORPathInAGridTest {
  MinimumXORPathInAGrid sut = new MinimumXORPathInAGrid();

  // ===========================================================================================
  // The floor: a 1 x 1 grid (Steps 1-2).
  // ===========================================================================================

  // Step 1: the smallest grid holding the smallest value. The start is already the target, so a
  //         search that looks for the bottom-right cell only after taking a move never finds it
  //         and returns its Integer.MAX_VALUE sentinel
  @Test
  void singleZeroCellCostsZero() {
    assertThat(sut.minCost(new int[][] {{0}})).isZero();
  }

  // Step 2: one cell is both the start and the end, and it counts once. Leaving out either
  //         endpoint answers 0, and so does XORing the shared cell in as the start and again as
  //         the end
  @Test
  void singleCellIsCountedOnce() {
    assertThat(sut.minCost(new int[][] {{7}})).isEqualTo(7);
  }

  // ===========================================================================================
  // What a path costs (Steps 3-5).
  // ===========================================================================================

  // Step 3: the cost is XOR, not sum. Two equal cells cancel to 0, while adding them answers 10
  //         and leaving out either endpoint answers 5
  @Test
  void equalCellsCancelUnderXor() {
    assertThat(sut.minCost(new int[][] {{5, 5}})).isZero();
  }

  // Step 4: a single row allows only right moves, so its one path XORs to 3, above every cell.
  //         A table of reachable XORs sized by the largest cell overruns, leaving out the start
  //         answers 2 and leaving out the end answers 1. A walk that may also step left goes
  //         right, left and right again, cancels both cells and answers 0
  @Test
  void singleRowXorsEveryCell() {
    assertThat(sut.minCost(new int[][] {{1, 2}})).isEqualTo(3);
  }

  // Step 5: the mirror of Step 4 on a 2 x 1 grid, down moves only. A solution that bounds
  //         columns by grid.length reads past the end of each one-cell row, and a walk that may
  //         step back up answers 0
  @Test
  void singleColumnXorsEveryCell() {
    assertThat(sut.minCost(new int[][] {{1}, {2}})).isEqualTo(3);
  }

  // ===========================================================================================
  // Choosing between paths (Steps 6-10). XOR has no optimal substructure: the smallest XOR into
  // a cell can lose to a larger one that a later cell cancels.
  // ===========================================================================================

  // Step 6: going right keeps the running XOR at 0 while going down raises it to 1, yet only the
  //         down-first path ends at 0, where the two 1s cancel. A greedy walk that takes the
  //         smaller running XOR at each step answers 1, and so does the XOR of the minimum-sum
  //         path
  @Test
  void smallestRunningXorIsNotAPrefixOfTheBestPath() {
    assertThat(sut.minCost(new int[][] {{0, 0}, {1, 1}})).isZero();
  }

  // Step 7: the two paths XOR to 1 and 2. Each bit is clear on some path but no path clears
  //         both, so a solution that minimizes each bit on its own answers 0. XORing every cell,
  //         including the one no path visits, answers 3
  @Test
  void bitsCannotBeMinimizedIndependently() {
    assertThat(sut.minCost(new int[][] {{0, 1}, {2, 0}})).isEqualTo(1);
  }

  // Step 8: the paths XOR to 1, 1 and 0. The bottom middle cell is reached with 0 from above and
  //         with 1 from the left, and only the 1 cancels the final 1. A forward DP that keeps the
  //         smallest XOR per cell, as Minimum Path Sum does, keeps the 0 and answers 1
  @Test
  void smallestXorIntoACellCanLoseLater() {
    assertThat(sut.minCost(new int[][] {{0, 0, 0}, {1, 0, 1}})).isZero();
  }

  // Step 9: the mirror of Step 8, the grid turned half a turn. A top-down memo keyed on row and
  //         column alone stores the smallest XOR from each cell to the end. From the top middle
  //         cell that is 0 along the bottom row, but only the 1 through the top-right cell cancels
  //         the starting 1, so the memo answers 1
  @Test
  void smallestXorOutOfACellCanLoseEarlier() {
    assertThat(sut.minCost(new int[][] {{1, 0, 1}, {0, 0, 0}})).isZero();
  }

  // Step 10: the six paths XOR to 0, 0, 1, 1, 3 and 3. Keeping the smallest and the largest XOR
  //          per cell, as Maximum Non Negative Product in a Matrix does, answers 1 whether it runs
  //          forward or top-down, and the forward min-only DP of Step 8 answers 3. Only the whole
  //          set of reachable XORs per cell, at most 1024 of them, is sound
  @Test
  void keepingTheMinAndMaxPerCellIsNotEnough() {
    int[][] grid = {
      {1, 0, 0},
      {1, 2, 0},
      {0, 1, 2}
    };
    assertThat(sut.minCost(grid)).isZero();
  }

  // ===========================================================================================
  // LeetCode examples (Steps 11-13).
  // ===========================================================================================

  // Step 11: the two paths XOR to 7 and 6, so the answer is the minimum, not the maximum 7.
  //          XORing every cell, including whichever of 2 and 3 a path skips, answers 4, and the
  //          XOR of the minimum-sum path, 1 + 2 + 4 = 7, answers 7
  @Test
  void leetCodeExample1() {
    assertThat(sut.minCost(new int[][] {{1, 2}, {3, 4}})).isEqualTo(6);
  }

  // Step 12: the cheaper path by sum, 6 + 5 + 8 = 19, XORs to 11, while the costlier one XORs to
  //          9. Taking the minimum-sum path's XOR answers 11, XORing every cell answers 12 and
  //          leaving out the end cell answers 1
  @Test
  void leetCodeExample2() {
    assertThat(sut.minCost(new int[][] {{6, 7}, {5, 8}})).isEqualTo(9);
  }

  // Step 13: a single row leaves one path, and here it cancels to 0, the smallest answer there
  //          is. Leaving out the start answers 2 and leaving out the end answers 5
  @Test
  void leetCodeExample3() {
    assertThat(sut.minCost(new int[][] {{2, 7, 5}})).isZero();
  }

  // ===========================================================================================
  // Value range (Steps 14-15). Cells reach 1023 = 2^10 - 1, so every XOR fits in 10 bits and a
  // table of reachable XORs needs exactly 1024 slots.
  // ===========================================================================================

  // Step 14: the ceiling value alone. A reachable table of 1023 slots, or of 1000 from misreading
  //          the bound, overruns at index 1023
  @Test
  void singleCellAtValueCeiling() {
    assertThat(sut.minCost(new int[][] {{1023}})).isEqualTo(1023);
  }

  // Step 15: no cell holds 1023, but 512 and 511 share no bits and XOR to it. A table sized by
  //          the largest cell, 513 slots, overruns. Leaving out the start answers 511 and leaving
  //          out the end answers 512
  @Test
  void pathXorReachesTheCeilingNoCellHolds() {
    assertThat(sut.minCost(new int[][] {{512, 511}})).isEqualTo(1023);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 16-20). m * n reaches 1000 and every XOR fits in 10 bits, so the
  // sets of XORs reachable at each cell cost at most 1000 x 1024, about 1e6 steps in all. A
  // search over paths faces 63-choose-24 = 1.6e17 of them on 25 x 40 and cannot finish. m and n
  // each reach 1000 but never together: a table allocated from the constants at
  // 1000 x 1000 x 1024 rather than at m x n x 1024 holds 1e9 entries.
  // ===========================================================================================

  // Step 16: the longest path a grid allows, one row of 1000 cells (see ramp) whose single path
  //          XORs to 488. With no choice to make, this pins the shape rather than the clock: a
  //          top-down search nests 1000 calls deep, which the default stack admits
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void singleRowOfThousandCells() {
    assertThat(sut.minCost(ramp(1, 1000))).isEqualTo(488);
  }

  // Step 17: the mirror of Step 16, one column of 1000 cells whose single path XORs to 1008. A
  //          solution that swaps the roles of m and n overruns on one of the two
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void singleColumnOfThousandCells() {
    assertThat(sut.minCost(ramp(1000, 1))).isEqualTo(1008);
  }

  // Step 18: the whole cell budget on a wide 25 x 40 grid, 1.6e17 paths of 64 cells each. The
  //          forward min-only DP of Step 8 answers 356, the top-down row-and-column memo of
  //          Step 9 answers 26, the min-and-max DP of Step 10 answers 334 and the greedy walk of
  //          Step 6 answers 618
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void wideGridAtCellBudget() {
    assertThat(sut.minCost(ramp(25, 40))).isZero();
  }

  // Step 19: a tall 32 x 31 grid of 992 cells and 2.3e17 paths. A ramp cell is odd exactly when
  //          its row plus column is odd, and each 62-cell path crosses 31 such cells, so bit 0 is
  //          set on every path and the minimum is 1, not 0. The forward min-only DP answers 19,
  //          the top-down memo 19, the forward min-and-max DP 91 and the greedy walk 837
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void tallGridWhereParityForcesAnOddAnswer() {
    assertThat(sut.minCost(ramp(32, 31))).isEqualTo(1);
  }

  // Step 20: the value ceiling at scale. Each of the 3.4e16 paths on a 20 x 50 grid crosses 69
  //          cells of 1023, an odd count, so every path XORs to 1023. A 1023-slot table overruns,
  //          and enumerating the paths cannot finish
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void everyPathAtValueCeiling() {
    assertThat(sut.minCost(filled(20, 50, 1023))).isEqualTo(1023);
  }

  // ===========================================================================================
  // Hygiene (Step 21).
  // ===========================================================================================

  // Step 21: one instance answers grids of different sizes, the largest in the middle. A table
  //          cached on the instance and sized by the first 2 x 2 grid overruns the 25 x 40 one, a
  //          best-so-far field never reset answers 9, 0, 0, 0, 0, and a row-column-XOR memo kept
  //          across calls replays Example 1's 6 for the last grid, which also starts from a 1
  @Test
  void oneInstanceAnswersGridsOfDifferentSizes() {
    assertThat(sut.minCost(new int[][] {{6, 7}, {5, 8}})).isEqualTo(9);
    assertThat(sut.minCost(ramp(25, 40))).isZero();
    assertThat(sut.minCost(new int[][] {{7}})).isEqualTo(7);
    assertThat(sut.minCost(new int[][] {{1, 2}, {3, 4}})).isEqualTo(6);
    assertThat(sut.minCost(new int[][] {{1, 0, 1}, {0, 0, 0}})).isZero();
  }

  /**
   * An m x n grid holding (37 * row + 91 * column) % 1024. Both coefficients are odd, so a cell is
   * odd exactly when its row plus column is odd.
   */
  private static int[][] ramp(int m, int n) {
    int[][] grid = new int[m][n];
    for (int r = 0; r < m; r++) {
      for (int c = 0; c < n; c++) {
        grid[r][c] = (37 * r + 91 * c) % 1024;
      }
    }
    return grid;
  }

  /** An m x n grid with every cell set to {@code value}. */
  private static int[][] filled(int m, int n, int value) {
    int[][] grid = new int[m][n];
    for (int[] row : grid) {
      Arrays.fill(row, value);
    }
    return grid;
  }
}
