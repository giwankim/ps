package jungol.jungol8406;

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
  private static int[] parent;
  private static int[] rank;
  private static int[] size;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();

      parent = new int[n + 1];
      for (int i = 1; i <= n; i++) {
        parent[i] = i;
      }
      rank = new int[n + 1];
      size = new int[n + 1];
      Arrays.fill(size, 1);

      int q = io.nextInt();
      while (q-- > 0) {
        int op = io.nextInt();
        if (op == 1) {
          int x = io.nextInt();
          int y = io.nextInt();
          union(x, y);
        } else {
          int x = io.nextInt();
          io.println(size[find(x)]);
        }
      }
    }
  }

  private static void union(int x, int y) {
    int px = find(x);
    int py = find(y);
    if (px == py) {
      return;
    }
    if (rank[px] < rank[py]) {
      parent[px] = py;
      size[py] += size[px];
    } else if (rank[px] > rank[py]) {
      parent[py] = px;
      size[px] += size[py];
    } else {
      parent[px] = py;
      rank[py]++;
      size[py] += size[px];
    }
  }

  private static int find(int x) {
    if (parent[x] == x) {
      return parent[x];
    }
    parent[x] = find(parent[x]);
    return parent[x];
  }

  public static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, OutputStream out) {
      super(out);
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
