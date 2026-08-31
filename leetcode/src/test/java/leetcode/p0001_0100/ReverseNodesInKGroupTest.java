package leetcode.p0001_0100;

import static leetcode.support.ListNodeAssertions.assertListEquals;

import leetcode.support.ListNode;
import org.junit.jupiter.api.Test;

class ReverseNodesInKGroupTest {
  ReverseNodesInKGroup sut = new ReverseNodesInKGroup();

  @Test
  void singleton() {
    assertListEquals(sut.reverseKGroup(ListNode.of(1), 1), ListNode.of(1));
  }

  @Test
  void kIsOne() {
    assertListEquals(sut.reverseKGroup(ListNode.of(1, 2, 3), 1), ListNode.of(1, 2, 3));
  }

  @Test
  void twoNodesSwapped() {
    assertListEquals(sut.reverseKGroup(ListNode.of(1, 2), 2), ListNode.of(2, 1));
  }

  @Test
  void kIsEqualToN() {
    assertListEquals(sut.reverseKGroup(ListNode.of(1, 2, 3), 3), ListNode.of(3, 2, 1));
  }

  @Test
  void twoGroups() {
    assertListEquals(sut.reverseKGroup(ListNode.of(1, 2, 3, 4), 2), ListNode.of(2, 1, 4, 3));
  }

  @Test
  void kIsDivisorOfN() {
    assertListEquals(
        sut.reverseKGroup(ListNode.of(1, 2, 3, 4, 5, 6), 2), ListNode.of(2, 1, 4, 3, 6, 5));
    assertListEquals(
        sut.reverseKGroup(ListNode.of(1, 2, 3, 4, 5, 6), 3), ListNode.of(3, 2, 1, 6, 5, 4));
  }

  @Test
  void kIsNotADivisorOfN() {
    assertListEquals(sut.reverseKGroup(ListNode.of(1, 2, 3, 4, 5), 2), ListNode.of(2, 1, 4, 3, 5));
    assertListEquals(sut.reverseKGroup(ListNode.of(1, 2, 3, 4, 5), 3), ListNode.of(3, 2, 1, 4, 5));
  }
}
