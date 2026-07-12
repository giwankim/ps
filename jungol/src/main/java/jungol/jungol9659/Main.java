package jungol.jungol9659;

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
  private static int[] parent, depth, size, height, dist;
  private static List<Integer>[] graph;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      graph = new ArrayList[n + 1];
      for (int i = 1; i <= n; i++) {
        graph[i] = new ArrayList<>();
      }
      for (int i = 0; i + 1 < n; i++) {
        int cid = io.nextInt();
        int pid = io.nextInt();
        graph[pid].add(cid);
        graph[cid].add(pid);
      }
      int x = io.nextInt();

      parent = new int[n + 1];
      depth = new int[n + 1];
      size = new int[n + 1];
      height = new int[n + 1];
      dist = new int[n + 1];
      Arrays.fill(parent, -1);
      Arrays.fill(depth, 0);
      Arrays.fill(size, 1);
      Arrays.fill(height, 0);
      Arrays.fill(dist, -1);
      dfs(1, -1);
      io.println(depth[x]);
      io.println(size[x]);
      io.println(height[x]);
      io.println(bfs(x));
    }
  }

  private static void dfs(int u, int prev) {
    for (int v : graph[u]) {
      if (v == prev) {
        continue;
      }
      parent[v] = u;
      depth[v] = depth[u] + 1;
      dfs(v, u);
      size[u] += size[v];
      height[u] = Math.max(height[u], height[v] + 1);
    }
  }

  private static int bfs(int u) {
    Queue<Integer> queue = new ArrayDeque<>();
    queue.offer(u);
    dist[u] = 0;
    while (!queue.isEmpty()) {
      int curr = queue.poll();
      for (int next : graph[curr]) {
        if (dist[next] == -1) {
          queue.offer(next);
          dist[next] = dist[curr] + 1;
        }
      }
    }
    int result = 0;
    for (int i = 1; i <= n; i++) {
      result = Math.max(result, dist[i]);
    }
    return result;
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
