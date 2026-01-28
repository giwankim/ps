package com.giwankim.grind75;

import com.giwankim.grind75.support.TreeNode;

public class DiameterOfBinaryTree {

  private int diameter = 0;

  public int diameterOfBinaryTree(TreeNode root) {
    getHeight(root);
    return diameter;
  }

  public int getHeight(TreeNode root) {
    if (root == null) {
      return 0;
    }

    int leftHeight = getHeight(root.left);
    int rightHeight = getHeight(root.right);

    diameter = Math.max(diameter, leftHeight + rightHeight);

    return Math.max(leftHeight, rightHeight) + 1;
  }
}
