package leetcode.p3501_3600;

import java.util.ArrayList;
import java.util.List;

public class MaximumProductOfTwoDigits {
  /**
   * @implNote Time {@code O(d^2)} — the extraction loop peels {@code d} digits, then the nested
   *     loops test each of the {@code d * (d - 1) / 2} unordered position pairs, which dominates.
   *     Auxiliary space {@code O(d)} for the {@code digits} list. Here {@code d = floor(log10(n)) +
   *     1} is the digit count of {@code n}, so this is {@code O(log^2 n)} time and {@code O(log n)}
   *     space; the constraint {@code n <= 10^9} caps {@code d} at 10, bounding the scan at 45
   *     pairs.
   */
  public int maxProduct(int n) {
    List<Integer> digits = new ArrayList<>();
    while (n > 0) {
      digits.add(n % 10);
      n /= 10;
    }

    int result = 0;
    for (int i = 0; i < digits.size(); i++) {
      for (int j = i + 1; j < digits.size(); j++) {
        result = Math.max(result, digits.get(i) * digits.get(j));
      }
    }
    return result;
  }
}
