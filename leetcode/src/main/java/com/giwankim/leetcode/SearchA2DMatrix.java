package com.giwankim.leetcode;

import java.util.Arrays;

public class SearchA2DMatrix {
  /**
   * <p>Time: {@code O(log m + log n) = O(log(m * n))}, where {@code m} is the number of rows
   * and {@code n} is the number of columns.
   *
   * <p>Space: {@code O(1)}.
   */
  public boolean searchMatrix(int[][] matrix, int target) {
    int lo = 0;
    int hi = matrix.length;
    while (lo < hi) {
      int mid = lo + (hi - lo) / 2;
      if (matrix[mid][0] > target) {
        hi = mid;
      } else {
        lo = mid + 1;
      }
    }
    int row = lo - 1;
    if (row < 0) {
      return false;
    }
    return Arrays.binarySearch(matrix[row], target) >= 0;
  }

  /**
   * <p>Time: {@code O(log(m * n))}, where {@code m} is the number of rows and {@code n} is
   * the number of columns.
   *
   * <p>Space: {@code O(1)}.
   */
  public boolean searchMatrix2(int[][] matrix, int target) {
    int n = matrix.length;
    int m = matrix[0].length;
    int lo = 0;
    int hi = n * m - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      int row = mid / m;
      int col = mid % m;
      if (matrix[row][col] == target) {
        return true;
      }
      if (matrix[row][col] < target) {
        lo = mid + 1;
      } else {
        hi = mid - 1;
      }
    }
    return false;
  }
}
