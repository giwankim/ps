package leetcode.p3701_3800;

/**
 * <a href="https://leetcode.com/problems/count-subarrays-with-majority-element-i/">3737. Count
 * Subarrays With Majority Element I</a>
 */
public class CountSubarraysWithMajorityElementI {
  /** @implNote Time {@code O(n²)}, auxiliary space {@code O(1)}, where {@code n = nums.length}. */
  public int countMajoritySubarrays(int[] nums, int target) {
    int n = nums.length;
    int result = 0;
    for (int s = 0; s < n; s++) {
      int count = 0;
      for (int e = s; e < n; e++) {
        if (nums[e] == target) {
          count++;
        }
        if (2 * count > e - s + 1) {
          result++;
        }
      }
    }
    return result;
  }
}
