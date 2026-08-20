package leetcode.p3301_3400;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

public class RemoveMethodsFromProject {
  private List<Integer>[] graph;
  private boolean[] suspicious;
  private boolean[] visited;

  /**
   * @implNote Time {@code O(n + m)}, space {@code O(n + m)}, where {@code m = invocations.length}.
   */
  public List<Integer> remainingMethods(int n, int k, int[][] invocations) {
    // build graph
    graph = new List[n];
    for (int i = 0; i < n; i++) {
      graph[i] = new ArrayList<>();
    }
    for (int[] invocation : invocations) {
      graph[invocation[0]].add(invocation[1]);
    }

    // mark all suspicious methods
    suspicious = new boolean[n];
    suspicious[k] = true;
    for (int v : graph[k]) {
      dfs(v);
    }

    // invocation from outside the suspicious group
    Queue<Integer> queue = new ArrayDeque<>();
    visited = new boolean[n];
    for (int u = 0; u < n; u++) {
      if (suspicious[u]) {
        continue;
      }
      queue.offer(u);
      visited[u] = true;
    }
    while (!queue.isEmpty()) {
      int u = queue.poll();
      for (int v : graph[u]) {
        if (visited[v]) {
          continue;
        }
        queue.offer(v);
        visited[v] = true;
      }
    }

    boolean canRemove = true;
    List<Integer> ans = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      if (suspicious[i] && visited[i]) {
        canRemove = false;
        break;
      }
      if (!suspicious[i]) {
        ans.add(i);
      }
    }
    if (!canRemove) {
      return IntStream.range(0, n).boxed().toList();
    }
    return ans;
  }

  private void dfs(int u) {
    suspicious[u] = true;
    for (int v : graph[u]) {
      if (suspicious[v]) {
        continue;
      }
      dfs(v);
    }
  }
}
