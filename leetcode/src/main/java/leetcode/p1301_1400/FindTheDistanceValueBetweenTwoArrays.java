package leetcode.p1301_1400;

/**
 * <a href="https://leetcode.com/problems/find-the-distance-value-between-two-arrays/">1385. Find
 * the Distance Value Between Two Arrays</a>
 */
public class FindTheDistanceValueBetweenTwoArrays {
  /**
   * @implNote Time {@code O(n * m)}, space {@code O(1)}, where {@code n = arr1.length} and {@code m
   *     = arr2.length}.
   */
  public int findTheDistanceValue(int[] arr1, int[] arr2, int d) {
    int ans = 0;
    for (int x : arr1) {
      boolean isClose = false;
      for (int y : arr2) {
        if (Math.abs(x - y) <= d) {
          isClose = true;
          break;
        }
      }
      if (!isClose) {
        ans++;
      }
    }
    return ans;
  }
}
