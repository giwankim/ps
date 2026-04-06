package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class SumRootToLeafNumbers {
  public int sumNumbers(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(n)
    return sumNumbers(root, 0);
  }

  private int sumNumbers(TreeNode root, int pathSum) {
    if (root == null) {
      return 0;
    }
    pathSum = pathSum * 10 + root.val;
    if (root.left == null && root.right == null) {
      return pathSum;
    }
    return sumNumbers(root.left, pathSum) + sumNumbers(root.right, pathSum);
  }
}
