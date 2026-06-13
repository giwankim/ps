package boj.boj10819;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int[] arr;
  private static int[] selected;
  private static boolean[] visited;
  private static int ans;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      arr = new int[n];
      for (int i = 0; i < n; i++) {
        arr[i] = io.nextInt();
      }
      selected = new int[n];
      visited = new boolean[n];
      ans = 0;
      backtrack(0);
      io.println(ans);
    }
  }

  private static void backtrack(int index) {
    if (index == n) {
      int sum = 0;
      for (int i = 0; i < n - 1; i++) {
        sum += Math.abs(selected[i] - selected[i + 1]);
      }
      ans = Math.max(ans, sum);
      return;
    }
    for (int i = 0; i < n; i++) {
      if (visited[i]) {
        continue;
      }
      selected[index] = arr[i];
      visited[i] = true;
      backtrack(index + 1);
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
