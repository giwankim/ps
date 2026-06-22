package boj.boj10942;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static int n, m;
  private static int[] arr;
  private static int[][] dp;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      arr = new int[n];
      for (int i = 0; i < n; i++) {
        arr[i] = io.nextInt();
      }

      dp = new int[n][n];
      for (int[] row : dp) {
        Arrays.fill(row, -1);
      }

      m = io.nextInt();
      while (m-- > 0) {
        int s = io.nextInt() - 1;
        int e = io.nextInt() - 1;
        io.println(isPalindrome(s, e));
      }
    }
  }

  private static int isPalindrome(int s, int e) {
    if (s >= e) {
      return 1;
    }
    if (dp[s][e] != -1) {
      return dp[s][e];
    }
    int result = arr[e] == arr[s] ? isPalindrome(s + 1, e - 1) : 0;
    dp[s][e] = result;
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
