package leetcode;

public class FindTheLargestAlmostMissingInteger {
  /**
   * @implNote Time {@code O((n - k + 1)(k + V))}, auxiliary space {@code O(V)} for {@code cnt} plus
   *     the single live {@code window}, where {@code n = nums.length} and {@code V = 51} is the
   *     size of the value range {@code 0..50}. Rebuilding {@code window} per start index counts a
   *     value once however often it repeats inside that window, which is exactly what "appears in
   *     one subarray" means.
   */
  public int largestInteger(int[] nums, int k) {
    int n = nums.length;
    int[] cnt = new int[51];
    for (int i = 0; i < n - k + 1; i++) {
      boolean[] window = new boolean[51];
      for (int j = i; j < i + k; j++) {
        window[nums[j]] = true;
      }
      for (int j = 0; j < 51; j++) {
        if (window[j]) {
          cnt[j]++;
        }
      }
    }

    for (int i = 50; i >= 0; i--) {
      if (cnt[i] == 1) {
        return i;
      }
    }
    return -1;
  }
}
