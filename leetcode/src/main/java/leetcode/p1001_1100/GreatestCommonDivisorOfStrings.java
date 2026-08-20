package leetcode.p1001_1100;

public class GreatestCommonDivisorOfStrings {
  /**
   * @implNote Time {@code O(g·(n + m))}, space {@code O(n + m)}, where {@code n = str1.length()},
   *     {@code m = str2.length()}, and {@code g = gcd(n, m)}.
   */
  public String gcdOfStrings(String str1, String str2) {
    int n = str1.length();
    int m = str2.length();
    String result = "";
    int gcd = gcd(n, m);
    for (int len = 1; len <= gcd; len++) {
      String prefix = str1.substring(0, len);
      if (isDivisor(prefix, str1) && isDivisor(prefix, str2)) {
        result = prefix;
      }
    }
    return result;
  }

  private static int gcd(int a, int b) {
    while (b != 0) {
      int temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }

  private boolean isDivisor(String divisor, String s) {
    if (s.length() % divisor.length() != 0) {
      return false;
    }
    int times = s.length() / divisor.length();
    return divisor.repeat(times).equals(s);
  }
}
