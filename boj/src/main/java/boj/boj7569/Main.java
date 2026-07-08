package boj.boj7569;

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
  private static int[][] DIRS = {{-1, 0, 0}, {1, 0, 0}, {0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}
  };

  private static int n, m, h;
  private static int[][][] box;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      m = io.nextInt();
      n = io.nextInt();
      h = io.nextInt();
      box = new int[n][m][h];
      for (int k = 0; k < h; k++) {
        for (int i = 0; i < n; i++) {
          for (int j = 0; j < m; j++) {
            box[i][j][k] = io.nextInt();
          }
        }
      }

      Queue<int[]> queue = new ArrayDeque<>();
      boolean[][][] visited = new boolean[n][m][h];
      int unripe = 0;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
          for (int k = 0; k < h; k++) {
            if (box[i][j][k] == 1) {
              queue.add(new int[] {i, j, k});
              visited[i][j][k] = true;
            } else if (box[i][j][k] == 0) {
              unripe++;
            }
          }
        }
      }

      int ans = -1;
      while (!queue.isEmpty()) {
        int sz = queue.size();
        for (int i = 0; i < sz; i++) {
          int[] curr = queue.poll();
          int x = curr[0];
          int y = curr[1];
          int z = curr[2];
          for (int[] dir : DIRS) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            int nz = z + dir[2];
            if (nx < 0
                || nx >= n
                || ny < 0
                || ny >= m
                || nz < 0
                || nz >= h
                || visited[nx][ny][nz]
                || box[nx][ny][nz] != 0) {
              continue;
            }
            queue.offer(new int[] {nx, ny, nz});
            visited[nx][ny][nz] = true;
            unripe--;
          }
        }
        ans++;
      }
      io.println(unripe != 0 ? -1 : ans);
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
