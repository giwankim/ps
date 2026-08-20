package leetcode.p0901_1000;

import leetcode.support.TreeNode;

/** <a href="https://leetcode.com/problems/range-sum-of-bst/">938. Range Sum of BST</a> */
public class RangeSumOfBST {
  public int rangeSumBST(TreeNode root, int low, int high) {
    if (root == null) {
      return 0;
    }
    if (root.val > high) {
      return rangeSumBST(root.left, low, high);
    }
    if (root.val < low) {
      return rangeSumBST(root.right, low, high);
    }
    return root.val + rangeSumBST(root.left, low, high) + rangeSumBST(root.right, low, high);
  }
}
