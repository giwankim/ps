package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class InvertBinaryTree {
  public TreeNode invertTree(TreeNode root) {
    if (root == null) {
      return null;
    }
    TreeNode tmp = root.left;
    root.left = invertTree(root.right);
    root.right = invertTree(tmp);
    return root;
  }
}
