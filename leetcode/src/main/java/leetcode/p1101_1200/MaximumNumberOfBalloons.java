package leetcode.p1101_1200;

/**
 * <a href="https://leetcode.com/problems/maximum-number-of-balloons/">1189. Maximum Number of
 * Balloons</a>
 */
public class MaximumNumberOfBalloons {
  public int maxNumberOfBalloons(String text) {
    int[] counts = new int[26];
    for (char c : text.toCharArray()) {
      counts[c - 'a']++;
    }
    int result = Integer.MAX_VALUE;
    for (char c : "balon".toCharArray()) {
      if (c == 'a' || c == 'b' || c == 'n') {
        result = Math.min(result, counts[c - 'a']);
      } else if (c == 'l' || c == 'o') {
        result = Math.min(result, counts[c - 'a'] / 2);
      }
    }
    return result;
  }
}
