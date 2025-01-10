package leetcode;

import java.util.*;

@SuppressWarnings("unchecked")
public class PossibleBipartition {
  public boolean possibleBipartition(int n, int[][] dislikes) {
    //    Map<Integer, List<Integer>> adjList = new HashMap<>();
    List<Integer>[] adjList = new List[n + 1];
    for (int i = 0; i < adjList.length; i++) {
      adjList[i] = new ArrayList<>();
    }

    for (int[] edge : dislikes) {
      int from = edge[0];
      int to = edge[1];
      //      adjList.computeIfAbsent(to, key -> new ArrayList<>()).add(from);
      //      adjList.computeIfAbsent(from, key -> new ArrayList<>()).add(to);
      adjList[from].add(to);
      adjList[to].add(from);
    }

    int[] colors = new int[n + 1];
    Arrays.fill(colors, -1);

    for (int i = 1; i <= n; i++) {
      if (colors[i] == -1 && !color(i, 0, colors, adjList)) {
        return false;
      }
    }
    return true;
  }

  private boolean color(int i, int color, int[] colors, List<Integer>[] adjList) {
    colors[i] = color;
    for (int neighbor : adjList[i]) {
      if (colors[neighbor] == color) {
        return false;
      }
      if (colors[neighbor] == -1 && !color(neighbor, 1 - color, colors, adjList)) {
        return false;
      }
    }
    return true;
  }
}
