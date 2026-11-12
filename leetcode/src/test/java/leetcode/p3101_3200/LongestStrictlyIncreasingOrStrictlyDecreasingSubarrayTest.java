package leetcode.p3101_3200;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class LongestStrictlyIncreasingOrStrictlyDecreasingSubarrayTest {
  LongestStrictlyIncreasingOrStrictlyDecreasingSubarray sut =
      new LongestStrictlyIncreasingOrStrictlyDecreasingSubarray();

  // ===========================================================================================
  // The floor and the two directions (Steps 1-6).
  // ===========================================================================================

  // Step 1: 1 <= nums.length, so a lone element is the smallest legal input. A single element is
  //         trivially strictly monotonic, so the answer is 1, never 0
  @Test
  void singleElementIsARunOfOne() {
    assertThat(sut.longestMonotonicSubarray(new int[] {7})).isEqualTo(1);
  }

  // Step 2: one rising step. A solution that only folds a run into the answer when the run
  //         breaks never sees a break here and answers 1
  @Test
  void twoRisingElementsFormARunOfTwo() {
    assertThat(sut.longestMonotonicSubarray(new int[] {1, 2})).isEqualTo(2);
  }

  // Step 3: the mirror of Step 2. A solution that only tracks increasing runs answers 1
  @Test
  void twoFallingElementsFormARunOfTwo() {
    assertThat(sut.longestMonotonicSubarray(new int[] {2, 1})).isEqualTo(2);
  }

  // Step 4: "strictly" rules out equal neighbors in both directions. A non-strict comparison
  //         (>= or <=) answers 2
  @Test
  void twoEqualElementsAreNotARun() {
    assertThat(sut.longestMonotonicSubarray(new int[] {5, 5})).isEqualTo(1);
  }

  // Step 5: the whole array is one increasing run
  @Test
  void wholeArrayIncreasing() {
    assertThat(sut.longestMonotonicSubarray(new int[] {1, 2, 3, 4, 5})).isEqualTo(5);
  }

  // Step 6: the mirror of Step 5
  @Test
  void wholeArrayDecreasing() {
    assertThat(sut.longestMonotonicSubarray(new int[] {5, 4, 3, 2, 1})).isEqualTo(5);
  }

  // ===========================================================================================
  // How runs break and join (Steps 7-12).
  // ===========================================================================================

  // Step 7: a plateau splits a run. [1, 2, 3] and [3, 4, 5] are separate, so the answer is 3.
  //         A non-strict comparison glues them into one run and answers 6
  @Test
  void plateauBreaksAnIncreasingRun() {
    assertThat(sut.longestMonotonicSubarray(new int[] {1, 2, 3, 3, 4, 5})).isEqualTo(3);
  }

  // Step 8: the peak belongs to both runs. The falling run is [9, 8, 7, 6], length 4. A single
  //         run counter that restarts at 1 on a direction change forgets that 9 already
  //         starts the falling run and answers 3
  @Test
  void turningPointIsSharedByBothRuns() {
    assertThat(sut.longestMonotonicSubarray(new int[] {1, 2, 9, 8, 7, 6})).isEqualTo(4);
  }

  // Step 9: every step flips direction, so no run is longer than 2. A solution that only resets
  //         on equal neighbors (any change continues the run) answers 6
  @Test
  void zigzagNeverExceedsTwo() {
    assertThat(sut.longestMonotonicSubarray(new int[] {1, 3, 2, 4, 3, 5})).isEqualTo(2);
  }

  // Step 10: the longest run, [1, 2, 3, 4], ends at the last element. A solution that only
  //          records a run when it breaks never records this one and answers 2
  @Test
  void longestRunAtTheEnd() {
    assertThat(sut.longestMonotonicSubarray(new int[] {3, 3, 1, 2, 3, 4})).isEqualTo(4);
  }

  // Step 11: the mirror of Step 10. The longest run, [4, 3, 2, 1], starts at index 0
  @Test
  void longestRunAtTheStart() {
    assertThat(sut.longestMonotonicSubarray(new int[] {4, 3, 2, 1, 1, 2})).isEqualTo(4);
  }

  // Step 12: a subarray is contiguous. The rising subsequence 1, 2, 3, 4 skips elements and does
  //          not count; every adjacent pair here flips direction. An LIS/LDS answers 4
  @Test
  void subarrayIsContiguousNotASubsequence() {
    assertThat(sut.longestMonotonicSubarray(new int[] {1, 5, 2, 6, 3, 7, 4})).isEqualTo(2);
  }

  // ===========================================================================================
  // The official examples (Steps 13-15).
  // ===========================================================================================

  // Step 13: [4, 3, 3, 2] is not decreasing because of the repeated 3. A non-strict comparison
  //          answers 4
  @Test
  void leetCodeExample1() {
    assertThat(sut.longestMonotonicSubarray(new int[] {1, 4, 3, 3, 2})).isEqualTo(2);
  }

  // Step 14: a constant array has no run longer than one element in either direction. A
  //          non-strict comparison answers 4
  @Test
  void leetCodeExample2() {
    assertThat(sut.longestMonotonicSubarray(new int[] {3, 3, 3, 3})).isEqualTo(1);
  }

  // Step 15: the answer comes from the decreasing direction alone. A solution that only tracks
  //          increasing runs answers 1
  @Test
  void leetCodeExample3() {
    assertThat(sut.longestMonotonicSubarray(new int[] {3, 2, 1})).isEqualTo(3);
  }

  // ===========================================================================================
  // The constraint bounds (Steps 16-20). With nums.length <= 50 and 1 <= nums[i] <= 50, even an
  // O(n^3) check of every subarray finishes instantly; the timeouts only guard against a loop
  // that never advances.
  // ===========================================================================================

  // Step 16: the longest possible increasing run, 1..50
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthIncreasingRamp() {
    int[] nums = new int[50];
    Arrays.setAll(nums, i -> i + 1);

    assertThat(sut.longestMonotonicSubarray(nums)).isEqualTo(50);
  }

  // Step 17: the longest possible decreasing run, 50..1
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthDecreasingRamp() {
    int[] nums = new int[50];
    Arrays.setAll(nums, i -> 50 - i);

    assertThat(sut.longestMonotonicSubarray(nums)).isEqualTo(50);
  }

  // Step 18: fifty copies of the top value. A non-strict comparison answers 50
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthConstantArray() {
    int[] nums = new int[50];
    Arrays.fill(nums, 50);

    assertThat(sut.longestMonotonicSubarray(nums)).isEqualTo(1);
  }

  // Step 19: a sawtooth 1..10 repeated five times. Each drop from 10 back to 1 is a falling run
  //          of only 2, so the answer is 10. Counting any non-equal step as a continuation
  //          answers 50
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthSawtooth() {
    int[] nums = new int[50];
    Arrays.setAll(nums, i -> i % 10 + 1);

    assertThat(sut.longestMonotonicSubarray(nums)).isEqualTo(10);
  }

  // Step 20: four rising runs of 1..7, then 1, 2, then a falling run 50, 48, ..., 12 that ends
  //          the array. The falling run is 20 long and starts at the peak 50, which also ends
  //          the rising run 1, 2, 50. Restarting at 1 on the turn answers 19, and only
  //          recording runs when they break answers 7
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthLongestRunIsTheFallingTail() {
    int[] nums = new int[50];
    Arrays.setAll(nums, i -> i < 30 ? i % 7 + 1 : 50 - (i - 30) * 2);

    assertThat(sut.longestMonotonicSubarray(nums)).isEqualTo(20);
  }

  // ===========================================================================================
  // Hygiene (Step 21).
  // ===========================================================================================

  // Step 21: one instance answers inputs of different sizes in turn, with the largest in the
  //          middle. A run length or best value kept on the instance leaks into the next call
  @Test
  void oneInstanceAnswersManyInputs() {
    int[] ramp = new int[50];
    Arrays.setAll(ramp, i -> i + 1);

    assertThat(sut.longestMonotonicSubarray(new int[] {3, 2, 1})).isEqualTo(3);
    assertThat(sut.longestMonotonicSubarray(ramp)).isEqualTo(50);
    assertThat(sut.longestMonotonicSubarray(new int[] {5, 5})).isEqualTo(1);
    assertThat(sut.longestMonotonicSubarray(new int[] {1, 4, 3, 3, 2})).isEqualTo(2);
  }
}
