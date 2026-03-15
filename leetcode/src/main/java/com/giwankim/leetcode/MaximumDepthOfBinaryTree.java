package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class MaximumDepthOfBinaryTree {
  public int maxDepth(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(h)
    if (root == null) {
      return 0;
    }
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
  }
}
