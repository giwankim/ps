package leetcode;

public class WordSearch {
  public boolean exist(char[][] board, String word) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        boolean[][] visited = new boolean[board.length][board[0].length];
        if (exist(i, j, 0, visited, board, word)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean exist(int x, int y, int i, boolean[][] visited, char[][] board, String word) {
    if (i == word.length()) {
      return true;
    }
    if (x < 0
        || x >= board.length
        || y < 0
        || y >= board[x].length
        || visited[x][y]
        || word.charAt(i) != board[x][y]) {
      return false;
    }
    visited[x][y] = true;
    if (exist(x - 1, y, i + 1, visited, board, word)
        || exist(x, y - 1, i + 1, visited, board, word)
        || exist(x, y + 1, i + 1, visited, board, word)
        || exist(x + 1, y, i + 1, visited, board, word)) {
      return true;
    }
    visited[x][y] = false;
    return false;
  }
}
