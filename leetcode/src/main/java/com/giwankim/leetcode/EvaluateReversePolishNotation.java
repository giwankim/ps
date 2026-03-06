package com.giwankim.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.function.IntBinaryOperator;

public class EvaluateReversePolishNotation {
  private static final Map<String, IntBinaryOperator> OPERATORS =
      Map.of(
          "+", (a, b) -> a + b,
          "-", (a, b) -> a - b,
          "*", (a, b) -> a * b,
          "/", (a, b) -> a / b);

  public int evalRPN(String[] tokens) {
    // Time complexity: O(n), Space complexity: O(n)
    Deque<Integer> operands = new ArrayDeque<>();
    for (String token : tokens) {
      IntBinaryOperator op = OPERATORS.get(token);
      if (op != null) {
        int b = operands.pop();
        int a = operands.pop();
        operands.push(op.applyAsInt(a, b));
      } else {
        operands.push(Integer.parseInt(token));
      }
    }
    return operands.pop();
  }
}
