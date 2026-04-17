package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class MaximumDepthOfBinaryTree {
  /**
   * @implNote Time {@code O(n)}, space {@code O(h)}.
   */
  public int maxDepth(TreeNode root) {
    if (root == null) {
      return 0;
    }
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
  }
}
