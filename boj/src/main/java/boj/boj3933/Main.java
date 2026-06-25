package boj.boj3933;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static final int MAX = 1 << 15;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int[][] dp = new int[MAX][5];
      dp[0][0] = 1;
      for (int s = 1; s * s < MAX; s++) {
        for (int cnt = 1; cnt <= 4; cnt++) {
          for (int v = s * s; v < MAX; v++) {
            dp[v][cnt] += dp[v - s * s][cnt - 1];
          }
        }
      }

      int n;
      while ((n = io.nextInt()) != 0) {
        int ans = dp[n][1] + dp[n][2] + dp[n][3] + dp[n][4];
        io.println(ans);
      }
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
