package boj.boj1695;

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
      io.println(palindrome(0, n - 1));
    }
  }

  private static int palindrome(int start, int end) {
    if (start >= end) {
      return 0;
    }
    if (dp[start][end] != -1) {
      return dp[start][end];
    }
    int result = arr[start] == arr[end]
        ? palindrome(start + 1, end - 1)
        : Math.min(palindrome(start + 1, end), palindrome(start, end - 1)) + 1;
    dp[start][end] = result;
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
