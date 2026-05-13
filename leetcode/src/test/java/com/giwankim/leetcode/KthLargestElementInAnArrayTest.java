package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KthLargestElementInAnArrayTest {
  KthLargestElementInAnArray sut = new KthLargestElementInAnArray();

  @Test
  void distinctElements() {
    assertThat(sut.findKthLargest(new int[] {3, 2, 1, 5, 6, 4}, 2)).isEqualTo(5);
    assertThat(sut.findKthLargest2(new int[] {3, 2, 1, 5, 6, 4}, 2)).isEqualTo(5);
  }

  @Test
  void repeatedElements() {
    assertThat(sut.findKthLargest(new int[] {3, 2, 3, 1, 2, 4, 5, 5, 6}, 4)).isEqualTo(4);
    assertThat(sut.findKthLargest2(new int[] {3, 2, 3, 1, 2, 4, 5, 5, 6}, 4)).isEqualTo(4);
  }

  @Test
  void singleElement() {
    assertThat(sut.findKthLargest(new int[] {7}, 1)).isEqualTo(7);
    assertThat(sut.findKthLargest2(new int[] {7}, 1)).isEqualTo(7);
  }

  @Test
  void kEqualsOneReturnsMaximum() {
    assertThat(sut.findKthLargest(new int[] {3, 2, 1, 5, 6, 4}, 1)).isEqualTo(6);
    assertThat(sut.findKthLargest2(new int[] {3, 2, 1, 5, 6, 4}, 1)).isEqualTo(6);
  }

  @Test
  void kEqualsLengthReturnsMinimum() {
    assertThat(sut.findKthLargest(new int[] {3, 2, 1, 5, 6, 4}, 6)).isEqualTo(1);
    assertThat(sut.findKthLargest2(new int[] {3, 2, 1, 5, 6, 4}, 6)).isEqualTo(1);
  }

  @Test
  void allNegativeNumbers() {
    assertThat(sut.findKthLargest(new int[] {-1, -5, -3, -2, -4}, 2)).isEqualTo(-2);
    assertThat(sut.findKthLargest2(new int[] {-1, -5, -3, -2, -4}, 2)).isEqualTo(-2);
  }

  @Test
  void allEqualElements() {
    assertThat(sut.findKthLargest(new int[] {7, 7, 7, 7, 7}, 3)).isEqualTo(7);
    assertThat(sut.findKthLargest2(new int[] {7, 7, 7, 7, 7}, 3)).isEqualTo(7);
  }

  @Test
  void kthLargestIsDuplicatedValue() {
    assertThat(sut.findKthLargest(new int[] {1, 2, 3, 3, 3, 4, 5}, 4)).isEqualTo(3);
    assertThat(sut.findKthLargest2(new int[] {1, 2, 3, 3, 3, 4, 5}, 4)).isEqualTo(3);
  }

  @Test
  void alreadySortedAscending() {
    assertThat(sut.findKthLargest(new int[] {1, 2, 3, 4, 5, 6, 7}, 3)).isEqualTo(5);
    assertThat(sut.findKthLargest2(new int[] {1, 2, 3, 4, 5, 6, 7}, 3)).isEqualTo(5);
  }

  @Test
  void alreadySortedDescending() {
    assertThat(sut.findKthLargest(new int[] {7, 6, 5, 4, 3, 2, 1}, 3)).isEqualTo(5);
    assertThat(sut.findKthLargest2(new int[] {7, 6, 5, 4, 3, 2, 1}, 3)).isEqualTo(5);
  }

  @Test
  void boundaryValueExtremes() {
    assertThat(sut.findKthLargest(new int[] {10000, -10000, 0, 10000, -10000}, 2))
        .isEqualTo(10000);
    assertThat(sut.findKthLargest2(new int[] {10000, -10000, 0, 10000, -10000}, 2))
        .isEqualTo(10000);
  }
}
