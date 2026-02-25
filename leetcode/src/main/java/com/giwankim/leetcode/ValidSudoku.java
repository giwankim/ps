package com.giwankim.leetcode;

public class ValidSudoku {
  public boolean isValidSudoku(char[][] board) {
    // Time complexity: O(1), Space complexity: O(1)
    boolean[][] rows = new boolean[9][9];
    boolean[][] cols = new boolean[9][9];
    boolean[][] boxes = new boolean[9][9];
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (board[i][j] == '.') {
          continue;
        }
        int index = board[i][j] - '1';
        int box = (i / 3) * 3 + j / 3;
        if (rows[i][index] || cols[j][index] || boxes[box][index]) {
          return false;
        }
        rows[i][index] = true;
        cols[j][index] = true;
        boxes[box][index] = true;
      }
    }
    return true;
  }
}
