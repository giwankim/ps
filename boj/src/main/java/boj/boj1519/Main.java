package boj.boj1519;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      int[] dp = new int[n + 1];
      Arrays.fill(dp, -1);
      for (int i = 10; i <= n; i++) {
        String str = String.valueOf(i);
        for (int s = 0; s < str.length(); s++) {
          int ps = 0;
          for (int e = s; e < str.length(); e++) {
            if (s == 0 && e == str.length() - 1) {
              continue;
            }
            ps = 10 * ps + (str.charAt(e) - '0');
            if (ps > 0 && dp[i - ps] == -1) {
              if (dp[i] == -1 || dp[i] > ps) {
                dp[i] = ps;
              }
            }
          }
        }
      }
      io.println(dp[n]);
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
