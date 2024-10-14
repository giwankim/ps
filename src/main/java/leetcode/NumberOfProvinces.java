package leetcode;

import java.util.LinkedList;
import java.util.Queue;

public class NumberOfProvinces {
  public int findCircleNum(int[][] isConnected) {
    int n = isConnected.length;
    boolean[] visited = new boolean[n];
    int result = 0;
    for (int i = 0; i < n; i++) {
      if (!visited[i]) {
        bfs(i, n, visited, isConnected);
        result += 1;
      }
    }
    return result;
  }

  private void bfs(int i, int n, boolean[] visited, int[][] isConnected) {
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(i);
    visited[i] = true;
    while (!queue.isEmpty()) {
      int j = queue.poll();
      for (int k = 0; k < n; k++) {
        if (j != k && !visited[k] && isConnected[j][k] == 1) {
          queue.offer(k);
          visited[k] = true;
        }
      }
    }
  }
}
