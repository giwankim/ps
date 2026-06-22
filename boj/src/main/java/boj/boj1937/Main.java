package boj.boj1937;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  public static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

  private static int n;
  private static int[][] forest;
  private static int[][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      forest = new int[n][n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          forest[i][j] = io.nextInt();
        }
      }
      dp = new int[n][n];
      for (int[] row : dp) {
        Arrays.fill(row, -1);
      }
      int ans = Integer.MIN_VALUE;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          ans = Math.max(ans, eat(i, j));
        }
      }
      io.println(ans);
    }
  }

  private static int eat(int x, int y) {
    if (dp[x][y] != -1) {
      return dp[x][y];
    }
    int result = 1;
    for (int[] dir : DIRECTIONS) {
      int nx = x + dir[0];
      int ny = y + dir[1];
      if (nx < 0 || nx >= n || ny < 0 || ny >= n) {
        continue;
      }
      if (forest[x][y] < forest[nx][ny]) {
        result = Math.max(result, eat(nx, ny) + 1);
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
