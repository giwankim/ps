package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class BinaryTreeMaximumPathSum {
  private int maxSum;

  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public int maxPathSum(TreeNode root) {
    maxSum = Integer.MIN_VALUE;
    maxDescendingPathSum(root);
    return maxSum;
  }

  private int maxDescendingPathSum(TreeNode root) {
    if (root == null) {
      return 0;
    }
    int left = Math.max(maxDescendingPathSum(root.left), 0);
    int right = Math.max(maxDescendingPathSum(root.right), 0);
    maxSum = Math.max(maxSum, left + right + root.val);
    return Math.max(left, right) + root.val;
  }
}
