package boj.boj5569;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static final int MOD = 100_000;

  private static int w;
  private static int h;
  private static int[][][][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      w = io.nextInt();
      h = io.nextInt();
      dp = new int[w + 1][h + 1][2][2];
      for (int[][][] cube : dp) {
        for (int[][] plane : cube) {
          for (int[] row : plane) {
            Arrays.fill(row, -1);
          }
        }
      }
      int ans = countPaths(2, 1, 0, 1) + countPaths(1, 2, 1, 1);
      ans %= MOD;
      io.println(ans);
    }
  }

  private static int countPaths(int x, int y, int dir, int canTurn) {
    if (x > w || y > h) {
      return 0;
    }
    if (x == w && y == h) {
      return 1;
    }
    if (dp[x][y][dir][canTurn] != -1) {
      return dp[x][y][dir][canTurn];
    }

    int result = 0;
    // turn
    if (canTurn == 1) {
      if (dir == 0) {
        result += countPaths(x, y + 1, 1, 0);
        result %= MOD;
      }
      if (dir == 1) {
        result += countPaths(x + 1, y, 0, 0);
        result %= MOD;
      }
    }
    // no turn
    if (dir == 0) {
      result += countPaths(x + 1, y, 0, 1);
      result %= MOD;
    }
    if (dir == 1) {
      result += countPaths(x, y + 1, 1, 1);
      result %= MOD;
    }

    dp[x][y][dir][canTurn] = result;
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
