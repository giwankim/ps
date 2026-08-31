package leetcode.p0001_0100;

import static leetcode.support.ListNodeAssertions.assertListEquals;

import java.util.stream.Stream;
import leetcode.support.ListNode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MergeTwoSortedListsTest {
  MergeTwoSortedLists sut = new MergeTwoSortedLists();

  @ParameterizedTest
  @MethodSource
  void mergeTwoLists(ListNode list1, ListNode list2, ListNode expected) {
    ListNode actual = sut.mergeTwoLists(list1, list2);
    assertListEquals(actual, expected);
  }

  private static Stream<Arguments> mergeTwoLists() {
    return Stream.of(
        Arguments.of(ListNode.of(1, 2, 4), ListNode.of(1, 3, 4), ListNode.of(1, 1, 2, 3, 4, 4)),
        Arguments.of(ListNode.of(), ListNode.of(), ListNode.of()),
        Arguments.of(ListNode.of(), ListNode.of(0), ListNode.of(0)),
        Arguments.of(ListNode.of(-9, 3), ListNode.of(5, 7), ListNode.of(-9, 3, 5, 7)));
  }
}
