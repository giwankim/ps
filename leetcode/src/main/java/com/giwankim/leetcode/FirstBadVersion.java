package com.giwankim.leetcode;

class VersionControl {
  boolean isBadVersion(int version) {
    throw new UnsupportedOperationException("Provided by harness");
  }
}

public class FirstBadVersion extends VersionControl {
  /**
   * Time complexity: {@code O(log n)} — the search window halves each iteration.
   *
   * <p>Space complexity: {@code O(1)} — only the {@code lo}, {@code hi}, and {@code mid} indices
   * are tracked.
   */
  public int firstBadVersion(int n) {
    int lo = 1;
    int hi = n;
    while (lo < hi) {
      int mid = lo + (hi - lo) / 2;
      if (isBadVersion(mid)) {
        hi = mid;
      } else {
        lo = mid + 1;
      }
    }
    return lo;
  }
}
