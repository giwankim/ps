package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RemoveDuplicatesFromSortedListIITest {
  RemoveDuplicatesFromSortedListII sut = new RemoveDuplicatesFromSortedListII();

  // Step 1: Base case — null input
  @Test
  void emptyList() {
    assertThat(sut.deleteDuplicates(null)).isNull();
  }

  // Step 2: Single node — no duplicates possible
  @Test
  void singleton() {
    assertThat(sut.deleteDuplicates(ListNode.of(1))).isEqualTo(ListNode.of(1));
  }

  // Step 3: Two distinct nodes — drives basic traversal
  @Test
  void twoDistinct() {
    assertThat(sut.deleteDuplicates(ListNode.of(1, 2))).isEqualTo(ListNode.of(1, 2));
  }

  // Step 4: Two-node duplicate — simplest case that forces duplicate detection and full removal
  @Test
  void twoNodeDuplicate() {
    assertThat(sut.deleteDuplicates(ListNode.of(1, 1))).isNull();
  }

  // Step 5: All distinct, longer list — validates traversal loop
  @Test
  void distinctElements() {
    assertThat(sut.deleteDuplicates(ListNode.of(1, 2, 3))).isEqualTo(ListNode.of(1, 2, 3));
  }

  // Step 6: Duplicates at head — drives head-change handling (dummy node)
  @Test
  void duplicatesAtHead() {
    assertThat(sut.deleteDuplicates(ListNode.of(1, 1, 2))).isEqualTo(ListNode.of(2));
  }

  // Step 7: Duplicates at tail — drives tail reconnection and tail.next = null
  @Test
  void duplicatesAtTail() {
    assertThat(sut.deleteDuplicates(ListNode.of(1, 2, 2))).isEqualTo(ListNode.of(1));
  }

  // Step 8: Duplicates in middle — drives skip-and-reconnect
  @Test
  void duplicatesInMiddle() {
    assertThat(sut.deleteDuplicates(ListNode.of(1, 2, 2, 3))).isEqualTo(ListNode.of(1, 3));
  }

  // Step 9: Adjacent duplicate groups — ensures loop restarts correctly after skipping
  @Test
  void adjacentDuplicateGroups() {
    assertThat(sut.deleteDuplicates(ListNode.of(1, 1, 2, 2, 3))).isEqualTo(ListNode.of(3));
  }

  // Step 10: Multiple scattered duplicate groups — general case
  @ParameterizedTest
  @MethodSource
  void multipleDuplicateGroups(ListNode head, ListNode expected) {
    assertThat(sut.deleteDuplicates(head)).isEqualTo(expected);
  }

  static Stream<Arguments> multipleDuplicateGroups() {
    return Stream.of(
        Arguments.argumentSet(
            "duplicates in middle", ListNode.of(1, 2, 3, 3, 4, 4, 5), ListNode.of(1, 2, 5)),
        Arguments.argumentSet(
            "duplicates at head and middle", ListNode.of(1, 1, 1, 2, 3), ListNode.of(2, 3)),
        Arguments.argumentSet(
            "duplicates at middle and tail", ListNode.of(1, 2, 3, 3, 3), ListNode.of(1, 2)));
  }

  // Step 11: All duplicates — entire list removed
  @ParameterizedTest
  @MethodSource
  void allDuplicates(ListNode head) {
    assertThat(sut.deleteDuplicates(head)).isNull();
  }

  static Stream<Arguments> allDuplicates() {
    return Stream.of(
        Arguments.argumentSet("single group", ListNode.of(1, 1, 1)),
        Arguments.argumentSet("two groups", ListNode.of(1, 1, 2, 2)));
  }
}
