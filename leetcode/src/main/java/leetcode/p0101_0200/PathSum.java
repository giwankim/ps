package leetcode.p0101_0200;

import leetcode.support.TreeNode;

/** <a href="https://leetcode.com/problems/path-sum/">112. Path Sum</a> */
public class PathSum {
  /** @implNote Time {@code O(n)}, space {@code O(h)}. */
  public boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) {
      return false;
    }
    targetSum -= root.val;
    if (root.left == null && root.right == null) {
      return targetSum == 0;
    }
    return hasPathSum(root.left, targetSum) || hasPathSum(root.right, targetSum);
  }
}
