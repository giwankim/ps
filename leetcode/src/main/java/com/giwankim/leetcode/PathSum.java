package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class PathSum {
  public boolean hasPathSum(TreeNode root, int targetSum) {
    // Time Complexity: O(n), Space Complexity: O(h)
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
