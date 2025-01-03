package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class MinRemoveToMakeValid {
  public String minRemoveToMakeValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    StringBuilder sb = new StringBuilder();
    for (char c : s.toCharArray()) {
      if (Character.isLetter(c)) {
        sb.append(c);
      } else if (c == '(') {
        stack.push(c);
        sb.append(c);
      } else if (c == ')' && !stack.isEmpty()) {
        stack.pop();
        sb.append(c);
      }
    }

    stack.clear();
    s = sb.reverse().toString();
    sb = new StringBuilder();
    for (char c : s.toCharArray()) {
      if (Character.isLetter(c)) {
        sb.append(c);
      } else if (c == ')') {
        stack.push(c);
        sb.append(c);
      } else if (c == '(' && !stack.isEmpty()) {
        stack.pop();
        sb.append(c);
      }
    }

    return sb.reverse().toString();
  }
}
