package leetcode.p0201_0300;

import static leetcode.support.ListNodeAssertions.assertListEquals;

import java.util.stream.Stream;
import leetcode.support.ListNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReverseLinkedListTest {
  @ParameterizedTest
  @MethodSource
  void reverseList(ListNode head, ListNode expected) {
    ListNode actual = new ReverseLinkedList().reverseList(head);
    assertListEquals(actual, expected);
  }

  private static Stream<Arguments> reverseList() {
    return Stream.of(
        Arguments.of(ListNode.of(1, 2, 3, 4, 5), ListNode.of(5, 4, 3, 2, 1)),
        Arguments.of(ListNode.of(1, 2, 3, 4, 5), ListNode.of(5, 4, 3, 2, 1)));
  }
}
