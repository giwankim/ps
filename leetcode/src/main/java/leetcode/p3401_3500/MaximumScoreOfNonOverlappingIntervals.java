package leetcode.p3401_3500;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <a href="https://leetcode.com/problems/maximum-score-of-non-overlapping-intervals/">3414. Maximum
 * Score of Non-overlapping Intervals</a>
 */
public class MaximumScoreOfNonOverlappingIntervals {
  private int n;
  private long[][] dp;
  private List<Integer>[][] memo;
  private Interval[] sorted;

  /**
   * @implNote Time {@code O(n log n)}: the sort, then {@link #maxWeight} and {@link #reconstruct}
   *     each fill at most {@code 4n} memoized states with one {@link #next} binary search apiece.
   *     Space {@code O(n)} for {@code sorted}, {@code dp}, {@code memo} (every entry holds at most
   *     four indices), and the recursion stack, where {@code n = intervals.size()}.
   */
  public int[] maximumWeight(List<List<Integer>> intervals) {
    n = intervals.size();
    dp = new long[n][5];
    for (long[] r : dp) {
      Arrays.fill(r, -1);
    }
    memo = (List<Integer>[][]) new List<?>[n][5];

    sorted = new Interval[n];
    for (int i = 0; i < n; i++) {
      List<Integer> interval = intervals.get(i);
      sorted[i] = new Interval(interval.get(0), interval.get(1), interval.get(2), i);
    }
    Arrays.sort(sorted, (a, b) -> Integer.compare(a.start, b.start));

    return reconstruct(0, 4).stream().mapToInt(Integer::intValue).toArray();
  }

  /**
   * @implNote Time {@code O(n log n)} across all calls: at most {@code 4n} states are computed,
   *     each with one {@link #next} binary search, and every memo hit is {@code O(1)}. Recursion
   *     depth reaches {@code n} along the skip chain, so the stack is {@code O(n)}, where {@code n
   *     = sorted.length}.
   */
  private long maxWeight(int i, int remaining) {
    if (i == n || remaining == 0) {
      return 0;
    }
    if (dp[i][remaining] != -1) {
      return dp[i][remaining];
    }
    long result = maxWeight(i + 1, remaining);
    result = Math.max(result, sorted[i].weight + maxWeight(next(i), remaining - 1));
    dp[i][remaining] = result;
    return result;
  }

  /**
   * @implNote Time {@code O(log n)} for the binary search over the start-sorted suffix, space
   *     {@code O(1)}, where {@code n = sorted.length}.
   */
  private int next(int i) {
    int result = n;
    int end = sorted[i].end;
    int lo = i + 1;
    int hi = n - 1;
    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (sorted[mid].start > end) {
        result = mid;
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }
    return result;
  }

  /**
   * @implNote Time {@code O(n log n)} across all calls: at most {@code 4n} memoized states, each
   *     with one {@link #next} binary search plus copying and sorting a list of at most four
   *     indices. Space {@code O(n)} for {@code memo} and the recursion stack, where {@code n =
   *     sorted.length}.
   */
  private List<Integer> reconstruct(int i, int remaining) {
    if (i == n || remaining == 0) {
      return Collections.emptyList();
    }
    if (memo[i][remaining] != null) {
      return memo[i][remaining];
    }
    long takeScore = sorted[i].weight + maxWeight(next(i), remaining - 1);
    long skipScore = maxWeight(i + 1, remaining);

    List<Integer> result = null;
    if (takeScore > skipScore) {
      result = new ArrayList<>(reconstruct(next(i), remaining - 1));
      result.add(sorted[i].index);
      result.sort(Integer::compareTo);
    } else if (takeScore < skipScore) {
      result = new ArrayList<>(reconstruct(i + 1, remaining));
    } else {
      List<Integer> take = new ArrayList<>(reconstruct(next(i), remaining - 1));
      take.add(sorted[i].index);
      take.sort(Integer::compareTo);
      List<Integer> skip = new ArrayList<>(reconstruct(i + 1, remaining));
      if (skip.getFirst() < take.getFirst()) {
        result = skip;
      } else {
        result = take;
      }
    }
    memo[i][remaining] = result;
    return result;
  }

  record Interval(int start, int end, int weight, int index) {}
}
