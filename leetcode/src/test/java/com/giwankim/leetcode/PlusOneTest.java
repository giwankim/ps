package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class PlusOneTest {
  PlusOne sut = new PlusOne();

  // Step 1: zero is the only valid input with leading digit 0, and increments to one.
  @Test
  void zeroBecomesOne() {
    assertThat(sut.plusOne(new int[] {0})).containsExactly(1);
  }

  // Step 2: single digit below nine increments in place without changing length.
  @Test
  void singleDigitBelowNineIncrementsWithoutCarry() {
    assertThat(sut.plusOne(new int[] {8})).containsExactly(9);
  }

  // Step 3: LeetCode Example 1 — multi-digit number without carry.
  @Test
  void leetCodeExampleOneIncrementsLastDigit() {
    assertThat(sut.plusOne(new int[] {1, 2, 3})).containsExactly(1, 2, 4);
  }

  // Step 4: LeetCode Example 2 — preserves all leading digits when the last digit is not nine.
  @Test
  void leetCodeExampleTwoIncrementsLastDigitOfLongerNumber() {
    assertThat(sut.plusOne(new int[] {4, 3, 2, 1})).containsExactly(4, 3, 2, 2);
  }

  // Step 5: LeetCode Example 3 — single nine expands to two digits.
  @Test
  void leetCodeExampleThreeSingleNineExpandsToTen() {
    assertThat(sut.plusOne(new int[] {9})).containsExactly(1, 0);
  }

  // Step 6: one trailing nine carries into the previous digit and resets the tail to zero.
  @Test
  void oneTrailingNineCarriesToPreviousDigit() {
    assertThat(sut.plusOne(new int[] {1, 2, 9})).containsExactly(1, 3, 0);
  }

  // Step 7: consecutive trailing nines cascade the carry left until a non-nine digit absorbs it.
  @Test
  void multipleTrailingNinesCascadeCarryToNearestNonNine() {
    assertThat(sut.plusOne(new int[] {1, 9, 9})).containsExactly(2, 0, 0);
  }

  // Step 8: carry stops before the most significant digit when an interior digit can absorb it.
  @Test
  void carryStopsAtInteriorNonNineWithoutAddingLeadingDigit() {
    assertThat(sut.plusOne(new int[] {9, 8, 9, 9})).containsExactly(9, 9, 0, 0);
  }

  // Step 9: all digits are nine, so the result grows by one and has no leading zero.
  @Test
  void allNinesExpandLengthAndResetEveryOriginalDigit() {
    assertThat(sut.plusOne(new int[] {9, 9, 9})).containsExactly(1, 0, 0, 0);
  }

  // Step 10: maximum constrained length confirms arbitrary-precision behavior without numeric overflow.
  @Test
  void maxLengthAllNinesExpandsToOneFollowedByZeros() {
    int[] digits = new int[100];
    Arrays.fill(digits, 9);
    int[] expected = new int[101];
    expected[0] = 1;

    assertThat(sut.plusOne(digits)).containsExactly(expected);
  }
}
