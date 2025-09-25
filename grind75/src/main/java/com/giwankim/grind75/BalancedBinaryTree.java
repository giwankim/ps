package com.giwankim.grind75;

import com.giwankim.grind75.support.TreeNode;

/**
 * This class provides functionality to determine if a binary tree is height-balanced. A binary tree
 * is considered balanced if, for every node in the tree, the absolute difference between the
 * heights of its left and right subtrees is at most 1.
 */
public class BalancedBinaryTree {
  public boolean isBalanced(TreeNode root) {
    if (root == null) {
      return true;
    }
    int leftHeight = height(root.left);
    int rightHeight = height(root.right);
    if (Math.abs(leftHeight - rightHeight) > 1) {
      return false;
    }
    return isBalanced(root.left) && isBalanced(root.right);
  }

  /**
   * Calculates the height of a binary tree rooted at the given node.
   *
   * @param root the root node of the binary tree, or null if the tree is empty
   * @return the height of the binary tree. Returns 0 if the tree is empty.
   */
  private int height(TreeNode root) {
    if (root == null) {
      return 0;
    }
    return Math.max(height(root.left), height(root.right)) + 1;
  }
}
