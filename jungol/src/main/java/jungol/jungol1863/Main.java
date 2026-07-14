package jungol.jungol1863;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int[] parent, rank;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();

      parent = new int[n + 1];
      for (int i = 1; i <= n; i++) {
        parent[i] = i;
      }
      rank = new int[n + 1];

      int ans = n;
      int m = io.nextInt();
      while (m-- > 0) {
        int i = io.nextInt();
        int j = io.nextInt();
        if (find(i) == find(j)) {
          continue;
        }
        union(i, j);
        ans--;
      }
      io.println(ans);
    }
  }

  private static void union(int i, int j) {
    int pi = find(i);
    int pj = find(j);
    if (pi == pj) {
      return;
    }
    if (rank[pi] < rank[pj]) {
      parent[pi] = pj;
    } else if (rank[pi] > rank[pj]) {
      parent[pj] = pi;
    } else {
      parent[pi] = pj;
      rank[pj]++;
    }
  }

  private static int find(int i) {
    if (parent[i] == i) {
      return parent[i];
    }
    parent[i] = find(parent[i]);
    return parent[i];
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
