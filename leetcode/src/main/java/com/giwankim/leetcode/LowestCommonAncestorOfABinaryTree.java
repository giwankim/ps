package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class LowestCommonAncestorOfABinaryTree {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null) {
      return null;
    }
    if (root == p || root == q) {
      return root;
    }
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }
    return root;
  }
}
