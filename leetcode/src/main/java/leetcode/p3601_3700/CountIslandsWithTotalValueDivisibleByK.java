package leetcode.p3601_3700;

/**
 * <a href="https://leetcode.com/problems/count-islands-with-total-value-divisible-by-k/">3619.
 * Count Islands With Total Value Divisible by K</a>
 */
public class CountIslandsWithTotalValueDivisibleByK {
  private int n, m;
  private int[][] grid;

  /**
   * @implNote Time {@code O(n * m)}, auxiliary space {@code O(n * m)} for the DFS call stack, where
   *     {@code n = grid.length} is the number of rows and {@code m = grid[0].length} the number of
   *     columns. Each land cell is sunk to {@code 0} on entry, so {@code grid} doubles as the
   *     visited set: a cell is expanded once and its four neighbors are probed once. A snake-shaped
   *     island drives the recursion depth to {@code n * m}, up to 1e5 frames. The island total is a
   *     {@code long} because up to 1e5 cells of 1e6 reach 1e11, past {@code int}.
   */
  public int countIslands(int[][] grid, int k) {
    n = grid.length;
    m = grid[0].length;
    this.grid = grid;

    int ans = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (grid[i][j] == 0) {
          continue;
        }
        if (dfs(i, j) % k == 0) {
          ans++;
        }
      }
    }
    return ans;
  }

  private long dfs(int i, int j) {
    if (i < 0 || i >= n || j < 0 || j >= m || grid[i][j] == 0) {
      return 0L;
    }
    long result = grid[i][j];
    grid[i][j] = 0;
    result += dfs(i - 1, j);
    result += dfs(i + 1, j);
    result += dfs(i, j - 1);
    result += dfs(i, j + 1);
    return result;
  }
}
