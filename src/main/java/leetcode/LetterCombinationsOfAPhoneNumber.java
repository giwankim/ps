package leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LetterCombinationsOfAPhoneNumber {
  private static final Map<Character, List<Character>> map =
      Map.ofEntries(
          Map.entry('2', List.of('a', 'b', 'c')),
          Map.entry('3', List.of('d', 'e', 'f')),
          Map.entry('4', List.of('g', 'h', 'i')),
          Map.entry('5', List.of('j', 'k', 'l')),
          Map.entry('6', List.of('m', 'n', 'o')),
          Map.entry('7', List.of('p', 'q', 'r', 's')),
          Map.entry('8', List.of('t', 'u', 'v')),
          Map.entry('9', List.of('w', 'x', 'y', 'z')));

  public List<String> letterCombinations(String digits) {
    if (digits.isEmpty()) {
      return Collections.emptyList();
    }
    List<String> result = new ArrayList<>();
    letterCombinations(0, digits, new StringBuilder(), result);
    return result;
  }

  private void letterCombinations(int i, String digits, StringBuilder sb, List<String> result) {
    if (i == digits.length()) {
      result.add(sb.toString());
      return;
    }
    for (char c : map.get(digits.charAt(i))) {
      sb.append(c);
      letterCombinations(i + 1, digits, sb, result);
      sb.deleteCharAt(i);
    }
  }
}
