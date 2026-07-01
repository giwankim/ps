package boj.boj2600;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static int[] b = new int[3];
  private static int[][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      b[0] = io.nextInt();
      b[1] = io.nextInt();
      b[2] = io.nextInt();

      for (int i = 0; i < 5; i++) {
        int k1 = io.nextInt();
        int k2 = io.nextInt();

        dp = new int[k1 + 1][k2 + 1];
        for (int[] row : dp) {
          Arrays.fill(row, -1);
        }

        if (canWin(k1, k2) == 1) {
          io.println("A");
        } else {
          io.println("B");
        }
      }
    }
  }

  private static int canWin(int k1, int k2) {
    if (k1 == 0 && k2 == 0) {
      return 0;
    }
    if (dp[k1][k2] != -1) {
      return dp[k1][k2];
    }
    int result = 0;
    for (int m : b) {
      if (k1 - m >= 0 && canWin(k1 - m, k2) == 0) {
        result = 1;
        break;
      }
      if (k2 - m >= 0 && canWin(k1, k2 - m) == 0) {
        result = 1;
        break;
      }
    }
    dp[k1][k2] = result;
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
