package com.giwankim.leetcode;

public class RotatingTheBox {

  public char[][] rotateTheBox(char[][] boxGrid) {
    int n = boxGrid.length;
    int m = boxGrid[0].length;

    char[][] result = new char[m][n];

    // rotate 90 degrees clockwise
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        result[j][n - i - 1] = boxGrid[i][j];
      }
    }

    // apply gravity
    for (int j = 0; j < n; j++) {
      int i = m - 1;
      while (i > 0) {
        if (result[i][j] == '.' && result[i - 1][j] == '#') {
          result[i][j] = '#';
          result[i - 1][j] = '.';
        }
        i -= 1;
      }
    }

    return result;
  }
}
