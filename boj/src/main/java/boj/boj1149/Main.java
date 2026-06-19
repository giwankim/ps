package boj.boj1149;

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
  private static int[][] houses;
  private static int[][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      houses = new int[n][3];
      for (int i = 0; i < n; i++) {
        houses[i][0] = io.nextInt();
        houses[i][1] = io.nextInt();
        houses[i][2] = io.nextInt();
      }
      dp = new int[n][3];
      for (int[] row : dp) {
        Arrays.fill(row, -1);
      }
      int ans = Math.min(minCostFrom(0, 0), Math.min(minCostFrom(0, 1), minCostFrom(0, 2)));
      io.println(ans);
    }
  }

  private static int minCostFrom(int house, int prev) {
    if (house == n) {
      return 0;
    }
    if (dp[house][prev] != -1) {
      return dp[house][prev];
    }
    int result = Integer.MAX_VALUE;
    for (int color = 0; color < 3; color++) {
      if (color == prev) {
        continue;
      }
      result = Math.min(result, minCostFrom(house + 1, color) + houses[house][color]);
    }
    dp[house][prev] = result;
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
