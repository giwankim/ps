package leetcode.p0201_0300;

import leetcode.support.TreeNode;

/**
 * <a href="https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/">236. Lowest
 * Common Ancestor of a Binary Tree</a>
 */
public class LowestCommonAncestorOfABinaryTree {
  /** @implNote Time {@code O(n)}, space {@code O(n)}. */
  public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root == null) {
      return null;
    }
    if (root == p || root == q) {
      return root;
    }
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }
    return root;
  }
}
