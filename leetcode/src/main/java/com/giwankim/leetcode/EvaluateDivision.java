package com.giwankim.leetcode;

import java.util.Arrays;
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
    // Time complexity: O(n * m), Space complexity: O(n) where n = |equations| and m = |queries|
    // graph (adjacency list)
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
        String variable = edge.getKey();
        if (visited.contains(variable)) {
          continue;
        }
        queue.offer(new Step(variable, step.product * edge.getValue()));
        visited.add(variable);
      }
    }
    return -1.0;
  }

  public double[] calcEquation2(
      List<List<String>> equations, double[] values, List<List<String>> queries) {
    // graph (adjacency list)
    Map<String, Map<String, Double>> adjList = newGraph(equations, values);
    // evaluate
    double[] result = new double[queries.size()];
    Arrays.fill(result, -1.0);
    for (int i = 0; i < result.length; i++) {
      String from = queries.get(i).getFirst();
      String to = queries.get(i).getLast();
      result[i] = calculate2(from, to, adjList, new HashSet<>());
    }
    return result;
  }

  private double calculate2(
      String from, String to, Map<String, Map<String, Double>> adjList, Set<String> visited) {
    if (!adjList.containsKey(from) || !adjList.containsKey(to)) {
      return -1.0;
    }
    if (adjList.get(from).containsKey(to)) {
      return adjList.get(from).get(to);
    }
    visited.add(from);
    for (Map.Entry<String, Double> edge : adjList.get(from).entrySet()) {
      String variable = edge.getKey();
      if (visited.contains(variable)) {
        continue;
      }
      double value = calculate2(variable, to, adjList, visited);
      if (value != -1.0) {
        return edge.getValue() * value;
      }
    }
    return -1.0;
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

  private record Step(String node, double product) {}
}
