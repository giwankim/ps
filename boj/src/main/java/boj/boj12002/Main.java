package boj.boj12002;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int[][] cows;
  private static int[] selected;
  private static int[] candidates;
  private static int k;
  private static int ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      cows = new int[n][2];
      for (int i = 0; i < n; i++) {
        cows[i][0] = io.nextInt();
        cows[i][1] = io.nextInt();
      }
      selected = new int[3];

      Integer[] indices = new Integer[n];
      for (int i = 0; i < n; i++) {
        indices[i] = i;
      }

      candidates = new int[12];
      k = 0;
      Arrays.sort(indices, Comparator.comparingInt(i -> cows[i][0]));
      for (int i = 0; i < 3; i++) {
        candidates[k++] = indices[i];
        candidates[k++] = indices[n - 1 - i];
      }
      Arrays.sort(indices, Comparator.comparingInt(i -> cows[i][1]));
      for (int i = 0; i < 3; i++) {
        candidates[k++] = indices[i];
        candidates[k++] = indices[n - 1 - i];
      }

      ans = Integer.MAX_VALUE;
      backtrack(0, 0);
      io.println(ans);
    }
  }

  private static void backtrack(int index, int start) {
    if (index == 3) {
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      for (int i = 0; i < n; i++) {
        if (i == selected[0] || i == selected[1] || i == selected[2]) {
          continue;
        }
        minX = Math.min(minX, cows[i][0]);
        minY = Math.min(minY, cows[i][1]);
        maxX = Math.max(maxX, cows[i][0]);
        maxY = Math.max(maxY, cows[i][1]);
      }
      int area = (maxX - minX) * (maxY - minY);
      ans = Math.min(ans, area);
      return;
    }
    for (int i = start; i < k; i++) {
      selected[index] = candidates[i];
      backtrack(index + 1, i + 1);
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
