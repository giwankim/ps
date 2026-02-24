package com.giwankim.leetcode;

public class SetMatrixZeroes {
  public void setZeroes(int[][] matrix) {
    // Time complexity: O(mn), Space complexity: O(1)
    boolean isFirstRow = false;
    for (int j = 0; j < matrix[0].length; j++) {
      if (matrix[0][j] == 0) {
        isFirstRow = true;
      }
    }
    boolean isFirstCol = false;
    for (int i = 0; i < matrix.length; i++) {
      if (matrix[i][0] == 0) {
        isFirstCol = true;
      }
    }

    for (int i = 1; i < matrix.length; i++) {
      for (int j = 1; j < matrix[i].length; j++) {
        if (matrix[i][j] == 0) {
          matrix[i][0] = 0;
          matrix[0][j] = 0;
        }
      }
    }

    for (int i = 1; i < matrix.length; i++) {
      for (int j = 1; j < matrix[i].length; j++) {
        if (matrix[i][0] == 0 || matrix[0][j] == 0) {
          matrix[i][j] = 0;
        }
      }
    }

    if (isFirstRow) {
      for (int j = 0; j < matrix[0].length; j++) {
        matrix[0][j] = 0;
      }
    }

    if (isFirstCol) {
      for (int i = 0; i < matrix.length; i++) {
        matrix[i][0] = 0;
      }
    }
  }
}
