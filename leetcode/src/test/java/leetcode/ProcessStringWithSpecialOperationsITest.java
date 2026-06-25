package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProcessStringWithSpecialOperationsITest {
  ProcessStringWithSpecialOperationsI sut = new ProcessStringWithSpecialOperationsI();

  // Step 1: smallest valid input — a single lowercase letter is appended verbatim.
  // Pins the base behavior; everything else builds on "letters append to result".
  @Test
  void singleLetterIsAppended() {
    assertThat(sut.processStr("a")).isEqualTo("a");
  }

  // Step 2: letters accumulate in input order (left to right). Catches an implementation
  // that prepends instead of appends — that bug would yield "cba" here.
  @Test
  void lettersAppendInInputOrder() {
    assertThat(sut.processStr("abc")).isEqualTo("abc");
  }

  // Step 3: '*' removes the last character of result. Pins delete-from-end specifically
  // (a delete-from-front bug would leave "b").
  @Test
  void starRemovesLastCharacter() {
    assertThat(sut.processStr("ab*")).isEqualTo("a");
  }

  // Step 4: '*' on an empty result is a no-op ("...if it exists"). This is the key guard:
  // a naive removeLast would throw StringIndexOutOfBounds / underflow here.
  @Test
  void starOnEmptyResultIsNoOp() {
    assertThat(sut.processStr("*")).isEmpty();
  }

  // Step 5: repeated '*' past empty keeps no-opping rather than underflowing.
  @Test
  void repeatedStarsStopAtEmpty() {
    assertThat(sut.processStr("a**")).isEmpty();
  }

  // Step 6: '#' duplicates the whole result (result = result + result). Catches confusing
  // '#' with reverse (would give "ba") or duplicating only the last char (would give "abb").
  @Test
  void hashDuplicatesResult() {
    assertThat(sut.processStr("ab#")).isEqualTo("abab");
  }

  // Step 7: '#' on an empty result stays empty (empty + empty).
  @Test
  void hashOnEmptyResultStaysEmpty() {
    assertThat(sut.processStr("#")).isEmpty();
  }

  // Step 8: '%' reverses the whole result. Catches treating '%' as a no-op (would give "abc")
  // or as a duplicate (would give "abcabc").
  @Test
  void percentReversesResult() {
    assertThat(sut.processStr("abc%")).isEqualTo("cba");
  }

  // Step 9: reversing a one-character result leaves it unchanged — a sanity boundary for '%'.
  @Test
  void percentOnSingleCharacterIsUnchanged() {
    assertThat(sut.processStr("a%")).isEqualTo("a");
  }

  // Step 10: '%' on an empty result stays empty (reverse of nothing is nothing).
  @Test
  void percentOnEmptyResultStaysEmpty() {
    assertThat(sut.processStr("%")).isEmpty();
  }

  // Step 11: reversing twice restores the original order — '%' is its own inverse.
  @Test
  void doubleReverseRestoresOriginalOrder() {
    assertThat(sut.processStr("ab%%")).isEqualTo("ab");
  }

  // Step 12: '%' acts on the already-duplicated string. "ab" -> "abab" -> reverse -> "baba".
  // Pins that operations apply left-to-right to the *current* result, not the raw input.
  @Test
  void duplicateThenReverse() {
    assertThat(sut.processStr("ab#%")).isEqualTo("baba");
  }

  // Step 13: '*' sees the grown string after '#'. "ab" -> "abab" -> remove last -> "aba".
  @Test
  void starRemovesFromDuplicatedResult() {
    assertThat(sut.processStr("ab#*")).isEqualTo("aba");
  }

  // Step 14: a delete in the middle of the sequence, then more appends and a duplicate.
  // "a" -> "ab" -> "a" -> "ac" -> "acac".
  @Test
  void appendsResumeAfterMidSequenceDelete() {
    assertThat(sut.processStr("ab*c#")).isEqualTo("acac");
  }

  // Step 15: duplicate, delete, then reverse in one chain.
  // "abc" -> "abcabc" -> "abcab" -> reverse -> "bacba".
  @Test
  void duplicateThenDeleteThenReverse() {
    assertThat(sut.processStr("abc#*%")).isEqualTo("bacba");
  }

  // Step 16: each '#' doubles the result, so n hashes after one letter give 2^n copies.
  // "a####" -> 2^4 = 16 'a's. Catches an implementation that duplicates only once or by a
  // fixed snapshot rather than re-doubling the current result each time.
  @Test
  void repeatedDuplicationGrowsExponentially() {
    assertThat(sut.processStr("a####")).isEqualTo("a".repeat(16));
  }

  // Step 17: official Example 1. "a#b%*":
  // a -> "a", # -> "aa", b -> "aab", % -> "baa", * -> "ba".
  @Test
  void leetCodeExample1() {
    assertThat(sut.processStr("a#b%*")).isEqualTo("ba");
  }

  // Step 18: official Example 2. "z*#":
  // z -> "z", * -> "", # -> "" (duplicating empty stays empty).
  @Test
  void leetCodeExample2() {
    assertThat(sut.processStr("z*#")).isEmpty();
  }

  // Step 19: maximum length (20) of plain letters passes straight through unchanged.
  @Test
  void allLettersAtMaxLengthPassThrough() {
    assertThat(sut.processStr("abcdefghijklmnopqrst")).isEqualTo("abcdefghijklmnopqrst");
  }
}
