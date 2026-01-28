package com.giwankim.grind75;

import com.giwankim.grind75.support.TreeNode;

/**
 * Maximum Depth of Binary Tree
 *
 * <p>Maximum depth is the number of nodes along the longest path from the root node down to the
 * farthest leaf node.
 */
public class MaximumDepthOfBinaryTree {

  public int maxDepth(TreeNode root) {
    if (root == null) {
      return 0;
    }
    return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
  }
}
