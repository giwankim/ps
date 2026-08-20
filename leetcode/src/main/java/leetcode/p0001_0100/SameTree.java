package leetcode.p0001_0100;

import leetcode.support.TreeNode;

/** <a href="https://leetcode.com/problems/same-tree/">100. Same Tree</a> */
public class SameTree {
  /** @implNote Time {@code O(n)}, space {@code O(h)}. */
  public boolean isSameTree(TreeNode p, TreeNode q) {
    if (p == null || q == null) {
      return p == null && q == null;
    }
    if (p.val != q.val) {
      return false;
    }
    return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
  }
}
