package leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MinimumScoreOfAPathBetweenTwoCities {
  /**
   * @implNote Time {@code O(n + m)}, auxiliary space {@code O(n + m)} for the adjacency list, the
   *     {@code visited} array, and the queue, where {@code m = roads.length} is the number of
   *     roads.
   */
  public int minScore(int n, int[][] roads) {
    int result = Integer.MAX_VALUE;
    List<List<int[]>> graph = newGraph(n, roads);
    Queue<Integer> queue = new ArrayDeque<>();
    boolean[] visited = new boolean[n + 1];
    queue.offer(1);
    visited[1] = true;
    while (!queue.isEmpty()) {
      int u = queue.poll();
      for (int[] e : graph.get(u)) {
        int v = e[0];
        int d = e[1];
        result = Math.min(result, d);
        if (visited[v]) {
          continue;
        }
        queue.offer(v);
        visited[v] = true;
      }
    }
    return result;
  }

  /**
   * @implNote Time {@code O(n + m)}, auxiliary space {@code O(n + m)} for the adjacency list, the
   *     {@code visited} array, and the recursion stack, which reaches {@code O(n)} depth on a
   *     path-shaped component, where {@code m = roads.length} is the number of roads. Unlike
   *     {@link #minScore}, the {@code O(n)} part lives on the fixed-size thread stack rather than
   *     the heap, so the deepest graphs need an enlarged {@code -Xss}.
   */
  public int minScore2(int n, int[][] roads) {
    List<List<int[]>> graph = newGraph(n, roads);
    boolean[] visited = new boolean[n + 1];
    return dfs(1, graph, visited);
  }

  private static int dfs(int s, List<List<int[]>> graph, boolean[] visited) {
    int result = Integer.MAX_VALUE;
    visited[s] = true;
    for (int[] e : graph.get(s)) {
      int v = e[0];
      int d = e[1];
      result = Math.min(result, d);
      if (visited[v]) {
        continue;
      }
      result = Math.min(result, dfs(v, graph, visited));
    }
    return result;
  }

  private static List<List<int[]>> newGraph(int n, int[][] roads) {
    List<List<int[]>> graph = new ArrayList<>(n + 1);
    for (int i = 0; i <= n; i++) {
      graph.add(new ArrayList<>());
    }
    for (int[] road : roads) {
      int a = road[0];
      int b = road[1];
      int distance = road[2];
      graph.get(a).add(new int[] {b, distance});
      graph.get(b).add(new int[] {a, distance});
    }
    return graph;
  }
}
