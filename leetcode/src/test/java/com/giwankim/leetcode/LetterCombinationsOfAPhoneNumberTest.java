package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class LetterCombinationsOfAPhoneNumberTest {
  LetterCombinationsOfAPhoneNumber sut = new LetterCombinationsOfAPhoneNumber();

  // Step 1: single 3-letter digit — forces the digit→letters map to be wired up.
  // This is also the minimum-length case per the LeetCode constraint digits.length >= 1.
  @Test
  void singleDigitTwoReturnsThreeLetters() {
    assertThat(sut.letterCombinations("2")).containsExactlyInAnyOrder("a", "b", "c");
  }

  // Step 2: single 4-letter digit — forces handling of the 4-letter groups (pqrs).
  @Test
  void singleDigitSevenReturnsFourLetters() {
    assertThat(sut.letterCombinations("7")).containsExactlyInAnyOrder("p", "q", "r", "s");
  }

  // Step 3: last digit in the map (wxyz) — guards against off-by-one at the tail.
  @Test
  void singleDigitNineReturnsFourLetters() {
    assertThat(sut.letterCombinations("9")).containsExactlyInAnyOrder("w", "x", "y", "z");
  }

  // Step 4: two 3-letter digits — introduces the cartesian product across positions
  // (the first case that genuinely needs recursion/backtracking). 3 × 3 = 9 combos.
  @Test
  void twoDigitsProduceCartesianProduct() {
    assertThat(sut.letterCombinations("23"))
        .containsExactlyInAnyOrder("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf");
  }

  // Step 5: mixed 3-letter and 4-letter digits — 3 × 4 = 12 combos across groups
  // of different widths.
  @Test
  void twoDigitsMixThreeAndFourLetterGroups() {
    assertThat(sut.letterCombinations("27"))
        .containsExactlyInAnyOrder(
            "ap", "aq", "ar", "as", "bp", "bq", "br", "bs", "cp", "cq", "cr", "cs");
  }

  // Step 6: both digits are 4-letter groups — 4 × 4 = 16 combos; catches
  // off-by-one errors in the inner iteration.
  @Test
  void twoFourLetterDigitsProduceSixteenCombos() {
    assertThat(sut.letterCombinations("79"))
        .containsExactlyInAnyOrder(
            "pw", "px", "py", "pz", "qw", "qx", "qy", "qz", "rw", "rx", "ry", "rz", "sw", "sx",
            "sy", "sz");
  }

  // Step 7: repeated digit — backtracking must not leak state between sibling
  // branches (classic StringBuilder-reset bug). 3 × 3 = 9 combos of the same group.
  @Test
  void repeatedDigitProducesAllOrderedPairs() {
    assertThat(sut.letterCombinations("22"))
        .containsExactlyInAnyOrder("aa", "ab", "ac", "ba", "bb", "bc", "ca", "cb", "cc");
  }

  // Step 8: three digits — recursion depth ≥ 3; the accumulator must grow and
  // shrink correctly across nested frames. 3 × 3 × 3 = 27 combos.
  @Test
  void threeDigitsProduceTwentySevenCombos() {
    assertThat(sut.letterCombinations("234"))
        .containsExactlyInAnyOrder(
            "adg", "adh", "adi", "aeg", "aeh", "aei", "afg", "afh", "afi", "bdg", "bdh", "bdi",
            "beg", "beh", "bei", "bfg", "bfh", "bfi", "cdg", "cdh", "cdi", "ceg", "ceh", "cei",
            "cfg", "cfh", "cfi");
  }

  // Step 9: maximum length per LeetCode constraint (digits.length <= 4).
  // Asserted structurally: exact size, a few spot-checked members at the extremes
  // and interior, and no duplicates. 3 × 3 × 3 × 3 = 81 combos.
  @Test
  void fourDigitsReachMaxLengthConstraint() {
    List<String> result = sut.letterCombinations("2345");
    assertThat(result).hasSize(81).contains("adgj", "cfil", "bfhk", "ceil").doesNotHaveDuplicates();
  }
}
