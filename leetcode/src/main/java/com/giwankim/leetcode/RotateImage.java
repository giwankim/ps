package com.giwankim.leetcode;

public class RotateImage {

  public void rotate(int[][] matrix) {
    // transpose
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < i; j++) {
        swap(matrix, i, j, j, i);
      }
    }

    // reverse each row
    for (int i = 0; i < matrix.length; i++) {
      int left = 0;
      int right = matrix[i].length - 1;
      while (left < right) {
        swap(matrix, i, left, i, right);
        left += 1;
        right -= 1;
      }
    }
  }

  private void swap(int[][] matrix, int i1, int j1, int i2, int j2) {
    int temp = matrix[i1][j1];
    matrix[i1][j1] = matrix[i2][j2];
    matrix[i2][j2] = temp;
  }
}
