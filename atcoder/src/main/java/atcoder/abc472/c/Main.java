package atcoder.abc472.c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      int m = io.nextInt();
      long k = io.nextLong();
      int[] a = new int[n];
      for (int i = 0; i < n; i++) {
        a[i] = io.nextInt();
      }

      Deque<Integer> eaten = new ArrayDeque<>();
      long total = 0L;
      for (int i = 0; i < n; i++) {
        while (!eaten.isEmpty() && i - eaten.peekFirst() >= m) {
          // prune front of eaten
          total -= a[eaten.pollFirst()];
        }
        if (total + a[i] <= k) {
          // eat
          total += a[i];
          eaten.offer(i);
          io.println("Yes");
          continue;
        }
        io.println("No");
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

    public boolean hasNext() throws IOException {
      while (st == null || !st.hasMoreTokens()) {
        String line = r.readLine();
        if (line == null) {
          return false;
        }
        st = new StringTokenizer(line);
      }
      return true;
    }

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
