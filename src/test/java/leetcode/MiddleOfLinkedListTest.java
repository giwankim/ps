package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import leetcode.support.ListNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MiddleOfLinkedListTest {

  @ParameterizedTest
  @MethodSource
  void middleNode(ListNode head, ListNode expected) {
    ListNode actual = new MiddleOfLinkedList().middleNode(head);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> middleNode() {
    return Stream.of(
        Arguments.of(ListNode.of(1, 2, 3, 4, 5), ListNode.of(3, 4, 5)),
        Arguments.of(ListNode.of(1, 2, 3, 4, 5, 6), ListNode.of(4, 5, 6)));
  }
}
