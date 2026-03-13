package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayDeque;
import java.util.Queue;

public class InvertBinaryTree {
  public TreeNode invertTree(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(h)
    if (root == null) {
      return null;
    }
    TreeNode tmp = root.left;
    root.left = invertTree(root.right);
    root.right = invertTree(tmp);
    return root;
  }

  public TreeNode invertTree2(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(n)
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
