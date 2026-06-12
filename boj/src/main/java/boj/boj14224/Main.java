package boj.boj14224;

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
      int k = io.nextInt();
      int[][] points = new int[n][2];
      for (int i = 0; i < n; i++) {
        points[i][0] = io.nextInt();
        points[i][1] = io.nextInt();
      }

      int side = -1;
      int lo = 0;
      int hi = Integer.MAX_VALUE;
      while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        if (isFeasible(points, k, mid)) {
          side = mid;
          hi = mid - 1;
        } else {
          lo = mid + 1;
        }
      }

      long ans = (side + 2);
      ans *= ans;
      io.println(ans);
    }
  }

  private static boolean isFeasible(int[][] points, int k, int side) {
    int n = points.length;
    for (int i = 0; i < n; i++) {
      long minX = points[i][0];
      long maxX = minX + side;
      for (int j = 0; j < n; j++) {
        long minY = points[j][1];
        long maxY = minY + side;
        int cnt = 0;
        for (int l = 0; l < n; l++) {
          int x = points[l][0];
          int y = points[l][1];
          if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
            cnt++;
          }
        }
        if (cnt >= k) {
          return true;
        }
      }
    }
    return false;
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
