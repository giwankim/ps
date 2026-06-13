package boj.boj15656;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int m;
  private static int[] arr;
  private static int[] selected;
  private static StringBuilder result;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      m = io.nextInt();
      arr = new int[n];
      for (int i = 0; i < n; i++) {
        arr[i] = io.nextInt();
      }
      selected = new int[m];
      result = new StringBuilder();

      Arrays.sort(arr);

      backtrack(0);
      io.print(result);
    }
  }

  private static void backtrack(int index) {
    if (index == m) {
      for (int i = 0; i < m; i++) {
        result.append(selected[i]).append(i == m - 1 ? "\n" : ' ');
      }
      return;
    }
    for (int i = 0; i < n; i++) {
      selected[index] = arr[i];
      backtrack(index + 1);
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
