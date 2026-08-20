package leetcode.p0101_0200;

import java.util.HashMap;
import java.util.Map;
import leetcode.support.Node;

/** <a href="https://leetcode.com/problems/clone-graph/">133. Clone Graph</a> */
public class CloneGraph {
  /** @implNote Time {@code O(n + m)}, space {@code O(n)}. */
  public Node cloneGraph(Node node) {
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
