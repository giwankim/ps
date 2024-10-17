package leetcode;

public class MissingNumber {
  public int missingNumber(int[] nums) {
    int n = nums.length;
    for (int i = 0; i < nums.length; i++) {
      n ^= i ^ nums[i];
    }
    return n;
  }
}
