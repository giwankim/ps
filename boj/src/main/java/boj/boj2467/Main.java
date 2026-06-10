package boj.boj2467;

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
      int[] liquids = new int[n];
      for (int i = 0; i < n; i++) {
        liquids[i] = io.nextInt();
      }
      int[] ans = binarySearch(liquids);
      io.println(liquids[ans[0]] + " " + liquids[ans[1]]);
    }
  }

  private static int[] binarySearch(int[] liquids) {
    int n = liquids.length;
    int minSum = Integer.MAX_VALUE;
    int[] result = {-1, -1};
    for (int i = 0; i < n - 1; i++) {
      int idx = lowerBound(liquids, i + 1, -liquids[i]);
      if (idx < n) {
        int sum = Math.abs(liquids[idx] + liquids[i]);
        if (sum < minSum) {
          minSum = sum;
          result[0] = i;
          result[1] = idx;
        }
      }
      if (idx - 1 > i) {
        int sum = Math.abs(liquids[idx - 1] + liquids[i]);
        if (sum < minSum) {
          minSum = sum;
          result[0] = i;
          result[1] = idx - 1;
        }
      }
    }
    return result;
  }

  private static int lowerBound(int[] arr, int from, int target) {
    int result = arr.length;
    int lo = from;
    int hi = arr.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (arr[mid] >= target) {
        result = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return result;
  }

  private static int[] twoPointer(int[] liquids) {
    int n = liquids.length;
    int bestSum = liquids[0] + liquids[n - 1];
    int[] result = {0, n - 1};
    int lo = 0;
    int hi = n - 1;
    while (lo < hi) {
      int sum = liquids[lo] + liquids[hi];
      if (Math.abs(sum) < Math.abs(bestSum)) {
        bestSum = sum;
        result[0] = lo;
        result[1] = hi;
      }
      if (sum == 0) {
        break;
      }
      if (sum < 0) {
        lo++;
      } else {
        hi--;
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
