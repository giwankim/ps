package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayDeque;
import java.util.Queue;

public class InvertBinaryTree {
  /**
   * @implNote Time {@code O(n)}, space {@code O(h)}.
   */
  public TreeNode invertTree(TreeNode root) {
    if (root == null) {
      return null;
    }
    TreeNode tmp = root.left;
    root.left = invertTree(root.right);
    root.right = invertTree(tmp);
    return root;
  }

  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public TreeNode invertTree2(TreeNode root) {
    if (root == null) {
      return null;
    }
    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
      TreeNode node = queue.poll();
      TreeNode tmp = node.left;
      node.left = node.right;
      node.right = tmp;
      if (node.left != null) {
        queue.offer(node.left);
      }
      if (node.right != null) {
        queue.offer(node.right);
      }
    }
    return root;
  }
}
