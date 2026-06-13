package boj.boj2961;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int[][] flavors;
  private static long ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      flavors = new int[n][2];
      for (int i = 0; i < n; i++) {
        flavors[i][0] = io.nextInt();
        flavors[i][1] = io.nextInt();
      }
      ans = Long.MAX_VALUE;
      backtrack(0, 1, 0, false);
      io.println(ans);
    }
  }

  private static void backtrack(int i, long sour, long bitter, boolean picked) {
    if (i == n) {
      if (picked) {
        ans = Math.min(ans, Math.abs(sour - bitter));
      }
      return;
    }
    backtrack(i + 1, sour * flavors[i][0], bitter + flavors[i][1], true);
    backtrack(i + 1, sour, bitter, picked);
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
