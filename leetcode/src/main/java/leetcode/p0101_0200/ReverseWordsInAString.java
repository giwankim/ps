package leetcode.p0101_0200;

import java.util.Arrays;
import java.util.List;

/**
 * <a href="https://leetcode.com/problems/reverse-words-in-a-string/">151. Reverse Words in a
 * String</a>
 */
public class ReverseWordsInAString {
  /** @implNote Time {@code O(n)}, space {@code O(n)}. */
  public String reverseWords(String s) {
    String[] words = s.trim().split("\\s+");
    List<String> reversedWords = Arrays.asList(words).reversed();
    return String.join(" ", reversedWords);
  }
}
