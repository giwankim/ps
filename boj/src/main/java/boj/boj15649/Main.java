package boj.boj15649;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int m;
  private static int[] selected;
  private static boolean[] visited;
  private static StringBuilder result;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      m = io.nextInt();
      selected = new int[m];
      visited = new boolean[n + 1];
      result = new StringBuilder();
      backtrack(0);
      io.print(result);
    }
  }

  private static void backtrack(int depth) {
    if (depth == m) {
      for (int i = 0; i < m; i++) {
        result.append(selected[i]).append(i == m - 1 ? "\n" : " ");
      }
      return;
    }
    for (int v = 1; v <= n; v++) {
      if (visited[v]) {
        continue;
      }
      selected[depth] = v;
      visited[v] = true;
      backtrack(depth + 1);
      visited[v] = false;
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
