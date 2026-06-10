package boj.boj8983;

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
      int m = io.nextInt();
      int n = io.nextInt();
      int l = io.nextInt();
      int[] guns = new int[m];
      for (int i = 0; i < m; i++) {
        guns[i] = io.nextInt();
      }
      int[][] animals = new int[n][2];
      for (int i = 0; i < n; i++) {
        animals[i][0] = io.nextInt();
        animals[i][1] = io.nextInt();
      }

      Arrays.sort(guns);

      int ans = 0;
      for (int[] animal : animals) {
        if (canShoot(animal, guns, l)) {
          ans++;
        }
      }
      io.println(ans);
    }
  }

  private static boolean canShoot(int[] animal, int[] guns, int l) {
    int idx = lowerBound(guns, animal[0]);
    int minDist = Integer.MAX_VALUE;
    if (idx < guns.length) {
      minDist = Math.abs(guns[idx] - animal[0]);
    }
    if (idx - 1 >= 0) {
      int dist = Math.abs(guns[idx - 1] - animal[0]);
      minDist = Math.min(minDist, dist);
    }
    return (long) minDist + animal[1] <= l;
  }

  private static int lowerBound(int[] arr, int target) {
    int result = arr.length;
    int lo = 0;
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
