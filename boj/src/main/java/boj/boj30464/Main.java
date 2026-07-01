package boj.boj30464;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int[] a;
  private static int[][][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      a = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = io.nextInt();
      }

      dp = new int[n][2][3];
      for (int[][] plane : dp) {
        for (int[] row : plane) {
          Arrays.fill(row, Integer.MIN_VALUE);
        }
      }
      dp[0][1][0] = 0;

      // turn 0
      for (int i = 0; i < n; i++) {
        relax(i, 1, 0);
      }
      // turn 1
      for (int i = n - 1; i >= 0; i--) {
        relax(i, 0, 1);
      }
      // turn 2
      for (int i = 0; i < n; i++) {
        relax(i, 1, 2);
      }

      int ans = Math.max(dp[n - 1][1][0], dp[n - 1][0][1]);
      ans = Math.max(ans, dp[n - 1][1][2]);
      if (ans == Integer.MIN_VALUE) {
        io.println(-1);
      } else {
        io.println(ans);
      }
    }
  }

  private static void relax(int curr, int dir, int turns) {
    if (curr == n - 1) {
      return;
    }
    if (a[curr] == 0) {
      return;
    }
    int v = dp[curr][dir][turns];
    if (v == Integer.MIN_VALUE) {
      return;
    }
    // continue
    int next = dir == 1 ? curr + a[curr] : curr - a[curr];
    if (next >= 0 && next < n) {
      dp[next][dir][turns] = Math.max(dp[next][dir][turns], v + 1);
    }
    // turn
    next = dir == 1 ? curr - a[curr] : curr + a[curr];
    if (next >= 0 && next < n && turns < 2) {
      dp[next][dir ^ 1][turns + 1] = Math.max(dp[next][dir ^ 1][turns + 1], v + 1);
    }
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
