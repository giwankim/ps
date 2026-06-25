package leetcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import leetcode.support.TreeNode;

public class CreateBinaryTreeFromDescriptions {
  /** @implNote Time {@code O(n)}, space {@code O(n)}, where {@code n = descriptions.length}. */
  public TreeNode createBinaryTree(int[][] descriptions) {
    Map<Integer, TreeNode> valToNode = new HashMap<>();
    Set<Integer> children = new HashSet<>();
    for (int[] description : descriptions) {
      boolean isLeft = description[2] == 1;
      TreeNode parent = valToNode.computeIfAbsent(description[0], TreeNode::new);
      TreeNode child = valToNode.computeIfAbsent(description[1], TreeNode::new);
      if (isLeft) {
        parent.left = child;
      } else {
        parent.right = child;
      }
      children.add(description[1]);
    }

    // find root
    for (Map.Entry<Integer, TreeNode> entry : valToNode.entrySet()) {
      if (!children.contains(entry.getKey())) {
        return entry.getValue();
      }
    }
    return null;
  }
}
