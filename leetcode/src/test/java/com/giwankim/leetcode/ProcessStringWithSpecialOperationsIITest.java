package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProcessStringWithSpecialOperationsIITest {
  ProcessStringWithSpecialOperationsII sut = new ProcessStringWithSpecialOperationsII();

  // Step 1: smallest valid input — the single letter sits at its only index, 0.
  // Pins the base case: a lone letter forms a length-1 result and k=0 selects it.
  @Test
  void singleLetterAtIndexZero() {
    assertThat(sut.processStr("a", 0)).isEqualTo('a');
  }

  // Step 2: the defining rule of the II variant — k past the end returns '.', not a letter
  // and not an exception. "a" has length 1, so index 1 is out of bounds.
  @Test
  void indexPastEndReturnsDot() {
    assertThat(sut.processStr("a", 1)).isEqualTo('.');
  }

  // Step 3: letters accumulate left to right, so index 0 is the first letter. Catches an
  // implementation that prepends instead of appends — that bug would yield 'c' here.
  @Test
  void firstIndexIsFirstLetter() {
    assertThat(sut.processStr("abc", 0)).isEqualTo('a');
  }

  // Step 4: and the last index is the last letter. The other half of the order check —
  // a prepend bug would yield 'a' here.
  @Test
  void lastIndexIsLastLetter() {
    assertThat(sut.processStr("abc", 2)).isEqualTo('c');
  }

  // Step 5: k equal to the length is out of bounds (indices are 0..length-1).
  // Pins the boundary at exactly length, the most common off-by-one.
  @Test
  void indexEqualToLengthIsOutOfBounds() {
    assertThat(sut.processStr("abc", 3)).isEqualTo('.');
  }

  // Step 6: '*' removes the last character, leaving 'a' at index 0. Pins delete-from-end.
  @Test
  void starRemovesLastCharacter() {
    assertThat(sut.processStr("ab*", 0)).isEqualTo('a');
  }

  // Step 7: the delete must actually shrink the tracked length — after "ab*" the result is
  // "a" (length 1), so index 1 is now out of bounds. Catches a '*' that returns the right
  // character but forgets to shorten the length used for the bounds check.
  @Test
  void starShrinksLengthSoFormerIndexIsOutOfBounds() {
    assertThat(sut.processStr("ab*", 1)).isEqualTo('.');
  }

  // Step 8: '*' on an empty result is a no-op ("...if it exists"). The result stays empty,
  // so any index is out of bounds. A naive removeLast would underflow instead of no-opping.
  @Test
  void starOnEmptyResultStaysEmpty() {
    assertThat(sut.processStr("*", 0)).isEqualTo('.');
  }

  // Step 9: repeated '*' past empty keeps no-opping rather than driving the length negative.
  @Test
  void repeatedStarsStopAtEmpty() {
    assertThat(sut.processStr("a**", 0)).isEqualTo('.');
  }

  // Step 10: '#' duplicates the result ("ab" -> "abab"); index 0 is unaffected.
  @Test
  void hashDuplicatesResultFirstHalf() {
    assertThat(sut.processStr("ab#", 0)).isEqualTo('a');
  }

  // Step 11: the crux of the backward mapping. Index 2 lives in the *second* copy of "abab"
  // and must fold back (modulus the pre-duplication length) to index 0 of "ab" = 'a'.
  // Catches an implementation that duplicates the last char only ("abb") or mishandles the fold.
  @Test
  void hashSecondHalfFoldsBackToFirstHalf() {
    assertThat(sut.processStr("ab#", 2)).isEqualTo('a');
  }

  // Step 12: the companion fold — index 3 of "abab" maps back to index 1 of "ab" = 'b'.
  @Test
  void hashSecondHalfFoldsBackToSecondCharacter() {
    assertThat(sut.processStr("ab#", 3)).isEqualTo('b');
  }

  // Step 13: the duplicated result has length 4, so index 4 is out of bounds.
  @Test
  void indexPastDuplicatedResultIsOutOfBounds() {
    assertThat(sut.processStr("ab#", 4)).isEqualTo('.');
  }

  // Step 14: '#' on an empty result stays empty (empty + empty), so every index is '.'.
  @Test
  void hashOnEmptyResultStaysEmpty() {
    assertThat(sut.processStr("#", 0)).isEqualTo('.');
  }

  // Step 15: '%' reverses the result ("abc" -> "cba"); index 0 becomes the old last char 'c'.
  // Pins the mirror mapping k -> length-1-k. A no-op '%' would leave 'a' here.
  @Test
  void percentReversesFirstIndexToOldLast() {
    assertThat(sut.processStr("abc%", 0)).isEqualTo('c');
  }

  // Step 16: the companion mirror — index 2 of "cba" is the old first char 'a'.
  @Test
  void percentReversesLastIndexToOldFirst() {
    assertThat(sut.processStr("abc%", 2)).isEqualTo('a');
  }

  // Step 17: reversing a one-character result leaves it unchanged — a boundary for the mirror.
  @Test
  void percentOnSingleCharacterIsUnchanged() {
    assertThat(sut.processStr("a%", 0)).isEqualTo('a');
  }

  // Step 18: '%' on an empty result stays empty, so the index is out of bounds.
  @Test
  void percentOnEmptyResultStaysEmpty() {
    assertThat(sut.processStr("%", 0)).isEqualTo('.');
  }

  // Step 19: reversing twice restores the original order — '%' is its own inverse.
  // "ab" -> "ba" -> "ab", so index 1 is 'b' again.
  @Test
  void doubleReverseRestoresOriginalOrder() {
    assertThat(sut.processStr("ab%%", 1)).isEqualTo('b');
  }

  // Step 20: operations compose on the *current* result, left to right.
  // "ab" -> "abab" (#) -> "baba" (%); index 0 is 'b'. Pins that '%' acts on the
  // already-duplicated string, not on the raw input.
  @Test
  void duplicateThenReverse() {
    assertThat(sut.processStr("ab#%", 0)).isEqualTo('b');
  }

  // Step 21: '*' sees the grown string after '#'. "ab" -> "abab" -> "aba"; index 2 is 'a'.
  @Test
  void starRemovesFromDuplicatedResult() {
    assertThat(sut.processStr("ab#*", 2)).isEqualTo('a');
  }

  // Step 22: appends resume after a mid-sequence delete and a later duplicate.
  // "a" -> "ab" -> "a" (*) -> "ac" -> "acac" (#); index 3 is 'c'.
  @Test
  void appendsResumeAfterMidSequenceDelete() {
    assertThat(sut.processStr("ab*c#", 3)).isEqualTo('c');
  }

  // Step 23: all three special ops chained. "abc" -> "abcabc" (#) -> "abcab" (*)
  // -> "bacba" (%); index 2 is 'c'. Exercises the backward walk through #, *, and % together.
  @Test
  void duplicateThenDeleteThenReverse() {
    assertThat(sut.processStr("abc#*%", 2)).isEqualTo('c');
  }

  // Step 24: that same chain has final length 5, so index 5 is out of bounds.
  @Test
  void outOfBoundsAfterChainedOperations() {
    assertThat(sut.processStr("abc#*%", 5)).isEqualTo('.');
  }

  // Step 25: each '#' doubles the result, so "a####" is 2^4 = 16 'a's; index 15 is the last
  // valid one. Catches duplicating only once or by a fixed snapshot rather than re-doubling.
  @Test
  void repeatedDuplicationLastValidIndex() {
    assertThat(sut.processStr("a####", 15)).isEqualTo('a');
  }

  // Step 26: and index 16 is just past that 16-char result — out of bounds.
  @Test
  void repeatedDuplicationFirstOutOfBoundsIndex() {
    assertThat(sut.processStr("a####", 16)).isEqualTo('.');
  }

  // Step 27: official Example 1. "a#b%*", k=1:
  // a -> "a", # -> "aa", b -> "aab", % -> "baa", * -> "ba"; index 1 is 'a'.
  @Test
  void leetCodeExample1() {
    assertThat(sut.processStr("a#b%*", 1)).isEqualTo('a');
  }

  // Step 28: official Example 2. "cd%#*#", k=3:
  // c -> "c", d -> "cd", % -> "dc", # -> "dcdc", * -> "dcd", # -> "dcddcd"; index 3 is 'd'.
  @Test
  void leetCodeExample2() {
    assertThat(sut.processStr("cd%#*#", 3)).isEqualTo('d');
  }

  // Step 29: official Example 3. "z*#", k=0:
  // z -> "z", * -> "", # -> ""; the result is empty so index 0 is out of bounds -> '.'.
  @Test
  void leetCodeExample3() {
    assertThat(sut.processStr("z*#", 0)).isEqualTo('.');
  }

  // Step 30: the reason II exists. "ab" then 40 '#' is "ab" repeated 2^40 times — length
  // 2^41 (~2.2 trillion). Any solution that materializes the string dies here; the backward
  // walk resolves a large *even* index to 'a' in O(n). 2^40 is even, so it maps to 'a'.
  @Test
  void hugeEvenIndexResolvesWithoutMaterializing() {
    assertThat(sut.processStr("ab" + "#".repeat(40), 1L << 40)).isEqualTo('a');
  }

  // Step 31: the last valid index of that 2^41-length string is 2^41-1 (odd) -> 'b'.
  @Test
  void hugeOddLastIndexResolvesToSecondCharacter() {
    assertThat(sut.processStr("ab" + "#".repeat(40), (1L << 41) - 1)).isEqualTo('b');
  }

  // Step 32: and exactly 2^41 — the length — is out of bounds even at this scale.
  @Test
  void hugeIndexEqualToLengthIsOutOfBounds() {
    assertThat(sut.processStr("ab" + "#".repeat(40), 1L << 41)).isEqualTo('.');
  }
}
