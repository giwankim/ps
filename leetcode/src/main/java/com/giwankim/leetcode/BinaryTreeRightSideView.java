package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class BinaryTreeRightSideView {
  /**
   * @implNote Time {@code O(n)}, space {@code O(n)}.
   */
  public List<Integer> rightSideView(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    rightSideView(root, result, 0);
    return result;
  }

  private void rightSideView(TreeNode root, List<Integer> result, int level) {
    if (root == null) {
      return;
    }
    if (result.size() == level) {
      result.add(root.val);
    }
    rightSideView(root.right, result, level + 1);
    rightSideView(root.left, result, level + 1);
  }
}
