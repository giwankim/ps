package boj.boj1637;

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
      int[] c = new int[n];
      int[] b = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = io.nextInt();
        c[i] = io.nextInt();
        b[i] = io.nextInt();
      }

      long ans = -1;
      long lo = 1;
      long hi = Integer.MAX_VALUE;
      while (lo <= hi) {
        long mid = lo + (hi - lo) / 2;
        if ((count(a, b, c, mid) & 1) == 1) {
          ans = mid;
          hi = mid - 1;
        } else {
          lo = mid + 1;
        }
      }
      if (ans == -1) {
        io.println("NOTHING");
        return;
      }
      long cnt = count(a, b, c, ans) - count(a, b, c, ans - 1);
      io.println(ans + " " + cnt);
    }
  }

  private static long count(int[] a, int[] b, int[] c, long x) {
    long result = 0;
    for (int i = 0; i < a.length; i++) {
      if (x < a[i]) {
        continue;
      }
      result += (Math.min(x, c[i]) - a[i]) / b[i] + 1;
    }
    return result;
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
