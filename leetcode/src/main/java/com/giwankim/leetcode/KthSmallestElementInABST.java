package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayDeque;
import java.util.Deque;

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

  public int kthSmallest2(TreeNode root, int k) {
    // Time Complexity: O(n), Space Complexity: O(n)
    Deque<TreeNode> stack = new ArrayDeque<>();
    pushAllLeft(root, stack);
    while (!stack.isEmpty()) {
      TreeNode node = stack.pop();
      k -= 1;
      if (k == 0) {
        return node.val;
      }
      pushAllLeft(node.right, stack);
    }
    return -1;
  }

  private void pushAllLeft(TreeNode root, Deque<TreeNode> stack) {
    while (root != null) {
      stack.push(root);
      root = root.left;
    }
  }
}
