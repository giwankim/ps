package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class SpiralMatrix {
  /**
   * @implNote Time {@code O(mn)}, space {@code O(1)}.
   */
  public List<Integer> spiralOrder(int[][] matrix) {
    List<Integer> result = new ArrayList<>();
    int left = 0;
    int right = matrix[0].length - 1;
    int top = 0;
    int bottom = matrix.length - 1;
    while (left <= right && top <= bottom) {
      // L -> R
      for (int j = left; j <= right; j++) {
        result.add(matrix[top][j]);
      }
      top++;
      // U -> D
      for (int i = top; i <= bottom; i++) {
        result.add(matrix[i][right]);
      }
      right--;
      // R -> L
      if (bottom >= top) {
        for (int j = right; j >= left; j--) {
          result.add(matrix[bottom][j]);
        }
        bottom--;
      }
      // D -> U
      if (left <= right) {
        for (int i = bottom; i >= top; i--) {
          result.add(matrix[i][left]);
        }
        left++;
      }
    }
    return result;
  }
}
