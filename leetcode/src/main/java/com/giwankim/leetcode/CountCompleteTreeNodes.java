package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class CountCompleteTreeNodes {
  public int countNodes(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(log n)
    if (root == null) {
      return 0;
    }
    return 1 + countNodes(root.left) + countNodes(root.right);
  }
}
