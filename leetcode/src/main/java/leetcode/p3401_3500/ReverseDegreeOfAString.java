package leetcode.p3401_3500;

/**
 * <a href="https://leetcode.com/problems/reverse-degree-of-a-string/">3498. Reverse Degree of a
 * String</a>
 */
public class ReverseDegreeOfAString {
  /** @implNote Time {@code O(n)}, space {@code O(1)}, where {@code n = s.length()}. */
  public int reverseDegree(String s) {
    int result = 0;
    for (int i = 0; i < s.length(); i++) {
      result += (26 - (s.charAt(i) - 'a')) * (i + 1);
    }
    return result;
  }
}
