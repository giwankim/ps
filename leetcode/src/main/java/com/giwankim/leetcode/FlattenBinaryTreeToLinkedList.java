package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class FlattenBinaryTreeToLinkedList {
  private TreeNode prev = null;

  /**
   * @implNote Time {@code O(n)}, space {@code O(h)}.
   */
  public void flatten(TreeNode root) {
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
