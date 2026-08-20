package leetcode.p3501_3600;

/**
 * <a href="https://leetcode.com/problems/smallest-palindromic-rearrangement-i/">3517. Smallest
 * Palindromic Rearrangement I</a>
 */
public class SmallestPalindromicRearrangementI {
  /**
   * @implNote Time {@code O(n)} since the counting pass and the fixed 26-bucket sweep each touch
   *     every character at most once — the alphabet itself supplies the sort order, so no
   *     comparison sort is needed — auxiliary space {@code O(n)} for the {@code half} builder and
   *     its reversed copy, while {@code cnt} and {@code mid} stay bounded by the 26-letter
   *     alphabet, where {@code n = s.length()}.
   */
  public String smallestPalindrome(String s) {
    int[] cnt = new int[26];
    for (char c : s.toCharArray()) {
      cnt[c - 'a']++;
    }

    StringBuilder half = new StringBuilder();
    StringBuilder mid = new StringBuilder();
    for (int i = 0; i < 26; i++) {
      half.repeat(i + 'a', cnt[i] / 2);
      cnt[i] -= 2 * (cnt[i] / 2);
      if (cnt[i] > 0) {
        mid.append((char) (i + 'a'));
      }
    }

    StringBuilder ans = new StringBuilder();
    ans.append(half);
    ans.append(mid);
    ans.append(new StringBuilder(half).reverse());
    return ans.toString();
  }
}
