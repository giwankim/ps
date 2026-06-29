package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class NumberOfStringsThatAppearAsSubstringsInWordTest {
  NumberOfStringsThatAppearAsSubstringsInWord sut =
      new NumberOfStringsThatAppearAsSubstringsInWord();

  // Step 1: smallest valid input — one length-1 pattern equal to a length-1 word
  // (1 <= patterns.length, patterns[i].length, word.length); a string is a substring of itself
  @Test
  void singlePatternEqualToWordCounts() {
    assertThat(sut.numOfStrings(new String[] {"a"}, "a")).isEqualTo(1);
  }

  // Step 2: the only pattern is absent from the word — nothing to count
  @Test
  void singlePatternAbsentFromWordCountsNothing() {
    assertThat(sut.numOfStrings(new String[] {"b"}, "a")).isEqualTo(0);
  }

  // Step 3: a pattern longer than the word can never be a substring of it
  @Test
  void patternLongerThanWordNeverMatches() {
    assertThat(sut.numOfStrings(new String[] {"abc"}, "ab")).isEqualTo(0);
  }

  // Step 4: the match need not be a prefix — an interior contiguous slice counts
  @Test
  void patternMatchingInteriorSliceCounts() {
    assertThat(sut.numOfStrings(new String[] {"cd"}, "abcde")).isEqualTo(1);
  }

  // Step 5: "substring" means contiguous — 'a','c','e' all occur but not back-to-back, so no match
  @Test
  void nonContiguousCharactersDoNotMatch() {
    assertThat(sut.numOfStrings(new String[] {"ace"}, "abcde")).isEqualTo(0);
  }

  // Step 6: a pattern equal to the whole word matches it exactly
  @Test
  void patternEqualToWholeWordMatches() {
    assertThat(sut.numOfStrings(new String[] {"abc"}, "abc")).isEqualTo(1);
  }

  // Step 7: LeetCode Example 3 — duplicate patterns are each counted independently
  @Test
  void leetCodeExample3DuplicatePatternsCountSeparately() {
    assertThat(sut.numOfStrings(new String[] {"a", "a", "a"}, "ab")).isEqualTo(3);
  }

  // Step 8: every pattern is a substring — the count equals patterns.length
  @Test
  void allPatternsMatching() {
    assertThat(sut.numOfStrings(new String[] {"a", "ab", "abc"}, "abc")).isEqualTo(3);
  }

  // Step 9: no pattern is a substring — the count is zero
  @Test
  void noPatternsMatching() {
    assertThat(sut.numOfStrings(new String[] {"x", "y", "z"}, "abc")).isEqualTo(0);
  }

  // Step 10: LeetCode Example 1 — "a", "abc", "bc" match; "d" does not
  @Test
  void leetCodeExample1() {
    assertThat(sut.numOfStrings(new String[] {"a", "abc", "bc", "d"}, "abc")).isEqualTo(3);
  }

  // Step 11: LeetCode Example 2 — repeated letters in the word; "a" and "b" match, "c" does not
  @Test
  void leetCodeExample2() {
    assertThat(sut.numOfStrings(new String[] {"a", "b", "c"}, "aaaaabbbbb")).isEqualTo(2);
  }

  // Step 12: upper constraint bounds — 100 patterns each of length 100 against a length-100 word,
  // all equal to the word, so every one matches and the count reaches patterns.length
  @Test
  void maximumSizeInputCountsEveryMatch() {
    String full = "a".repeat(100);
    String[] patterns = new String[100];
    Arrays.fill(patterns, full);
    assertThat(sut.numOfStrings(patterns, full)).isEqualTo(100);
  }
}
