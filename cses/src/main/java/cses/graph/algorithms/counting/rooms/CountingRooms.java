package cses.graph.algorithms.counting.rooms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class CountingRooms {
  private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

  private static int n, m;
  private static char[][] map;
  private static boolean[][] visited;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      m = io.nextInt();
      map = new char[n][m];
      for (int i = 0; i < n; i++) {
        map[i] = io.next().toCharArray();
      }
      visited = new boolean[n][m];

      int ans = 0;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
          if (map[i][j] == '#' || visited[i][j]) {
            continue;
          }
          ans++;
          bfs(i, j);
        }
      }
      io.println(ans);
    }
  }

  private static void bfs(int i, int j) {
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[] {i, j});
    visited[i][j] = true;
    while (!queue.isEmpty()) {
      int[] curr = queue.poll();
      int x = curr[0];
      int y = curr[1];
      for (int[] dir : DIRS) {
        int nx = x + dir[0];
        int ny = y + dir[1];
        if (nx < 0 || nx >= n || ny < 0 || ny >= m || map[nx][ny] == '#' || visited[nx][ny]) {
          continue;
        }
        queue.offer(new int[] {nx, ny});
        visited[nx][ny] = true;
      }
    }
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
