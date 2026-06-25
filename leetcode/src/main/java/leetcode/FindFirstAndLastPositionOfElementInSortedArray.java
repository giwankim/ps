package leetcode;

public class FindFirstAndLastPositionOfElementInSortedArray {
  /** @implNote Time {@code O(log n)}, space {@code O(1)}, where {@code n = nums.length}. */
  public int[] searchRange(int[] nums, int target) {
    return new int[] {firstIndexOf(nums, target), lastIndexOf(nums, target)};
  }

  private int firstIndexOf(int[] nums, int target) {
    int result = -1;
    int lo = 0;
    int hi = nums.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (nums[mid] == target) {
        result = mid;
        hi = mid - 1;
      } else if (nums[mid] > target) {
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return result;
  }

  private int lastIndexOf(int[] nums, int target) {
    int result = -1;
    int lo = 0;
    int hi = nums.length - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (nums[mid] == target) {
        result = mid;
        lo = mid + 1;
      } else if (nums[mid] > target) {
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return result;
  }
}
