package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class MinimumAbsoluteDifferenceInBST {
  TreeNode prev;
  int minDifference;

  public int getMinimumDifference(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(n)
    prev = null;
    minDifference = Integer.MAX_VALUE;
    findMinDifference(root);
    return minDifference;
  }

  private void findMinDifference(TreeNode root) {
    if (root == null) {
      return;
    }
    findMinDifference(root.left);
    if (prev != null) {
      minDifference = Math.min(minDifference, root.val - prev.val);
    }
    prev = root;
    findMinDifference(root.right);
  }
}
