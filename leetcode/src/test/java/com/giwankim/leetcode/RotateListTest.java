package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RotateListTest {
  RotateList sut = new RotateList();

  @Test
  void emptyList() {
    assertThat(sut.rotateRight(null, 1)).isNull();
  }

  @Test
  void singleton() {
    assertThat(sut.rotateRight(ListNode.of(1), 1)).isEqualTo(ListNode.of(1));
    assertThat(sut.rotateRight(ListNode.of(1), 2)).isEqualTo(ListNode.of(1));
  }

  @Test
  void noRotation() {
    assertThat(sut.rotateRight(ListNode.of(1, 2, 3), 0)).isEqualTo(ListNode.of(1, 2, 3));
  }

  @ParameterizedTest
  @MethodSource
  void rotateRight(ListNode head, int k, ListNode expected) {
    assertThat(sut.rotateRight(head, k)).isEqualTo(expected);
  }

  static Stream<Arguments> rotateRight() {
    return Stream.of(
        Arguments.argumentSet(
            "rotate 1 times", ListNode.of(1, 2, 3, 4, 5), 1, ListNode.of(5, 1, 2, 3, 4)),
        Arguments.argumentSet(
            "rotate 2 times", ListNode.of(1, 2, 3, 4, 5), 2, ListNode.of(4, 5, 1, 2, 3)),
        Arguments.argumentSet(
            "rotate 3 times", ListNode.of(1, 2, 3, 4, 5), 3, ListNode.of(3, 4, 5, 1, 2)),
        Arguments.argumentSet(
            "rotate 4 times", ListNode.of(1, 2, 3, 4, 5), 4, ListNode.of(2, 3, 4, 5, 1)),
        Arguments.argumentSet(
            "rotate 5 times", ListNode.of(1, 2, 3, 4, 5), 5, ListNode.of(1, 2, 3, 4, 5)));
  }

  @Test
  void rotateMoreThenLength() {
    ListNode head = ListNode.of(0, 1, 2);
    assertThat(sut.rotateRight(head, 4)).isEqualTo(ListNode.of(2, 0, 1));
  }
}
