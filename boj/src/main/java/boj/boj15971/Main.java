package boj.boj15971;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
  private static int n;
  private static List<List<int[]>> graph;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      int s = io.nextInt();
      int t = io.nextInt();
      graph = new ArrayList<>(n + 1);
      for (int i = 0; i <= n; i++) {
        graph.add(new ArrayList<>());
      }
      for (int i = 0; i + 1 < n; i++) {
        int u = io.nextInt();
        int v = io.nextInt();
        int d = io.nextInt();
        graph.get(u).add(new int[] {v, d});
        graph.get(v).add(new int[] {u, d});
      }
      int[] dist = bfs(s);
      int[] dist2 = bfs(t);
      int ans = s == t ? 0 : Integer.MAX_VALUE;
      for (int u = 1; u <= n; u++) {
        for (int[] e : graph.get(u)) {
          int v = e[0];
          ans = Math.min(ans, dist[u] + dist2[v]);
          ans = Math.min(ans, dist[u] + dist2[u]);
        }
      }
      io.println(ans);
    }
  }

  private static int[] bfs(int s) {
    int[] dist = new int[n + 1];
    Arrays.fill(dist, -1);
    Queue<Integer> queue = new ArrayDeque<>();
    queue.offer(s);
    dist[s] = 0;
    while (!queue.isEmpty()) {
      int u = queue.poll();
      for (int[] e : graph.get(u)) {
        int v = e[0];
        int d = e[1];
        if (dist[v] != -1) {
          continue;
        }
        queue.offer(v);
        dist[v] = dist[u] + d;
      }
    }
    return dist;
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
