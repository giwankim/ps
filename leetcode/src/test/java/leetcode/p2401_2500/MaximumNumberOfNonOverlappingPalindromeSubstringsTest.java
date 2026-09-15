package leetcode.p2401_2500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MaximumNumberOfNonOverlappingPalindromeSubstringsTest {
  MaximumNumberOfNonOverlappingPalindromeSubstrings sut =
      new MaximumNumberOfNonOverlappingPalindromeSubstrings();

  // ===========================================================================================
  // Minimum length and palindrome validity (Steps 1-9).
  // ===========================================================================================

  // Step 1: the smallest legal input is already a palindrome. Requiring a pair of matching
  //         characters incorrectly rejects this single-character selection
  @Test
  void singleCharacterIsAPalindrome() {
    assertThat(sut.maxPalindromes("a", 1)).isEqualTo(1);
  }

  // Step 2: at k = 1 every position can be selected separately, including both alphabet extremes.
  //         Maximizing one palindrome's length misses the requested number of substrings
  @Test
  void k1SelectsEveryCharacter() {
    assertThat(sut.maxPalindromes("abcdefghijklmnopqrstuvwxyz", 1)).isEqualTo(26);
  }

  // Step 3: length exactly k qualifies. A strict greater-than check discards the only candidate
  @Test
  void lengthExactlyKIsAllowed() {
    assertThat(sut.maxPalindromes("aa", 2)).isEqualTo(1);
  }

  // Step 4: matching the length alone is insufficient, and the empty selection is legal.
  //         Counting single letters here ignores the minimum length
  @Test
  void distinctPairLeavesTheSelectionEmpty() {
    assertThat(sut.maxPalindromes("ab", 2)).isZero();
  }

  // Step 5: "aba" qualifies although neither length-2 window does. Checking only windows of
  //         exactly k, or only even-length palindromes, loses this candidate
  @Test
  void k2AcceptsAnOddPalindromeLongerThanK() {
    assertThat(sut.maxPalindromes("aba", 2)).isEqualTo(1);
  }

  // Step 6: the parity mirror of Step 5. "abba" qualifies at k = 3, while its inner "bb" is
  //         too short. Expanding only around a character misses this even-length palindrome
  @Test
  void k3AcceptsAnEvenPalindromeLongerThanK() {
    assertThat(sut.maxPalindromes("abba", 3)).isEqualTo(1);
  }

  // Step 7: k may equal the entire string length. Excluding the final character, or confusing
  //         the number of matching pairs with the palindrome length, loses "aabaa"
  @Test
  void kEqualsLengthAcceptsTheWholePalindrome() {
    assertThat(sut.maxPalindromes("aabaa", 5)).isEqualTo(1);
  }

  // Step 8: equal endpoints do not make a palindrome when the interior differs. At k = 4
  //         shorter candidates cannot hide an incomplete palindrome check
  @Test
  void matchingEndpointsStillRequireAPalindromicInterior() {
    assertThat(sut.maxPalindromes("abca", 4)).isZero();
  }

  // Step 9: "aaa" exists as a subsequence, but no contiguous palindrome reaches length 3.
  //         Skipping characters inside a candidate violates the definition of substring
  @Test
  void aPalindromicSubsequenceDoesNotQualify() {
    assertThat(sut.maxPalindromes("aabca", 3)).isZero();
  }

  // ===========================================================================================
  // Overlap, unused characters, and competing selections (Steps 10-16).
  // ===========================================================================================

  // Step 10: the prefix and suffix "aba" share the middle character. Counting both permits
  //          an overlap at the boundary, and counting every qualifying palindrome also overcounts
  @Test
  void chosenPalindromesCannotShareAnEndpoint() {
    assertThat(sut.maxPalindromes("ababa", 3)).isEqualTo(1);
  }

  // Step 11: adjacent "aa" occurrences use distinct positions and both count despite equal text.
  //          Requiring a gap, deduplicating strings, or taking the longest palindrome gives only 1
  @Test
  void adjacentEqualPalindromesCountAsSeparateOccurrences() {
    assertThat(sut.maxPalindromes("aaaa", 2)).isEqualTo(2);
  }

  // Step 12: select "aba" and "cdc", leaving the prefix, middle separator, and suffix unused.
  //          Requiring the selection to partition the entire string rejects a valid optimum
  @Test
  void unusedCharactersMayPrecedeSeparateAndFollowSelections() {
    assertThat(sut.maxPalindromes("xabaxcdcy", 3)).isEqualTo(2);
  }

  // Step 13: "racecar", "aceca", and "cec" are nested candidates. Different lengths or
  //          different text do not make them non-overlapping, so they contribute only 1
  @Test
  void nestedPalindromesCompeteForTheSamePositions() {
    assertThat(sut.maxPalindromes("racecar", 3)).isEqualTo(1);
  }

  // Step 14: choosing the earliest starting palindrome takes "abba" and leaves "ba", scoring 1.
  //          Skipping the first character allows adjacent "bb" and "aba", scoring 2
  @Test
  void skippingTheEarliestStartingPalindromeAllowsMoreSelections() {
    assertThat(sut.maxPalindromes("abbaba", 2)).isEqualTo(2);
  }

  // Step 15: the globally shortest candidate is the central "aa", which blocks both "aba"
  //          occurrences. Selecting those two length-3 occurrences beats shortest-first greed
  @Test
  void theGloballyShortestPalindromeCanBlockTheBestSelection() {
    assertThat(sut.maxPalindromes("abaaba", 2)).isEqualTo(2);
  }

  // Step 16: both odd-length palindromes meet at a legal boundary and the second ends at n - 1.
  //          Advancing one character too far after "aba" loses "cdc"
  @Test
  void adjacentOddPalindromesCanIncludeTheFinalCharacter() {
    assertThat(sut.maxPalindromes("abacdc", 3)).isEqualTo(2);
  }

  // ===========================================================================================
  // Official examples (Steps 17-18).
  // ===========================================================================================

  // Step 17: the statement selects "aba" and "dbbd", leaving "cc" unused. Requiring exactly
  //          k characters loses "dbbd", while counting "cc" ignores the minimum length
  @Test
  void leetCodeExample1() {
    assertThat(sut.maxPalindromes("abaccdbbd", 3)).isEqualTo(2);
  }

  // Step 18: repeated letters allow palindromic subsequences, but there is no qualifying
  //          substring. The explanation rules out rearranging or deleting internal characters
  @Test
  void leetCodeExample2() {
    assertThat(sut.maxPalindromes("adbcda", 2)).isZero();
  }

  // ===========================================================================================
  // Constraint bounds: 1 <= k <= s.length <= 2000, lowercase English letters (Steps 19-25).
  //
  // O(n^2) preprocessing and selection fit comfortably at n = 2000. Exhaustively branching over
  // overlapping choices without memoization is exponential on the dense cases below. Separate
  // thread timeouts guard against that blowup without prescribing a particular algorithm.
  // ===========================================================================================

  // Step 19: at maximum length and minimum k, every character contributes 1 even though the
  //          alphabet repeats. Counting distinct letters caps the answer incorrectly at 26
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithK1SelectsEveryPosition() {
    String s = "abcdefghijklmnopqrstuvwxyz".repeat(77).substring(0, 2000);

    assertThat(sut.maxPalindromes(s, 1)).isEqualTo(2000);
  }

  // Step 20: every substring is a palindrome, but disjoint length-2 selections cap the count at
  //          1000. This dense overlap case exposes unbounded enumeration of compatible choices
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumRepeatedStringPacksTheMaximumNumberOfPairs() {
    assertThat(sut.maxPalindromes("a".repeat(2000), 2)).isEqualTo(1000);
  }

  // Step 21: alternating letters have only odd-length palindromes, so k = 2 needs length 3.
  //          Disjoint triples give floor(2000 / 3) = 666, with two characters left unused
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumAlternatingStringRequiresOddLengthSelections() {
    assertThat(sut.maxPalindromes("ab".repeat(1000), 2)).isEqualTo(666);
  }

  // Step 22: either length-1999 window qualifies, but they overlap. A nearly full-length
  //          threshold must still allow an unused character and must not count both windows
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k1999AllowsOneNearlyFullLengthPalindrome() {
    assertThat(sut.maxPalindromes("z".repeat(2000), 1999)).isEqualTo(1);
  }

  // Step 23: the largest legal k admits the entire even-length palindrome. Exclusive upper
  //          bounds on the length or right endpoint incorrectly drop the only candidate
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k2000AcceptsTheEntireMaximumLengthPalindrome() {
    assertThat(sut.maxPalindromes("z".repeat(2000), 2000)).isEqualTo(1);
  }

  // Step 24: a 1999-character palindrome is still too short at k = 2000. Ignoring the final
  //          mismatching character or relaxing the length threshold manufactures a selection
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void k2000RejectsAnAlmostFullLengthPalindrome() {
    assertThat(sut.maxPalindromes("a".repeat(1999) + "z", 2000)).isZero();
  }

  // Step 25: no adjacent letters or letters two positions apart match, ruling out every
  //          palindrome center of length at least 2. Repeated letters farther apart do not help
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumAlphabetCycleHasNoQualifyingSubstring() {
    String s = "abcdefghijklmnopqrstuvwxyz".repeat(77).substring(0, 2000);

    assertThat(sut.maxPalindromes(s, 2)).isZero();
  }

  // ===========================================================================================
  // Repeated calls on one instance (Steps 26-27). String inputs are immutable.
  // ===========================================================================================

  // Step 26: changing only k changes the optimum. Reusing a memoized answer keyed solely by
  //          the string or its positions leaks a previous threshold into the next call
  @Test
  void sameStringCanBeQueriedWithDifferentThresholds() {
    String s = "aaaaa";

    assertThat(sut.maxPalindromes(s, 2)).isEqualTo(2);
    assertThat(sut.maxPalindromes(s, 3)).isEqualTo(1);
    assertThat(sut.maxPalindromes(s, 1)).isEqualTo(5);
    assertThat(sut.maxPalindromes(s, 5)).isEqualTo(1);
    assertThat(sut.maxPalindromes(s, 2)).isEqualTo(2);
  }

  // Step 27: grow, replace the string with different text of the same length, then shrink.
  //          A retained palindrome table, selection boundary, or accumulated count must reset
  @Test
  void oneInstanceHandlesChangingContentAndSizes() {
    assertThat(sut.maxPalindromes("ab", 2)).isZero();
    assertThat(sut.maxPalindromes("abaccdbbd", 3)).isEqualTo(2);
    assertThat(sut.maxPalindromes("abcdefghi", 3)).isZero();
    assertThat(sut.maxPalindromes("z", 1)).isEqualTo(1);
  }
}
