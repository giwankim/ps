package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class SmallestSubsequenceOfDistinctCharacters {
  /**
   * @implNote Time {@code O(n)} since each character is pushed and popped at most once, auxiliary
   *     space {@code O(1)} because the stack and lookup tables are bounded by the 26-letter
   *     alphabet, where {@code n = s.length()}.
   */
  public String smallestSubsequence(String s) {
    int n = s.length();
    int[] last = new int[26];
    for (int i = 0; i < n; i++) {
      last[s.charAt(i) - 'a'] = i;
    }

    Deque<Character> stack = new ArrayDeque<>();
    boolean[] inStack = new boolean[26];
    for (int i = 0; i < n; i++) {
      char c = s.charAt(i);
      if (inStack[c - 'a']) {
        continue;
      }
      while (!stack.isEmpty() && stack.peek() > c && last[stack.peek() - 'a'] > i) {
        inStack[stack.pop() - 'a'] = false;
      }
      stack.push(c);
      inStack[c - 'a'] = true;
    }

    StringBuilder result = new StringBuilder();
    for (char c : stack) {
      result.append(c);
    }
    return result.reverse().toString();
  }
}
