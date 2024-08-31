package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
  public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(nums);

    for (int i = 0; i < nums.length; i++) {
      if (i > 0 && nums[i] == nums[i - 1]) {
        continue;
      }

      int lo = i + 1;
      int hi = nums.length - 1;
      while (lo < hi) {
        int target = nums[i] + nums[lo] + nums[hi];
        if (target == 0) {
          result.add(List.of(nums[i], nums[lo], nums[hi]));
          while (lo < hi && nums[lo] == nums[lo + 1]) {
            lo += 1;
          }
          while (lo < hi && nums[hi] == nums[hi - 1]) {
            hi -= 1;
          }
          lo += 1;
          hi -= 1;
        } else if (target < 0) {
          lo += 1;
        } else {
          hi -= 1;
        }
      }
    }
    return result;
  }
}
