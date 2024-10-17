package leetcode;

public class MissingNumber {
  public int missingNumber(int[] nums) {
    int curr = 0;
    int n = -1;
    boolean hasZero = false;
    for (int num : nums) {
      n = Math.max(n, num);
      curr += num;
      if (num == 0) {
        hasZero = true;
      }
    }
    int sum = ((n + 1) * n) / 2;
    if (curr == sum) {
      return hasZero ? n + 1 : 0;
    }
    return sum - curr;
  }
}
