package atcoder.abc471.c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Main {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      TreeSet<Integer> set = new TreeSet<>();
      for (int i = 0; i < n; i++) {
        int x = io.nextInt();
        set.add(x);
      }
      int pos = 0;
      long ans = 0;
      while (!set.isEmpty()) {
        Integer lower = set.lower(pos); // nearest left cookie
        Integer higher = set.higher(pos); // nearest right cookie
        if (lower == null) {
          ans += higher - pos;
          pos = higher;
          set.remove(higher);
        } else if (higher == null) {
          ans += pos - lower;
          pos = lower;
          set.remove(lower);
        } else if (higher - pos < pos - lower) {
          ans += higher - pos;
          pos = higher;
          set.remove(higher);
        } else {
          ans += pos - lower;
          pos = lower;
          set.remove(lower);
        }
      }
      io.println(ans);
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
