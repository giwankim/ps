package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class PathSum {
  /**
   * @implNote Time {@code O(n)}, space {@code O(h)}.
   */
  public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) {
      return false;
    }
    targetSum -= root.val;
    if (root.left == null && root.right == null) {
      return targetSum == 0;
    }
    return hasPathSum(root.left, targetSum) || hasPathSum(root.right, targetSum);
  }
}
