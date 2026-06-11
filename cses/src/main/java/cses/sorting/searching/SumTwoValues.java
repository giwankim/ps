package cses.sorting.searching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class SumTwoValues {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      int x = io.nextInt();
      int[][] nums = new int[n][2];
      for (int i = 0; i < n; i++) {
        nums[i][0] = io.nextInt();
        nums[i][1] = i + 1;
      }

      Arrays.sort(nums, Comparator.comparingInt(a -> a[0]));

      int lo = 0;
      int hi = n - 1;
      boolean found = false;
      while (lo < hi) {
        long sum = (long) nums[lo][0] + nums[hi][0];
        if (sum == x) {
          found = true;
          break;
        } else if (sum < x) {
          lo += 1;
        } else {
          hi -= 1;
        }
      }
      if (found) {
        io.println(nums[lo][1] + " " + nums[hi][1]);
      } else {
        io.println("IMPOSSIBLE");
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
