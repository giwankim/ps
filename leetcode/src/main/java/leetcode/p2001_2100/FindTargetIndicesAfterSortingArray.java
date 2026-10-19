package leetcode.p2001_2100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <a href="https://leetcode.com/problems/find-target-indices-after-sorting-array/">2089. Find
 * Target Indices After Sorting Array</a>
 */
public class FindTargetIndicesAfterSortingArray {
  /**
   * @implNote Time {@code O(n log n)} from sorting {@code nums}, after which a single linear scan
   *     collects the matching indices; auxiliary space {@code O(log n)} for the sort's recursion
   *     stack since it reuses {@code nums} in place, excluding the output, where {@code n =
   *     nums.length}.
   */
  public List<Integer> targetIndices(int[] nums, int target) {
    Arrays.sort(nums);
    List<Integer> ans = new ArrayList<>();
    for (int i = 0; i < nums.length; i++) {
      if (nums[i] == target) {
        ans.add(i);
      }
    }
    return ans;
  }
}
