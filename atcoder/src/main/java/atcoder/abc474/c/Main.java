package atcoder.abc474.c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      int n = io.nextInt();
      int q = io.nextInt();
      int[] p = new int[n];
      for (int i = 0; i < n; i++) {
        p[i] = io.nextInt();
      }

      int[] last = new int[n + 1];
      Arrays.fill(last, -1);
      int[] ops = new int[q];
      for (int i = 0; i < q; i++) {
        ops[i] = io.nextInt();
        last[ops[i]] = i;
      }
      List<Integer> suf = new ArrayList<>();
      for (int i = 0; i < q; i++) {
        if (last[ops[i]] == i) {
          suf.add(ops[i]);
        }
      }

      int[] ans = new int[n];
      int i = 0;
      for (int x : p) {
        if (last[x] == -1) {
          ans[i++] = x;
        }
      }
      for (int x : suf) {
        ans[i++] = x;
      }

      StringBuilder sb = new StringBuilder();
      for (int j = 0; j < n; j++) {
        sb.append(ans[j]).append(j < n - 1 ? ' ' : '\n');
      }
      io.print(sb);
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
