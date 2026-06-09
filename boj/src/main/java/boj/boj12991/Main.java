package boj.boj12991;

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
      int k = io.nextInt();
      int[] a = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = io.nextInt();
      }
      int[] b = new int[n];
      for (int i = 0; i < n; i++) {
        b[i] = io.nextInt();
      }

      Arrays.sort(a);
      Arrays.sort(b);

      long ans = -1;
      long lo = (long) a[0] * b[0];
      long hi = (long) a[n - 1] * b[n - 1];
      while (lo <= hi) {
        long mid = lo + (hi - lo) / 2;
        if (count(a, b, mid) >= k) {
          ans = mid;
          hi = mid - 1;
        } else {
          lo = mid + 1;
        }
      }
      io.println(ans);
    }
  }

  private static int count(int[] a, int[] b, long target) {
    int result = 0;
    for (int i = 0; i < a.length; i++) {
      result += upperBound(b, target / a[i]);
    }
    return result;
  }

  private static int upperBound(int[] arr, long target) {
    int result = arr.length;
    int lo = 0;
    int hi = arr.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (arr[mid] > target) {
        result = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
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
