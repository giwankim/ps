package leetcode.p0601_0700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Tests for <a href="https://leetcode.com/problems/palindromic-substrings/">647. Palindromic
 * Substrings</a>.
 */
class PalindromicSubstringsTest {
  PalindromicSubstrings sut = new PalindromicSubstrings();

  // ===========================================================================================
  // What counts as a palindromic substring (Steps 1-7).
  // ===========================================================================================

  // Step 1: the constraints allow a length of 1, so a lone character is the smallest legal input
  //         and is itself a palindrome. A solution that requires a matching pair answers 0
  @Test
  void singleCharacterIsAPalindrome() {
    assertThat(sut.countSubstrings("a")).isEqualTo(1);
  }

  // Step 2: two different characters contribute only their singletons
  @Test
  void twoDistinctCharactersHaveOnlyTheirSingletons() {
    assertThat(sut.countSubstrings("ab")).isEqualTo(2);
  }

  // Step 3: the mirror of Step 2, and the earliest point that kills an expansion which only ever
  //         starts from a single character. Odd centers alone find the two singletons and answer 2
  @Test
  void twoEqualCharactersAddAnEvenLengthPalindrome() {
    assertThat(sut.countSubstrings("aa")).isEqualTo(3);
  }

  // Step 4: three singletons plus "aba" itself
  @Test
  void oddLengthPalindromeAddsOneAroundItsCenter() {
    assertThat(sut.countSubstrings("aba")).isEqualTo(4);
  }

  // Step 5: the even-length mirror of Step 4. Four singletons, "bb", and "abba". Expanding only
  //         around single characters answers 4
  @Test
  void evenLengthPalindromeAddsOneBetweenItsCenters() {
    assertThat(sut.countSubstrings("abba")).isEqualTo(6);
  }

  // Step 6: the ends of "abca" match but the interior does not, so only the four singletons count.
  //         Testing the endpoints without requiring a palindromic interior answers 5
  @Test
  void matchingEndpointsStillRequireAPalindromicInterior() {
    assertThat(sut.countSubstrings("abca")).isEqualTo(4);
  }

  // Step 7: substrings are contiguous. The two letters "a" of "aca" form a palindrome only by
  //         skipping the middle character, so counting palindromic subsequences answers 5
  @Test
  void palindromicSubsequenceDoesNotQualify() {
    assertThat(sut.countSubstrings("aca")).isEqualTo(4);
  }

  // ===========================================================================================
  // Every occurrence counts separately (Steps 8-12).
  // ===========================================================================================

  // Step 8: equal substrings at different positions are counted once each, so "aaa" holds three
  //         singletons, two copies of "aa", and itself. Counting distinct strings answers 3
  @Test
  void repeatedCharacterCountsEachOccurrenceSeparately() {
    assertThat(sut.countSubstrings("aaa")).isEqualTo(6);
  }

  // Step 9: with every character equal, all n * (n + 1) / 2 substrings qualify. Counting distinct
  //         strings answers 4, and odd centers alone answer 6
  @Test
  void allEqualCharactersCountEverySubstring() {
    assertThat(sut.countSubstrings("aaaa")).isEqualTo(10);
  }

  // Step 10: the mirror of Step 9. With no character repeated, the count equals the length
  @Test
  void allDistinctCharactersCountOnlySingletons() {
    assertThat(sut.countSubstrings("abcdef")).isEqualTo(6);
  }

  // Step 11: palindromes of both parities nest inside one another here. Five singletons, "aa"
  //          twice, "aba", and the whole string make 9, while "aaba" and "abaa" qualify for
  //          neither. Testing endpoints without a palindromic interior answers 11, and odd
  //          centers alone answer 7
  @Test
  void nestedPalindromesEachCountSeparately() {
    assertThat(sut.countSubstrings("aabaa")).isEqualTo(9);
  }

  // Step 12: every palindrome in "abacaba" is odd-length and they overlap heavily: seven
  //          singletons, "aba" twice, "aca", "bacab", and the whole string. Counting distinct
  //          strings answers 7, and testing endpoints alone answers 14
  @Test
  void alternatingCentersProduceOverlappingPalindromes() {
    assertThat(sut.countSubstrings("abacaba")).isEqualTo(12);
  }

  // ===========================================================================================
  // The official examples (Steps 13-14).
  // ===========================================================================================

  // Step 13: the statement's first example, whose explanation lists only the three singletons
  @Test
  void leetCodeExample1() {
    assertThat(sut.countSubstrings("abc")).isEqualTo(3);
  }

  // Step 14: the statement's second example. Its explanation spells out "a", "a", "a", "aa",
  //          "aa", and "aaa", which is exactly why a distinct-string count is wrong
  @Test
  void leetCodeExample2() {
    assertThat(sut.countSubstrings("aaa")).isEqualTo(6);
  }

  // ===========================================================================================
  // Constraint bounds, s.length up to 1000 (Steps 15-18). An all-equal string of 1000 characters
  // is the worst case for checking each substring on its own: 500,500 substrings averaging 333
  // characters. A palindrome table or center expansion finishes in milliseconds, while any
  // attempt to enumerate subsequences cannot finish at all.
  // ===========================================================================================

  // Step 15: the largest answer the constraints permit, 1000 * 1001 / 2, which still fits an int.
  //          Counting distinct strings answers 1000 here, since the substrings are just "a"
  //          repeated one through one thousand times
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllEqualCountsEverySubstring() {
    assertThat(sut.countSubstrings("a".repeat(1000))).isEqualTo(500_500);
  }

  // Step 16: the mirror of Step 15 at maximum length. Equal characters sit 26 apart, and no
  //          window can close around them, so only the 1000 singletons count
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthCyclingAlphabetHasNoLongerPalindrome() {
    String s = "abcdefghijklmnopqrstuvwxyz".repeat(40).substring(0, 1000);
    assertThat(sut.countSubstrings(s)).isEqualTo(1000);
  }

  // Step 17: alternating characters admit every odd-length window and no even-length one, which
  //          is the deepest odd expansion a maximum-length input can force
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAlternatingCountsOddLengthPalindromes() {
    assertThat(sut.countSubstrings("ab".repeat(500))).isEqualTo(250_500);
  }

  // Step 18: two alternating halves meeting at "abba" put the single deepest even center in the
  //          middle of a maximum-length input, where an off-by-one in the even pass shows up
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithASeamCountsBothSides() {
    assertThat(sut.countSubstrings("ab".repeat(250) + "ba".repeat(250))).isEqualTo(126_000);
  }

  // ===========================================================================================
  // Hygiene (Step 19).
  // ===========================================================================================

  // Step 19: one instance answers several inputs of different lengths, deliberately out of order
  //          with the longest in the middle. A count or a table cached on the instance rather
  //          than reset per call fails here while every case above still passes
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.countSubstrings("aaa")).isEqualTo(6);
    assertThat(sut.countSubstrings("ab")).isEqualTo(2);
    assertThat(sut.countSubstrings("a".repeat(1000))).isEqualTo(500_500);
    assertThat(sut.countSubstrings("abc")).isEqualTo(3);
    assertThat(sut.countSubstrings("abba")).isEqualTo(6);
  }
}
