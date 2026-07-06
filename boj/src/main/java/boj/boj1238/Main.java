package boj.boj1238;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
  private static int n, m, x;
  private static List<int[]>[] g, rg;
  private static int[] dist, rDist;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      m = io.nextInt();
      x = io.nextInt();
      g = new List[n + 1];
      rg = new List[n + 1];
      for (int i = 1; i <= n; i++) {
        g[i] = new ArrayList<>();
        rg[i] = new ArrayList<>();
      }
      for (int i = 0; i < m; i++) {
        int u = io.nextInt();
        int v = io.nextInt();
        int d = io.nextInt();
        g[u].add(new int[] {v, d});
        rg[v].add(new int[] {u, d});
      }
      dist = new int[n + 1];
      rDist = new int[n + 1];
      Arrays.fill(dist, Integer.MAX_VALUE);
      Arrays.fill(rDist, Integer.MAX_VALUE);
      dijkstra(x);
      rDijkstra(x);
      int ans = Integer.MIN_VALUE;
      for (int i = 1; i <= n; i++) {
        ans = Math.max(ans, dist[i] + rDist[i]);
      }
      io.println(ans);
    }
  }

  private static void dijkstra(int s) {
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    pq.offer(new int[] {0, s});
    dist[s] = 0;
    while (!pq.isEmpty()) {
      int[] curr = pq.poll();
      int d = curr[0];
      int u = curr[1];
      if (d > dist[u]) {
        continue;
      }
      for (int[] e : g[u]) {
        int v = e[0];
        int w = e[1];
        if (dist[v] > d + w) {
          dist[v] = d + w;
          pq.offer(new int[] {dist[v], v});
        }
      }
    }
  }

  private static void rDijkstra(int s) {
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    pq.offer(new int[] {0, s});
    rDist[s] = 0;
    while (!pq.isEmpty()) {
      int[] curr = pq.poll();
      int d = curr[0];
      int u = curr[1];
      if (d > rDist[u]) {
        continue;
      }
      for (int[] e : rg[u]) {
        int v = e[0];
        int w = e[1];
        if (rDist[v] > d + w) {
          rDist[v] = d + w;
          pq.offer(new int[] {rDist[v], v});
        }
      }
    }
  }

  private static class FastIO extends PrintWriter {
    private final BufferedReader r;
    private StringTokenizer st;

    public FastIO() {
      this(System.in, System.out);
    }

    public FastIO(InputStream in, PrintStream out) {
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
