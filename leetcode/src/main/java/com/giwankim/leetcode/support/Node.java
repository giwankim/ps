package com.giwankim.leetcode.support;

import java.util.ArrayList;
import java.util.List;
import lombok.ToString;

@ToString(of = "val")
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

  public static Node from(List<List<Integer>> adjLists) {
    int n = adjLists.size();
    Node[] nodes = new Node[n + 1];
    for (int i = 1; i <= n; i++) {
      nodes[i] = new Node(i);
    }
    for (int i = 1; i <= n; i++) {
      for (int neighbor : adjLists.get(i - 1)) {
        nodes[i].neighbors.add(nodes[neighbor]);
      }
    }
    return nodes[1];
  }
}
