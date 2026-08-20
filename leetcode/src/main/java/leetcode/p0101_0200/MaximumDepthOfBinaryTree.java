package leetcode.p0101_0200;

import leetcode.support.TreeNode;

public class MaximumDepthOfBinaryTree {
  /** @implNote Time {@code O(n)}, space {@code O(h)}. */
  public int maxDepth(TreeNode root) {
    if (root == null) {
      return 0;
    }
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
  }
}
