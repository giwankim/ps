package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class CountCompleteTreeNodes {
  public int countNodes(TreeNode root) {
    // Time Complexity: O(log^2 n), Space Complexity: O(log n)
    if (root == null) {
      return 0;
    }
    int leftHeight = height(root, true);
    int rightHeight = height(root, false);
    if (leftHeight == rightHeight) {
      return (1 << leftHeight) - 1;
    }
    return 1 + countNodes(root.left) + countNodes(root.right);
  }

  private int height(TreeNode root, boolean goLeft) {
    int result = 0;
    while (root != null) {
      result += 1;
      root = goLeft ? root.left : root.right;
    }
    return result;
  }
}
