package grind75;

import grind75.support.TreeNode;

public class InvertBinaryTree {

  public TreeNode invertTree(TreeNode root) {
    if (root == null) {
      return null;
    }
    TreeNode temp = invertTree(root.left);
    root.left = invertTree(root.right);
    root.right = temp;
    return root;
  }
}
