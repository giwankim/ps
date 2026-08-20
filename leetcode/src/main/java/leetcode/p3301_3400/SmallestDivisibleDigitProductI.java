package leetcode.p3301_3400;

public class SmallestDivisibleDigitProductI {
  /**
   * @implNote Time {@code O(d)}, auxiliary space {@code O(1)}, where {@code d} is the number of
   *     decimal digits in the answer.
   *     <p><b>Why the scan is bounded:</b> any number ending in 0 has digit product 0, and 0 is
   *     divisible by every {@code t >= 1}, so every multiple of 10 satisfies the condition outright
   *     — whatever {@code t} is. The next multiple of 10 at or above {@code n} is at most 9 steps
   *     away, so the scan stops after at most 10 candidates. The bound is tight: {@code n = 91, t =
   *     10} rejects 91..99 and settles on 100.
   *     <p><b>Cost per candidate:</b> multiplying the digits out costs {@code O(d)}, so the total
   *     is {@code O(10 * d) = O(d)} — constant under these constraints, where {@code n <= 100} caps
   *     the answer at 100 and {@code d} at 3.
   */
  public int smallestNumber(int n, int t) {
    while (true) {
      int prod = 1;
      int temp = n;
      while (temp > 0) {
        prod *= temp % 10;
        temp /= 10;
      }
      if (prod % t == 0) {
        return n;
      }
      n += 1;
    }
  }
}
