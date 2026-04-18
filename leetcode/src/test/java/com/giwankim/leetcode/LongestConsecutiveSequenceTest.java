package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LongestConsecutiveSequenceTest {
  LongestConsecutiveSequence sut = new LongestConsecutiveSequence();

  @Test
  void shouldReturnZeroForEmptyArray() {
    assertThat(sut.longestConsecutive(new int[] {})).isZero();
    assertThat(sut.longestConsecutive2(new int[] {})).isZero();
  }

  @Test
  void shouldReturnOneForSingleElement() {
    assertThat(sut.longestConsecutive(new int[] {0})).isEqualTo(1);
    assertThat(sut.longestConsecutive2(new int[] {0})).isEqualTo(1);
  }

  @Test
  void shouldReturnOneForNonConsecutiveElements() {
    assertThat(sut.longestConsecutive(new int[] {10, 30})).isEqualTo(1);
    assertThat(sut.longestConsecutive2(new int[] {10, 30})).isEqualTo(1);
  }

  @Test
  void shouldReturnTwoForConsecutivePair() {
    assertThat(sut.longestConsecutive(new int[] {1, 2})).isEqualTo(2);
    assertThat(sut.longestConsecutive2(new int[] {1, 2})).isEqualTo(2);
  }

  @Test
  void shouldFindLongestSequenceAmongMultipleSequences() {
    assertThat(sut.longestConsecutive(new int[] {100, 4, 200, 1, 3, 2})).isEqualTo(4);
    assertThat(sut.longestConsecutive2(new int[] {100, 4, 200, 1, 3, 2})).isEqualTo(4);
  }

  @Test
  void shouldHandleLongConsecutiveSequence() {
    assertThat(sut.longestConsecutive(new int[] {0, 3, 7, 2, 5, 8, 4, 6, 0, 1})).isEqualTo(9);
    assertThat(sut.longestConsecutive2(new int[] {0, 3, 7, 2, 5, 8, 4, 6, 0, 1}))
        .isEqualTo(9);
  }

  @Test
  void shouldIgnoreDuplicates() {
    assertThat(sut.longestConsecutive(new int[] {1, 2, 0, 1})).isEqualTo(3);
    assertThat(sut.longestConsecutive2(new int[] {1, 2, 0, 1})).isEqualTo(3);
  }

  @Test
  void shouldHandleNegativeNumbers() {
    assertThat(sut.longestConsecutive(new int[] {-1, 0, 1})).isEqualTo(3);
    assertThat(sut.longestConsecutive2(new int[] {-1, 0, 1})).isEqualTo(3);
  }
}
