package jungol.jungol5402;

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
  private static int[][] ducks;
  private static int[] parent;
  private static int[] rank;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      ducks = new int[n + 1][2];
      for (int i = 1; i <= n; i++) {
        ducks[i][0] = io.nextInt();
        ducks[i][1] = io.nextInt();
      }

      parent = new int[n + 1];
      rank = new int[n + 1];

      int ans = -1;
      int lo = 0;
      int hi = 1_250_000_000;
      while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        if (isFeasible(mid)) {
          ans = mid;
          hi = mid - 1;
        } else {
          lo = mid + 1;
        }
      }
      io.println(ans);
    }
  }

  private static boolean isFeasible(int x) {
    for (int i = 1; i <= n; i++) {
      parent[i] = i;
    }
    Arrays.fill(rank, 0);
    for (int i = 1; i <= n; i++) {
      for (int j = i + 1; j <= n; j++) {
        int dx = ducks[i][0] - ducks[j][0];
        int dy = ducks[i][1] - ducks[j][1];
        if (dx * dx + dy * dy <= x) {
          union(i, j);
        }
      }
    }
    for (int i = 1; i < n; i++) {
      if (find(i) != find(i + 1)) {
        return false;
      }
    }
    return true;
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
