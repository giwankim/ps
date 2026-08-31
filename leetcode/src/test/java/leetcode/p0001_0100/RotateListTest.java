package leetcode.p0001_0100;

import static leetcode.support.ListNodeAssertions.assertListEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import leetcode.support.ListNode;
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
    assertListEquals(sut.rotateRight(ListNode.of(1), 1), ListNode.of(1));
    assertListEquals(sut.rotateRight(ListNode.of(1), 2), ListNode.of(1));
  }

  @Test
  void noRotation() {
    assertListEquals(sut.rotateRight(ListNode.of(1, 2, 3), 0), ListNode.of(1, 2, 3));
  }

  @ParameterizedTest
  @MethodSource
  void rotateRight(ListNode head, int k, ListNode expected) {
    assertListEquals(sut.rotateRight(head, k), expected);
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
    assertListEquals(sut.rotateRight(head, 4), ListNode.of(2, 0, 1));
  }
}
