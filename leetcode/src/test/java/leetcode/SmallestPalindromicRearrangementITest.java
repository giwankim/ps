package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SmallestPalindromicRearrangementITest {
  SmallestPalindromicRearrangementI sut = new SmallestPalindromicRearrangementI();

  // Step 1: smallest valid input (1 <= s.length) — one character is already the answer
  // (LeetCode Example 1)
  @Test
  void singleCharacterIsAlreadySmallest() {
    assertThat(sut.smallestPalindrome("z")).isEqualTo("z");
  }

  // Step 2: an even palindrome of one repeated letter has only one arrangement
  @Test
  void identicalPairIsUnchanged() {
    assertThat(sut.smallestPalindrome("aa")).isEqualTo("aa");
  }

  // Step 3: an odd count leaves one copy stranded in the center after the halves are formed
  @Test
  void oddCountOfOneLetterKeepsOneInTheMiddle() {
    assertThat(sut.smallestPalindrome("bbb")).isEqualTo("bbb");
  }

  // Step 4: an input already in canonical form must come back untouched — the transformation
  // is idempotent
  @Test
  void alreadySmallestPalindromeIsUnchanged() {
    assertThat(sut.smallestPalindrome("abba")).isEqualTo("abba");
  }

  // Step 5: the first real rearrangement — the leading half sorts ascending and the trailing
  // half mirrors it
  @Test
  void mirroredHalvesAreSortedAscending() {
    assertThat(sut.smallestPalindrome("baab")).isEqualTo("abba");
  }

  // Step 6: parity outranks order — 'a' has the odd count, so it is pinned to the center even
  // though it is the smallest letter and would sort first
  @Test
  void oddCountLetterStaysInTheMiddleEvenWhenSmallest() {
    assertThat(sut.smallestPalindrome("cbabc")).isEqualTo("bcacb");
  }

  // Step 7: the same rule with the largest letter — 'z' is stuck in the center while a/b sort
  // around it
  @Test
  void oddCountLetterStaysInTheMiddleEvenWhenLargest() {
    assertThat(sut.smallestPalindrome("bazab")).isEqualTo("abzba");
  }

  // Step 8: the floor-division subtlety — 'a' appears 3 times, so one copy goes to each half
  // (3 / 2 = 1) and only the leftover sits in the middle
  @Test
  void oddCountLetterAlsoContributesToTheHalves() {
    assertThat(sut.smallestPalindrome("baaab")).isEqualTo("ababa");
  }

  // Step 9: LeetCode Example 2 — a:2 b:3 gives half "ab", middle 'b', mirror "ba"
  @Test
  void leetCodeExample2() {
    assertThat(sut.smallestPalindrome("babab")).isEqualTo("abbba");
  }

  // Step 10: LeetCode Example 3 — three even counts give half "acd" and no middle character
  @Test
  void leetCodeExample3() {
    assertThat(sut.smallestPalindrome("daccad")).isEqualTo("acddca");
  }

  // Step 11: a letter contributes count / 2 copies to each half, not merely one — a:4 puts
  // "aa" in the leading half
  @Test
  void repeatedLetterContributesHalfItsCountToEachSide() {
    assertThat(sut.smallestPalindrome("abaaba")).isEqualTo("aabbaa");
  }

  // Step 12: multi-copy halves combined with a forced middle — a:2 b:4 c:1 gives half "abb",
  // middle 'c', mirror "bba"
  @Test
  void multipleRepeatsAroundAMiddleCharacter() {
    assertThat(sut.smallestPalindrome("babcbab")).isEqualTo("abbcbba");
  }

  // Step 13: all 26 lowercase letters — the counting sweep must run 'a' through 'z' in order
  @Test
  void everyLetterOfTheAlphabetSortsAscending() {
    String s = "zyxwvutsrqponmlkjihgfedcba" + "abcdefghijklmnopqrstuvwxyz";
    String expected = "abcdefghijklmnopqrstuvwxyz" + "zyxwvutsrqponmlkjihgfedcba";
    assertThat(sut.smallestPalindrome(s)).isEqualTo(expected);
  }

  // Step 14: the upper constraint bound (s.length == 10^5) — 50000 of each letter collapse to
  // "a" * 25000, "b" * 50000, "a" * 25000, and the build must stay linear
  @Test
  void maximumLengthInputAtTheConstraintBound() {
    String s = "ba".repeat(25_000) + "ab".repeat(25_000);
    String expected = "a".repeat(25_000) + "b".repeat(50_000) + "a".repeat(25_000);
    assertThat(sut.smallestPalindrome(s)).isEqualTo(expected);
  }
}
