package leetcode.p1701_1800;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MergeStringsAlternatelyTest {
  MergeStringsAlternately sut = new MergeStringsAlternately();

  // Step 1: smallest valid input — one character each (1 <= word1.length, word2.length),
  // which pins down the only ordering rule: word1 goes first
  @Test
  void singleCharacterEachStartsWithWord1() {
    assertThat(sut.mergeAlternately("a", "p")).isEqualTo("ap");
  }

  // Step 2: equal lengths — every character pairs up, so nothing is ever appended
  @Test
  void equalLengthsInterleaveWithNoLeftover() {
    assertThat(sut.mergeAlternately("ab", "pq")).isEqualTo("apbq");
  }

  // Step 3: word2 is one longer — its final character trails the interleaved prefix
  @Test
  void word2LongerByOneAppendsItsLastCharacter() {
    assertThat(sut.mergeAlternately("ab", "pqr")).isEqualTo("apbqr");
  }

  // Step 4: the mirror case — word1 is one longer, so word1 supplies the trailing character
  @Test
  void word1LongerByOneAppendsItsLastCharacter() {
    assertThat(sut.mergeAlternately("abc", "pq")).isEqualTo("apbqc");
  }

  // Step 5: alternation stops after a single pair — the rest of word2 is appended verbatim
  @Test
  void restOfWord2TrailsWhenWord1HasOneCharacter() {
    assertThat(sut.mergeAlternately("a", "pqrs")).isEqualTo("apqrs");
  }

  // Step 6: the mirror case — the rest of word1 is appended verbatim, still in its original order
  @Test
  void restOfWord1TrailsWhenWord2HasOneCharacter() {
    assertThat(sut.mergeAlternately("abcd", "p")).isEqualTo("apbcd");
  }

  // Step 7: merging is positional, not value based — identical words double every character
  @Test
  void identicalWordsDoubleEveryCharacter() {
    assertThat(sut.mergeAlternately("abc", "abc")).isEqualTo("aabbcc");
  }

  // Step 8: repeated letters within a word do not collapse or reorder
  @Test
  void repeatedLettersKeepTheirPositions() {
    assertThat(sut.mergeAlternately("aaa", "bbb")).isEqualTo("ababab");
  }

  // Step 9: LeetCode Example 1 — equal lengths, a b c interleaved with p q r
  @Test
  void leetCodeExample1() {
    assertThat(sut.mergeAlternately("abc", "pqr")).isEqualTo("apbqcr");
  }

  // Step 10: LeetCode Example 2 — word2 is longer, so "rs" lands at the end
  @Test
  void leetCodeExample2() {
    assertThat(sut.mergeAlternately("ab", "pqrs")).isEqualTo("apbqrs");
  }

  // Step 11: LeetCode Example 3 — word1 is longer, so "cd" lands at the end
  @Test
  void leetCodeExample3() {
    assertThat(sut.mergeAlternately("abcd", "pq")).isEqualTo("apbqcd");
  }

  // Step 12: upper constraint bound with equal lengths — 100 + 100 characters interleave
  // into 200, so no character is dropped or duplicated at scale
  @Test
  void maximumEqualLengthsInterleaveFully() {
    String word1 = "a".repeat(100);
    String word2 = "b".repeat(100);
    assertThat(sut.mergeAlternately(word1, word2)).isEqualTo("ab".repeat(100)).hasSize(200);
  }

  // Step 13: widest possible length gap — one pair, then a 99 character tail from word1
  @Test
  void maximumLengthGapAppendsTheLongTail() {
    String word1 = "a".repeat(100);
    assertThat(sut.mergeAlternately(word1, "z"))
        .isEqualTo("az" + "a".repeat(99))
        .hasSize(101);
  }
}
