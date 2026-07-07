package leetcode;

public class ConcatenateNonZeroDigitsAndMultiplyBySumI {
  /** @implNote Time {@code O(log n)}, space {@code O(1)}. */
  public long sumAndMultiply(int n) {
    long x = 0;
    long sum = 0;
    long pow = 1;
    while (n != 0) {
      int digit = n % 10;
      sum += digit;
      if (digit != 0) {
        x += pow * digit;
        pow *= 10;
      }
      n /= 10;
    }
    return x * sum;
  }
}
