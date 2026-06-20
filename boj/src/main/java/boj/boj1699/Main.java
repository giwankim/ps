package boj.boj1699;

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
  private static int[] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      dp = new int[n + 1];
      Arrays.fill(dp, -1);
      io.println(solve(n));
    }
  }

  private static int solve(int x) {
    if (x < 0) {
      return Integer.MAX_VALUE;
    }
    if (x == 0) {
      return 0;
    }
    if (dp[x] != -1) {
      return dp[x];
    }
    int result = Integer.MAX_VALUE;
    for (int i = 1; i * i <= x; i++) {
      result = Math.min(result, 1 + solve(x - i * i));
    }
    dp[x] = result;
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
