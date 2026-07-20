package leetcode;

import java.util.ArrayList;
import java.util.List;

public class Shift2DGrid {
  /**
   * @implNote Time {@code O(k * n * m)} for {@code k} single-step simulation rounds, each moving
   *     every cell once and copying the round's result back into {@code grid}, auxiliary space
   *     {@code O(n * m)} for the {@code result} grid, where {@code n = grid.length} is the number
   *     of rows and {@code m = grid[0].length} the number of columns.
   */
  public List<List<Integer>> shiftGrid(int[][] grid, int k) {
    int n = grid.length;
    int m = grid[0].length;

    List<List<Integer>> result = new ArrayList<>(n);
    for (int i = 0; i < n; i++) {
      List<Integer> row = new ArrayList<>(m);
      for (int j = 0; j < m; j++) {
        row.add(grid[i][j]);
      }
      result.add(row);
    }

    while (k-- > 0) {
      int shift = grid[n - 1][m - 1];
      for (int i = 0; i < n; i++) {
        result.get(i).set(0, shift);
        for (int j = 0; j < m - 1; j++) {
          result.get(i).set(j + 1, grid[i][j]);
        }
        shift = grid[i][m - 1];
      }

      // copy into grid
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
          grid[i][j] = result.get(i).get(j);
        }
      }
    }
    return result;
  }
}
