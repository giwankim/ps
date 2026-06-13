package boj.boj14501;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int[] time;
  private static int[] price;
  private static int ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      time = new int[n];
      price = new int[n];
      for (int i = 0; i < n; i++) {
        time[i] = io.nextInt();
        price[i] = io.nextInt();
      }
      ans = 0;
      backtrack(0, 0);
      io.println(ans);
    }
  }

  public static void backtrack(int index, int profit) {
    if (index > n) {
      return;
    }
    if (index == n) {
      ans = Math.max(ans, profit);
      return;
    }
    backtrack(index + time[index], profit + price[index]);
    backtrack(index + 1, profit);
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, OutputStream out) {
      super(out); // PrintWriter(OutputStream) buffers through an internal BufferedWriter
      r = new BufferedReader(new InputStreamReader(in));
    }

    /** Next whitespace-delimited token, pulling fresh lines across boundaries as needed. */
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
