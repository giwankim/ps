package leetcode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class FindASafeWalkThroughAGrid {
  private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  /**
   * @implNote Time {@code O(n * m)}, auxiliary space {@code O(n * m)} for the {@code dist} grid and
   *     the deque, where {@code n = grid.size()} is the number of rows and {@code m =
   *     grid.getFirst().size()} the number of columns. 0-1 BFS polls cells in nondecreasing damage
   *     order (safe cells jump to the deque's front, unsafe cells wait at the back), so each cell
   *     is enqueued at most once.
   */
  public boolean findSafeWalk(List<List<Integer>> grid, int health) {
    int n = grid.size();
    int m = grid.getFirst().size();
    int[][] dist = new int[n][m];
    for (int[] row : dist) {
      Arrays.fill(row, Integer.MAX_VALUE);
    }
    Deque<int[]> queue = new ArrayDeque<>();
    queue.offer(new int[] {0, 0});
    dist[0][0] = grid.getFirst().getFirst();
    while (!queue.isEmpty()) {
      int[] curr = queue.poll();
      int x = curr[0];
      int y = curr[1];
      if (x == n - 1 && y == m - 1) {
        return health - dist[x][y] >= 1;
      }
      for (int[] dir : DIRECTIONS) {
        int nx = x + dir[0];
        int ny = y + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= m || dist[nx][ny] != Integer.MAX_VALUE) {
          continue;
        }
        if (grid.get(nx).get(ny) == 0) {
          queue.offerFirst(new int[] {nx, ny});
        } else {
          queue.offerLast(new int[] {nx, ny});
        }
        dist[nx][ny] = dist[x][y] + grid.get(nx).get(ny);
      }
    }
    return false;
  }
}
