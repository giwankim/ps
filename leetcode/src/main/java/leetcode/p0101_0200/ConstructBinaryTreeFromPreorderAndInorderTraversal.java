package leetcode.p0101_0200;

import java.util.HashMap;
import java.util.Map;
import leetcode.support.TreeNode;

/**
 * <a
 * href="https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/">105.
 * Construct Binary Tree from Preorder and Inorder Traversal</a>
 */
public class ConstructBinaryTreeFromPreorderAndInorderTraversal {
  private int preIndex = 0;

  /** @implNote Time {@code O(n)}, space {@code O(n)}. */
  public TreeNode buildTree(int[] preorder, int[] inorder) {
    Map<Integer, Integer> inorderIndex = new HashMap<>();
    for (int i = 0; i < inorder.length; i++) {
      inorderIndex.put(inorder[i], i);
    }
    return buildTree(preorder, 0, inorder.length - 1, inorderIndex);
  }

  private TreeNode buildTree(
      int[] preorder, int left, int right, Map<Integer, Integer> inorderIndex) {
    if (left > right) {
      return null;
    }
    TreeNode root = new TreeNode(preorder[preIndex++]);
    root.left = buildTree(preorder, left, inorderIndex.get(root.val) - 1, inorderIndex);
    root.right = buildTree(preorder, inorderIndex.get(root.val) + 1, right, inorderIndex);
    return root;
  }
}
