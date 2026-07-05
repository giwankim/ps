package boj.boj1753;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
  private static int v, e, k;
  private static List<List<int[]>> graph;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      v = io.nextInt();
      e = io.nextInt();
      k = io.nextInt();
      graph = new ArrayList<>(v + 1);
      for (int i = 0; i <= v; i++) {
        graph.add(new ArrayList<>());
      }
      for (int i = 0; i < e; i++) {
        int u = io.nextInt();
        int v = io.nextInt();
        int w = io.nextInt();
        graph.get(u).add(new int[] {v, w});
      }

      int[] dist = new int[v + 1];
      Arrays.fill(dist, Integer.MAX_VALUE);
      PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
      pq.offer(new int[] {0, k});
      dist[k] = 0;
      while (!pq.isEmpty()) {
        int[] node = pq.poll();
        int d = node[0];
        int u = node[1];
        if (d > dist[u]) {
          continue;
        }
        for (int[] e : graph.get(u)) {
          int v = e[0];
          int w = e[1];
          if (dist[v] > d + w) {
            dist[v] = d + w;
            pq.offer(new int[] {dist[v], v});
          }
        }
      }

      for (int i = 1; i <= v; i++) {
        if (dist[i] == Integer.MAX_VALUE) {
          io.println("INF");
        } else {
          io.println(dist[i]);
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
