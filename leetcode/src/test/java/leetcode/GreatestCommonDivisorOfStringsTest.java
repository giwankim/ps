package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GreatestCommonDivisorOfStringsTest {
  GreatestCommonDivisorOfStrings sut = new GreatestCommonDivisorOfStrings();

  // Step 1: smallest valid input — one character each (1 <= str1.length, str2.length).
  // A string always divides itself once, so the answer is the string itself
  @Test
  void identicalSingleCharactersDivideEachOther() {
    assertThat(sut.gcdOfStrings("A", "A")).isEqualTo("A");
  }

  // Step 2: the only other single character case — no common divisor exists, and the
  // contract says to return the empty string rather than null
  @Test
  void differentSingleCharactersHaveNoCommonDivisor() {
    assertThat(sut.gcdOfStrings("A", "B")).isEmpty();
  }

  // Step 3: equal strings of any length — one repetition of the whole string divides both
  @Test
  void identicalStringsAreTheirOwnDivisor() {
    assertThat(sut.gcdOfStrings("ABC", "ABC")).isEqualTo("ABC");
  }

  // Step 4: LeetCode Example 1 — str2 tiles str1 exactly, so str2 itself is the answer
  @Test
  void shorterStringThatTilesTheLongerOneIsTheAnswer() {
    assertThat(sut.gcdOfStrings("ABCABC", "ABC")).isEqualTo("ABC");
  }

  // Step 5: the mirror of step 4 — the operation is symmetric, so swapping the arguments
  // must not change the result
  @Test
  void argumentOrderDoesNotChangeTheResult() {
    assertThat(sut.gcdOfStrings("ABC", "ABCABC")).isEqualTo("ABC");
  }

  // Step 6: LeetCode Example 2 — neither string tiles the other (4 does not divide 6), so
  // the answer is strictly shorter than both: gcd(6, 4) = 2 characters
  @Test
  void divisorIsShorterThanBothWhenNeitherStringTilesTheOther() {
    assertThat(sut.gcdOfStrings("ABABAB", "ABAB")).isEqualTo("AB");
  }

  // Step 7: LeetCode Example 3 — equal lengths but no shared prefix at all
  @Test
  void equalLengthStringsWithNoSharedContentReturnEmpty() {
    assertThat(sut.gcdOfStrings("LEET", "CODE")).isEmpty();
  }

  // Step 8: LeetCode Example 4 — 3 divides 6 and "AAA" is a prefix of "AAAAAB", yet "AAA"
  // repeated twice is "AAAAAA", not "AAAAAB". A length-only check is not enough
  @Test
  void matchingLengthsAndPrefixStillRequireTheRepetitionToHold() {
    assertThat(sut.gcdOfStrings("AAAAAB", "AAA")).isEmpty();
  }

  // Step 9: coprime lengths (3 and 2) squeeze the candidate down to a single character,
  // which here does divide both
  @Test
  void coprimeLengthsCollapseToASingleCharacter() {
    assertThat(sut.gcdOfStrings("AAA", "AA")).isEqualTo("A");
  }

  // Step 10: coprime lengths again (4 and 3), but the one character candidate "A" fails
  // against "ABAB", so nothing divides both
  @Test
  void coprimeLengthsWithMixedContentReturnEmpty() {
    assertThat(sut.gcdOfStrings("ABAB", "ABA")).isEmpty();
  }

  // Step 11: the longest common prefix is "AAAA", but 4 does not divide 6 — the answer is
  // the gcd(6, 4) = 2 prefix instead, so greedily taking the common prefix is wrong
  @Test
  void answerIsShorterThanTheLongestCommonPrefix() {
    assertThat(sut.gcdOfStrings("AAAAAA", "AAAA")).isEqualTo("AA");
  }

  // Step 12: the repeating unit is multi character rather than a single letter:
  // gcd(9, 6) = 3 picks out "ABC"
  @Test
  void multiCharacterUnitIsRecovered() {
    assertThat(sut.gcdOfStrings("ABCABCABC", "ABCABC")).isEqualTo("ABC");
  }

  // Step 13: rotations of each other share every letter and every length divisor, yet no
  // common divisor exists because any candidate must be a prefix of both
  @Test
  void rotationsShareNoDivisor() {
    assertThat(sut.gcdOfStrings("ABAB", "BABA")).isEmpty();
  }

  // Step 14: a one character overlap that must not be mistaken for a divisor — "AB" is a
  // prefix of both, but 2 divides neither 5 nor 3
  @Test
  void sharedPrefixWithIndivisibleLengthsReturnsEmpty() {
    assertThat(sut.gcdOfStrings("ABABA", "ABA")).isEmpty();
  }

  // Step 15: upper constraint bound (1000 characters) with a proper common divisor —
  // gcd(1000, 600) = 200, so the answer is "AB" repeated 100 times
  @Test
  void maximumLengthStringsYieldTheGcdLengthPrefix() {
    String str1 = "AB".repeat(500);
    String str2 = "AB".repeat(300);
    assertThat(sut.gcdOfStrings(str1, str2)).isEqualTo("AB".repeat(100)).hasSize(200);
  }

  // Step 16: worst case for a prefix scan — 1000 and 999 are coprime, so only a single
  // character can survive
  @Test
  void maximumCoprimeLengthsCollapseToASingleCharacter() {
    assertThat(sut.gcdOfStrings("A".repeat(1000), "A".repeat(999))).isEqualTo("A");
  }

  // Step 17: maximum lengths where a single trailing character breaks every candidate,
  // so the answer stays empty even though the strings agree on 999 of 1000 positions
  @Test
  void maximumLengthStringsDifferingInOneCharacterReturnEmpty() {
    assertThat(sut.gcdOfStrings("A".repeat(999) + "B", "A".repeat(1000))).isEmpty();
  }
}
