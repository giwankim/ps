package leetcode.p0101_0200;

import java.util.*;
import leetcode.support.TreeNode;

/**
 * <a href="https://leetcode.com/problems/binary-tree-level-order-traversal/">102. Binary Tree Level
 * Order Traversal</a>
 */
public class BinaryTreeLevelOrderTraversal {
  /** @implNote Time {@code O(n)}, space {@code O(n)}. */
  public List<List<Integer>> levelOrder(TreeNode root) {
    if (root == null) {
      return Collections.emptyList();
    }
    List<List<Integer>> result = new ArrayList<>();
    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
      int size = queue.size();
      List<Integer> level = new ArrayList<>();
      while (size-- > 0) {
        TreeNode node = queue.poll();
        level.add(node.val);
        if (node.left != null) {
          queue.offer(node.left);
        }
        if (node.right != null) {
          queue.offer(node.right);
        }
      }
      result.add(level);
    }
    return result;
  }
}
