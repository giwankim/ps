package leetcode.p0101_0200;

/** <a href="https://leetcode.com/problems/find-peak-element/">162. Find Peak Element</a> */
public class FindPeakElement {
  /**
   * Time: {@code O(log n)}.
   *
   * <p>Space: {@code O(1)}.
   */
  public int findPeakElement(int[] nums) {
    int low = 0;
    int high = nums.length - 1;
    while (low < high) {
      int mid = low + (high - low) / 2;
      if (nums[mid] < nums[mid + 1]) {
        low = mid + 1;
      } else {
        high = mid;
      }
    }
    return low;
  }
}
