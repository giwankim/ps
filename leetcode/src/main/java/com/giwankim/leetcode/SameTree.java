package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class SameTree {
  public boolean isSameTree(TreeNode p, TreeNode q) {
    // Time Complexity: O(n), Space Complexity: O(h)
    if (p == null || q == null) {
      return p == null && q == null;
    }
    if (p.val != q.val) {
      return false;
    }
    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
  }
}
