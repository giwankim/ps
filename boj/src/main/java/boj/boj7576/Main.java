package boj.boj7576;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
  public static final int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  private static int n, m;
  private static int[][] box;
  private static boolean[][] visited;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      m = io.nextInt();
      n = io.nextInt();
      box = new int[n][m];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
          box[i][j] = io.nextInt();
        }
      }

      int unripe = 0;
      Queue<int[]> queue = new ArrayDeque<>();
      visited = new boolean[n][m];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
          if (box[i][j] == 1) {
            queue.offer(new int[] {i, j});
            visited[i][j] = true;
          } else if (box[i][j] == 0) {
            unripe++;
          }
        }
      }

      int ans = -1;
      while (!queue.isEmpty()) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
          int[] curr = queue.poll();
          int x = curr[0];
          int y = curr[1];
          for (int[] dir : DIRS) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx < 0 || nx >= n || ny < 0 || ny >= m || visited[nx][ny] || box[nx][ny] != 0) {
              continue;
            }
            queue.offer(new int[] {nx, ny});
            visited[nx][ny] = true;
            unripe--;
          }
        }
        ans++;
      }

      if (unripe == 0) {
        io.println(ans);
      } else {
        io.println(-1);
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
