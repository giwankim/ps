package leetcode;

public class FirstMissingPositive {
  public int firstMissingPositive(int[] nums) {
    for (int i = 0; i < nums.length; i++) {
      while (0 < nums[i] && nums[i] <= nums.length && nums[i] != nums[nums[i] - 1]) {
        swap(i, nums[i] - 1, nums);
      }
    }

    for (int i = 0; i < nums.length; i++) {
      if (nums[i] != i + 1) {
        return i + 1;
      }
    }
    return nums.length + 1;
  }

  private void swap(int i, int j, int[] nums) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
  }
}
