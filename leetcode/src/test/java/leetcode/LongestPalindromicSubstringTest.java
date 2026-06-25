package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LongestPalindromicSubstringTest {
  LongestPalindromicSubstring sut = new LongestPalindromicSubstring();

  // Step 1: smallest valid input under 1 <= s.length — a lone character is trivially a
  // palindrome and must be returned as-is. Guards against an implementation that only ever
  // grows a palindrome from a multi-character center and forgets the length-1 base case,
  // returning "" for a single char.
  @Test
  void singleCharacterIsItsOwnPalindrome() {
    assertThat(sut.longestPalindrome("a")).isEqualTo("a");
  }

  // Step 2: the minimal non-trivial palindrome — two equal characters form an even-length
  // palindrome that is also the whole string. First case where the answer is longer than
  // one character; catches an algorithm that never considers even-length (between-char)
  // centers and so can only ever return a single character here.
  @Test
  void twoIdenticalCharactersFormEvenPalindrome() {
    assertThat(sut.longestPalindrome("bb")).isEqualTo("bb");
  }

  // Step 3: no palindrome longer than one character exists, so any single character is a
  // correct answer. Both "a" and "b" are valid, so we cannot assert a specific string —
  // we assert the defining invariants (length 1, a palindrome, a substring of the input).
  // Pins down that the result is never the empty string and never a non-palindromic pair.
  @Test
  void noPalindromeLongerThanOneReturnsSingleCharacter() {
    assertValidLongestPalindrome("ab", 1);
  }

  // Step 4: palindrome equality is case-sensitive — 'a' and 'A' are different characters,
  // so "aA" is NOT a palindrome and the longest is length 1. An implementation that lower-
  // cases or case-folds before comparing would wrongly return "aA"; this case is the only
  // one that distinguishes a case-sensitive comparison from a case-insensitive one, and the
  // answer is ambiguous (either character is valid) so we again assert the invariants.
  @Test
  void caseSensitivePalindromeRejectsMixedCase() {
    assertValidLongestPalindrome("aA", 1);
  }

  // Step 5: LeetCode Example 2 — "cbbd" -> "bb". The longest palindrome sits in the
  // interior, is shorter than the whole string, and has an even center. Catches an
  // implementation that returns the whole input, or that anchors only at index 0.
  @Test
  void leetCodeExample2() {
    assertThat(sut.longestPalindrome("cbbd")).isEqualTo("bb");
  }

  // Step 6: LeetCode Example 1 — "babad" admits two valid answers, "bab" and "aba", both
  // length 3. Because the problem explicitly permits either, we assert length plus the
  // palindrome/substring invariants instead of a specific string, so the test does not
  // over-constrain a correct implementation that happens to return the other valid answer.
  @Test
  void leetCodeExample1() {
    assertValidLongestPalindrome("babad", 3);
  }

  // Step 7: the whole string is an odd-length palindrome — "racecar". Exercises an odd
  // center (a single pivot character) expanding symmetrically to both ends. An off-by-one
  // in the odd-expansion bounds would drop the first or last character and return length 5.
  @Test
  void wholeStringIsOddLengthPalindrome() {
    assertThat(sut.longestPalindrome("racecar")).isEqualTo("racecar");
  }

  // Step 8: the whole string is an even-length palindrome — "abba". Complements Step 7 by
  // exercising an even center (the gap between the two 'b's). Together they pin both center
  // parities; an implementation handling only one parity fails exactly one of these two.
  @Test
  void wholeStringIsEvenLengthPalindrome() {
    assertThat(sut.longestPalindrome("abba")).isEqualTo("abba");
  }

  // Step 9: the answer "aa" sits at the very start of "aabcd". Catches an expand-around-
  // center loop that starts its center index at 1 (skipping index 0) and so never discovers
  // a palindrome anchored at the first character.
  @Test
  void palindromeAtTheStartIsFound() {
    assertThat(sut.longestPalindrome("aabcd")).isEqualTo("aa");
  }

  // Step 10: the answer "aa" sits at the very end of "dcbaa". Mirror of Step 9 — catches a
  // loop whose upper bound stops one center too early and misses the final even center, or
  // that drops the trailing character.
  @Test
  void palindromeAtTheEndIsFound() {
    assertThat(sut.longestPalindrome("dcbaa")).isEqualTo("aa");
  }

  // Step 11: a short palindrome ("xx") appears before a longer one ("abccba"). The result
  // must be the longer, later palindrome. Catches an implementation that returns the first
  // palindrome it finds rather than tracking the maximum across the whole scan.
  @Test
  void tracksLongerPalindromeAppearingLater() {
    assertThat(sut.longestPalindrome("xxabccba")).isEqualTo("abccba");
  }

  // Step 12: the mirror of Step 11 — the longer palindrome ("abccba") appears first, a
  // shorter one ("xx") later. The result must stay the earlier, longer palindrome. Catches
  // an implementation that overwrites its best answer with any later palindrome (e.g. using
  // ">=" where it means ">") and so ends up returning the shorter trailing match.
  @Test
  void keepsLongerPalindromeAppearingEarlier() {
    assertThat(sut.longestPalindrome("abccbaxx")).isEqualTo("abccba");
  }

  // Step 13: every character is identical, so expansions from adjacent centers overlap and
  // the whole string is the answer. Stresses that overlapping/maximal expansions are bounded
  // correctly and that the global maximum is the entire input, not some interior fragment.
  @Test
  void allIdenticalCharactersReturnWholeString() {
    assertThat(sut.longestPalindrome("aaaa")).isEqualTo("aaaa");
  }

  // Step 14: the constraint allows digits as well as letters ("s consist of only digits and
  // English letters"). "12321" is an odd-length numeric palindrome; confirms the comparison
  // is a plain character match that works for digits, not a letters-only assumption.
  @Test
  void digitsAreValidPalindromeCharacters() {
    assertThat(sut.longestPalindrome("12321")).isEqualTo("12321");
  }

  // Step 15: upper constraint bound — s.length may be 1000. A string of 1000 identical
  // characters has itself as the longest palindrome, so the result must be returned in full.
  // Confirms the O(n^2) scan handles the maximum input width without truncating the answer.
  @Test
  void handlesMaximumLengthInput() {
    String input = "a".repeat(1000);
    assertThat(sut.longestPalindrome(input)).isEqualTo(input);
  }

  // For inputs that admit more than one valid answer (ties in length), assert the defining
  // invariants of a correct result rather than a specific string: it must have the known
  // maximal length, be a substring of the input, and read the same forwards and backwards.
  private void assertValidLongestPalindrome(String s, int expectedLength) {
    String result = sut.longestPalindrome(s);
    assertThat(result).hasSize(expectedLength);
    assertThat(s).contains(result);
    assertThat(result).isEqualTo(new StringBuilder(result).reverse().toString());
  }
}
