package leetcode.p0101_0200;

import leetcode.support.TreeNode;

/**
 * <a href="https://leetcode.com/problems/flatten-binary-tree-to-linked-list/">114. Flatten Binary
 * Tree to Linked List</a>
 */
public class FlattenBinaryTreeToLinkedList {
  private TreeNode prev = null;

  /** @implNote Time {@code O(n)}, space {@code O(h)}. */
  public void flatten(TreeNode root) {
    if (root == null) {
      return;
    }
    flatten(root.right);
    flatten(root.left);
    root.left = null;
    root.right = prev;
    prev = root;
  }
}
