package boj.boj23831;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static int n, a, b;
  private static int[] p, q, r, s;
  private static int[][][][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      a = io.nextInt();
      b = io.nextInt();
      p = new int[n];
      q = new int[n];
      r = new int[n];
      s = new int[n];
      for (int i = 0; i < n; i++) {
        p[i] = io.nextInt();
        q[i] = io.nextInt();
        r[i] = io.nextInt();
        s[i] = io.nextInt();
      }
      dp = new int[n + 1][a + 1][n + 1][2];
      for (int[][][] cube : dp) {
        for (int[][] plane : cube) {
          for (int[] row : plane) {
            Arrays.fill(row, -1);
          }
        }
      }
      io.println(schedule(0, 0, 0, 0));
    }
  }

  private static int schedule(int index, int rest, int study, int prev) {
    if (rest > a) {
      return Integer.MIN_VALUE;
    }
    if (index == n) {
      if (study < b) {
        return Integer.MIN_VALUE;
      }
      return 0;
    }
    if (dp[index][rest][study][prev] != -1) {
      return dp[index][rest][study][prev];
    }
    int result = Integer.MIN_VALUE;
    result = Math.max(result, p[index] + schedule(index + 1, rest, study + 1, 0));
    result = Math.max(result, q[index] + schedule(index + 1, rest, study + 1, 0));
    if (prev == 0) {
      result = Math.max(result, r[index] + schedule(index + 1, rest, study, 1));
    }
    result = Math.max(result, s[index] + schedule(index + 1, rest + 1, study, 0));
    dp[index][rest][study][prev] = result;
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
