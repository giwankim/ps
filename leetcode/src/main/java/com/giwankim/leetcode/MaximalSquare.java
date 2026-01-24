package com.giwankim.leetcode;

public class MaximalSquare {

  public int maximalSquare(char[][] matrix) {
    int result = 0;
    int m = matrix.length;
    int n = matrix[0].length;

    int[][] a = new int[m][n];
    for (int i = 0; i < m; i++) {
      a[i][0] = matrix[i][0] == '1' ? 1 : 0;
    }
    for (int j = 0; j < n; j++) {
      a[0][j] = matrix[0][j] == '1' ? 1 : 0;
    }

    for (int i = 1; i < m; i++) {
      for (int j = 1; j < n; j++) {
        if (matrix[i][j] == '0') {
          a[i][j] = 0;
        } else {
          a[i][j] = 1 + Math.min(Math.min(a[i - 1][j], a[i][j - 1]), a[i - 1][j - 1]);
        }
      }
    }

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        result = Math.max(result, a[i][j] * a[i][j]);
      }
    }
    return result;
  }
}
