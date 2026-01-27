package com.giwankim.grind75;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

public class EvaluateReversePolishNotation {
  private static final Set<String> OPERATORS = Set.of("+", "-", "*", "/");

  public int evalRPN(String[] tokens) {
    Deque<Integer> operands = new ArrayDeque<>();

    for (String token : tokens) {
      if (OPERATORS.contains(token)) {
        int b = operands.pop();
        int a = operands.pop();
        int result =
            switch (token) {
              case "+" -> a + b;
              case "-" -> a - b;
              case "*" -> a * b;
              case "/" -> a / b;
              default -> throw new IllegalArgumentException("Unknown operator: " + token);
            };
        operands.push(result);
      } else {
        operands.push(Integer.parseInt(token));
      }
    }

    return operands.pop();
  }
}
