package leetcode.p0001_0100;

import java.util.HashMap;
import java.util.Map;

/** <a href="https://leetcode.com/problems/two-sum/">1. Two Sum</a> */
public class TwoSum {
  /**
   * @implNote Time {@code O(n)} expected, auxiliary space {@code O(n)} for the {@code valueToIndex}
   *     map, where {@code n = nums.length}. One pass hashes each value only after probing for its
   *     complement, so an element is never paired with itself.
   */
  public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> valueToIndex = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
      if (valueToIndex.containsKey(target - nums[i])) {
        return new int[] {valueToIndex.get(target - nums[i]), i};
      }
      valueToIndex.put(nums[i], i);
    }
    return new int[0];
  }
}
