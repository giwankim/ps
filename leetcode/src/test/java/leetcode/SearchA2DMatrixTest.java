package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SearchA2DMatrixTest {
  SearchA2DMatrix sut = new SearchA2DMatrix();

  // Step 1: 1x1 matrix — target equals the only cell, the simplest possible hit
  @Test
  void singleCellMatchesTarget() {
    int[][] matrix = {{5}};
    assertThat(sut.searchMatrix(matrix, 5)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 5)).isTrue();
  }

  // Step 2: 1x1 matrix — target smaller than the only cell, miss below
  @Test
  void singleCellTargetSmallerReturnsFalse() {
    int[][] matrix = {{5}};
    assertThat(sut.searchMatrix(matrix, 3)).isFalse();
    assertThat(sut.searchMatrix2(matrix, 3)).isFalse();
  }

  // Step 3: 1x1 matrix — target larger than the only cell, miss above
  @Test
  void singleCellTargetLargerReturnsFalse() {
    int[][] matrix = {{5}};
    assertThat(sut.searchMatrix(matrix, 7)).isFalse();
    assertThat(sut.searchMatrix2(matrix, 7)).isFalse();
  }

  // Step 4: single row — target equals the first element (left edge of row)
  @Test
  void singleRowTargetMatchesFirstElement() {
    int[][] matrix = {{1, 3, 5, 7}};
    assertThat(sut.searchMatrix(matrix, 1)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 1)).isTrue();
  }

  // Step 5: single row — target equals the last element (right edge of row)
  @Test
  void singleRowTargetMatchesLastElement() {
    int[][] matrix = {{1, 3, 5, 7}};
    assertThat(sut.searchMatrix(matrix, 7)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 7)).isTrue();
  }

  // Step 6: single row — target found in the interior of the row
  @Test
  void singleRowTargetMatchesInteriorElement() {
    int[][] matrix = {{1, 3, 5, 7}};
    assertThat(sut.searchMatrix(matrix, 5)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 5)).isTrue();
  }

  // Step 7: single row — target falls into a gap between two elements, miss
  @Test
  void singleRowTargetInGapReturnsFalse() {
    int[][] matrix = {{1, 3, 5, 7}};
    assertThat(sut.searchMatrix(matrix, 4)).isFalse();
    assertThat(sut.searchMatrix2(matrix, 4)).isFalse();
  }

  // Step 8: single column — target lives in the first row
  @Test
  void singleColumnTargetInFirstRow() {
    int[][] matrix = {{1}, {4}, {7}, {10}};
    assertThat(sut.searchMatrix(matrix, 1)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 1)).isTrue();
  }

  // Step 9: single column — target lives in the last row
  @Test
  void singleColumnTargetInLastRow() {
    int[][] matrix = {{1}, {4}, {7}, {10}};
    assertThat(sut.searchMatrix(matrix, 10)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 10)).isTrue();
  }

  // Step 10: single column — target falls between two rows, miss
  @Test
  void singleColumnTargetBetweenRowsReturnsFalse() {
    int[][] matrix = {{1}, {4}, {7}, {10}};
    assertThat(sut.searchMatrix(matrix, 5)).isFalse();
    assertThat(sut.searchMatrix2(matrix, 5)).isFalse();
  }

  // Step 11: multi-row — target is the overall minimum (top-left corner)
  @Test
  void multiRowTargetAtTopLeftCorner() {
    int[][] matrix = {{1, 3, 5}, {7, 9, 11}, {13, 15, 17}};
    assertThat(sut.searchMatrix(matrix, 1)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 1)).isTrue();
  }

  // Step 12: multi-row — target is the overall maximum (bottom-right corner)
  @Test
  void multiRowTargetAtBottomRightCorner() {
    int[][] matrix = {{1, 3, 5}, {7, 9, 11}, {13, 15, 17}};
    assertThat(sut.searchMatrix(matrix, 17)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 17)).isTrue();
  }

  // Step 13: multi-row — target equals the LAST element of an interior row (row-boundary)
  @Test
  void multiRowTargetAtEndOfInteriorRow() {
    int[][] matrix = {{1, 3, 5}, {7, 9, 11}, {13, 15, 17}};
    assertThat(sut.searchMatrix(matrix, 11)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 11)).isTrue();
  }

  // Step 14: multi-row — target equals the FIRST element of an interior row (row-boundary)
  @Test
  void multiRowTargetAtStartOfInteriorRow() {
    int[][] matrix = {{1, 3, 5}, {7, 9, 11}, {13, 15, 17}};
    assertThat(sut.searchMatrix(matrix, 7)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 7)).isTrue();
  }

  // Step 15: multi-row — target falls in the gap between two rows
  //   (last-of-row N < target < first-of-row N+1)
  @Test
  void multiRowTargetInGapBetweenRowsReturnsFalse() {
    int[][] matrix = {{1, 3, 5}, {7, 9, 11}, {13, 15, 17}};
    assertThat(sut.searchMatrix(matrix, 12)).isFalse();
    assertThat(sut.searchMatrix2(matrix, 12)).isFalse();
  }

  // Step 16: multi-row — target smaller than every element (lower-bound miss)
  @Test
  void targetSmallerThanAllReturnsFalse() {
    int[][] matrix = {{1, 3, 5}, {7, 9, 11}, {13, 15, 17}};
    assertThat(sut.searchMatrix(matrix, 0)).isFalse();
    assertThat(sut.searchMatrix2(matrix, 0)).isFalse();
  }

  // Step 17: multi-row — target larger than every element (upper-bound miss)
  @Test
  void targetLargerThanAllReturnsFalse() {
    int[][] matrix = {{1, 3, 5}, {7, 9, 11}, {13, 15, 17}};
    assertThat(sut.searchMatrix(matrix, 100)).isFalse();
    assertThat(sut.searchMatrix2(matrix, 100)).isFalse();
  }

  // Step 18: LeetCode Example 1 — matrix=[[1,3,5,7],[10,11,16,20],[23,30,34,60]], target=3 → true
  @Test
  void leetCodeExampleOneTargetFound() {
    int[][] matrix = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
    assertThat(sut.searchMatrix(matrix, 3)).isTrue();
    assertThat(sut.searchMatrix2(matrix, 3)).isTrue();
  }

  // Step 19: LeetCode Example 2 — same matrix, target=13 sits in gap between rows → false
  @Test
  void leetCodeExampleTwoTargetNotFound() {
    int[][] matrix = {{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
    assertThat(sut.searchMatrix(matrix, 13)).isFalse();
    assertThat(sut.searchMatrix2(matrix, 13)).isFalse();
  }

  // Step 20: negative values are ordered correctly — target found in an interior row
  @Test
  void negativeValuesTargetFound() {
    int[][] matrix = {{-10, -8, -5}, {-3, -1, 2}, {4, 6, 9}};
    assertThat(sut.searchMatrix(matrix, -1)).isTrue();
    assertThat(sut.searchMatrix2(matrix, -1)).isTrue();
  }

  // Step 21: negative values — target absent (gap between negative values)
  @Test
  void negativeValuesTargetNotFound() {
    int[][] matrix = {{-10, -8, -5}, {-3, -1, 2}, {4, 6, 9}};
    assertThat(sut.searchMatrix(matrix, -7)).isFalse();
    assertThat(sut.searchMatrix2(matrix, -7)).isFalse();
  }
}
