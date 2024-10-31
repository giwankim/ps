package leetcode;

import java.util.HashMap;
import java.util.Map;
import leetcode.support.Node;

public class CloneGraph {
  public Node cloneGraph(Node node) {
    Map<Integer, Node> map = new HashMap<>();
    return cloneGraph(node, map);
  }

  public Node cloneGraph(Node node, Map<Integer, Node> map) {
    if (node == null) {
      return null;
    }
    if (map.containsKey(node.val)) {
      return map.get(node.val);
    }
    Node clone = new Node(node.val);
    map.put(node.val, clone);
    for (Node neighbor : node.neighbors) {
      clone.neighbors.add(cloneGraph(neighbor, map));
    }
    return clone;
  }
}
