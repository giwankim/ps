package com.giwankim.leetcode;

public class RotateImage {
  public void rotate(int[][] matrix) {
    // Time complexity: O(n^2), Space complexity: O(1)
    transpose(matrix);
    reverse(matrix);
  }

  private void transpose(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < i; j++) {
        swap(matrix, i, j, j, i);
      }
    }
  }

  private void reverse(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      int lo = 0;
      int hi = matrix[i].length - 1;
      while (lo < hi) {
        swap(matrix, i, lo, i, hi);
        lo++;
        hi--;
      }
    }
  }

  private void swap(int[][] matrix, int i, int j, int k, int l) {
    int tmp = matrix[i][j];
    matrix[i][j] = matrix[k][l];
    matrix[k][l] = tmp;
  }
}
