package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class Shift2DGridTest {
  Shift2DGrid sut = new Shift2DGrid();

  // Step 1: the lower bound k = 0 — no shift operation is applied, the grid comes back as-is
  @Test
  void zeroShiftsReturnGridUnchanged() {
    assertThat(sut.shiftGrid(new int[][] {{1, 2}, {3, 4}}, 0))
        .isEqualTo(List.of(List.of(1, 2), List.of(3, 4)));
  }

  // Step 2: smallest grid (1 x 1) — the single cell wraps onto itself, so even the
  //         maximum k = 100 changes nothing
  @Test
  void singleCellGridIsUnchangedByAnyShiftCount() {
    assertThat(sut.shiftGrid(new int[][] {{7}}, 100)).isEqualTo(List.of(List.of(7)));
  }

  // Step 3: single row — only the grid[i][n-1] -> grid[0][0] wrap fires, so one shift
  //         is a plain right rotation of the row
  @Test
  void singleRowRotatesRightByOne() {
    assertThat(sut.shiftGrid(new int[][] {{1, 2, 3, 4}}, 1))
        .isEqualTo(List.of(List.of(4, 1, 2, 3)));
  }

  // Step 4: single column (n = 1) — every element is in the last column, so each shift
  //         pushes the whole column down one row with wrap
  @Test
  void singleColumnRotatesDownward() {
    assertThat(sut.shiftGrid(new int[][] {{1}, {2}, {3}}, 2))
        .isEqualTo(List.of(List.of(2), List.of(3), List.of(1)));
  }

  // Step 5: LeetCode Example 1 — one shift on a 3 x 3 grid; 9 wraps from the bottom-right
  //         corner to grid[0][0] and everything else slides one cell right
  @Test
  void leetCodeExample1() {
    assertThat(sut.shiftGrid(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 1))
        .isEqualTo(List.of(List.of(9, 1, 2), List.of(3, 4, 5), List.of(6, 7, 8)));
  }

  // Step 6: rectangular grid with k < n — elements cross the row boundary mid-row,
  //         so rows of the result mix elements of two input rows
  @Test
  void shiftCrossesRowBoundaryInRectangularGrid() {
    assertThat(sut.shiftGrid(new int[][] {{1, 2, 3}, {4, 5, 6}}, 2))
        .isEqualTo(List.of(List.of(5, 6, 1), List.of(2, 3, 4)));
  }

  // Step 7: LeetCode Example 2 — k equal to the row width n shifts every full row down
  //         one position, with the last row wrapping to the top
  @Test
  void leetCodeExample2() {
    assertThat(sut.shiftGrid(
            new int[][] {{3, 8, 1, 9}, {19, 7, 2, 5}, {4, 6, 11, 10}, {12, 0, 21, 13}}, 4))
        .isEqualTo(List.of(
            List.of(12, 0, 21, 13),
            List.of(3, 8, 1, 9),
            List.of(19, 7, 2, 5),
            List.of(4, 6, 11, 10)));
  }

  // Step 8: LeetCode Example 3 — k equal to the cell count m * n is a full cycle,
  //         restoring the original grid
  @Test
  void leetCodeExample3FullCycleRestoresGrid() {
    assertThat(sut.shiftGrid(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 9))
        .isEqualTo(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9)));
  }

  // Step 9: k larger than the cell count — 5 shifts on 4 cells must reduce to 5 % 4 = 1,
  //         not over-rotate
  @Test
  void shiftCountLargerThanCellCountWrapsAround() {
    assertThat(sut.shiftGrid(new int[][] {{1, 2}, {3, 4}}, 5))
        .isEqualTo(List.of(List.of(4, 1), List.of(2, 3)));
  }

  // Step 10: value bounds — negative values down to -1000 and the maximum 1000 move
  //          like any other element
  @Test
  void negativeAndBoundaryValuesShiftNormally() {
    assertThat(sut.shiftGrid(new int[][] {{-1000, -1}, {0, 1000}}, 1))
        .isEqualTo(List.of(List.of(1000, -1000), List.of(-1, 0)));
  }

  // Step 11: maximum k = 100 on a grid whose size does not divide it — 100 % 6 = 4,
  //          so the last four elements rotate to the front
  @Test
  void maximumShiftCountReducesModuloCellCount() {
    assertThat(sut.shiftGrid(new int[][] {{1, 2, 3}, {4, 5, 6}}, 100))
        .isEqualTo(List.of(List.of(3, 4, 5), List.of(6, 1, 2)));
  }
}
