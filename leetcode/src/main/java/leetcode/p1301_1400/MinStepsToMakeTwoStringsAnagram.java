package leetcode.p1301_1400;

/**
 * <a
 * href="https://leetcode.com/problems/minimum-number-of-steps-to-make-two-strings-anagram/">1347.
 * Minimum Number of Steps to Make Two Strings Anagram</a>
 */
public class MinStepsToMakeTwoStringsAnagram {
  public int minSteps(String s, String t) {
    int result = 0;

    int[] counts = new int[26];
    for (char c : t.toCharArray()) {
      counts[c - 'a'] += 1;
    }
    for (char c : s.toCharArray()) {
      counts[c - 'a'] -= 1;
    }

    for (int count : counts) {
      if (count > 0) {
        result += count;
      }
    }

    return result;
  }
}
