package com.giwankim.leetcode;

import com.giwankim.leetcode.support.TreeNode;

public class ConvertSortedArrayToBinarySearchTree {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(log n)} excluding the output tree,
   *     where {@code n = nums.length}.
   *     <p><b>Time:</b> each call picks one mid element to allocate exactly one
   *     {@link TreeNode} and recurses on two disjoint halves of size at most {@code n/2},
   *     giving the recurrence {@code T(n) = 2·T(n/2) + O(1)}. Applying the master theorem
   *     with {@code a = 2}, {@code b = 2}, {@code f(n) = O(1)}: {@code n^log_b(a) = n}
   *     strictly dominates {@code f(n)}, so case 1 gives {@code T(n) = Θ(n)} — equivalently,
   *     the leaves dominate, with exactly {@code n} of them (one per array element).
   *     <p><b>Space:</b> the exact halving makes the tree height-balanced, so the recursion
   *     depth is {@code ⌈log_2(n + 1)⌉ = O(log n)}, which bounds the auxiliary stack space.
   *     The constructed tree itself occupies {@code O(n)} and is the caller's output.
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
