package atcoder.abc472.d;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
  public static final int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
  private static int h, w, k;
  private static char[][] grid;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      h = io.nextInt();
      w = io.nextInt();
      k = io.nextInt();
      grid = new char[h][w];
      for (int i = 0; i < h; i++) {
        grid[i] = io.next().toCharArray();
      }
      // find safe empty cell
      boolean[] rows = new boolean[h];
      boolean[] cols = new boolean[w];
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          if (grid[i][j] == '#') {
            rows[i] = true;
            cols[j] = true;
          }
        }
      }
      Queue<int[]> queue = new LinkedList<>();
      boolean[][] visited = new boolean[h][w];
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          if (rows[i] || cols[j]) {
            continue;
          }
          queue.offer(new int[] {i, j});
          visited[i][j] = true;
        }
      }

      long ans = 0L;
      while (!queue.isEmpty()) {
        if (k-- < 0) {
          break;
        }
        int size = queue.size();
        for (int i = 0; i < size; i++) {
          int[] cell = queue.poll();
          ans++;
          for (int[] dir : DIRS) {
            int nx = cell[0] + dir[0];
            int ny = cell[1] + dir[1];
            if (nx < 0 || nx >= h || ny < 0 || ny >= w || visited[nx][ny] || grid[nx][ny] == '#') {
              continue;
            }
            queue.offer(new int[] {nx, ny});
            visited[nx][ny] = true;
          }
        }
      }
      io.println(ans);
    }
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, OutputStream out) {
      super(out); // PrintWriter(OutputStream) buffers through an internal BufferedWriter
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
