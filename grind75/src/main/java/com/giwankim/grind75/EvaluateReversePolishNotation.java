package com.giwankim.grind75;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.function.IntBinaryOperator;

public class EvaluateReversePolishNotation {

  private static final Map<String, IntBinaryOperator> OPERATIONS =
      Map.of(
          "+", Integer::sum,
          "-", (a, b) -> a - b,
          "*", (a, b) -> a * b,
          "/", (a, b) -> a / b);

  public int evalRPN(String[] tokens) {
    Deque<Integer> operands = new ArrayDeque<>();

    for (String token : tokens) {
      IntBinaryOperator op = OPERATIONS.get(token);
      if (op == null) {
        operands.push(Integer.parseInt(token));
      } else {
        int b = operands.pop();
        int a = operands.pop();
        operands.push(op.applyAsInt(a, b));
      }
    }

    return operands.pop();
  }
}
