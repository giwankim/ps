package leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class MinRemoveToMakeValidParentheses {
  public String minRemoveToMakeValid(String s) {
    StringBuilder sb = new StringBuilder(s);
    Deque<Integer> stack = new ArrayDeque<>();

    for (int i = 0; i < s.length(); i++) {
      char c = sb.charAt(i);
      if (c == '(') {
        stack.push(i);
      } else if (c == ')') {
        if (!stack.isEmpty()) {
          stack.pop();
        } else {
          sb.setCharAt(i, '*');
        }
      }
    }

    while (!stack.isEmpty()) {
      sb.setCharAt(stack.pop(), '*');
    }

    return sb.toString().replace("*", "");
  }
}
