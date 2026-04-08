package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class MinimumAbsoluteDifferenceInBST {
  TreeNode prev;
  int minDifference;

  public int getMinimumDifference(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(n)
    prev = null;
    minDifference = Integer.MAX_VALUE;
    findMinDifference(root);
    return minDifference;
  }

  private void findMinDifference(TreeNode root) {
    if (root == null) {
      return;
    }
    findMinDifference(root.left);
    if (prev != null) {
      minDifference = Math.min(minDifference, root.val - prev.val);
    }
    prev = root;
    findMinDifference(root.right);
  }

  public int getMinimumDifference2(TreeNode root) {
    // Time Complexity: O(n), Space Complexity: O(n)
    List<TreeNode> nodes = new ArrayList<>();
    inorder(root, nodes);
    int result = Integer.MAX_VALUE;
    for (int i = 0; i + 1 < nodes.size(); i++) {
      int diff = nodes.get(i + 1).val - nodes.get(i).val;
      result = Math.min(result, diff);
    }
    return result;
  }

  private void inorder(TreeNode root, List<TreeNode> nodes) {
    if (root == null) {
      return;
    }
    inorder(root.left, nodes);
    nodes.add(root);
    inorder(root.right, nodes);
  }
}
