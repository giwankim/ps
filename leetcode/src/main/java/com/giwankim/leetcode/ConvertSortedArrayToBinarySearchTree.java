package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class ConvertSortedArrayToBinarySearchTree {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(log n)}, where {@code n = nums.length}.
   */
  public TreeNode sortedArrayToBST(int[] nums) {
    return sortedArrayToBST(nums, 0, nums.length - 1);
  }

  private TreeNode sortedArrayToBST(int[] nums, int left, int right) {
    if (left > right) {
      return null;
    }
    int mid = left + (right - left) / 2;
    TreeNode leftTree = sortedArrayToBST(nums, left, mid - 1);
    TreeNode rightTree = sortedArrayToBST(nums, mid + 1, right);
    return new TreeNode(nums[mid], leftTree, rightTree);
  }
}
