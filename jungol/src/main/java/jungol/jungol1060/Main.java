package jungol.jungol1060;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static int[] parent;
  private static int[] rank;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();

      List<int[]> edges = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          int cost = io.nextInt();
          if (cost == 0) {
            continue;
          }
          edges.add(new int[] {cost, i, j});
        }
      }

      parent = new int[n];
      for (int i = 0; i < n; i++) {
        parent[i] = i;
      }
      rank = new int[n];

      edges.sort(Comparator.comparingInt(a -> a[0]));
      int ans = 0;
      for (int[] edge : edges) {
        int cost = edge[0];
        int u = edge[1];
        int v = edge[2];
        if (find(u) == find(v)) {
          continue;
        }
        union(u, v);
        ans += cost;
      }
      io.println(ans);
    }
  }

  private static int find(int x) {
    if (parent[x] == x) {
      return parent[x];
    }
    parent[x] = find(parent[x]);
    return parent[x];
  }

  private static void union(int x, int y) {
    int px = find(x);
    int py = find(y);
    if (px == py) {
      return;
    }
    if (rank[px] < rank[py]) {
      parent[px] = py;
    } else if (rank[px] > rank[py]) {
      parent[py] = px;
    } else {
      parent[px] = py;
      rank[py]++;
    }
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
