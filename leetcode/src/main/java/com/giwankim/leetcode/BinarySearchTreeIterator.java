package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayDeque;
import java.util.Deque;

public class BinarySearchTreeIterator {
  static class BSTIterator {
    // Time Complexity: O(n) / amortized O(1), Space Complexity: O(h)
    private final Deque<TreeNode> stack = new ArrayDeque<>();

    public BSTIterator(TreeNode root) {
      pushAllLeftNodes(root);
    }

    public int next() {
      TreeNode node = stack.pop();
      pushAllLeftNodes(node.right);
      return node.val;
    }

    private void pushAllLeftNodes(TreeNode root) {
      while (root != null) {
        stack.push(root);
        root = root.left;
      }
    }

    public boolean hasNext() {
      return !stack.isEmpty();
    }
  }
}
