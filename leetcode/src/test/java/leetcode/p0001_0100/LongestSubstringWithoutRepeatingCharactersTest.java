package leetcode.p0001_0100;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LongestSubstringWithoutRepeatingCharactersTest {
  LongestSubstringWithoutRepeatingCharacters sut = new LongestSubstringWithoutRepeatingCharacters();

  // Step 1: smallest valid input — the empty string is in range (0 <= s.length)
  @Test
  void emptyStringReturnsZero() {
    assertThat(sut.lengthOfLongestSubstring("")).isZero();
  }

  // Step 2: one character is its own longest duplicate-free substring
  @Test
  void singleCharacterReturnsOne() {
    assertThat(sut.lengthOfLongestSubstring("a")).isOne();
  }

  // Step 3: a space is an ordinary character, not a separator or a trimmed edge
  @Test
  void singleSpaceReturnsOne() {
    assertThat(sut.lengthOfLongestSubstring(" ")).isOne();
  }

  // Step 4: two different characters both fit in the window
  @Test
  void twoDistinctCharactersReturnsTwo() {
    assertThat(sut.lengthOfLongestSubstring("ab")).isEqualTo(2);
  }

  // Step 5: the mirror case — a repeat admits only one of the two
  @Test
  void twoIdenticalCharactersReturnsOne() {
    assertThat(sut.lengthOfLongestSubstring("aa")).isOne();
  }

  // Step 6: with no repeats at all, the whole string is the answer
  @Test
  void allDistinctCharactersReturnsFullLength() {
    assertThat(sut.lengthOfLongestSubstring("abcdef")).isEqualTo(6);
  }

  // Step 7: the opposite extreme — every character repeats (LeetCode Example 2)
  @Test
  void allIdenticalCharactersReturnsOne() {
    assertThat(sut.lengthOfLongestSubstring("bbbbb")).isOne();
  }

  // Step 8: a repeat at the front must not poison the rest of the scan
  @Test
  void leadingDuplicateStillCountsTheTail() {
    assertThat(sut.lengthOfLongestSubstring("aab")).isEqualTo(2);
  }

  // Step 9: after 'a' repeats the window drops the stale 'a' but keeps 'b', so "bac" survives
  @Test
  void duplicateOutsideTheWindowIsIgnored() {
    assertThat(sut.lengthOfLongestSubstring("abac")).isEqualTo(3);
  }

  // Step 10: the answer sits at the front and every later window is shorter (LeetCode Example 1)
  @Test
  void answerAtTheStartSurvivesLaterRepeats() {
    assertThat(sut.lengthOfLongestSubstring("abcabcbb")).isEqualTo(3);
  }

  // Step 11: the answer is the interior "wke" — the longer "pwke" is a subsequence,
  // not a substring, so contiguity must be enforced (LeetCode Example 3)
  @Test
  void answerInTheMiddleMustStayContiguous() {
    assertThat(sut.lengthOfLongestSubstring("pwwkew")).isEqualTo(3);
  }

  // Step 12: the best window is the last one — "vdf" — so the maximum has to be
  // re-checked after the final character, not only when the window shrinks
  @Test
  void answerEndingAtTheLastCharacter() {
    assertThat(sut.lengthOfLongestSubstring("dvdf")).isEqualTo(3);
  }

  // Step 13: after "bb" the window starts at index 2; the 'a' repeating at index 3 last
  // occurred at index 0, so the left boundary must never be dragged backwards
  @Test
  void leftBoundaryNeverMovesBackwards() {
    assertThat(sut.lengthOfLongestSubstring("abba")).isEqualTo(2);
  }

  // Step 14: the trailing 't' repeats one that already fell out of the window,
  // which must not shrink "mzuxt" any further
  @Test
  void staleDuplicateDoesNotShrinkTheWindow() {
    assertThat(sut.lengthOfLongestSubstring("tmmzuxt")).isEqualTo(5);
  }

  // Step 15: comparison is case-sensitive — 'a' and 'A' are different characters
  @Test
  void uppercaseAndLowercaseAreDistinct() {
    assertThat(sut.lengthOfLongestSubstring("aAbB")).isEqualTo(4);
  }

  // Step 16: digits are in the allowed alphabet — "1234" is the longest run
  @Test
  void digitsAreDistinctCharacters() {
    assertThat(sut.lengthOfLongestSubstring("1231234")).isEqualTo(4);
  }

  // Step 17: all four constraint classes at once — letter, uppercase, digit, symbol, space
  @Test
  void mixedLettersDigitsSymbolsAndSpaces() {
    assertThat(sut.lengthOfLongestSubstring("aA1! ")).isEqualTo(5);
  }

  // Step 18: a repeated space breaks the window exactly like a repeated letter
  @Test
  void repeatedSpaceBreaksTheWindow() {
    assertThat(sut.lengthOfLongestSubstring("a b c")).isEqualTo(3);
  }

  // Step 19: symbols repeat like anything else — the window slides past the first '!'
  @Test
  void repeatedSymbolBreaksTheWindow() {
    assertThat(sut.lengthOfLongestSubstring("!@#$!")).isEqualTo(4);
  }

  // Step 20: upper constraint bound (10^5) with a single repeated character
  @Test
  void maximumLengthAllIdenticalReturnsOne() {
    assertThat(sut.lengthOfLongestSubstring("a".repeat(100_000))).isOne();
  }

  // Step 21: upper constraint bound where the window shrinks at every step —
  // the worst case for the left pointer, which must stay amortized O(n)
  @Test
  void maximumLengthAlternatingPairReturnsTwo() {
    assertThat(sut.lengthOfLongestSubstring("ab".repeat(50_000))).isEqualTo(2);
  }

  // Step 22: upper constraint bound holding a wide window — the alphabet cycles, so the
  // answer is the full 26 letters and the window carries 26 characters the whole way
  @Test
  void maximumLengthAlphabetCycleReturnsTwentySix() {
    assertThat(sut.lengthOfLongestSubstring("abcdefghijklmnopqrstuvwxyz".repeat(3_846)))
        .isEqualTo(26);
  }
}
