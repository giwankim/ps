package boj.boj2583;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
  private static final int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  private static int m, n, k;
  private static int[][] grid;
  private static boolean[][] visited;
  private static List<Integer> areas;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      m = io.nextInt();
      n = io.nextInt();
      k = io.nextInt();
      grid = new int[n][m];
      visited = new boolean[n][m];
      for (int i = 0; i < k; i++) {
        int x1 = io.nextInt();
        int y1 = io.nextInt();
        int x2 = io.nextInt();
        int y2 = io.nextInt();
        for (int x = x1; x < x2; x++) {
          for (int y = y1; y < y2; y++) {
            grid[x][y] = 1;
          }
        }
      }

      areas = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
          if (visited[i][j] || grid[i][j] == 1) {
            continue;
          }
          areas.add(bfs(i, j));
        }
      }
      areas.sort(Integer::compareTo);
      StringBuilder ans = new StringBuilder();
      ans.append(areas.size()).append('\n');
      for (int i = 0; i < areas.size(); i++) {
        if (i == areas.size() - 1) {
          ans.append(areas.get(i));
        } else {
          ans.append(areas.get(i)).append(' ');
        }
      }
      io.println(ans);
    }
  }

  private static int bfs(int x, int y) {
    int result = 0;
    Queue<int[]> queue = new ArrayDeque<>();
    queue.offer(new int[] {x, y});
    visited[x][y] = true;
    while (!queue.isEmpty()) {
      int[] curr = queue.poll();
      result++;
      for (int[] dir : DIRS) {
        int nx = curr[0] + dir[0];
        int ny = curr[1] + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= m || visited[nx][ny] || grid[nx][ny] == 1) {
          continue;
        }
        queue.offer(new int[] {nx, ny});
        visited[nx][ny] = true;
      }
    }
    return result;
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, PrintStream out) {
      super(out);
      r = new BufferedReader(new InputStreamReader(in));
    }

    public boolean hasNext() throws IOException {
      while (st == null || !st.hasMoreTokens()) {
        String line = r.readLine();
        if (line == null) {
          return false;
        }
        st = new StringTokenizer(line);
      }
      return true;
    }

    public String next() throws IOException {
      while (st == null || !st.hasMoreTokens()) {
        st = new StringTokenizer(r.readLine());
      }
      return st.nextToken();
    }

    public int nextInt() throws IOException {
      return Integer.parseInt(next());
    }

    public long nextLong() throws IOException {
      return Long.parseLong(next());
    }

    public double nextDouble() throws IOException {
      return Double.parseDouble(next());
    }
  }
}
