package leetcode;

import java.util.LinkedList;
import java.util.Queue;

public class RottingOranges {
  private static int[] dx = {-1, 1, 0, 0};
  private static int[] dy = {0, 0, -1, 1};

  public int orangesRotting(int[][] grid) {
    int count = 0;
    int fresh = 0;

    Queue<int[]> queue = new LinkedList<>();

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] == 2) {
          queue.offer(new int[]{i, j});
        } else if (grid[i][j] == 1) {
          fresh += 1;
        }
      }
    }

    if (fresh == 0) {
      return 0;
    }

    while (!queue.isEmpty()) {
      count += 1;
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        int[] pos = queue.poll();
        int x = pos[0];
        int y = pos[1];
        for (int d = 0; d < 4; d++) {
          int nx = x + dx[d];
          int ny = y + dy[d];
          if (nx < 0 || nx >= grid.length || ny < 0 || ny >= grid[0].length) {
            continue;
          }
          if (grid[nx][ny] == 1) {
            fresh -= 1;
            grid[nx][ny] = 2;
            queue.offer(new int[]{nx, ny});
          }
        }
      }
    }

    if (fresh > 0) {
      return -1;
    }

    return count - 1;
  }
}
