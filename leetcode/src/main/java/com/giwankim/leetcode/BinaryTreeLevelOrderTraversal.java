package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.*;

public class BinaryTreeLevelOrderTraversal {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
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
