package leetcode;

public class PartitionArrayAccordingToGivenPivot {
  /**
   * @implNote Time {@code O(n)}, auxiliary space {@code O(1)} excluding the output, where {@code n
   *     = nums.length}.
   */
  public int[] pivotArray(int[] nums, int pivot) {
    int[] result = new int[nums.length];
    int lo = 0;
    int hi = nums.length - 1;
    for (int i = 0, j = nums.length - 1; i < nums.length && j >= 0; i++, j--) {
      if (nums[i] < pivot) {
        result[lo++] = nums[i];
      }
      if (nums[j] > pivot) {
        result[hi--] = nums[j];
      }
    }

    for (int i = lo; i <= hi; i++) {
      result[i] = pivot;
    }

    return result;
  }
}
