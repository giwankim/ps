package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LetterCombinationsOfAPhoneNumber {
  private static final Map<Character, List<Character>> digitToLetters =
      Map.of(
          '2', List.of('a', 'b', 'c'),
          '3', List.of('d', 'e', 'f'),
          '4', List.of('g', 'h', 'i'),
          '5', List.of('j', 'k', 'l'),
          '6', List.of('m', 'n', 'o'),
          '7', List.of('p', 'q', 'r', 's'),
          '8', List.of('t', 'u', 'v'),
          '9', List.of('w', 'x', 'y', 'z'));

  /**
   * @implNote Time {@code O(4^n * n)}, auxiliary space {@code O(n)}, where {@code n =
   *     digits.length()}.
   */
  public List<String> letterCombinations(String digits) {
    List<String> result = new ArrayList<>();
    letterCombinations(digits, 0, new StringBuilder(), result);
    return result;
  }

  private void letterCombinations(
      String digits, int index, StringBuilder current, List<String> result) {
    if (index == digits.length()) {
      result.add(current.toString());
      return;
    }
    for (char letter : digitToLetters.get(digits.charAt(index))) {
      current.append(letter);
      letterCombinations(digits, index + 1, current, result);
      current.deleteCharAt(current.length() - 1);
    }
  }
}
