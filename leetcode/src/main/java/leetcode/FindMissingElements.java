package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindMissingElements {
  /**
   * @implNote Time {@code O(n log n + k)} — the in-place {@code Arrays.sort} dominates the single
   *     pass that then walks every adjacent pair and emits each gap once — auxiliary space
   *     {@code O(log n)} for that sort's recursion, beyond the {@code O(k)} result, where {@code n
   *     = nums.length} and {@code k} is the number of missing integers, {@code max - min + 1 - n}.
   */
  public List<Integer> findMissingElements(int[] nums) {
    List<Integer> ans = new ArrayList<>();
    Arrays.sort(nums);
    for (int i = 1; i < nums.length; i++) {
      for (int j = nums[i - 1] + 1; j < nums[i]; j++) {
        ans.add(j);
      }
    }
    return ans;
  }
}
