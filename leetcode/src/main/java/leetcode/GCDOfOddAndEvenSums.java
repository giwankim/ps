package leetcode;

public class GCDOfOddAndEvenSums {
  /**
   * @implNote Time {@code O(n)} from the two summation loops; the Euclid reduction adds only
   *     {@code O(log n)}. Auxiliary space {@code O(1)}.
   */
  public int gcdOfOddEvenSums(int n) {
    int sumOdd = 0;
    for (int i = 0; i < n; i++) {
      sumOdd += 2 * i + 1;
    }
    int sumEven = 0;
    for (int i = 0; i < n; i++) {
      sumEven += 2 * (i + 1);
    }
    return gcd(sumOdd, sumEven);
  }

  private static int gcd(int a, int b) {
    while (b != 0) {
      int temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }
}
