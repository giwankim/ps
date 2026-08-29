package leetcode.p2901_3000;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <a
 * href="https://leetcode.com/problems/make-lexicographically-smallest-array-by-swapping-elements/">2948.
 * Make Lexicographically Smallest Array by Swapping Elements</a>
 */
public class MakeLexicographicallySmallestArrayBySwappingElements {
  public int[] lexicographicallySmallestArray(int[] nums, int limit) {
    int n = nums.length;
    Integer[] order = new Integer[n];
    Arrays.setAll(order, i -> i);
    Arrays.sort(order, (i, j) -> Integer.compare(nums[i], nums[j]));

    int[] ans = new int[n];
    int l = 0;
    for (int i = 1; i < n; i++) {
      if (nums[order[i]] - nums[order[i - 1]] > limit) {
        // flush
        List<Integer> indices = new ArrayList<>(Arrays.asList(order).subList(l, i));
        indices.sort(null);
        for (int j = 0; j < indices.size(); j++) {
          ans[indices.get(j)] = nums[order[l + j]];
        }
        l = i;
      }
    }
    // flush
    List<Integer> indices = new ArrayList<>(Arrays.asList(order).subList(l, n));
    indices.sort(null);
    for (int i = 0; i < indices.size(); i++) {
      ans[indices.get(i)] = nums[order[l + i]];
    }
    return ans;
  }
}
