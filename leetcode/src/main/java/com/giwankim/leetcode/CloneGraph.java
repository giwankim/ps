package com.giwankim.leetcode;

import com.giwankim.leetcode.support.Node;
import java.util.HashMap;
import java.util.Map;

public class CloneGraph {
  public Node cloneGraph(Node node) {
    // Time Complexity: O(n + m), Space Complexity: O(n)
    Map<Integer, Node> valToClone = new HashMap<>();
    return cloneGraph(node, valToClone);
  }

  private Node cloneGraph(Node node, Map<Integer, Node> valToClone) {
    if (node == null) {
      return null;
    }
    if (valToClone.containsKey(node.val)) {
      return valToClone.get(node.val);
    }
    Node clonedNode = valToClone.computeIfAbsent(node.val, Node::new);
    for (Node neighbor : node.neighbors) {
      Node clonedNeighbor = cloneGraph(neighbor, valToClone);
      clonedNode.neighbors.add(clonedNeighbor);
    }
    return clonedNode;
  }
}
