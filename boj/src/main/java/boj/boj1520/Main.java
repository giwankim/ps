package boj.boj1520;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

  private static int n, m;
  private static int[][] map;
  private static int[][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      m = io.nextInt();
      map = new int[n][m];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
          map[i][j] = io.nextInt();
        }
      }
      dp = new int[n][m];
      for (int[] row : dp) {
        Arrays.fill(row, -1);
      }
      io.println(countFrom(0, 0));
    }
  }

  private static int countFrom(int x, int y) {
    if (x == n - 1 && y == m - 1) {
      return 1;
    }
    if (dp[x][y] != -1) {
      return dp[x][y];
    }
    int result = 0;
    for (int[] dir : DIRECTIONS) {
      int nx = x + dir[0];
      int ny = y + dir[1];
      if (nx >= 0 && nx < n && ny >= 0 && ny < m && map[x][y] > map[nx][ny]) {
        result += countFrom(nx, ny);
      }
    }
    dp[x][y] = result;
    return result;
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, OutputStream out) {
      super(out);
      r = new BufferedReader(new InputStreamReader(in));
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
