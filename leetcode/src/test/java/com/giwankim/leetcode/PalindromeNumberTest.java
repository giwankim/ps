package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PalindromeNumberTest {
  PalindromeNumber sut = new PalindromeNumber();

  // Step 1: zero is the smallest non-negative input and reads the same backward.
  @Test
  void zeroIsPalindrome() {
    assertThat(sut.isPalindrome(0)).isTrue();
  }

  // Step 2: every positive single-digit number is a palindrome.
  @Test
  void positiveSingleDigitIsPalindrome() {
    assertThat(sut.isPalindrome(7)).isTrue();
  }

  // Step 3: LeetCode Example 1 — odd number of digits with matching outer digits.
  @Test
  void leetCodeExample1() {
    assertThat(sut.isPalindrome(121)).isTrue();
  }

  // Step 4: LeetCode Example 2 — the minus sign means a negative number is never a palindrome.
  @Test
  void leetCodeExample2() {
    assertThat(sut.isPalindrome(-121)).isFalse();
  }

  // Step 5: LeetCode Example 3 — reversing 10 would require a leading zero.
  @Test
  void leetCodeExample3() {
    assertThat(sut.isPalindrome(10)).isFalse();
  }

  // Step 6: another negative shape that would be palindromic without the sign.
  @Test
  void negativeNumberWithMirroredDigitsIsNotPalindrome() {
    assertThat(sut.isPalindrome(-101)).isFalse();
  }

  // Step 7: two matching digits form the smallest multi-digit palindrome.
  @Test
  void twoDigitPalindrome() {
    assertThat(sut.isPalindrome(11)).isTrue();
  }

  // Step 8: two different digits catch implementations that only check length or positivity.
  @Test
  void twoDigitNonPalindrome() {
    assertThat(sut.isPalindrome(12)).isFalse();
  }

  // Step 9: even number of digits with all mirrored pairs matching.
  @Test
  void evenLengthPalindrome() {
    assertThat(sut.isPalindrome(1221)).isTrue();
  }

  // Step 10: odd number of digits where the center digit is ignored by the mirror comparison.
  @Test
  void oddLengthPalindrome() {
    assertThat(sut.isPalindrome(12321)).isTrue();
  }

  // Step 11: same first and last digit is not enough; inner digits must also mirror.
  @Test
  void matchingOuterDigitsButDifferentInnerDigitsIsNotPalindrome() {
    assertThat(sut.isPalindrome(1231)).isFalse();
  }

  // Step 12: trailing zero prevents any positive multi-digit number from being a palindrome.
  @Test
  void positiveNumberEndingInZeroIsNotPalindrome() {
    assertThat(sut.isPalindrome(1000)).isFalse();
  }

  // Step 13: zeros inside the number are valid when they mirror exactly.
  @Test
  void interiorZerosCanMirror() {
    assertThat(sut.isPalindrome(1001)).isTrue();
  }

  // Step 14: largest 32-bit signed value from the constraint is not a palindrome.
  @Test
  void integerMaxValueIsNotPalindrome() {
    assertThat(sut.isPalindrome(Integer.MAX_VALUE)).isFalse();
  }

  // Step 15: smallest 32-bit signed value is negative, so it is not a palindrome.
  @Test
  void integerMinValueIsNotPalindrome() {
    assertThat(sut.isPalindrome(Integer.MIN_VALUE)).isFalse();
  }

  // Step 16: the largest palindrome that fits in a signed 32-bit int — the next palindrome up
  // would exceed Integer.MAX_VALUE. Pins correct handling at the top of the value range, and
  // would catch overflow in any future int-reversal-based implementation.
  @Test
  void largestPalindromeWithinIntRange() {
    assertThat(sut.isPalindrome(2_147_447_412)).isTrue();
  }

  // Step 17: a negative single digit is still negative — the sign check must reject it even
  // though its absolute value reads the same backward. Pins that the x < 0 guard fires before
  // any digit or length logic.
  @Test
  void negativeSingleDigitIsNotPalindrome() {
    assertThat(sut.isPalindrome(-7)).isFalse();
  }
}
