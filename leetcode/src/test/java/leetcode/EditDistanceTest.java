package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EditDistanceTest {
  EditDistance sut = new EditDistance();

  // Step 1: smallest valid input — both words may be empty (0 <= word1.length, word2.length)
  @Test
  void bothWordsEmptyReturnsZero() {
    assertThat(sut.minDistance("", "")).isEqualTo(0);
  }

  // Step 2: no operations are needed when the words already match
  @Test
  void identicalWordsReturnZero() {
    assertThat(sut.minDistance("abc", "abc")).isEqualTo(0);
  }

  // Step 3: building up from nothing costs one insert per character of the target
  @Test
  void emptyFirstWordCostsOneInsertPerCharacter() {
    assertThat(sut.minDistance("", "abc")).isEqualTo(3);
  }

  // Step 4: the mirror case — emptying a word costs one delete per character
  @Test
  void emptySecondWordCostsOneDeletePerCharacter() {
    assertThat(sut.minDistance("abc", "")).isEqualTo(3);
  }

  // Step 5: the replace operation in isolation
  @Test
  void singleCharacterMismatchCostsOneReplace() {
    assertThat(sut.minDistance("a", "b")).isEqualTo(1);
  }

  // Step 6: the insert operation in isolation
  @Test
  void appendingOneCharacterCostsOneInsert() {
    assertThat(sut.minDistance("ab", "abc")).isEqualTo(1);
  }

  // Step 7: the delete operation in isolation
  @Test
  void removingOneCharacterCostsOneDelete() {
    assertThat(sut.minDistance("abc", "ab")).isEqualTo(1);
  }

  // Step 8: an insert can land in the middle, not only at the end
  @Test
  void insertingInTheMiddleCostsOne() {
    assertThat(sut.minDistance("ac", "abc")).isEqualTo(1);
  }

  // Step 9: replace is its own operation costing 1, not a delete plus an insert costing 2
  @Test
  void replaceBeatsDeleteThenInsert() {
    assertThat(sut.minDistance("abc", "abd")).isEqualTo(1);
  }

  // Step 10: there is no swap operation — transposing two characters costs two replaces,
  // so this stays 2 rather than the 1 that a Damerau-style distance would report
  @Test
  void transposedCharactersCostTwoNotOne() {
    assertThat(sut.minDistance("ab", "ba")).isEqualTo(2);
  }

  // Step 11: a matching prefix and suffix are both free; only the middle differs
  @Test
  void matchingPrefixAndSuffixAreFree() {
    assertThat(sut.minDistance("abcdef", "abcxef")).isEqualTo(1);
  }

  // Step 12: equal-length words sharing no characters need one replace per position
  @Test
  void disjointWordsOfEqualLengthCostThatLength() {
    assertThat(sut.minDistance("abc", "xyz")).isEqualTo(3);
  }

  // Step 13: disjoint words of unequal length cost the longer length —
  // two replaces to cover the overlap, then two inserts for the tail
  @Test
  void disjointWordsOfUnequalLengthCostTheLongerLength() {
    assertThat(sut.minDistance("ab", "wxyz")).isEqualTo(4);
  }

  // Step 14: with a repeated character the answer is just the length difference
  @Test
  void repeatedCharactersCostOnlyTheLengthDifference() {
    assertThat(sut.minDistance("aaaa", "aa")).isEqualTo(2);
  }

  // Step 15: duplicates spread across two runs still resolve to two deletes
  @Test
  void duplicatesInSeparateRunsCostTwoDeletes() {
    assertThat(sut.minDistance("aabb", "ab")).isEqualTo(2);
  }

  // Step 16: LeetCode Example 1 — replace 'h' with 'r', then remove 'r' and 'e'
  @Test
  void leetCodeExample1() {
    assertThat(sut.minDistance("horse", "ros")).isEqualTo(3);
  }

  // Step 17: LeetCode Example 2 — one delete, three replaces and one insert
  @Test
  void leetCodeExample2() {
    assertThat(sut.minDistance("intention", "execution")).isEqualTo(5);
  }

  // Step 18: the textbook Levenshtein pair — replace 'k', replace 'e', insert 'g'
  @Test
  void kittenToSittingCostsThree() {
    assertThat(sut.minDistance("kitten", "sitting")).isEqualTo(3);
  }

  // Step 19: an optimal edit script can mix operations — two inserts and one replace
  @Test
  void mixedInsertsAndReplacesFindTheCheapestScript() {
    assertThat(sut.minDistance("sunday", "saturday")).isEqualTo(3);
  }

  // Step 20: insert and delete are duals of each other, so the distance is symmetric
  @Test
  void distanceIsSymmetricInItsArguments() {
    assertThat(sut.minDistance("ros", "horse")).isEqualTo(3);
    assertThat(sut.minDistance("execution", "intention")).isEqualTo(5);
  }

  // Step 21: upper constraint bound (500) with nothing in common — one replace per position
  @Test
  void maximumLengthDisjointWordsCostFiveHundred() {
    assertThat(sut.minDistance("a".repeat(500), "b".repeat(500))).isEqualTo(500);
  }

  // Step 22: upper constraint bound against an empty word — 500 deletes
  @Test
  void maximumLengthWordAgainstEmptyCostsFiveHundred() {
    assertThat(sut.minDistance("a".repeat(500), "")).isEqualTo(500);
  }

  // Step 23: the largest inputs must not cost anything when they already match
  @Test
  void maximumLengthIdenticalWordsReturnZero() {
    assertThat(sut.minDistance("a".repeat(500), "a".repeat(500))).isEqualTo(0);
  }
}
