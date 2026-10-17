package leetcode.p3501_3600;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.com/problems/minimum-swaps-to-sort-by-digit-sum/">3551. Minimum Swaps
 * to Sort by Digit Sum</a>
 */
public class MinimumSwapsToSortByDigitSum {
  /**
   * @implNote Time {@code O(n log n log M)} since the comparator recomputes both digit sums, each
   *     peeling up to {@code floor(log10(M)) + 1} digits, on every one of the sort's {@code O(n log
   *     n)} comparisons — the constraint {@code nums[i] <= 10^9} caps that at 10 digits, so this is
   *     effectively {@code O(n log n)} — while the cycle walk marks each index once in
   *     {@code O(n)}; auxiliary space {@code O(n)} for {@code idx}, the sort's merge buffer, and
   *     {@code visited}, where {@code n = nums.length} and {@code M = max(nums)}.
   */
  public int minSwaps(int[] nums) {
    int n = nums.length;

    List<Integer> idx = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      idx.add(i);
    }
    idx.sort((i, j) -> {
      int cmp = Integer.compare(digitSum(nums[i]), digitSum(nums[j]));
      return cmp != 0 ? cmp : Integer.compare(nums[i], nums[j]);
    });

    int ncycles = 0;
    boolean[] visited = new boolean[n];
    for (int i = 0; i < n; i++) {
      if (visited[i]) {
        continue;
      }
      ncycles++;
      int j = i;
      while (!visited[j]) {
        visited[j] = true;
        j = idx.get(j);
      }
    }

    return n - ncycles;
  }

  private int digitSum(int x) {
    int res = 0;
    while (x != 0) {
      res += x % 10;
      x /= 10;
    }
    return res;
  }
}
