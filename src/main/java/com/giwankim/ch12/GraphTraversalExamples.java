package com.giwankim.ch12;

import java.util.*;

public class GraphTraversalExamples {
  static Map<Integer, List<Integer>> graph = new HashMap<>();

  public static List<Integer> recursiveDFS(int v, List<Integer> discovered) {
    discovered.add(v);
    for (int w : graph.get(v)) {
      if (!discovered.contains(w)) {
        discovered = recursiveDFS(w, discovered);
      }
    }
    return discovered;
  }

  public static List<Integer> iterativeDFS(int v) {
    List<Integer> discovered = new ArrayList<>();
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(v);
    while (!stack.isEmpty()) {
      v = stack.pop();
      if (!discovered.contains(v)) {
        discovered.add(v);
        for (int w : graph.get(v)) {
          stack.push(w);
        }
      }
    }
    return discovered;
  }

  public static List<Integer> iterativeBFS(int start) {
    List<Integer> discovered = new ArrayList<>();
    discovered.add(start);
    Queue<Integer> queue = new LinkedList<>();
    queue.add(start);
    while (!queue.isEmpty()) {
      int v = queue.poll();
      for (int w : graph.get(v)) {
        if (!discovered.contains(w)) {
          discovered.add(w);
          queue.add(w);
        }
      }
    }
    return discovered;
  }

  public static void main(String[] args) {
    graph.put(1, Arrays.asList(2, 3, 4));
    graph.put(2, Arrays.asList(5));
    graph.put(3, Arrays.asList(5));
    graph.put(4, Arrays.asList());
    graph.put(5, Arrays.asList(6, 7));
    graph.put(6, Arrays.asList());
    graph.put(7, Arrays.asList(3));

    List<Integer> r = new ArrayList<>();
    // 재귀 구조 DFS
    System.out.println(recursiveDFS(1, r));
    // 반복 구조 DFS
    System.out.println(iterativeDFS(1));
    // 반복 구조 BFS
    System.out.println(iterativeBFS(1));
  }
}
