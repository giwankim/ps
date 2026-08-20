package leetcode.p1401_1500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MaximumProductOfTwoElementsInAnArrayTest {
  MaximumProductOfTwoElementsInAnArray sut = new MaximumProductOfTwoElementsInAnArray();

  // Step 1: smallest input the constraints allow (2 <= nums.length) — exactly one pair exists,
  // so the only thing under test is that *both* factors are decremented (LeetCode Example 3).
  // A plain nums[i]*nums[j] returns 21 here, and decrementing one side returns 14 or 18.
  @Test
  void minimumLengthLeavesOnePair() {
    assertThat(sut.maxProduct(new int[] {3, 7})).isEqualTo(12);
  }

  // Step 2: smallest value the constraints allow (1 <= nums[i]) — every factor collapses to 0.
  // Multiplying before subtracting returns 1 here.
  @Test
  void smallestValuesProduceZero() {
    assertThat(sut.maxProduct(new int[] {1, 1})).isEqualTo(0);
  }

  // Step 3: more elements than needed — the two largest win, not the first two
  // (LeetCode Example 1)
  @Test
  void extraElementsUseTheTwoLargest() {
    assertThat(sut.maxProduct(new int[] {3, 4, 5, 2})).isEqualTo(12);
  }

  // Step 4: the same values shuffled — the input arrives unsorted, so position must not matter
  @Test
  void unsortedInputGivesTheSameAnswer() {
    assertThat(sut.maxProduct(new int[] {5, 2, 4, 3})).isEqualTo(12);
  }

  // Step 5: two *different indices* may hold the same value (LeetCode Example 2).
  // Picking the two largest distinct values returns 12 here.
  @Test
  void repeatedMaximumIsUsedTwice() {
    assertThat(sut.maxProduct(new int[] {1, 5, 4, 5})).isEqualTo(16);
  }

  // Step 6: the two occurrences of the maximum are separated by a smaller value —
  // an "x != max" guard in the runner-up update returns 12 instead of 16
  @Test
  void separatedDuplicateMaximumsStillPair() {
    assertThat(sut.maxProduct(new int[] {5, 3, 5})).isEqualTo(16);
  }

  // Step 7: ascending input — every element is a new maximum, so the old maximum must be
  // demoted to runner-up. A tracker that overwrites the maximum without demoting returns 0 here.
  @Test
  void ascendingInputDemotesEachPreviousMaximum() {
    assertThat(sut.maxProduct(new int[] {1, 2, 3, 4})).isEqualTo(6);
  }

  // Step 8: descending input — the pair sits at the front and nothing after it ever promotes,
  // exercising the branch where a candidate updates neither slot
  @Test
  void descendingInputUsesTheLeadingPair() {
    assertThat(sut.maxProduct(new int[] {9, 8, 2, 1})).isEqualTo(56);
  }

  // Step 9: the pair straddles both ends with smaller values between — the runner-up is only
  // discovered on the final element, so the scan cannot stop early
  @Test
  void pairSplitAcrossTheEndsIsFound() {
    assertThat(sut.maxProduct(new int[] {9, 2, 2, 8})).isEqualTo(56);
  }

  // Step 10: i and j must be different indices, so a lone large value cannot pair with itself.
  // Squaring the maximum returns 81 here.
  @Test
  void loneLargeValueCannotPairWithItself() {
    assertThat(sut.maxProduct(new int[] {1, 1, 10, 1})).isEqualTo(0);
  }

  // Step 11: every value identical — the pair is that value with itself, one copy each
  @Test
  void allIdenticalValuesSquareThatValue() {
    assertThat(sut.maxProduct(new int[] {7, 7, 7, 7})).isEqualTo(36);
  }

  // Step 12: largest value the constraints allow (nums[i] <= 10^3) — 999 * 999 is the biggest
  // answer possible, and it still fits comfortably in an int
  @Test
  void largestValuesProduceMaximumPossibleAnswer() {
    assertThat(sut.maxProduct(new int[] {1000, 1000})).isEqualTo(998_001);
  }

  // Step 13: the same upper bound reached with a distinct runner-up, so the two slots must hold
  // different values rather than one value counted twice
  @Test
  void distinctLargestValuesUseBothMaxima() {
    assertThat(sut.maxProduct(new int[] {1000, 999, 1})).isEqualTo(997_002);
  }

  // Step 14: largest array the constraints allow (nums.length <= 500) with the winning pair
  // scattered to opposite ends of a uniform filler
  @Test
  void largestArrayScansEveryElement() {
    int[] nums = new int[500];
    Arrays.fill(nums, 3);
    nums[0] = 1000;
    nums[499] = 500;
    assertThat(sut.maxProduct(nums)).isEqualTo(498_501);
  }
}
