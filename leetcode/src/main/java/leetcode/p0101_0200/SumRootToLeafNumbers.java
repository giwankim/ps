package leetcode.p0101_0200;

import leetcode.support.TreeNode;

/**
 * <a href="https://leetcode.com/problems/sum-root-to-leaf-numbers/">129. Sum Root to Leaf
 * Numbers</a>
 */
public class SumRootToLeafNumbers {
  /** @implNote Time {@code O(n)}, space {@code O(n)}. */
  public int sumNumbers(TreeNode root) {
    return sumNumbers(root, 0);
  }

  private int sumNumbers(TreeNode root, int pathSum) {
    if (root == null) {
      return 0;
    }
    pathSum = pathSum * 10 + root.val;
    if (root.left == null && root.right == null) {
      return pathSum;
    }
    return sumNumbers(root.left, pathSum) + sumNumbers(root.right, pathSum);
  }
}
