package com.giwankim.grind75;

import com.giwankim.grind75.support.TreeNode;

public class DiameterOfBinaryTree {

  public int diameterOfBinaryTree(TreeNode root) {
    if (root == null) {
      return 0;
    }
    int diameter = getHeight(root.left) + getHeight(root.right);
    int subtreeMaxDiameter =
        Math.max(diameterOfBinaryTree(root.left), diameterOfBinaryTree(root.right));
    return Math.max(diameter, subtreeMaxDiameter);
  }

  private int getHeight(TreeNode root) {
    if (root == null) {
      return 0;
    }
    return Math.max(getHeight(root.left), getHeight(root.right)) + 1;
  }
}
