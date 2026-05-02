package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class ConvertSortedArrayToBinarySearchTree {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(log n)} excluding the output tree,
   *     where {@code n = nums.length}.
   *     <p><b>Time:</b> each call picks one mid element to allocate exactly one
   *     {@link TreeNode} and recurses on two disjoint halves of size at most {@code n/2},
   *     giving the recurrence {@code T(n) = 2·T(n/2) + O(1)}.
   *     <p><b>Space:</b> the exact halving makes the tree height-balanced, so the recursion
   *     depth is {@code ⌈log_2(n + 1)⌉ = O(log n)}, which bounds the auxiliary stack space.
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
