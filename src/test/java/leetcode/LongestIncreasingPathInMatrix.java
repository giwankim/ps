package leetcode;

public class LongestIncreasingPathInMatrix {
  private static int[] dx = {0, 0, -1, 1};
  private static int[] dy = {-1, 1, 0, 0};

  public int longestIncreasingPath(int[][] matrix) {
    int[][] memo = new int[matrix.length][matrix[0].length];
    int result = Integer.MIN_VALUE;
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {
        result = Math.max(result, longestIncreasingPath(i, j, matrix, memo));
      }
    }
    return result;
  }

  private int longestIncreasingPath(int i, int j, int[][] matrix, int[][] memo) {
    if (memo[i][j] != 0) {
      return memo[i][j];
    }
    int result = 1;
    for (int d = 0; d < 4; d++) {
      int nx = i + dx[d];
      int ny = j + dy[d];
      if (nx < 0 || nx >= matrix.length || ny < 0 || ny >= matrix[0].length) {
        continue;
      }
      if (matrix[i][j] < matrix[nx][ny]) {
        result = Math.max(result, longestIncreasingPath(nx, ny, matrix, memo) + 1);
      }
    }
    memo[i][j] = result;
    return result;
  }
}
