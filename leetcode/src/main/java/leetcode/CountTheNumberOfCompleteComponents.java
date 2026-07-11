package leetcode;

import java.util.ArrayList;
import java.util.List;

public class CountTheNumberOfCompleteComponents {
  private static List<Integer>[] graph;
  private static boolean[] visited;
  private static int sz, deg;

  /**
   * @implNote Time {@code O(n + m)}, auxiliary space {@code O(n + m)} for the adjacency lists and
   *     the DFS recursion stack, where {@code m = edges.length}.
   */
  public int countCompleteComponents(int n, int[][] edges) {
    graph = new List[n];
    for (int i = 0; i < n; i++) {
      graph[i] = new ArrayList<>();
    }
    for (int[] edge : edges) {
      graph[edge[0]].add(edge[1]);
      graph[edge[1]].add(edge[0]);
    }
    visited = new boolean[n];
    int result = 0;
    for (int i = 0; i < n; i++) {
      if (visited[i]) {
        continue;
      }
      sz = 0;
      deg = 0;
      dfs(i);
      if (deg == sz * (sz - 1)) {
        result++;
      }
    }
    return result;
  }

  private static void dfs(int u) {
    visited[u] = true;
    sz++;
    deg += graph[u].size();
    for (int v : graph[u]) {
      if (visited[v]) {
        continue;
      }
      dfs(v);
    }
  }
}
