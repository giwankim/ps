package boj.boj11066;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static int k;
  private static int[] pages;
  private static int[][] dp;
  private static int[] prefix;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int t = io.nextInt();
      while (t-- > 0) {
        k = io.nextInt();
        pages = new int[k];
        for (int i = 0; i < k; i++) {
          pages[i] = io.nextInt();
        }

        dp = new int[k][k];
        for (int[] row : dp) {
          Arrays.fill(row, -1);
        }

        prefix = new int[k + 1];
        for (int i = 1; i <= k; i++) {
          prefix[i] = prefix[i - 1] + pages[i - 1];
        }

        io.println(combine(0, k - 1));
      }
    }
  }

  private static int combine(int start, int end) {
    if (start == end) {
      return 0;
    }
    if (dp[start][end] != -1) {
      return dp[start][end];
    }
    int result = Integer.MAX_VALUE;
    for (int mid = start; mid < end; mid++) {
      result = Math.min(result, combine(start, mid) + combine(mid + 1, end));
    }
    result += prefix[end + 1] - prefix[start];
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
