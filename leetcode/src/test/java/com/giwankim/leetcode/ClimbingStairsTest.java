package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClimbingStairsTest {
  ClimbingStairs sut = new ClimbingStairs();

  // Step 1: smallest valid input — single stair, the only base case at the lower bound (n = 1).
  @Test
  void n1ReturnsOne() {
    assertThat(sut.climbStairs(1)).isEqualTo(1);
  }

  // Step 2: LeetCode Example 1 — {1+1, 2}.
  @Test
  void n2ReturnsTwo() {
    assertThat(sut.climbStairs(2)).isEqualTo(2);
  }

  // Step 3: LeetCode Example 2 — {1+1+1, 1+2, 2+1}.
  @Test
  void n3ReturnsThree() {
    assertThat(sut.climbStairs(3)).isEqualTo(3);
  }

  // Step 4: first input where f(n) = f(n-1) + f(n-2) becomes load-bearing — proves it's not just n.
  @Test
  void n4ReturnsFive() {
    assertThat(sut.climbStairs(4)).isEqualTo(5);
  }

  // Step 5: continues the Fibonacci progression — guards an off-by-one in the recurrence.
  @Test
  void n5ReturnsEight() {
    assertThat(sut.climbStairs(5)).isEqualTo(8);
  }

  // Step 6: mid-range value (Fib(11) = 89) — distinguishes a correct DP from accidentally-correct
  // small cases.
  @Test
  void n10ReturnsEightyNine() {
    assertThat(sut.climbStairs(10)).isEqualTo(89);
  }

  // Step 7: upper bound from constraint 1 <= n <= 45 — Fib(46) = 1,836,311,903 fits in int but only
  // barely;
  // a naive O(2^n) recursion would also time out here.
  @Test
  void n45ReturnsFibonacciFortySix() {
    assertThat(sut.climbStairs(45)).isEqualTo(1_836_311_903);
  }
}
