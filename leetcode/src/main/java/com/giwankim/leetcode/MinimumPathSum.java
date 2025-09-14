package com.giwankim.leetcode;

import java.util.Arrays;

public class MinimumPathSum {

  public int minPathSum(int[][] grid) {
    int n = grid.length;
    int m = grid[0].length;

    int[][] d = new int[n][m];

    // initialize the first row and column
    d[0][0] = grid[0][0];
    for (int i = 1; i < n; i++) {
      d[i][0] = d[i - 1][0] + grid[i][0];
    }
    for (int j = 1; j < m; j++) {
      d[0][j] = d[0][j - 1] + grid[0][j];
    }

    // find minimum path sum for the remaining cells
    for (int i = 1; i < n; i++) {
      for (int j = 1; j < m; j++) {
        d[i][j] = Math.min(d[i - 1][j], d[i][j - 1]) + grid[i][j];
      }
    }

    return d[n - 1][m - 1];
  }

  public int minPathSumRecursive(int[][] grid) {
    int[][] cache = new int[grid.length][grid[0].length];
    for (int[] row : cache) {
      Arrays.fill(row, -1);
    }
    return findMinPathSum(0, 0, grid, cache);
  }

  private int findMinPathSum(int row, int column, int[][] grid, int[][] cache) {
    if (row < 0 || row >= grid.length || column < 0 || column >= grid[0].length) {
      return Integer.MAX_VALUE;
    }

    // reached the bottom right corner
    if (row == grid.length - 1 && column == grid[0].length - 1) {
      return grid[row][column];
    }

    // cache hit
    if (cache[row][column] != -1) {
      return cache[row][column];
    }

    // cache miss so calculate the minimum path sum
    cache[row][column] =
        Math.min(
            findMinPathSum(row + 1, column, grid, cache),
            findMinPathSum(row, column + 1, grid, cache))
            + grid[row][column];

    return cache[row][column];
  }
}
