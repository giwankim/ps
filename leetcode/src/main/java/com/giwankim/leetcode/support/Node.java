package com.giwankim.leetcode.support;

import java.util.*;

public class Node {
  public int val;
  public List<Node> neighbors;

  public Node() {
    this(0);
  }

  public Node(int val) {
    this(val, new ArrayList<>());
  }

  public Node(int val, List<Node> neighbors) {
    this.val = val;
    this.neighbors = neighbors;
  }

  public static Node fromLists(List<List<Integer>> adjLists) {
    Map<Integer, Node> nodeMap = new HashMap<>();

    // create nodes
    for (int i = 1; i <= adjLists.size(); i++) {
      nodeMap.put(i, new Node(i));
    }

    // add edges
    for (int i = 1; i <= adjLists.size(); i++) {
      Node node = nodeMap.get(i);
      List<Integer> neighborNodes = adjLists.get(i - 1);
      for (int neighbor : neighborNodes) {
        node.neighbors.add(nodeMap.get(neighbor));
      }
    }

    return nodeMap.get(1);
  }

  public List<Integer> values() {
    return neighbors.stream().mapToInt(n -> n.val).sorted().boxed().toList();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Node node)) {
      return false;
    }
    return val == node.val && Objects.equals(values(), node.values());
  }

  @Override
  public int hashCode() {
    return Objects.hash(val, values());
  }

  @Override
  public String toString() {
    return "Node{" + "val=" + val + '}';
  }
}
