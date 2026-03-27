package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.HashMap;
import java.util.Map;

public class ConstructBinaryTreeFromPreorderAndInorderTraversal {
  private int preIndex = 0;

  public TreeNode buildTree(int[] preorder, int[] inorder) {
    // Time Complexity: O(n), Space Complexity: O(n)
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
