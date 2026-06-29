package leetcode;

public class NumberOfStringsThatAppearAsSubstringsInWord {
  /**
   * @implNote Time {@code O(p * n * m)}, auxiliary space {@code O(1)}, where {@code p =
   *     patterns.length} is the number of patterns, {@code n = word.length()} is the length of the
   *     word, and {@code m} is the maximum pattern length — each {@link String#contains} is a naive
   *     {@code O(n * m)} scan.
   */
  public int numOfStrings(String[] patterns, String word) {
    int result = 0;
    for (String pattern : patterns) {
      if (word.contains(pattern)) {
        result++;
      }
    }
    return result;
  }
}
