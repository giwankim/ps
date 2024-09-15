package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import datatype.ListNode;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PalindromeLinkedListTest {
  @ParameterizedTest
  @MethodSource
  void isPalindrome(ListNode head, boolean expected) {
    boolean actual = new PalindromeLinkedList().isPalindrome(head);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> isPalindrome() {
    return Stream.of(
        Arguments.of(ListNode.createList(1, 2, 2, 1), true),
        Arguments.of(ListNode.createList(1, 2), false));
  }
}
