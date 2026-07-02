package leetcode;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class FindASafeWalkThroughAGrid {
  private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  public boolean findSafeWalk(List<List<Integer>> grid, int health) {
    int n = grid.size();
    int m = grid.getFirst().size();
    Queue<int[]> queue = new ArrayDeque<>();
    int[][] best = new int[n][m];
    int startH = grid.getFirst().getFirst() == 0 ? health : health - 1;
    queue.offer(new int[] {0, 0, startH});
    best[0][0] = startH;
    while (!queue.isEmpty()) {
      int[] curr = queue.poll();
      int x = curr[0];
      int y = curr[1];
      int h = curr[2];
      if (curr[0] == n - 1 && curr[1] == m - 1) {
        return true;
      }
      for (int[] dir : DIRECTIONS) {
        int nx = x + dir[0];
        int ny = y + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= m || h - grid.get(nx).get(ny) <= best[nx][ny]) {
          continue;
        }
        int nh = h - grid.get(nx).get(ny);
        queue.offer(new int[] {nx, ny, nh});
        best[nx][ny] = nh;
      }
    }
    return false;
  }
}
