package leetcode.p2101_2200;

/**
 * <a href="https://leetcode.com/problems/check-if-all-as-appears-before-all-bs/">2124. Check if All
 * A's Appears Before All B's</a>
 */
public class CheckIfAllASAppearsBeforeAllBS {
  /** @implNote Time {@code O(n)}, auxiliary space {@code O(1)}, where {@code n = s.length()}. */
  public boolean checkString(String s) {
    int lastA = s.lastIndexOf('a');
    int firstB = s.indexOf('b');
    if (lastA == -1 || firstB == -1) {
      return true;
    }
    return lastA < firstB;
  }
}
