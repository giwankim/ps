package leetcode.p3601_3700;

/**
 * <a href="https://leetcode.com/problems/check-divisibility-by-digit-sum-and-product/">3622. Check
 * Divisibility by Digit Sum and Product</a>
 */
public class CheckDivisibilityByDigitSumAndProduct {
  /**
   * @implNote Time {@code O(d)} for the single digit-peeling loop, where {@code d = floor(log10(n))
   *     + 1} is the digit count of {@code n}, so this is {@code O(log n)}. Auxiliary space
   *     {@code O(1)}. The divisor needs no zero guard: a positive {@code n} has at least one
   *     nonzero digit, so {@code sum} is at least 1 even when a zero digit collapses {@code prod}
   *     to 0. It cannot overflow either — the constraint {@code n <= 10^6} caps it at {@code 54 +
   *     9^6 = 531495}.
   */
  public boolean checkDivisibility(int n) {
    int sum = 0;
    int prod = 1;
    int num = n;
    while (num > 0) {
      int digit = num % 10;
      sum += digit;
      prod *= digit;
      num /= 10;
    }
    return (n % (sum + prod) == 0);
  }
}
