package leetcode.p0101_0200;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class DistinctSubsequencesTest {
  DistinctSubsequences sut = new DistinctSubsequences();

  // ===========================================================================================
  // The floor and one rule at a time (Steps 1-10).
  //
  // A subsequence of s is what remains after deleting zero or more letters without reordering
  // the rest. The answer counts the ways to pick positions of s that spell t, so the same
  // resulting string reached through different positions counts every time.
  // ===========================================================================================

  // Step 1: smallest valid input (1 <= s.length, t.length). One letter matched against itself has
  //         exactly one way, and it is the DP's whole base case in one cell
  @Test
  void singleLetterMatchesItselfOnce() {
    assertThat(sut.numDistinct("a", "a")).isEqualTo(1);
  }

  // Step 2: the mirror of Step 1. Nothing in s spells t, so the answer is 0 rather than an
  //         exception or a leftover base-case 1
  @Test
  void singleLetterMismatchHasNoWay() {
    assertThat(sut.numDistinct("a", "b")).isZero();
  }

  // Step 3: s is the source and t the target. Deleting the trailing letter of s leaves t, which
  //         is the only way, so the answer is 1. A solution that swaps the two operands answers 0
  @Test
  void tThatIsAPrefixOfSHasOneWay() {
    assertThat(sut.numDistinct("abc", "ab")).isEqualTo(1);
  }

  // Step 4: the mirror of Step 3. Deleting letters can only shorten s, so a t longer than s can
  //         never be reached and the answer is 0. Swapping the operands answers 1
  @Test
  void tLongerThanSHasNoWay() {
    assertThat(sut.numDistinct("ab", "abc")).isZero();
  }

  // Step 5: equal strings. Every letter of s is needed, so deleting any one of them breaks the
  //         match and the only subsequence equal to t is s itself
  @Test
  void equalStringsHaveExactlyOneWay() {
    assertThat(sut.numDistinct("abc", "abc")).isEqualTo(1);
  }

  // Step 6: the matching letter sits at the first index of s and then at the last. A loop that
  //         stops one short of the end, or starts one past the beginning, drops one of these
  @Test
  void matchMayComeFromEitherEndOfS() {
    assertThat(sut.numDistinct("abc", "a")).isEqualTo(1);
    assertThat(sut.numDistinct("abc", "c")).isEqualTo(1);
  }

  // Step 7: the letters of t are all present and in order, but never adjacent in s. A solution
  //         that counts occurrences of t as a substring answers 0
  @Test
  void subsequenceNeedNotBeContiguous() {
    assertThat(sut.numDistinct("axbxc", "abc")).isEqualTo(1);
  }

  // Step 8: s holds exactly the letters of t, in the wrong order. Deleting cannot reorder, so the
  //         answer is 0. A solution that compares letter counts, or multiplies the count of each
  //         letter of t in s, answers 1
  @Test
  void orderOfLettersMatters() {
    assertThat(sut.numDistinct("ba", "ab")).isZero();
  }

  // Step 9: t agrees with s up to its last letter and then asks for one s does not have. A partial
  //         match is worth nothing, so the answer is 0, not the 1 of the matched prefix
  @Test
  void partialMatchCountsForNothing() {
    assertThat(sut.numDistinct("abc", "abd")).isZero();
  }

  // Step 10: s and t consist of English letters, which includes both cases, and 'A' is not 'a'.
  //          In "aAaA" the target "aA" is spelled by a lowercase a followed by a later uppercase A:
  //          index 0 with 1, 0 with 3, and 2 with 3, so 3 ways. A solution that folds case sees
  //          "aaaa" and answers 6 (and 1 for the first pair), and a 26-slot table indexed by
  //          c - 'a' throws on the uppercase letters
  @Test
  void lettersAreCaseSensitive() {
    assertThat(sut.numDistinct("A", "a")).isZero();
    assertThat(sut.numDistinct("aAaA", "aA")).isEqualTo(3);
  }

  // ===========================================================================================
  // Counting positions, not strings (Steps 11-16).
  //
  // This is where the problem's difficulty lives. The count is over index choices, so repeated
  // letters in s multiply the ways, repeated letters in t must each consume a different position,
  // and the positions chosen for t must increase left to right.
  // ===========================================================================================

  // Step 11: three positions of s each spell "a" on their own, and each is a separate way. A
  //          solution that collects the distinct resulting strings in a set answers 1
  @Test
  void everyPositionOfARepeatedLetterIsADistinctWay() {
    assertThat(sut.numDistinct("aaa", "a")).isEqualTo(3);
  }

  // Step 12: t repeats a letter, and each copy must come from a different position of s, so
  //          "aa" is spelled by "aa" in exactly one way. This is the case that catches the
  //          classic rolled-up DP bug: a single row updated left to right lets the letter just
  //          consumed for t[0] be consumed again for t[1] in the same pass, and answers 3
  @Test
  void repeatedLetterInTUsesEachPositionOfSOnce() {
    assertThat(sut.numDistinct("aa", "aa")).isEqualTo(1);
  }

  // Step 13: choose two of three equal letters, which is 3 ways. The left-to-right rolled-up DP
  //          of Step 12 answers 6, and multiplying letter counts answers 9
  @Test
  void chooseTwoOfThreeRepeatedLetters() {
    assertThat(sut.numDistinct("aaa", "aa")).isEqualTo(3);
  }

  // Step 14: two positions for the a and two for the b, with every a before every b, so the
  //          choices are independent and multiply to 4
  @Test
  void independentChoicesMultiply() {
    assertThat(sut.numDistinct("aabb", "ab")).isEqualTo(4);
  }

  // Step 15: the same letter counts as Step 14, interleaved. The b at index 1 cannot follow the a
  //          at index 2, so only 3 of the 4 pairs are in order. Multiplying letter counts, which
  //          survived Step 14, answers 4
  @Test
  void orderConstraintBreaksTheProduct() {
    assertThat(sut.numDistinct("abab", "ab")).isEqualTo(3);
  }

  // Step 16: Step 15 with three letters. Of the 8 triples of a, b and c positions only 4 increase
  //          left to right: a0 b1 c2, a0 b1 c5, a0 b4 c5, and a3 b4 c5
  @Test
  void threeLetterInterleavingCountsOnlyIncreasingTriples() {
    assertThat(sut.numDistinct("abcabc", "abc")).isEqualTo(4);
  }

  // ===========================================================================================
  // Worked examples and answer magnitude (Steps 17-19).
  // ===========================================================================================

  // Step 17: LeetCode Example 1. The three b's of "rabbbit" supply the two b's of "rabbit" in 3
  //          ways, and every way spells the same string, which is the reading the explanation's
  //          three highlighted copies exist to pin down. The rolled-up DP bug of Step 12 answers
  //          6, multiplying letter counts answers 9, and substring counting answers 0
  @Test
  void leetCodeExample1() {
    assertThat(sut.numDistinct("rabbbit", "rabbit")).isEqualTo(3);
  }

  // Step 18: LeetCode Example 2. "babgbag" has three b's, two a's and two g's, so the letter-count
  //          product is 12, but only 5 of those position triples increase left to right: b0 a1 g3,
  //          b0 a1 g6, b0 a5 g6, b2 a5 g6, and b4 a5 g6. Substring counting finds the one
  //          contiguous "bag" and answers 1
  @Test
  void leetCodeExample2() {
    assertThat(sut.numDistinct("babgbag", "bag")).isEqualTo(5);
  }

  // Step 19: the answer is guaranteed to fit a signed 32-bit int, and this one nearly fills it.
  //          Choosing 15 of 34 equal letters has C(34, 15) = 1,855,967,520 ways. Enumerating the
  //          2^34 subsets of s to count them is about 1.7 * 10^10 checks and cannot finish, while
  //          the DP fills 34 * 15 cells
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void answerCanApproachTheIntLimit() {
    assertThat(sut.numDistinct("a".repeat(34), "a".repeat(15))).isEqualTo(1_855_967_520);
  }

  // ===========================================================================================
  // Upper end of the constraints (1 <= s.length, t.length <= 1000).
  //
  // What these pin down is the running time. The intended solution is the O(n * m) table over the
  // prefixes of s and t, or its rolled-up O(m) row, which is 10^6 cells and finishes in
  // milliseconds. A recursion on i and j that forgets to memoize revisits the same pair
  // exponentially often, and enumerating the subsequences of s is 2^1000. The timeouts are set far
  // above what the table needs, so they are evidence about complexity class rather than about
  // constant factors.
  //
  // They run on a separate thread because JUnit's default SAME_THREAD mode only compares elapsed
  // time after the method returns: against a solution slow enough to matter, a same-thread timeout
  // hangs the build instead of reporting the failure it exists to report.
  // ===========================================================================================

  // Step 20: Step 5 at maximum length. Every one of the 1000 letters is needed, so the answer is
  //          1, and every cell of the 10^6-cell table sits on a matching pair of letters. A
  //          memoized recursion also meets its deepest call stack here, about 2000 frames
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthEqualStringsHaveOneWay() {
    assertThat(sut.numDistinct("a".repeat(1000), "a".repeat(1000))).isEqualTo(1);
  }

  // Step 21: 999 a's followed by one b, against "aaab". The b is forced, and the three a's are any
  //          three of the 999, so C(999, 3) = 165,668,499 ways. Every cell of the table is a
  //          binomial no larger than that, so nothing overflows along the way
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthBinomialAnswer() {
    assertThat(sut.numDistinct("a".repeat(999) + "b", "aaab")).isEqualTo(165_668_499);
  }

  // Step 22: Step 15 at maximum length. In "ab" repeated 500 times the a at index 2i is followed
  //          by 500 - i b's, so "ab" is spelled 500 + 499 + ... + 1 = 125,250 ways, while the
  //          mirror target "ba" finds one a fewer after each b and is spelled 124,750 ways.
  //          Multiplying letter counts answers 250,000 for both
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void alternatingStringCountsOrderedPairsBothWays() {
    String s = "ab".repeat(500);

    assertThat(sut.numDistinct(s, "ab")).isEqualTo(125_250);
    assertThat(sut.numDistinct(s, "ba")).isEqualTo(124_750);
  }

  // Step 23: both strings near maximum length, "ab" repeated 500 times against "ab" repeated 499
  //          times. Exactly two letters are deleted, one a and one b, and what remains still
  //          alternates only when the two were adjacent: any of the 500 "ab" pairs or the 499 "ba"
  //          pairs, so 999 ways. Half of the table's 10^6 cells are on matching letters, which
  //          makes this the fullest workout of the recurrence
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void alternatingStringMissingOnePairHasLinearWays() {
    assertThat(sut.numDistinct("ab".repeat(500), "ab".repeat(499))).isEqualTo(999);
  }

  // Step 24: the full alphabet repeated to length 1000, which is 38 complete copies and a partial
  //          "a" to "l", so 39 blocks each holding one a, one b and one c. Spelling "abc" needs the
  //          blocks of its three letters to be nondecreasing, C(41, 3) = 10,660 ways, while "cba"
  //          needs them strictly increasing, C(39, 3) = 9,139 ways. Letter counts alone cannot tell
  //          the two targets apart: both products are 39^3
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void alphabetRampCountsTriplesInBothDirections() {
    String s = alphabetRamp(1000);

    assertThat(sut.numDistinct(s, "abc")).isEqualTo(10_660);
    assertThat(sut.numDistinct(s, "cba")).isEqualTo(9_139);
  }

  // Step 25: the guarantee that the answer fits a 32-bit int says nothing about the table. Here
  //          the cell for 500 a's chosen from 1000 holds C(1000, 500), about 2.7 * 10^299, far past
  //          long, and the answer is still 0 because s has no b. Plain int addition wraps modulo
  //          2^32 and lands on the exact answer anyway, since the true answer is below 2^31 and the
  //          recurrence only adds. A solution that adds with Math.addExact, or treats a wrapped
  //          negative cell as an error, throws instead of answering
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void intermediateCountsMayOverflowWhileTheAnswerFits() {
    assertThat(sut.numDistinct("a".repeat(1000), "a".repeat(500) + "b")).isZero();
  }

  // ===========================================================================================
  // Hygiene (Step 26). Strings are immutable, so there is no input to check for modification.
  // ===========================================================================================

  // Step 26: several pairs answered by one instance, the largest in the middle and the lengths
  //          deliberately out of order. A solution that keeps its table or row on the instance
  //          instead of resetting it per call answers the later pairs from the earlier ones'
  //          leftovers. The fourth pair is Step 4 at maximum length
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersStringsOfAnyLengthInAnyOrder() {
    assertThat(sut.numDistinct("babgbag", "bag")).isEqualTo(5);
    assertThat(sut.numDistinct("a".repeat(1000), "a".repeat(1000))).isEqualTo(1);
    assertThat(sut.numDistinct("a", "b")).isZero();
    assertThat(sut.numDistinct("a".repeat(999), "a".repeat(1000))).isZero();
    assertThat(sut.numDistinct("abab", "ab")).isEqualTo(3);
    assertThat(sut.numDistinct("rabbbit", "rabbit")).isEqualTo(3);
  }

  /** The lowercase alphabet repeated until the string is n letters long. */
  private static String alphabetRamp(int n) {
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append((char) ('a' + i % 26));
    }
    return sb.toString();
  }
}
