package grind75;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {
  /**
   * @implNote Time {@code O(n)} expected, auxiliary space {@code O(n)} for the {@code map}, where
   *     {@code n = nums.length}. One pass hashes each value only after probing for its complement,
   *     so an element is never paired with itself.
   */
  public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
      if (map.containsKey(target - nums[i])) {
        return new int[] {map.get(target - nums[i]), i};
      }
      map.put(nums[i], i);
    }
    return null;
  }
}
