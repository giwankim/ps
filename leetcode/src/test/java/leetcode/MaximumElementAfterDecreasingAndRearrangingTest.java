package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MaximumElementAfterDecreasingAndRearrangingTest {
  MaximumElementAfterDecreasingAndRearranging sut =
      new MaximumElementAfterDecreasingAndRearranging();

  // Step 1: smallest valid input — the first element must become 1, so a lone element is 1
  @Test
  void singleElementBecomesOne() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {1})).isEqualTo(1);
  }

  // Step 2: even a huge lone value is forced down to 1 (the decrease operation + first-must-be-1)
  @Test
  void singleLargeElementIsDecreasedToOne() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {1_000_000_000}))
        .isEqualTo(1);
  }

  // Step 3: two equal values can't both grow — the second stays clamped at 1
  @Test
  void twoEqualElementsCannotGrowPastOne() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {1, 1}))
        .isEqualTo(1);
  }

  // Step 4: two consecutive values already satisfy |diff| <= 1, reaching 2
  @Test
  void twoConsecutiveElementsReachTwo() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {1, 2}))
        .isEqualTo(2);
  }

  // Step 5: a far-too-large second value is decreased down to 1 + 1 = 2
  @Test
  void largeSecondValueIsDecreasedToFitTheChain() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {1, 100}))
        .isEqualTo(2);
  }

  // Step 6: a descending array must be rearranged ascending — [5,4,3,2,1] -> [1,2,3,4,5]
  @Test
  void descendingArrayIsRearrangedToFullLength() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {5, 4, 3, 2, 1}))
        .isEqualTo(5);
  }

  // Step 7: equal values do NOT collapse merely for being duplicates — each large value can be
  // decreased to a distinct rung, so [7,7,7,7] rearranges into [1,2,3,4] for a max of 4 (= n).
  // (Contrast Step 3: duplicates only stall when the value itself is too small to climb.)
  @Test
  void equalLargeValuesDecreaseToAFullChain() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {7, 7, 7, 7}))
        .isEqualTo(4);
  }

  // Step 8: a plateau of duplicates caps the answer below the length —
  // sorted [1,2,2,2,5] clamps to [1,2,2,2,3], so the max is 3 even though n = 5
  @Test
  void duplicatePlateauCapsBelowLength() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {1, 2, 2, 2, 5}))
        .isEqualTo(3);
  }

  // Step 9: when every value is large enough, the answer equals the array length (here 3)
  @Test
  void answerIsBoundedByArrayLength() {
    int[] arr = {1_000_000_000, 1_000_000_000, 1_000_000_000};
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(arr)).isEqualTo(3);
  }

  // Step 10: LeetCode Example 1 — sorted [1,1,2,2,2] clamps to a max of 2
  @Test
  void leetCodeExample1() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {2, 2, 1, 2, 1}))
        .isEqualTo(2);
  }

  // Step 11: LeetCode Example 2 — rearrange to [1,100,1000], decrease to [1,2,3], max is 3
  @Test
  void leetCodeExample2() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {100, 1, 1000}))
        .isEqualTo(3);
  }

  // Step 12: LeetCode Example 3 — the array already satisfies the conditions, max is 5
  @Test
  void leetCodeExample3() {
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(new int[] {1, 2, 3, 4, 5}))
        .isEqualTo(5);
  }

  // Step 13: upper constraint bound — n = 10^5 of max values yields exactly n, well within int
  @Test
  void maximumLengthStaysWithinIntRange() {
    int[] arr = new int[100_000];
    Arrays.fill(arr, 1_000_000_000);
    assertThat(sut.maximumElementAfterDecrementingAndRearranging(arr)).isEqualTo(100_000);
  }
}
