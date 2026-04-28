package com.giwankim.leetcode;

public class MaximumSubarray {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(1)},
   *     where {@code n = nums.length}.
   *     <p>Streaming variant of the prefix-sum approach. Any subarray sum
   *     equals the difference of two prefix sums
   *     ({@code sum(nums[l..r]) = prefix[r + 1] - prefix[l]}), so the largest
   *     subarray ending at index {@code i} is {@code prefix[i + 1] - min}
   *     where {@code min} is the minimum of {@code prefix[0]} through
   *     {@code prefix[i]}. The loop maintains {@code currentSum} as the
   *     running prefix sum and {@code minSum} as that running minimum, with
   *     {@code result} tracking the running maximum of
   *     {@code currentSum - minSum}. The update order — read {@code minSum}
   *     into {@code result}, then refresh {@code minSum} — keeps the chosen
   *     left boundary strictly before the current element, so the difference
   *     always represents a non-empty subarray. The empty prefix is included
   *     in the {@code minSum} candidates (its value of {@code 0} is the
   *     initializer); this lets single-element subarrays compete, which is
   *     what makes the algorithm correct when every value in {@code nums}
   *     is negative.
   */
  public int maxSubArray(int[] nums) {
    int result = Integer.MIN_VALUE;
    int currentSum = 0;
    int minSum = 0;
    for (int num : nums) {
      currentSum += num;
      result = Math.max(result, currentSum - minSum);
      minSum = Math.min(minSum, currentSum);
    }
    return result;
  }
}
