package leetcode;

public class CountSubarraysWithMajorityElementII {
  public long countMajoritySubarrays(int[] nums, int target) {
    int n = nums.length;
    long[] prefix = new long[n + 1];
    for (int i = 1; i <= n; i++) {
      prefix[i] = prefix[i - 1] + (nums[i - 1] == target ? 1 : -1);
    }
    return countLess(prefix, 0, n, new long[n + 1]);
  }

  private static long countLess(long[] arr, int lo, int hi, long[] buf) {
    if (lo >= hi) {
      return 0;
    }
    int mid = lo + (hi - lo) / 2;
    long result = countLess(arr, lo, mid, buf) + countLess(arr, mid + 1, hi, buf);
    result += merge(arr, lo, mid, hi, buf);
    return result;
  }

  private static long merge(long[] arr, int lo, int mid, int hi, long[] buf) {
    long result = 0;
    int left = lo;
    int right = mid + 1;
    int out = lo;
    while (left <= mid && right <= hi) {
      if (arr[left] < arr[right]) {
        result += hi - right + 1;
        buf[out++] = arr[left++];
      } else {
        buf[out++] = arr[right++];
      }
    }
    while (left <= mid) {
      buf[out++] = arr[left++];
    }
    while (right <= hi) {
      buf[out++] = arr[right++];
    }
    System.arraycopy(buf, lo, arr, lo, hi - lo + 1);
    return result;
  }
}
