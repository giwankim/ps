package leetcode;

public class TwoSum {
  public int[] twoSum(int[] nums, int target) {
    //    Arrays.sort(nums);
    int left = 0;
    int right = nums.length;
    while (left < right) {
      int sum = nums[left] + nums[right];
      if (sum < target) {
        left += 1;
      } else if (sum > target) {
        right -= 1;
      } else if (sum == target) {
        return new int[] {left, right};
      }
    }
    return null;
  }
}
