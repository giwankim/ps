package com.giwankim.grind75;

import com.giwankim.grind75.support.TreeNode;

public class LowestCommonAncestorBinarySearchTree {
  public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    // empty tree or found either p or q
    if (root == null || root == p || root == q) {
      return root;
    }

    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);

    // if both left and right are null, then root is the LCA
    if (left != null && right != null) {
      return root;
    }

    // if one of left and right is null, then LCA is in the other branch
    return left == null ? right : left;
  }
}
