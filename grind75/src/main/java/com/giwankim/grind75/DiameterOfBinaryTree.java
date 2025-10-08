package com.giwankim.grind75;

import com.giwankim.grind75.support.TreeNode;

public class DiameterOfBinaryTree {

  public int diameterOfBinaryTree(TreeNode root) {
    if (root == null) {
      return 0;
    }

    // result is the maximum of diameters of the left subtree, right subtree, and subtree rooted at current node
    int result = Math.max(diameterOfBinaryTree(root.left), diameterOfBinaryTree(root.right));

    // get diameter passing through the current node
    int diameter = 0;
    if (root.left != null) {
      diameter += height(root.left) + 1;
    }
    if (root.right != null) {
      diameter += height(root.right) + 1;
    }

    return Math.max(result, diameter);
  }

  /**
   * Computes the height of the binary tree rooted at the given node. The height is defined as the
   * number of edges on the longest path from the root node to a leaf node.
   *
   * @param root the root node of the binary tree
   * @return the height of the binary tree; 0 if the tree is empty
   */
  public int height(TreeNode root) {
    if (root == null) {
      return 0;
    }
    if (root.left == null && root.right == null) {
      return 0;
    }
    return Math.max(height(root.left), height(root.right)) + 1;
  }
}
