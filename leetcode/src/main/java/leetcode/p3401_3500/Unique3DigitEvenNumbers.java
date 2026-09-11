package leetcode.p3401_3500;

import java.util.HashSet;
import java.util.Set;

/**
 * <a href="https://leetcode.com/problems/unique-3-digit-even-numbers/">3483. Unique 3-Digit Even
 * Numbers</a>
 */
public class Unique3DigitEvenNumbers {
  /**
   * @implNote Time {@code O(n^3)} over the ordered triples of distinct indices, at most {@code 10 *
   *     9 * 8 = 720} under {@code digits.length <= 10}. Auxiliary space {@code O(1)}, since
   *     {@code result} holds only even three-digit values and there are just 450 of those, where
   *     {@code n = digits.length}.
   */
  public int totalNumbers(int[] digits) {
    Set<Integer> result = new HashSet<>();
    int n = digits.length;
    for (int i = 0; i < n; i++) {
      if (digits[i] == 0) {
        continue;
      }
      for (int j = 0; j < n; j++) {
        if (j == i) {
          continue;
        }
        for (int k = 0; k < n; k++) {
          if (k == i || k == j) {
            continue;
          }
          if ((digits[k] & 1) != 0) {
            continue;
          }
          result.add(digits[i] * 100 + digits[j] * 10 + digits[k]);
        }
      }
    }
    return result.size();
  }
}
