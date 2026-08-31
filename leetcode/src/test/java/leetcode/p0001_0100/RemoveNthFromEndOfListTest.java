package leetcode.p0001_0100;

import static leetcode.support.ListNodeAssertions.assertListEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import leetcode.support.ListNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RemoveNthFromEndOfListTest {
  RemoveNthFromEndOfList sut = new RemoveNthFromEndOfList();

  @Test
  void removeOnlyNode() {
    assertThat(sut.removeNthFromEnd(ListNode.of(1), 1)).isNull();
  }

  @Test
  void removeTailOfTwoNodeList() {
    assertListEquals(sut.removeNthFromEnd(ListNode.of(1, 2), 1), ListNode.of(1));
  }

  @Test
  void removeHeadOfTwoNodeList() {
    assertListEquals(sut.removeNthFromEnd(ListNode.of(1, 2), 2), ListNode.of(2));
  }

  @Test
  void removeTail() {
    assertListEquals(sut.removeNthFromEnd(ListNode.of(1, 2, 3), 1), ListNode.of(1, 2));
  }

  @Test
  void removeHead() {
    assertListEquals(sut.removeNthFromEnd(ListNode.of(1, 2, 3), 3), ListNode.of(2, 3));
  }

  @ParameterizedTest
  @MethodSource
  void removeMiddleNode(ListNode head, int n, ListNode expected) {
    assertListEquals(sut.removeNthFromEnd(head, n), expected);
  }

  static Stream<Arguments> removeMiddleNode() {
    return Stream.of(
        Arguments.argumentSet(
            "remove second node from end in longer list",
            ListNode.of(1, 2, 3, 4, 5),
            2,
            ListNode.of(1, 2, 3, 5)),
        Arguments.argumentSet(
            "remove third node from end in longer list",
            ListNode.of(1, 2, 3, 4, 5),
            3,
            ListNode.of(1, 2, 4, 5)),
        Arguments.argumentSet(
            "remove fourth node from end in longer list",
            ListNode.of(1, 2, 3, 4, 5),
            4,
            ListNode.of(1, 3, 4, 5)));
  }
}
