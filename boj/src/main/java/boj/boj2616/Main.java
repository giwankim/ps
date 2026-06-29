package boj.boj2616;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      int[] a = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = io.nextInt();
      }
      int k = io.nextInt();

      int[] prefix = new int[n + 1];
      for (int i = 1; i <= n; i++) {
        prefix[i] = prefix[i - 1] + a[i - 1];
      }

      int[][] dp = new int[n + 1][4];
      for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= 3; j++) {
          dp[i][j] = dp[i - 1][j];
          if (i >= k) {
            int window = prefix[i] - prefix[i - k];
            dp[i][j] = Math.max(dp[i][j], dp[i - k][j - 1] + window);
          }
        }
      }
      io.println(dp[n][3]);
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
