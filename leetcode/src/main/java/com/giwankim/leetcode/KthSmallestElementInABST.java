package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class KthSmallestElementInABST {
  public int kthSmallest(TreeNode root, int k) {
    // Time Complexity: O(n), Space Complexity: O(n)
    List<Integer> nodes = new ArrayList<>();
    inorder(root, nodes);
    return nodes.get(k - 1);
  }

  private void inorder(TreeNode root, List<Integer> nodes) {
    if (root == null) {
      return;
    }
    inorder(root.left, nodes);
    nodes.add(root.val);
    inorder(root.right, nodes);
  }
}
