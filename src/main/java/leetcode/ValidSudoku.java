package leetcode;

public class ValidSudoku {
  public boolean isValidSudoku(char[][] board) {
    int n = 9;
    boolean[][] rows = new boolean[n][n];
    boolean[][] columns = new boolean[n][n];
    boolean[][] boxes = new boolean[n][n];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        char c = board[i][j];
        if (c == '.') {
          continue;
        }

        int position = c - '1';

        // row
        if (rows[i][position]) {
          return false;
        }
        rows[i][position] = true;

        // columns
        if (columns[j][position]) {
          return false;
        }
        columns[j][position] = true;

        // sub-box
        int idx = (i / 3) * 3 + (j / 3);
        if (boxes[idx][position]) {
          return false;
        }
        boxes[idx][position] = true;
      }
    }

    return true;
  }
}
