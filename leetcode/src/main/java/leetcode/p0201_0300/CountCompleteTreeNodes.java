package leetcode.p0201_0300;

import leetcode.support.TreeNode;

/**
 * <a href="https://leetcode.com/problems/count-complete-tree-nodes/">222. Count Complete Tree
 * Nodes</a>
 */
public class CountCompleteTreeNodes {
  /** @implNote Time {@code O(log^2 n)}, space {@code O(log n)}. */
  public int countNodes(TreeNode root) {
    if (root == null) {
      return 0;
    }
    int leftHeight = height(root, true);
    int rightHeight = height(root, false);
    if (leftHeight == rightHeight) {
      return (1 << leftHeight) - 1;
    }
    return 1 + countNodes(root.left) + countNodes(root.right);
  }

  private int height(TreeNode root, boolean goLeft) {
    int result = 0;
    while (root != null) {
      result += 1;
      root = goLeft ? root.left : root.right;
    }
    return result;
  }
}
