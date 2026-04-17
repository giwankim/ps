package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class CourseSchedule {
  /**
   * @implNote Time {@code O(n + m)}, space {@code O(n + m)}.
   */
  public boolean canFinish(int numCourses, int[][] prerequisites) {
    // graph (adjacency list)
    List<List<Integer>> adjList = new ArrayList<>(numCourses);
    for (int i = 0; i < numCourses; i++) {
      adjList.add(new ArrayList<>());
    }
    for (int[] prerequisite : prerequisites) {
      adjList.get(prerequisite[1]).add(prerequisite[0]);
    }
    // topological sort;
    State[] states = new State[numCourses];
    for (int i = 0; i < numCourses; i++) {
      if (hasCycle(i, adjList, states)) {
        return false;
      }
    }
    return true;
  }

  private boolean hasCycle(int i, List<List<Integer>> adjList, State[] states) {
    if (states[i] == State.VISITED) {
      return false;
    }
    if (states[i] == State.VISITING) {
      return true;
    }
    states[i] = State.VISITING;
    for (int neighbor : adjList.get(i)) {
      if (hasCycle(neighbor, adjList, states)) {
        return true;
      }
    }
    states[i] = State.VISITED;
    return false;
  }

  private enum State {
    UNVISITED,
    VISITING,
    VISITED
  }
}
