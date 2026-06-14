package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.support.ListNode;
import org.junit.jupiter.api.Test;

class MaximumTwinSumOfALinkedListTest {
  MaximumTwinSumOfALinkedList sut = new MaximumTwinSumOfALinkedList();

  // Step 1: smallest valid input — an even length of 2 has exactly one twin pair (node 0 with
  // node 1), so its sum is the answer (2 <= n)
  @Test
  void twoNodeListReturnsTheSumOfItsOnlyPair() {
    assertThat(sut.pairSum(ListNode.of(1, 2))).isEqualTo(3);
  }

  // Step 2: a twin sum is commutative — reversing the same pair leaves the answer unchanged
  @Test
  void twinSumIsTheSameRegardlessOfPairOrder() {
    assertThat(sut.pairSum(ListNode.of(2, 1))).isEqualTo(3);
  }

  // Step 3: LeetCode Example 1 — four nodes where both pairs (5+1, 4+2) tie at 6
  @Test
  void leetCodeExample1() {
    assertThat(sut.pairSum(ListNode.of(5, 4, 2, 1))).isEqualTo(6);
  }

  // Step 4: LeetCode Example 2 — the outer pair (4+3=7) beats the inner pair (2+2=4)
  @Test
  void leetCodeExample2() {
    assertThat(sut.pairSum(ListNode.of(4, 2, 2, 3))).isEqualTo(7);
  }

  // Step 5: the mirror of Example 2 — now the inner pair (5+5=10) beats the outer pair (1+1=2),
  // so the winner is not always the first/outermost pair
  @Test
  void maximumCanComeFromTheInnerPair() {
    assertThat(sut.pairSum(ListNode.of(1, 5, 5, 1))).isEqualTo(10);
  }

  // Step 6: with six nodes the winning pair (9+8=17) is the middle one — neither the outermost
  // (2+2=4) nor the central (1+1=2) pair — so every pair must be considered
  @Test
  void maximumCanComeFromAnInteriorPair() {
    assertThat(sut.pairSum(ListNode.of(2, 9, 1, 1, 8, 2))).isEqualTo(17);
  }

  // Step 7: when every value is identical, all twin sums are equal to twice that value
  @Test
  void allEqualValuesReturnTwiceThatValue() {
    assertThat(sut.pairSum(ListNode.of(3, 3, 3, 3))).isEqualTo(6);
  }

  // Step 8: a longer eight-node list with a single, non-obvious maximum — pairs sum to
  // 5, 8, 6 and 11, so the answer is 11
  @Test
  void longerListPicksTheSingleLargestTwinSum() {
    assertThat(sut.pairSum(ListNode.of(4, 2, 1, 3, 8, 5, 6, 1))).isEqualTo(11);
  }

  // Step 9: upper value bound — two nodes at the max value (10^5) twin to 2*10^5 = 200000,
  // which still fits comfortably in an int
  @Test
  void largeValuesReachTheTwinSumUpperBound() {
    assertThat(sut.pairSum(ListNode.of(100000, 1, 1, 100000))).isEqualTo(200000);
  }

  // Step 10: LeetCode Example 3 — a single twin pair carrying the maximum allowed value
  @Test
  void leetCodeExample3() {
    assertThat(sut.pairSum(ListNode.of(1, 100000))).isEqualTo(100001);
  }

  // Step 11: maximum length (n = 10^5). Values 1..n make every twin pair (i, n-1-i) sum to
  // n + 1, so the answer is 100001 — confirming the solution scales to the largest input
  @Test
  void maximumLengthListIsHandled() {
    int n = 100000;
    int[] values = new int[n];
    for (int i = 0; i < n; i++) {
      values[i] = i + 1;
    }
    assertThat(sut.pairSum(ListNode.of(values))).isEqualTo(100001);
  }
}
