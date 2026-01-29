package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class CourseSchedule {
  public static final int UNVISITED = 0;
  public static final int VISITING = 1;
  public static final int VISITED = 2;

  public boolean canFinish(int numCourses, int[][] prerequisites) {
    // construct graph
    List<Integer>[] graph = new ArrayList[numCourses];
    for (int i = 0; i < graph.length; i++) {
      graph[i] = new ArrayList<>();
    }
    for (int[] p : prerequisites) {
      graph[p[1]].add(p[0]);
    }

    // topological sort
    int[] dfsNum = new int[numCourses];
    for (int v = 0; v < graph.length; v++) {
      if (dfsNum[v] == UNVISITED && !dfs(v, graph, dfsNum)) {
        return false;
      }
    }
    return true;
  }

  private boolean dfs(int v, List<Integer>[] graph, int[] dfsNum) {
    if (dfsNum[v] == VISITED) {
      return true;
    }
    if (dfsNum[v] == VISITING) {
      return false;
    }
    dfsNum[v] = VISITING;
    for (int w : graph[v]) {
      if (!dfs(w, graph, dfsNum)) {
        return false;
      }
    }
    dfsNum[v] = VISITED;
    return true;
  }
}
