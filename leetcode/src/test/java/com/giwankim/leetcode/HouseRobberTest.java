package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class HouseRobberTest {
  HouseRobber sut = new HouseRobber();

  // Step 1: smallest valid input from the constraint 1 <= nums.length.
  @Test
  void oneHouseReturnsThatHouseAmount() {
    assertThat(sut.rob(new int[] {7})).isEqualTo(7);
    assertThat(sut.rob2(new int[] {7})).isEqualTo(7);
  }

  // Step 2: two adjacent houses cannot both be robbed, so choose the larger value.
  @Test
  void twoHousesReturnsLargerAmount() {
    assertThat(sut.rob(new int[] {2, 7})).isEqualTo(7);
    assertThat(sut.rob2(new int[] {2, 7})).isEqualTo(7);
  }

  // Step 3: LeetCode Example 1 — rob houses 1 and 3 for 1 + 3.
  @Test
  void exampleOneSkipsAdjacentHouse() {
    assertThat(sut.rob(new int[] {1, 2, 3, 1})).isEqualTo(4);
    assertThat(sut.rob2(new int[] {1, 2, 3, 1})).isEqualTo(4);
  }

  // Step 4: LeetCode Example 2 — three non-adjacent houses outperform any pair.
  @Test
  void exampleTwoCombinesNonAdjacentHouses() {
    assertThat(sut.rob(new int[] {2, 7, 9, 3, 1})).isEqualTo(12);
    assertThat(sut.rob2(new int[] {2, 7, 9, 3, 1})).isEqualTo(12);
  }

  // Step 5: later non-adjacent houses can beat an earlier adjacent choice.
  @Test
  void choosesNonAdjacentEndpointsOverAdjacentMiddle() {
    assertThat(sut.rob(new int[] {2, 1, 1, 2})).isEqualTo(4);
    assertThat(sut.rob2(new int[] {2, 1, 1, 2})).isEqualTo(4);
  }

  // Step 6: house values may be zero per 0 <= nums[i].
  @Test
  void allZeroHouseValuesReturnZero() {
    assertThat(sut.rob(new int[] {0, 0, 0})).isEqualTo(0);
    assertThat(sut.rob2(new int[] {0, 0, 0})).isEqualTo(0);
  }

  // Step 7: maximum constrained length at max per-house value confirms no int overflow.
  @Test
  void maxLengthAndMaxValueInputReturnsEveryOtherHouseTotal() {
    int[] nums = new int[100];
    Arrays.fill(nums, 400);

    assertThat(sut.rob(nums)).isEqualTo(20_000);
    assertThat(sut.rob2(nums)).isEqualTo(20_000);
  }
}
