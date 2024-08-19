package com.giwankim.leetcode;

public class NumberOfIslands {
  public int numIslands(char[][] grid) {
    int count = 0;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] == '1') {
          dfs(i, j, grid);
          count += 1;
        }
      }
    }
    return count;
  }

  private void dfs(int i, int j, char[][] grid) {
    if (i < 0 || i == grid.length || j < 0 || j == grid[i].length) {
      return;
    }
    if (grid[i][j] == '0') {
      return;
    }
    grid[i][j] = '0';
    dfs(i, j + 1, grid);
    dfs(i, j - 1, grid);
    dfs(i + 1, j, grid);
    dfs(i - 1, j, grid);
  }
}
