package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NQueens {
  public List<List<String>> solveNQueens(int n) {
    List<List<String>> result = new ArrayList<>();
    char[][] board = new char[n][n];
    for (char[] row : board) {
      Arrays.fill(row, '.');
    }
    solveNQueens(0, n, board, result);
    return result;
  }

  private void solveNQueens(int i, int n, char[][] board, List<List<String>> result) {
    if (i == n) {
      List<String> convertedBoard = Arrays.stream(board).map(String::new).toList();
      result.add(convertedBoard);
      return;
    }
    for (int j = 0; j < n; j++) {
      if (isValid(i, j, board)) {
        board[i][j] = 'Q';
        solveNQueens(i + 1, n, board, result);
        board[i][j] = '.';
      }
    }
  }

  private boolean isValid(int row, int col, char[][] board) {
    // check column
    for (int i = row; i >= 0; i--) {
      if (board[i][col] == 'Q') {
        return false;
      }
    }

    // check diagonal
    for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
      if (board[i][j] == 'Q') {
        return false;
      }
    }

    // check anti-diagonal
    for (int i = row, j = col; i >= 0 && j < board[0].length; i--, j++) {
      if (board[i][j] == 'Q') {
        return false;
      }
    }

    return true;
  }
}
