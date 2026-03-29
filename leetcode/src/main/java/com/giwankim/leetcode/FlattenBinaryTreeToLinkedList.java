package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class FlattenBinaryTreeToLinkedList {
  private TreeNode prev = null;

  public void flatten(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(h)
    if (root == null) {
      return;
    }
    flatten(root.right);
    flatten(root.left);
    root.left = null;
    root.right = prev;
    prev = root;
  }
}
