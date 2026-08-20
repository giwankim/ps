package leetcode.p0201_0300;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * <a href="https://leetcode.com/problems/kth-largest-element-in-an-array/">215. Kth Largest Element
 * in an Array</a>
 */
public class KthLargestElementInAnArray {
  /** @implNote Time {@code O(n log k)}, space {@code O(k)}, where {@code n = nums.length}. */
  public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    for (int num : nums) {
      pq.offer(num);
      if (pq.size() > k) {
        pq.poll();
      }
    }
    return pq.poll();
  }

  public int findKthLargest2(int[] nums, int k) {
    Arrays.sort(nums);
    return nums[nums.length - k];
  }
}
