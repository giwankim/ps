package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class KthSmallestElementInABST {
  private int count;
  private int val;

  public int kthSmallest(TreeNode root, int k) {
    // Time Complexity: O(n), Space Complexity: O(n)
    count = 0;
    inorder(root, k);
    return val;
  }

  private void inorder(TreeNode root, int k) {
    if (root == null) {
      return;
    }
    inorder(root.left, k);
    count += 1;
    if (count == k) {
      val = root.val;
      return;
    }
    inorder(root.right, k);
  }
}
