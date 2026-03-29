package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.HashMap;
import java.util.Map;

public class ConstructBinaryTreeFromInorderAndPostorderTraversal {
  private int postIndex;

  public TreeNode buildTree(int[] inorder, int[] postorder) {
    // Time Complexity: O(n), Space Complexity: O(n)
    postIndex = postorder.length - 1;
    Map<Integer, Integer> inorderIndex = new HashMap<>();
    for (int i = 0; i < inorder.length; i++) {
      inorderIndex.put(inorder[i], i);
    }
    return buildTree(postorder, inorderIndex, 0, inorder.length - 1);
  }

  private TreeNode buildTree(
      int[] postOrder, Map<Integer, Integer> inorderIndex, int left, int right) {
    if (left > right) {
      return null;
    }
    TreeNode root = new TreeNode(postOrder[postIndex]);
    postIndex -= 1;
    root.right = buildTree(postOrder, inorderIndex, inorderIndex.get(root.val) + 1, right);
    root.left = buildTree(postOrder, inorderIndex, left, inorderIndex.get(root.val) - 1);
    return root;
  }
}
