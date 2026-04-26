package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GenerateParenthesesTest {
  GenerateParentheses sut = new GenerateParentheses();

  // Step 1: smallest valid input — single pair (LeetCode Example 2)
  @Test
  void n1ReturnsSingleWellFormedPair() {
    assertThat(sut.generateParenthesis(1)).containsExactlyInAnyOrder("()");
  }

  // Step 2: two pairs — nested "(())" vs. sequential "()()"
  @Test
  void n2ReturnsNestedAndSequentialArrangements() {
    assertThat(sut.generateParenthesis(2)).containsExactlyInAnyOrder("(())", "()()");
  }

  // Step 3: three pairs — five arrangements (LeetCode Example 1)
  @Test
  void n3ReturnsFiveArrangements() {
    assertThat(sut.generateParenthesis(3))
        .containsExactlyInAnyOrder("((()))", "(()())", "(())()", "()(())", "()()()");
  }

  // Step 4: four pairs — all 14 arrangements (fourth Catalan number)
  @Test
  void n4ReturnsAllFourteenArrangements() {
    assertThat(sut.generateParenthesis(4))
        .containsExactlyInAnyOrder(
            "(((())))",
            "((()()))",
            "((())())",
            "((()))()",
            "(()(()))",
            "(()()())",
            "(()())()",
            "(())(())",
            "(())()()",
            "()((()))",
            "()(()())",
            "()(())()",
            "()()(())",
            "()()()()");
  }

  // Step 5: five pairs — 42 arrangements (C_5)
  @Test
  void n5ReturnsFortyTwoArrangements() {
    assertThat(sut.generateParenthesis(5)).hasSize(42);
  }

  // Step 6: six pairs — 132 arrangements (C_6)
  @Test
  void n6ReturnsOneHundredThirtyTwoArrangements() {
    assertThat(sut.generateParenthesis(6)).hasSize(132);
  }

  // Step 7: seven pairs — 429 arrangements (C_7)
  @Test
  void n7ReturnsFourHundredTwentyNineArrangements() {
    assertThat(sut.generateParenthesis(7)).hasSize(429);
  }

  // Step 8: eight pairs — 1430 arrangements (C_8, upper-bound constraint)
  @Test
  void n8ReturnsOneThousandFourHundredThirtyArrangements() {
    assertThat(sut.generateParenthesis(8)).hasSize(1430);
  }

  // Step 9: length property — every string has length 2n
  @Test
  void everyResultHasLengthTwoNForN5() {
    assertThat(sut.generateParenthesis(5)).allSatisfy(s -> assertThat(s).hasSize(10));
  }

  // Step 10: every string contains exactly n '(' and n ')'
  @Test
  void everyResultHasEqualOpenAndCloseCountsForN5() {
    assertThat(sut.generateParenthesis(5)).allSatisfy(s -> {
      assertThat(s.chars().filter(c -> c == '(').count()).isEqualTo(5L);
      assertThat(s.chars().filter(c -> c == ')').count()).isEqualTo(5L);
    });
  }

  // Step 11: prefix-balance validity — never more ')' than '(' in any prefix, balanced at end
  @Test
  void everyResultIsWellFormedByPrefixBalanceForN5() {
    assertThat(sut.generateParenthesis(5)).allSatisfy(s -> {
      int balance = 0;
      for (char c : s.toCharArray()) {
        balance += (c == '(') ? 1 : -1;
        assertThat(balance).isGreaterThanOrEqualTo(0);
      }
      assertThat(balance).isZero();
    });
  }

  // Step 12: no duplicate strings in the result
  @Test
  void allResultsAreDistinctForN5() {
    assertThat(sut.generateParenthesis(5)).doesNotHaveDuplicates();
  }

  // Step 13: implementation-locking regression guard — DFS left-first traversal order
  @Test
  void n3ProducesResultsInDfsLeftFirstOrder() {
    assertThat(sut.generateParenthesis(3))
        .containsExactly("((()))", "(()())", "(())()", "()(())", "()()()");
  }
}
