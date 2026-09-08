package leetcode.p0901_1000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class DistinctSubsequencesIITest {
  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

  DistinctSubsequencesII sut = new DistinctSubsequencesII();

  // ===========================================================================================
  // One or two letters (Steps 1-4).
  // ===========================================================================================

  // Step 1: smallest valid input (1 <= s.length). The only non-empty subsequence is the letter
  //         itself. A solution that counts the empty subsequence as well answers 2
  @Test
  void singleLetterHasOneSubsequence() {
    assertThat(sut.distinctSubseqII("a")).isEqualTo(1);
  }

  // Step 2: two different letters give "a", "b" and "ab". A solution that only counts single
  //         letters answers 2, and one that counts the empty subsequence answers 4
  @Test
  void twoDifferentLettersGiveThreeSubsequences() {
    assertThat(sut.distinctSubseqII("ab")).isEqualTo(3);
  }

  // Step 3: two equal letters give only "a" and "aa": the two ways of picking a single 'a' are the
  //         same string and count once. Counting every index subset answers 3. So does a solution
  //         that keeps last-seen positions in a zero-filled table and tests it with "> 0", because
  //         the repeat of s[0] is then never noticed
  @Test
  void twoEqualLettersCollapseToTwoSubsequences() {
    assertThat(sut.distinctSubseqII("aa")).isEqualTo(2);
  }

  // Step 4: Step 3 at the far end of the alphabet. 'z' - 'a' is 25, so a per-letter table sized 25
  //         throws on it
  @Test
  void lastLetterOfTheAlphabetIndexesTheTable() {
    assertThat(sut.distinctSubseqII("zz")).isEqualTo(2);
  }

  // ===========================================================================================
  // Where the repeat sits (Steps 5-12).
  // ===========================================================================================

  // Step 5: with no repeated letter every index subset is a different string, so the count is
  //         2^4 - 1 = 15
  @Test
  void fourDistinctLettersGiveAllFifteenSubsets() {
    assertThat(sut.distinctSubseqII("abcd")).isEqualTo(15);
  }

  // Step 6: a repeat then a new letter: "a", "aa", "b", "ab", "aab" -> 5. Appending the second 'a'
  //         doubles the count but re-creates every subsequence that already ended at the first 'a',
  //         and there are exactly as many of those as there were subsequences before the first 'a'.
  //         Subtracting the count after the previous occurrence instead of before it answers 3
  @Test
  void repeatThenNewLetterSubtractsTheCountBeforeThePreviousOccurrence() {
    assertThat(sut.distinctSubseqII("aab")).isEqualTo(5);
  }

  // Step 7: the repeat is separated by another letter. "zy" and "yz" are different strings, so
  //         "zyz" has "z", "y", "zy", "yz", "zz", "zyz" -> 6. A solution that treats the input as a
  //         multiset of letters, multiplying (count + 1) per letter, answers 5. One that only
  //         merges adjacent repeats answers 7
  @Test
  void separatedRepeatKeepsBothOrders() {
    assertThat(sut.distinctSubseqII("zyz")).isEqualTo(6);
  }

  // Step 8: both letters repeat, interleaved. "abab" -> a, b, aa, ab, ba, bb, aab, aba, abb, bab,
  //         abab = 11. The multiset formula answers 8, subtracting the count after the previous
  //         occurrence answers 7, and missing the repeat of s[0] answers 13
  @Test
  void interleavedRepeatsOfTwoLetters() {
    assertThat(sut.distinctSubseqII("abab")).isEqualTo(11);
  }

  // Step 9: a letter seen three times. On the third 'a' the count to subtract is the one before the
  //         latest previous 'a', not the first one. "abaa" -> 9; subtracting the count before the
  //         first 'a' every time answers 12
  @Test
  void thirdOccurrenceSubtractsTheLatestPreviousCount() {
    assertThat(sut.distinctSubseqII("abaa")).isEqualTo(9);
  }

  // Step 10: a palindrome with one letter at four positions and another at two. "abacaba" -> 77.
  //          The first-occurrence bug of Step 9 compounds and answers 102, the multiset formula 29
  @Test
  void nestedRepeatsInAPalindrome() {
    assertThat(sut.distinctSubseqII("abacaba")).isEqualTo(77);
  }

  // Step 11: a run of one letter has exactly as many distinct subsequences as its length: "a",
  //          "aa", ..., up to the whole run. Ten a's -> 10. Counting index subsets answers 1023,
  //          the first-occurrence bug answers 512
  @Test
  void runOfOneLetterCountsItsLength() {
    assertThat(sut.distinctSubseqII("aaaaaaaaaa")).isEqualTo(10);
  }

  // Step 12: two letters alternating ten times -> 231. Nowhere near 2^10 - 1 = 1023, but far above
  //          the multiset formula's 35, because order among the a's and b's matters while repeated
  //          index choices do not
  @Test
  void alternatingLettersTenTimes() {
    assertThat(sut.distinctSubseqII("ababababab")).isEqualTo(231);
  }

  // ===========================================================================================
  // The official examples (Steps 13-15).
  // ===========================================================================================

  // Step 13: three distinct letters -> "a", "b", "c", "ab", "ac", "bc", "abc" = 7. Rules out the
  //          count that includes the empty subsequence (8)
  @Test
  void leetCodeExample1() {
    assertThat(sut.distinctSubseqII("abc")).isEqualTo(7);
  }

  // Step 14: "aba" -> "a", "b", "ab", "aa", "ba", "aba" = 6. The Explanation lists both "ab" and
  //          "ba", ruling out the multiset reading (5), and lists "a" once, ruling out the
  //          index-subset reading (7)
  @Test
  void leetCodeExample2() {
    assertThat(sut.distinctSubseqII("aba")).isEqualTo(6);
  }

  // Step 15: "aaa" -> "a", "aa", "aaa" = 3. Subtracting the count before the first 'a' on every
  //          repeat answers 4
  @Test
  void leetCodeExample3() {
    assertThat(sut.distinctSubseqII("aaa")).isEqualTo(3);
  }

  // ===========================================================================================
  // Modulo 10^9 + 7 (Steps 16-20). The exact counts are known at these lengths, so each step
  // pins one arithmetic mistake rather than a timeout.
  // ===========================================================================================

  // Step 16: the whole alphabet once, the longest all-distinct input the constraints allow.
  //          2^26 - 1 = 67,108,863 is below the modulus, so a correct reduction leaves it untouched
  @Test
  void wholeAlphabetIsTwoToTheTwentySixMinusOne() {
    assertThat(sut.distinctSubseqII(ALPHABET)).isEqualTo(67_108_863);
  }

  // Step 17: the alphabet plus "abcd" is the shortest cycled prefix whose exact count,
  //          1,073,741,791, crosses the modulus. It still fits in an int, so a solution that never
  //          reduces returns it unchanged instead of 1,073,741,791 - 1,000,000,007 = 73,741,784
  @Test
  void firstCountPastTheModulusIsReduced() {
    assertThat(sut.distinctSubseqII(ALPHABET + "abcd")).isEqualTo(73_741_784);
  }

  // Step 18: the alphabet plus "abcdefgh". A solution that keeps, per letter, the count of
  //          subsequences ending in that letter has 26 reduced values to add up, and their raw sum
  //          first passes 2^31 here. Summing them in an int without reducing as it goes answers
  //          -115,099,228
  @Test
  void sumOfPerLetterCountsMustNotOverflowInt() {
    assertThat(sut.distinctSubseqII(ALPHABET + "abcdefgh")).isEqualTo(179_868_040);
  }

  // Step 19: two alphabets plus "abc". The reduced doubled count first drops below the value being
  //          subtracted here, so (2 * count % MOD - previous) % MOD goes negative in Java and a
  //          solution that does not add the modulus back answers -17,865,774
  @Test
  void subtractionAfterReductionMustAddTheModulusBack() {
    assertThat(sut.distinctSubseqII(ALPHABET + ALPHABET + "abc")).isEqualTo(982_134_233);
  }

  // Step 20: two alphabets plus "abcdefghijk". The other order of operations, doubling the reduced
  //          count without reducing again before the subtraction, survives Step 19 and every
  //          maximum-length step below, because a negative remainder carried forward is still
  //          congruent and happens to land non-negative at the end. It first goes wrong at 63
  //          letters and answers -329,421,275
  @Test
  void subtractionBeforeReductionMustAddTheModulusBackToo() {
    assertThat(sut.distinctSubseqII(ALPHABET + ALPHABET + "abcdefghijk")).isEqualTo(670_578_732);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 21-23). 2000 letters: enumerating subsequences into a set touches up
  // to 2^2000 strings and cannot finish, while the O(n) or O(26 n) recurrence finishes in
  // microseconds.
  // ===========================================================================================

  // Step 21: Step 11 at maximum length. 2000 a's have exactly 2000 distinct subsequences, the one
  //          value at this size that can be checked by hand
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthRunCountsItsLength() {
    assertThat(sut.distinctSubseqII("a".repeat(2000))).isEqualTo(2000);
  }

  // Step 22: the alphabet cycled to 2000 letters, so every letter recurs about 77 times and all 26
  //          per-letter counts sit near the modulus at the end. The negative remainder of Step 19
  //          answers -4,915,685 and the int overflow of Step 18 answers -467,477,979
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAlphabetCycle() {
    assertThat(sut.distinctSubseqII(alphabetCycle(2000))).isEqualTo(995_084_322);
  }

  // Step 23: 2000 letters chosen by i * i % 26, a deterministic scramble in which only the 14
  //          quadratic residues of 26 ever appear, each recurring at uneven distances.
  //          -> 792,670,025; the int overflow of Step 18 answers -49,382,393
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthScrambledLetters() {
    assertThat(sut.distinctSubseqII(squareResidues(2000))).isEqualTo(792_670_025);
  }

  // ===========================================================================================
  // Hygiene (Step 24). The input is a String, so there is no in-place mutation to guard against.
  // ===========================================================================================

  // Step 24: one instance answers inputs of different lengths, deliberately out of order with the
  //          longest in the middle. A per-letter table or a last-seen array kept on the instance
  //          instead of being reset per call answers the later strings from the earlier ones'
  //          leftovers
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersStringsOfAnyLengthInAnyOrder() {
    assertThat(sut.distinctSubseqII("abab")).isEqualTo(11);
    assertThat(sut.distinctSubseqII(alphabetCycle(2000))).isEqualTo(995_084_322);
    assertThat(sut.distinctSubseqII("aba")).isEqualTo(6);
    assertThat(sut.distinctSubseqII("a")).isEqualTo(1);
  }

  /** The alphabet repeated end to end: letter i is 'a' + i % 26. */
  private static String alphabetCycle(int n) {
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append((char) ('a' + i % 26));
    }
    return sb.toString();
  }

  /** Letter i is 'a' + i * i % 26, so only the 14 quadratic residues of 26 ever appear. */
  private static String squareResidues(int n) {
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append((char) ('a' + i * i % 26));
    }
    return sb.toString();
  }
}
