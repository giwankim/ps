package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class ValidateBinarySearchTree {
  private TreeNode prev;

  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public boolean isValidBST(TreeNode root) {
    prev = null;
    return inorderIsValidBST(root);
  }

  private boolean inorderIsValidBST(TreeNode root) {
    if (root == null) {
      return true;
    }
    if (!inorderIsValidBST(root.left)) {
      return false;
    }
    if (prev != null && prev.val >= root.val) {
      return false;
    }
    prev = root;
    return inorderIsValidBST(root.right);
  }

  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public boolean isValidBST2(TreeNode root) {
    return isValidBST2(root, Long.MIN_VALUE, Long.MAX_VALUE);
  }

  private boolean isValidBST2(TreeNode root, long min, long max) {
    if (root == null) {
      return true;
    }
    if (min >= root.val || root.val >= max) {
      return false;
    }
    return isValidBST2(root.left, min, root.val) && isValidBST2(root.right, root.val, max);
  }
}
