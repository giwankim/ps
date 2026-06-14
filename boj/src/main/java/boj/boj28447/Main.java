package boj.boj28447;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int k;
  private static int[][] c;
  private static int[] selected;
  private static boolean[] visited;
  private static int ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      k = io.nextInt();
      c = new int[n][n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          c[i][j] = io.nextInt();
        }
      }
      selected = new int[k];
      visited = new boolean[n];
      ans = Integer.MIN_VALUE;
      backtrack(0, 0);
      io.println(ans);
    }
  }

  private static void backtrack(int index, int start) {
    if (index == k) {
      int sum = 0;
      for (int i = 0; i < k; i++) {
        for (int j = i + 1; j < k; j++) {
          sum += c[selected[i]][selected[j]];
        }
      }
      ans = Math.max(ans, sum);
      return;
    }
    for (int i = start; i < n; i++) {
      if (visited[i]) {
        continue;
      }
      selected[index] = i;
      visited[i] = true;
      backtrack(index + 1, i + 1);
      visited[i] = false;
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
