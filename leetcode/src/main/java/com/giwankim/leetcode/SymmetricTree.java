package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.LinkedList;
import java.util.Queue;

public class SymmetricTree {
  public boolean isSymmetric(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(n)
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    queue.offer(root);
    while (!queue.isEmpty()) {
      TreeNode t1 = queue.poll();
      TreeNode t2 = queue.poll();
      if (t1 == null && t2 == null) {
        continue;
      }
      if (t1 == null || t2 == null) {
        return false;
      }
      if (t1.val != t2.val) {
        return false;
      }
      queue.offer(t1.left);
      queue.offer(t2.right);
      queue.offer(t1.right);
      queue.offer(t2.left);
    }
    return true;
  }

  public boolean isSymmetric2(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(n)
    return isMirror(root, root);
  }

  private boolean isMirror(TreeNode t1, TreeNode t2) {
    if (t1 == null && t2 == null) {
      return true;
    }
    if (t1 == null || t2 == null) {
      return false;
    }
    if (t1.val != t2.val) {
      return false;
    }
    return isMirror(t1.left, t2.right) && isMirror(t1.right, t2.left);
  }
}
