package leetcode;

public class MaximalSquare {
  public int maximalSquare(char[][] matrix) {
    int result = 0;
    int[][] a = new int[matrix.length + 1][matrix[0].length + 1];
    for (int i = 1; i <= matrix.length; i++) {
      for (int j = 1; j <= matrix[0].length; j++) {
        if (matrix[i - 1][j - 1] == '0') {
          continue;
        }
        a[i][j] = 1 + Math.min(a[i - 1][j - 1], Math.min(a[i - 1][j], a[i][j - 1]));
        result = Math.max(result, a[i][j]);
      }
    }
    return result * result;
  }
}
