package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaximumLengthSubstringWithTwoOccurrencesTest {
  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

  MaximumLengthSubstringWithTwoOccurrences sut = new MaximumLengthSubstringWithTwoOccurrences();

  // Step 1: smallest valid input — the constraint floor is 2 <= s.length, so there is
  // no empty or single-character case; two distinct characters both fit
  @Test
  void twoDistinctCharactersReturnsTwo() {
    assertThat(sut.maximumLengthSubstring("ab")).isEqualTo(2);
  }

  // Step 2: the mirror case — "at most two" allows a pair, so a repeat is still legal
  @Test
  void twoIdenticalCharactersReturnsTwo() {
    assertThat(sut.maximumLengthSubstring("aa")).isEqualTo(2);
  }

  // Step 3: the third occurrence is the first thing that can ever shrink a window
  @Test
  void thirdOccurrenceForcesTheWindowToShrink() {
    assertThat(sut.maximumLengthSubstring("aaa")).isEqualTo(2);
  }

  // Step 4: more of the same character adds nothing (LeetCode Example 2)
  @Test
  void allIdenticalCharactersReturnsTwo() {
    assertThat(sut.maximumLengthSubstring("aaaa")).isEqualTo(2);
  }

  // Step 5: with no character repeating at all, the whole string is the answer
  @Test
  void allDistinctCharactersReturnsFullLength() {
    assertThat(sut.maximumLengthSubstring("abcde")).isEqualTo(5);
  }

  // Step 6: the budget is per character, not per window — four characters are fine
  // as long as neither one appears a third time
  @Test
  void everyCharacterExactlyTwiceReturnsFullLength() {
    assertThat(sut.maximumLengthSubstring("aabb")).isEqualTo(4);
  }

  // Step 7: the same rule with a third character — budgets never pool
  @Test
  void separateCharactersDoNotShareTheBudget() {
    assertThat(sut.maximumLengthSubstring("aabbcc")).isEqualTo(6);
  }

  // Step 8: the two allowed occurrences need not be adjacent
  @Test
  void interleavedPairsReturnFullLength() {
    assertThat(sut.maximumLengthSubstring("abab")).isEqualTo(4);
  }

  // Step 9: an overflowing run at the front must not poison the tail — "aab" survives
  @Test
  void leadingOverflowStillCountsTheTail() {
    assertThat(sut.maximumLengthSubstring("aaab")).isEqualTo(3);
  }

  // Step 10: the mirror — an overflowing run at the end must not discard the prefix,
  // so the maximum seen before the final shrink has to be remembered
  @Test
  void trailingOverflowStillCountsThePrefix() {
    assertThat(sut.maximumLengthSubstring("baaa")).isEqualTo(3);
  }

  // Step 11: counts must be decremented as the left boundary advances; a stale count
  // would leave 'a' permanently over budget and collapse every later window
  @Test
  void countsAreDecrementedAsTheWindowShrinks() {
    assertThat(sut.maximumLengthSubstring("aabaa")).isEqualTo(3);
  }

  // Step 12: shrink by the minimum — after "aaa" the boundary moves one step to index 1,
  // not past the violation, so the window keeps the earlier 'a' and reaches "aabcdef"
  @Test
  void windowShrinksOnlyAsFarAsNeeded() {
    assertThat(sut.maximumLengthSubstring("aaabcdef")).isEqualTo(7);
  }

  // Step 13: one shrink can evict characters that were never over budget — the leading
  // 'c' is dropped on the way to the offending 'a', so shrinking must loop, not step once
  @Test
  void shrinkingEvictsCharactersThatWereNotOverBudget() {
    assertThat(sut.maximumLengthSubstring("cabaa")).isEqualTo(4);
  }

  // Step 14: the run "bbb" forces repeated shrinks and the answer is the trailing
  // "bcba", found only after the window recovers (LeetCode Example 1)
  @Test
  void repeatedRunsStillYieldTheTrailingWindow() {
    assertThat(sut.maximumLengthSubstring("bcbbbcba")).isEqualTo(4);
  }

  // Step 15: the best window "aabcde" touches neither end — it starts after the leading
  // run and stops before the trailing one
  @Test
  void answerInTheMiddleBeatsBothEnds() {
    assertThat(sut.maximumLengthSubstring("aaabcdeaaa")).isEqualTo(6);
  }

  // Step 16: the best window "babc" is the last one, so the maximum must be re-checked
  // after the final character, not only when the window shrinks
  @Test
  void answerEndingAtTheLastCharacter() {
    assertThat(sut.maximumLengthSubstring("bbbabc")).isEqualTo(4);
  }

  // Step 17: every letter appears three times, so the window slides at a fixed width of
  // 6 — the left boundary advances once per overflow and never jumps backwards
  @Test
  void cyclingPatternKeepsTheWindowWidthSteady() {
    assertThat(sut.maximumLengthSubstring("abcabcabc")).isEqualTo(6);
  }

  // Step 18: every letter exactly twice — 52 is the longest window any input can have,
  // since 53 lowercase letters must repeat one of them three times
  @Test
  void everyLetterTwiceReturnsFiftyTwo() {
    assertThat(sut.maximumLengthSubstring(ALPHABET.repeat(2))).isEqualTo(52);
  }

  // Step 19: upper constraint bound (100) with a single repeated character
  @Test
  void maximumLengthAllIdenticalReturnsTwo() {
    assertThat(sut.maximumLengthSubstring("a".repeat(100))).isEqualTo(2);
  }

  // Step 20: upper constraint bound where the window overflows at every step —
  // the worst case for the left pointer, which must stay amortized O(n)
  @Test
  void maximumLengthAlternatingPairReturnsFour() {
    assertThat(sut.maximumLengthSubstring("ab".repeat(50))).isEqualTo(4);
  }

  // Step 21: upper constraint bound holding the widest possible window — the alphabet
  // cycles, so the answer is the pigeonhole ceiling of 52 rather than the full 100
  @Test
  void maximumLengthAlphabetCycleReturnsFiftyTwo() {
    assertThat(sut.maximumLengthSubstring(ALPHABET.repeat(3) + ALPHABET.substring(0, 22)))
        .isEqualTo(52);
  }
}
