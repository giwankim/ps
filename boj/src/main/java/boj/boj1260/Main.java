package boj.boj1260;

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
  private static int n, m;
  private static List<List<Integer>> graph;
  private static boolean[] visited;
  private static StringBuilder result;

  public static void main(String[] args) throws IOException {
    try (FastIO io = new FastIO()) {
      n = io.nextInt();
      m = io.nextInt();
      int v = io.nextInt();

      graph = new ArrayList<>(n + 1);
      for (int i = 0; i <= n; i++) {
        graph.add(new ArrayList<>());
      }
      for (int i = 0; i < m; i++) {
        int a = io.nextInt();
        int b = io.nextInt();
        graph.get(a).add(b);
        graph.get(b).add(a);
      }
      for (List<Integer> edges : graph) {
        edges.sort(Integer::compareTo);
      }

      visited = new boolean[n + 1];
      result = new StringBuilder();
      dfs(v);
      result.setLength(result.length() - 1);
      result.append('\n');
      Arrays.fill(visited, false);
      bfs(v);
      result.setLength(result.length() - 1);
      io.println(result);
    }
  }

  private static void dfs(int v) {
    visited[v] = true;
    result.append(v).append(' ');
    for (int w : graph.get(v)) {
      if (visited[w]) {
        continue;
      }
      dfs(w);
    }
  }

  private static void bfs(int s) {
    Queue<Integer> queue = new ArrayDeque<>();
    queue.add(s);
    visited[s] = true;
    while (!queue.isEmpty()) {
      int v = queue.poll();
      result.append(v).append(' ');
      for (int w : graph.get(v)) {
        if (visited[w]) {
          continue;
        }
        queue.offer(w);
        visited[w] = true;
      }
    }
  }

  private static class FastIO extends PrintWriter {
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
