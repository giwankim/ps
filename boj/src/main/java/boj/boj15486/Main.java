package boj.boj15486;

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
  private static int[] t;
  private static int[] p;
  private static int[] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      t = new int[n];
      p = new int[n];
      for (int i = 0; i < n; i++) {
        t[i] = io.nextInt();
        p[i] = io.nextInt();
      }
      dp = new int[n];
      Arrays.fill(dp, -1);
      io.println(backtrack(0));
    }
  }

  private static int backtrack(int day) {
    if (day > n) {
      return Integer.MIN_VALUE;
    }
    if (day == n) {
      return 0;
    }
    if (dp[day] != -1) {
      return dp[day];
    }
    int take = backtrack(day + t[day]) + p[day];
    int skip = backtrack(day + 1);
    dp[day] = Math.max(take, skip);
    return dp[day];
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
