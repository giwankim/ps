package com.giwankim.leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class EvaluateDivision {
  public double[] calcEquation(
      List<List<String>> equations, double[] values, List<List<String>> queries) {
    // build graph as adjacency list
    Map<String, Map<String, Double>> adjList = newGraph(equations, values);
    // evaluate
    double[] result = new double[queries.size()];
    for (int i = 0; i < result.length; i++) {
      String from = queries.get(i).getFirst();
      String to = queries.get(i).getLast();
      result[i] = calculate(from, to, adjList);
    }
    return result;
  }

  private static Map<String, Map<String, Double>> newGraph(
      List<List<String>> equations, double[] values) {
    Map<String, Map<String, Double>> adjList = new HashMap<>();
    for (int i = 0; i < equations.size(); i++) {
      String from = equations.get(i).getFirst();
      String to = equations.get(i).getLast();
      double value = values[i];
      adjList.computeIfAbsent(from, _ -> new HashMap<>()).put(to, value);
      adjList.computeIfAbsent(to, _ -> new HashMap<>()).put(from, 1 / value);
    }
    return adjList;
  }

  private double calculate(String from, String to, Map<String, Map<String, Double>> adjList) {
    if (!adjList.containsKey(from) && !adjList.containsKey(to)) {
      return -1.0;
    }
    Set<String> visited = new HashSet<>();
    Queue<Step> queue = new LinkedList<>();
    queue.offer(new Step(from, 1.0));
    visited.add(from);
    while (!queue.isEmpty()) {
      Step step = queue.poll();
      if (step.node.equals(to)) {
        return step.product;
      }
      for (Map.Entry<String, Double> edge : adjList.getOrDefault(step.node, Map.of()).entrySet()) {
        if (visited.contains(edge.getKey())) {
          continue;
        }
        queue.offer(new Step(edge.getKey(), step.product * edge.getValue()));
        visited.add(edge.getKey());
      }
    }
    return -1.0;
  }

  private record Step(String node, double product) {}
}
