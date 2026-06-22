package boj.boj20544;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  public static final int MOD = 1_000_000_007;
  private static int n;
  private static int[][][] dp;
  private static int[][][] dp2;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      dp = new int[n + 1][3][3];
      for (int[][] plane : dp) {
        for (int[] row : plane) {
          Arrays.fill(row, -1);
        }
      }
      dp2 = new int[n + 1][3][3];
      for (int[][] plane : dp2) {
        for (int[] row : plane) {
          Arrays.fill(row, -1);
        }
      }
      io.println(countFrom(1, 0, 0) - countFromExcludeTwo(1, 0, 0));
    }
  }

  private static int countFrom(int index, int prev, int prePrev) {
    if (index == n) {
      return 1;
    }
    if (dp[index][prev][prePrev] != -1) {
      return dp[index][prev][prePrev];
    }
    int result = countFrom(index + 1, 0, prev);
    if (prev > 0 && prePrev > 0) { // 선인장 3개 연속 X
      return result;
    }
    result += countFrom(index + 1, 1, prev);
    result %= MOD;
    if (prev != 2) {
      result += countFrom(index + 1, 2, prev);
      result %= MOD;
    }
    dp[index][prev][prePrev] = result;
    return result;
  }

  private static int countFromExcludeTwo(int index, int prev, int prePrev) {
    if (index == n) {
      return 1;
    }
    if (dp2[index][prev][prePrev] != -1) {
      return dp2[index][prev][prePrev];
    }
    int result = countFromExcludeTwo(index + 1, 0, prev);
    if (prev > 0 && prePrev > 0) {
      return result;
    }
    result += countFromExcludeTwo(index + 1, 1, prev);
    result %= MOD;
    dp2[index][prev][prePrev] = result;
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
