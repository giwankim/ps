package leetcode.p2001_2100;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class RemovingMinimumAndMaximumFromArrayTest {
  RemovingMinimumAndMaximumFromArray sut = new RemovingMinimumAndMaximumFromArray();

  // ===========================================================================================
  // One scenario at a time (Steps 1-5).
  // ===========================================================================================

  // Step 1: the constraint floor is n = 1, and the single element is then both the minimum and
  //         the maximum. One deletion removes both. A solution that counts one deletion per
  //         extreme answers 2
  @Test
  void singleElementIsBothMinAndMax() {
    assertThat(sut.minimumDeletions(new int[] {7})).isEqualTo(1);
  }

  // Step 2: with two elements, both are extremes and both must go, whichever order they sit in.
  //         A solution that only removes the cheaper extreme answers 1
  @Test
  void twoElementsBothMustBeDeleted() {
    assertThat(sut.minimumDeletions(new int[] {3, 7})).isEqualTo(2);
  }

  // Step 3: nums = [10, 1, 5, 6, 7] -> max at index 0, min at index 1: one front sweep of 2
  //         covers both. Costing each extreme independently on its cheaper side sums 1 + 2 = 3,
  //         double-counting the overlap, and a solver that always takes one from each side
  //         answers 5
  @Test
  void bothExtremesNearFrontComeOffTheFrontTogether() {
    assertThat(sut.minimumDeletions(new int[] {10, 1, 5, 6, 7})).isEqualTo(2);
  }

  // Step 4: the mirror of Step 3. nums = [7, 6, 5, 1, 10] -> min at index 3, max at index 4: one
  //         back sweep of 2 covers both. A solver biased toward the front answers 5
  @Test
  void bothExtremesNearBackComeOffTheBackTogether() {
    assertThat(sut.minimumDeletions(new int[] {7, 6, 5, 1, 10})).isEqualTo(2);
  }

  // Step 5: nums = [1, 5, 6, 7, 10] -> min at the front edge, max at the back edge: take one
  //         from each side. A solution that forgets this split scenario and only compares
  //         front-only against back-only answers 5 instead of 2
  @Test
  void extremesAtOppositeEndsSplitOneFromEachSide() {
    assertThat(sut.minimumDeletions(new int[] {1, 5, 6, 7, 10})).isEqualTo(2);
  }

  // ===========================================================================================
  // Choosing between the scenarios (Steps 6-7).
  // ===========================================================================================

  // Step 6: nums = [5, 6, 1, 10, 7, 8] -> min at index 2 and max at index 3, adjacent in the
  //         middle. A single front sweep of 4 wins. Splitting one extreme to each side costs
  //         3 + 3 = 6, and so does the independent cheaper-side sum: both overshoot the answer 4
  @Test
  void adjacentMiddlePairFavorsOneSidedSweep() {
    assertThat(sut.minimumDeletions(new int[] {5, 6, 1, 10, 7, 8})).isEqualTo(4);
  }

  // Step 7: nums = [100000, 0, -100000] spans the full value range of the constraints, max at
  //         the front and min at the back: one deletion from each side. A no-split solver
  //         answers 3
  @Test
  void boundaryValuesSpanTheFullRange() {
    assertThat(sut.minimumDeletions(new int[] {100000, 0, -100000})).isEqualTo(2);
  }

  // ===========================================================================================
  // The official examples (Steps 8-11).
  // ===========================================================================================

  // Step 8: the explanation prescribes mixing sides: 2 off the front removes the max 10 at
  //         index 1, 3 off the back removes the min 1 at index 5, totaling 5. A chooser limited
  //         to front-only or back-only answers 6
  @Test
  void leetCodeExample1() {
    assertThat(sut.minimumDeletions(new int[] {2, 10, 7, 5, 4, 1, 8, 6})).isEqualTo(5);
  }

  // Step 9: the explanation removes 3 from the front only, covering -4 at index 1 and 19 at
  //         index 2 in one sweep. An always-split solver answers 8, and the independent
  //         cheaper-side sum answers 5
  @Test
  void leetCodeExample2() {
    assertThat(sut.minimumDeletions(new int[] {0, -4, 19, 1, 8, -2, -3, 5})).isEqualTo(3);
  }

  // Step 10: the official single-element case, same rule as Step 1 with the statement's input
  @Test
  void leetCodeExample3() {
    assertThat(sut.minimumDeletions(new int[] {101})).isEqualTo(1);
  }

  // Step 11: example 2 reversed -> both extremes now sit near the back and the 3 deletions come
  //          off the back instead. Kills a solution hard-coded to sweep from the front
  @Test
  void mirrorOfExample2ComesOffTheBack() {
    assertThat(sut.minimumDeletions(new int[] {5, -3, -2, 8, 1, 19, -4, 0})).isEqualTo(3);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 12-13). At n = 100000, enumerating every pair of front and back
  // deletion counts is on the order of 5 * 10^9 checks and cannot finish in the timeout, while
  // one O(n) scan for the two extreme indices finishes in milliseconds.
  // ===========================================================================================

  // Step 12: a full-size ramp from -50000 to 49999 puts the min at index 0 and the max at
  //          index 99999, so the answer is one deletion from each side
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthRampTakesOneFromEachEnd() {
    assertThat(sut.minimumDeletions(ramp())).isEqualTo(2);
  }

  // Step 13: the same ramp with the min swapped to index 33333 and the max to index 66666.
  //          Front-only costs 66667, back-only also costs 66667, and the split costs 66668, so
  //          all three scenarios must actually be compared to answer 66667
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthBuriedExtremesNeedAllThreeScenarios() {
    assertThat(sut.minimumDeletions(rampWithBuriedExtremes())).isEqualTo(66667);
  }

  // ===========================================================================================
  // Hygiene (Steps 14-15).
  // ===========================================================================================

  // Step 14: sorting is a tempting way to find the min and max values, but it destroys the
  //          indices the answer depends on and mutates the caller's array. The input must come
  //          back untouched
  @Test
  void inputArrayIsNotModified() {
    int[] nums = {2, 10, 7, 5, 4, 1, 8, 6};
    sut.minimumDeletions(nums);
    assertThat(nums).containsExactly(2, 10, 7, 5, 4, 1, 8, 6);
  }

  // Step 15: one instance answers inputs of different sizes out of order, the largest in the
  //          middle. Catches extreme indices or the length cached on the instance instead of
  //          derived per call
  @Test
  void oneInstanceAnswersManyInputsOutOfOrder() {
    assertThat(sut.minimumDeletions(new int[] {1, 5, 6, 7, 10})).isEqualTo(2);
    assertThat(sut.minimumDeletions(ramp())).isEqualTo(2);
    assertThat(sut.minimumDeletions(new int[] {7})).isEqualTo(1);
  }

  private static int[] ramp() {
    int[] nums = new int[100_000];
    Arrays.setAll(nums, i -> i - 50_000);
    return nums;
  }

  private static int[] rampWithBuriedExtremes() {
    int[] nums = ramp();
    swap(nums, 0, 33_333);
    swap(nums, 99_999, 66_666);
    return nums;
  }

  private static void swap(int[] nums, int i, int j) {
    int t = nums[i];
    nums[i] = nums[j];
    nums[j] = t;
  }
}
