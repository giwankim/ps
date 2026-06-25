package boj.boj2216;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static int a, b, c, n, m;
  private static String x, y;
  private static int[][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      a = io.nextInt();
      b = io.nextInt();
      c = io.nextInt();
      x = io.next();
      y = io.next();
      n = x.length();
      m = y.length();
      dp = new int[n + 1][m + 1];
      for (int[] row : dp) {
        Arrays.fill(row, -1);
      }
      io.println(maxPoints(n, m));
    }
  }

  private static int maxPoints(int i, int j) {
    if (i == 0 || j == 0) {
      return i == 0 ? j * b : i * b;
    }
    if (dp[i][j] != -1) {
      return dp[i][j];
    }
    int result = Math.max(maxPoints(i - 1, j) + b, maxPoints(i, j - 1) + b);
    if (x.charAt(i - 1) == y.charAt(j - 1)) {
      result = Math.max(result, maxPoints(i - 1, j - 1) + a);
    } else {
      result = Math.max(result, maxPoints(i - 1, j - 1) + c);
    }
    dp[i][j] = result;
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
