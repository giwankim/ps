package boj.boj12865;

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
  private static int k;
  private static int[][] items;
  private static int[][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      k = io.nextInt();
      items = new int[n][2];
      for (int i = 0; i < n; i++) {
        items[i][0] = io.nextInt();
        items[i][1] = io.nextInt();
      }
      dp = new int[n][k + 1];
      for (int[] row : dp) {
        Arrays.fill(row, -1);
      }
      io.println(maxValueFrom(0, 0));
    }
  }

  private static int maxValueFrom(int item, int weight) {
    if (weight > k) {
      return Integer.MIN_VALUE;
    }
    if (item == n) {
      return 0;
    }
    if (dp[item][weight] != -1) {
      return dp[item][weight];
    }
    int pick = maxValueFrom(item + 1, weight + items[item][0]) + items[item][1];
    int skip = maxValueFrom(item + 1, weight);
    dp[item][weight] = Math.max(pick, skip);
    return dp[item][weight];
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
