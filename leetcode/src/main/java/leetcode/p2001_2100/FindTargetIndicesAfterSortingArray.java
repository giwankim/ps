package leetcode.p2001_2100;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.com/problems/find-target-indices-after-sorting-array/">2089. Find
 * Target Indices After Sorting Array</a>
 */
public class FindTargetIndicesAfterSortingArray {

  /**
   * @implNote Time {@code O(n)} from one pass counting elements below {@code target} and a second
   *     pass emitting the indices of the matches, which start at that count in sorted order;
   *     auxiliary space {@code O(1)} excluding the output, where {@code n = nums.length}.
   */
  public List<Integer> targetIndices(int[] nums, int target) {
    int lessThan = 0;
    for (int num : nums) {
      if (num < target) {
        lessThan++;
      }
    }
    List<Integer> ans = new ArrayList<>();
    for (int num : nums) {
      if (num == target) {
        ans.add(lessThan++);
      }
    }
    return ans;
  }
}
