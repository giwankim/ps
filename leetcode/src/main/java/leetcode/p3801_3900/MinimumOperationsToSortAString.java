package leetcode.p3801_3900;

import java.nio.CharBuffer;
import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/minimum-operations-to-sort-a-string/">3863. Minimum
 * Operations to Sort a String</a>
 */
public class MinimumOperationsToSortAString {
  /**
   * @implNote Time {@code O(n)}: past 1750 elements {@code Arrays.sort(char[])} is a counting sort
   *     over the {@code 2^16} {@code char} values, and the sorted check, the endpoint test, and the
   *     two occurrence scans for the minimum and maximum are each a single pass. Auxiliary space
   *     {@code O(n)} for the sorted copy, plus the sort's fixed {@code 2^16}-entry count table,
   *     where {@code n = s.length()}.
   */
  public int minOperations(String s) {
    char[] sorted = s.toCharArray();
    Arrays.sort(sorted);

    if (s.contentEquals(CharBuffer.wrap(sorted))) {
      return 0;
    }

    int n = s.length();

    if (n <= 2) {
      return -1;
    }

    char min = sorted[0];
    char max = sorted[n - 1];

    if (s.charAt(0) == min || s.charAt(n - 1) == max) {
      return 1;
    }

    for (int i = 0; i < n - 1; i++) {
      if (s.charAt(i) == min) {
        return 2;
      }
    }

    for (int i = 1; i < n; i++) {
      if (s.charAt(i) == max) {
        return 2;
      }
    }

    return 3;
  }
}
