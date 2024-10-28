package leetcode;

import leetcode.support.TreeNode;

public class ValidateBinarySearchTree {
  public boolean isValidBST(TreeNode root) {
    if (root == null) {
      return true;
    }
    return isValidBST(root.left, Long.MIN_VALUE, root.val)
        && isValidBST(root.right, root.val, Long.MAX_VALUE);
  }

  private boolean isValidBST(TreeNode root, long minValue, long maxValue) {
    if (root == null) {
      return true;
    }
    int val = root.val;
    if (val <= minValue || val >= maxValue) {
      return false;
    }
    return isValidBST(root.left, minValue, val) && isValidBST(root.right, val, maxValue);
  }
}
