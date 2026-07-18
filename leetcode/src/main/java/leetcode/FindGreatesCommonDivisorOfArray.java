package leetcode;

public class FindGreatesCommonDivisorOfArray {
  /**
   * @implNote Time {@code O(n + log m)} from one min/max scan plus a Euclidean gcd on the two
   *     extremes, auxiliary space {@code O(1)}, where {@code n = nums.length} and {@code m =
   *     min(nums)}.
   */
  public int findGCD(int[] nums) {
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    for (int num : nums) {
      min = Math.min(min, num);
      max = Math.max(max, num);
    }
    return gcd(min, max);
  }

  private static int gcd(int a, int b) {
    while (b != 0) {
      int temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }
}
