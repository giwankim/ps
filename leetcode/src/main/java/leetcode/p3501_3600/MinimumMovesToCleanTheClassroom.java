package leetcode.p3501_3600;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * <a href="https://leetcode.com/problems/minimum-moves-to-clean-the-classroom/">3568. Minimum Moves
 * to Clean the Classroom</a>
 */
public class MinimumMovesToCleanTheClassroom {
  private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

  /**
   * @implNote Time {@code O(n * m * 2^k * energy)} since a (cell, mask) state re-enters the queue
   *     only when its entry in the monotone {@code best} table strictly rises, capping admissions
   *     at {@code energy + 1} per state with four O(1) neighbor probes per poll; auxiliary space
   *     matches that bound for the queue in the worst case, atop {@code O(n * m * 2^k)} for
   *     {@code best}, where {@code n = classroom.length}, {@code m = classroom[0].length()}, and
   *     {@code k} counts the {@code 'L'} cells (at most 10).
   */
  public int minMoves(String[] classroom, int energy) {
    int n = classroom.length;
    int m = classroom[0].length();

    int[][] litterIdx = new int[n][m];
    int litters = 0;
    int sx = -1, sy = -1;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (classroom[i].charAt(j) == 'S') {
          sx = i;
          sy = j;
        } else if (classroom[i].charAt(j) == 'L') {
          litterIdx[i][j] = litters++;
        }
      }
    }

    int full = (1 << litters) - 1;

    int[][][] best = new int[n][m][1 << litters];
    for (int[][] a : best) {
      for (int[] b : a) {
        Arrays.fill(b, Integer.MIN_VALUE);
      }
    }
    best[sx][sy][0] = energy;

    Queue<State> queue = new ArrayDeque<>();
    queue.offer(new State(sx, sy, 0, 0));

    while (!queue.isEmpty()) {
      State state = queue.poll();
      if (state.mask == full) {
        return state.steps;
      }

      for (int[] dir : DIRS) {
        int nx = state.x + dir[0];
        int ny = state.y + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= m || classroom[nx].charAt(ny) == 'X') {
          continue;
        }
        int nmask =
            classroom[nx].charAt(ny) == 'L' ? state.mask | (1 << litterIdx[nx][ny]) : state.mask;
        int e = best[state.x][state.y][state.mask];
        int ne = classroom[nx].charAt(ny) == 'R' ? energy : e - 1;
        if (e == 0 || ne <= best[nx][ny][nmask]) {
          continue;
        }
        best[nx][ny][nmask] = ne;
        queue.offer(new State(nx, ny, nmask, state.steps + 1));
      }
    }
    return -1;
  }

  private record State(int x, int y, int mask, int steps) {}
}
