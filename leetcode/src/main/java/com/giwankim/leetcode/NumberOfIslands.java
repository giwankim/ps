package com.giwankim.leetcode;

public class NumberOfIslands {
  public int numIslands(char[][] grid) {
    // Time complexity: O(m * n), Space complexity: O(1)
    int result = 0;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] == '1') {
          result += 1;
          dfs(grid, i, j);
        }
      }
    }
    return result;
  }

  private void dfs(char[][] grid, int i, int j) {
    if (i < 0 || i >= grid.length || j < 0 || j >= grid[i].length || grid[i][j] == '0') {
      return;
    }
    grid[i][j] = '0';
    dfs(grid, i + 1, j);
    dfs(grid, i - 1, j);
    dfs(grid, i, j + 1);
    dfs(grid, i, j - 1);
  }
}
