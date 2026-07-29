package jungol.jungol8474;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static List<Integer>[] edges;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();

      edges = new List[n + 1];
      for (int i = 1; i <= n; i++) {
        edges[i] = new ArrayList<>();
      }
      for (int i = 0; i < n - 1; i++) {
        int x = io.nextInt();
        int y = io.nextInt();
        edges[x].add(y);
        edges[y].add(x);
      }

      int[] dist = new int[n + 1];
      Arrays.fill(dist, Integer.MAX_VALUE);
      int[] src = new int[n + 1];
      Arrays.fill(src, -1);

      Queue<Integer> queue = new ArrayDeque<>();
      for (int v = 1; v <= n; v++) {
        if (edges[v].size() == 1) { // leaf node
          queue.add(v);
          dist[v] = 0;
          src[v] = v;
        }
      }

      while (!queue.isEmpty()) {
        int u = queue.poll();
        for (int v : edges[u]) {
          if (u == v) {
            continue;
          }
          if (dist[v] == Integer.MAX_VALUE) {
            queue.add(v);
            dist[v] = dist[u] + 1;
            src[v] = src[u];
          }
        }
      }

      int[] ans = {Integer.MAX_VALUE, -1, -1};
      for (int u = 1; u <= n; u++) {
        for (int v : edges[u]) {
          if (src[u] != src[v]) {
            if (dist[u] + dist[v] + 1 < ans[0]) {
              ans[0] = dist[u] + dist[v] + 1;
              ans[1] = src[u];
              ans[2] = src[v];
            }
          }
        }
      }
      io.println(ans[0]);
      io.println(ans[1] + " " + ans[2]);
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
