package leetcode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class FindTheSafestPathInAGrid {
  private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  /**
   * @implNote Time {@code O(n² log n)}, space {@code O(n²)}, where {@code n = grid.size()}.
   *     <p><b>Time:</b> {@link #getDistances} runs one multi-source BFS over all {@code n²} cells
   *     in {@code O(n²)}. The safeness factor then falls out of a binary search over the candidate
   *     range {@code [0, 2n]} — {@code O(log n)} probes, each an {@code O(n²)} reachability BFS via
   *     {@link #pathExists} — giving {@code O(n² log n)}, which dominates the precompute.
   *     <p><b>Space:</b> {@code O(n²)} for the distance grid plus the {@code visited} array and
   *     queue of each BFS.
   */
  public int maximumSafenessFactor(List<List<Integer>> grid) {
    int n = grid.size();
    int[][] dists = getDistances(grid);
    int lo = 0;
    int hi = n * 2;
    int result = Integer.MIN_VALUE;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (pathExists(dists, mid)) {
        result = mid;
        lo = mid + 1;
      } else {
        hi = mid - 1;
      }
    }
    return result;
  }

  /**
   * @implNote Time {@code O(n²)}, space {@code O(n²)}, where {@code n = dists.length}. A single BFS
   *     from the top-left corner, admitting only cells at least {@code safeness} from a thief,
   *     visits each cell at most once.
   */
  private static boolean pathExists(int[][] dists, int safeness) {
    int n = dists.length;
    if (dists[0][0] < safeness || dists[n - 1][n - 1] < safeness) {
      return false;
    }
    Queue<int[]> queue = new ArrayDeque<>();
    boolean[][] visited = new boolean[n][n];
    queue.offer(new int[] {0, 0});
    visited[0][0] = true;
    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      int x = cell[0];
      int y = cell[1];
      if (x == n - 1 && y == n - 1) {
        return true;
      }
      for (int[] dir : DIRECTIONS) {
        int nx = x + dir[0];
        int ny = y + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= n || visited[nx][ny] || dists[nx][ny] < safeness) {
          continue;
        }
        queue.offer(new int[] {nx, ny});
        visited[nx][ny] = true;
      }
    }
    return false;
  }

  /**
   * @implNote Time {@code O(n²)}, space {@code O(n²)}, where {@code n = grid.size()}. Multi-source
   *     BFS seeded from every thief settles each of the {@code n²} cells exactly once, giving its
   *     Manhattan distance to the nearest thief.
   */
  private static int[][] getDistances(List<List<Integer>> grid) {
    int n = grid.size();
    int[][] result = new int[n][n];
    for (int[] row : result) {
      Arrays.fill(row, Integer.MAX_VALUE);
    }

    Queue<int[]> queue = new ArrayDeque<>();
    boolean[][] visited = new boolean[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (grid.get(i).get(j) == 1) {
          queue.offer(new int[] {i, j});
          visited[i][j] = true;
        }
      }
    }

    int d = 0;
    while (!queue.isEmpty()) {
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        int[] cell = queue.poll();
        int x = cell[0];
        int y = cell[1];
        result[x][y] = d;
        for (int[] dir : DIRECTIONS) {
          int nx = x + dir[0];
          int ny = y + dir[1];
          if (nx < 0 || nx >= n || ny < 0 || ny >= n || visited[nx][ny]) {
            continue;
          }
          queue.offer(new int[] {nx, ny});
          visited[nx][ny] = true;
        }
      }
      d++;
    }
    return result;
  }
}
