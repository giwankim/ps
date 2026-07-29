package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SmallestPalindromicRearrangementIITest {
  SmallestPalindromicRearrangementII sut = new SmallestPalindromicRearrangementII();

  // Step 1: smallest valid input (1 <= s.length) — one character has exactly one arrangement
  @Test
  void singleCharacterHasExactlyOneArrangement() {
    assertThat(sut.smallestPalindrome("a", 1)).isEqualTo("a");
  }

  // Step 2: the other return shape — asking past the last arrangement yields an empty string
  @Test
  void kBeyondTheOnlyArrangementIsEmpty() {
    assertThat(sut.smallestPalindrome("a", 2)).isEmpty();
  }

  // Step 3: a repeated letter still admits only one distinct palindrome — rearrangements that
  // produce the same string are counted once
  @Test
  void identicalPairHasExactlyOneArrangement() {
    assertThat(sut.smallestPalindrome("aa", 1)).isEqualTo("aa");
  }

  // Step 4: LeetCode Example 2 — "aa" has one arrangement, so k = 2 overruns it
  @Test
  void leetCodeExample2() {
    assertThat(sut.smallestPalindrome("aa", 2)).isEmpty();
  }

  // Step 5: the first arrangement is always the ascending half mirrored
  @Test
  void firstArrangementIsTheAscendingHalf() {
    assertThat(sut.smallestPalindrome("abba", 1)).isEqualTo("abba");
  }

  // Step 6: LeetCode Example 1 — the second of the two halves "ab" and "ba"
  @Test
  void leetCodeExample1() {
    assertThat(sut.smallestPalindrome("abba", 2)).isEqualTo("baab");
  }

  // Step 7: two distinct halves exist, so k = 3 overruns them
  @Test
  void kOneBeyondTwoArrangementsIsEmpty() {
    assertThat(sut.smallestPalindrome("abba", 3)).isEmpty();
  }

  // Step 8: LeetCode Example 3 — odd length, so 'c' is pinned to the center and only "ab" / "ba"
  // vary
  @Test
  void leetCodeExample3() {
    assertThat(sut.smallestPalindrome("bacab", 1)).isEqualTo("abcba");
  }

  // Step 9: the pinned middle does not move when the halves advance to the next arrangement
  @Test
  void middleCharacterStaysPinnedWhileTheHalvesAdvance() {
    assertThat(sut.smallestPalindrome("bacab", 2)).isEqualTo("bacab");
  }

  // Step 10: parity outranks order — 'a' holds the odd count, so it sits in the center even
  // though it is the smallest letter and would otherwise lead the half
  @Test
  void smallestLetterCanBeTheForcedMiddle() {
    assertThat(sut.smallestPalindrome("bcacb", 2)).isEqualTo("cbabc");
  }

  // Step 11: the same rule with the largest letter — 'z' is stuck in the center while a/b vary
  @Test
  void largestLetterCanBeTheForcedMiddle() {
    assertThat(sut.smallestPalindrome("abzba", 2)).isEqualTo("bazab");
  }

  // Step 12: three distinct letters give 3! = 6 halves (abc, acb, bac, bca, cab, cba); k = 4
  // forces a first-character advance past the two halves that begin with 'a'
  @Test
  void midRangeKSkipsWholeBlocksOfArrangements() {
    assertThat(sut.smallestPalindrome("abccba", 4)).isEqualTo("bcaacb");
  }

  // Step 13: the last arrangement is the descending half mirrored
  @Test
  void lastArrangementIsTheDescendingHalf() {
    assertThat(sut.smallestPalindrome("abccba", 6)).isEqualTo("cbaabc");
  }

  // Step 14: exactly one past the last arrangement is the boundary that off-by-one errors miss
  @Test
  void kOneBeyondTheLastArrangementIsEmpty() {
    assertThat(sut.smallestPalindrome("abccba", 7)).isEqualTo("");
  }

  // Step 15: duplicates inside the half collapse — half "aab" yields 3!/2! = 3 arrangements
  // (aab, aba, baa), not 6, so k = 2 lands on "aba"
  @Test
  void duplicateLettersInTheHalfCollapseArrangements() {
    assertThat(sut.smallestPalindrome("aabbaa", 2)).isEqualTo("abaaba");
  }

  // Step 16: the count really is 3 and not 6 — treating the half as distinct letters would
  // wrongly return an arrangement here
  @Test
  void kBeyondTheCollapsedCountIsEmpty() {
    assertThat(sut.smallestPalindrome("aabbaa", 4)).isEqualTo("");
  }

  // Step 17: two repeated letters — half "aabb" yields 4!/(2!2!) = 6 arrangements
  // (aabb, abab, abba, baab, baba, bbaa) and k = 4 selects "baab"
  @Test
  void multinomialCountWithTwoRepeatedLetters() {
    assertThat(sut.smallestPalindrome("aabbbbaa", 4)).isEqualTo("baabbaab");
  }

  // Step 18: repeats and a forced middle together — half "abb" yields 3 arrangements around the
  // pinned 'c', and k = 3 selects the last of them
  @Test
  void repeatedLettersAroundAForcedMiddle() {
    assertThat(sut.smallestPalindrome("babcbab", 3)).isEqualTo("bbacabb");
  }

  // Step 19: 26 distinct letters give 26! ~ 4.0e26 arrangements, which overflows a long — the
  // count must be saturated against k rather than evaluated exactly
  @Test
  void arrangementCountBeyondLongRangeMustNotOverflow() {
    String s = "abcdefghijklmnopqrstuvwxyz" + "zyxwvutsrqponmlkjihgfedcba";
    String expected = "abcdefghijklmnopqrstuvwxzy" + "yzxwvutsrqponmlkjihgfedcba";
    assertThat(sut.smallestPalindrome(s, 2)).isEqualTo(expected);
  }

  // Step 20: the upper constraint bound for k (k <= 10^6) — 10 distinct letters give
  // 10! = 3628800 halves, and the 1000000th is "chidjbfega"
  @Test
  void maximumKAtTheConstraintBound() {
    assertThat(sut.smallestPalindrome("abcdefghij" + "jihgfedcba", 1_000_000))
        .isEqualTo("chidjbfega" + "agefbjdihc");
  }

  // Step 21: the upper constraint bound for s (s.length == 10^4) — half "a" * 2500 + "b" * 2500
  // has C(5000, 2500) ~ 1e1504 arrangements, so k = 1 must short-circuit instead of counting
  @Test
  void maximumLengthInputReturnsTheAscendingHalfFirst() {
    String s = "a".repeat(2500) + "b".repeat(5000) + "a".repeat(2500);
    assertThat(sut.smallestPalindrome(s, 1)).isEqualTo(s);
  }

  // Step 22: the same astronomically large count with k = 2 — the half advances by one position
  // to "a" * 2499 + "ba" + "b" * 2499, which mirrors into a single "ba" / "ab" seam
  @Test
  void maximumLengthInputAtTheConstraintBound() {
    String s = "a".repeat(2500) + "b".repeat(5000) + "a".repeat(2500);
    String expected = "a".repeat(2499) + "ba" + "b".repeat(4998) + "ab" + "a".repeat(2499);
    assertThat(sut.smallestPalindrome(s, 2)).isEqualTo(expected);
  }
}
