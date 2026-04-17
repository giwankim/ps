package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class MinimumAbsoluteDifferenceInBST {
  TreeNode prev;
  int minDifference;

  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public int getMinimumDifference(TreeNode root) {
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

  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public int getMinimumDifference2(TreeNode root) {
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
