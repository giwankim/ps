package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import org.junit.jupiter.api.Test;

class ReverseNodesInKGroupTest {
  ReverseNodesInKGroup sut = new ReverseNodesInKGroup();

  @Test
  void singleton() {
    assertThat(sut.reverseKGroup(ListNode.of(1), 1)).isEqualTo(ListNode.of(1));
  }

  @Test
  void kIsOne() {
    assertThat(sut.reverseKGroup(ListNode.of(1, 2, 3), 1)).isEqualTo(ListNode.of(1, 2, 3));
  }

  @Test
  void twoNodesSwapped() {
    assertThat(sut.reverseKGroup(ListNode.of(1, 2), 2)).isEqualTo(ListNode.of(2, 1));
  }

  @Test
  void kIsEqualToN() {
    assertThat(sut.reverseKGroup(ListNode.of(1, 2, 3), 3)).isEqualTo(ListNode.of(3, 2, 1));
  }

  @Test
  void twoGroups() {
    assertThat(sut.reverseKGroup(ListNode.of(1, 2, 3, 4), 2)).isEqualTo(ListNode.of(2, 1, 4, 3));
  }

  @Test
  void kIsDivisorOfN() {
    assertThat(sut.reverseKGroup(ListNode.of(1, 2, 3, 4, 5, 6), 2))
        .isEqualTo(ListNode.of(2, 1, 4, 3, 6, 5));
    assertThat(sut.reverseKGroup(ListNode.of(1, 2, 3, 4, 5, 6), 3))
        .isEqualTo(ListNode.of(3, 2, 1, 6, 5, 4));
  }

  @Test
  void kIsNotADivisorOfN() {
    assertThat(sut.reverseKGroup(ListNode.of(1, 2, 3, 4, 5), 2))
        .isEqualTo(ListNode.of(2, 1, 4, 3, 5));
    assertThat(sut.reverseKGroup(ListNode.of(1, 2, 3, 4, 5), 3))
        .isEqualTo(ListNode.of(3, 2, 1, 4, 5));
  }
}
