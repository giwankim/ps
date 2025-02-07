package leetcode;

public class UniquePaths {
  public int uniquePaths(int m, int n) {
    int[][] a = new int[m + 1][n + 1];
    a[1][1] = 1;
    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        a[i][j] += a[i - 1][j] + a[i][j - 1];
      }
    }
    return a[m][n];
  }
}
