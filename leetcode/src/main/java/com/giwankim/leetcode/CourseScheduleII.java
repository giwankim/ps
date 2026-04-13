package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class CourseScheduleII {
  public int[] findOrder(int numCourses, int[][] prerequisites) {
    // Time complexity: O(n + m), Space complexity: O(n + m)
    // graph (adjacency list)
    List<List<Integer>> adjList = new ArrayList<>(numCourses);
    for (int i = 0; i < numCourses; i++) {
      adjList.add(new ArrayList<>());
    }
    for (int[] prerequisite : prerequisites) {
      adjList.get(prerequisite[1]).add(prerequisite[0]);
    }
    // topological sort;
    List<Integer> result = new ArrayList<>();
    State[] states = new State[numCourses];
    for (int i = 0; i < numCourses; i++) {
      if (!dfs(i, adjList, states, result)) {
        return new int[0];
      }
    }
    Collections.reverse(result);
    return IntStream.range(0, result.size()).map(result::get).toArray();
  }

  private boolean dfs(
      int v, List<List<Integer>> adjList, State[] states, List<Integer> sortedList) {
    if (states[v] == State.VISITED) {
      return true;
    }
    if (states[v] == State.VISITING) {
      return false;
    }
    states[v] = State.VISITING;
    for (int w : adjList.get(v)) {
      if (!dfs(w, adjList, states, sortedList)) {
        return false;
      }
    }
    states[v] = State.VISITED;
    sortedList.add(v);
    return true;
  }

  private enum State {
    UNVISITED,
    VISITING,
    VISITED
  }
}
