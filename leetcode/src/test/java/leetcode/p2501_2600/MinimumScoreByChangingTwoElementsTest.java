package leetcode.p2501_2600;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumScoreByChangingTwoElementsTest {
  MinimumScoreByChangingTwoElements sut = new MinimumScoreByChangingTwoElements();

  // Step 1: At the minimum length, equal values already give both pairwise scores zero.
  @Test
  void equalTripleKeepsZeroScore() {
    assertThat(sut.minimizeSum(new int[] {4, 4, 4})).isZero();
  }

  // Step 2: With four distinct values, two must survive. The closest pair is 5 and 8.
  @Test
  void fourDistinctValuesLeaveAPositiveScore() {
    assertThat(sut.minimizeSum(new int[] {8, 1, 20, 5})).isEqualTo(3);
  }

  // Step 3: Changing both low outliers leaves 50, 52, and 55. Changing just 1 leaves 10.
  @Test
  void bothChangesCanRaiseSmallValues() {
    assertThat(sut.minimizeSum(new int[] {52, 1, 55, 10, 50})).isEqualTo(5);
  }

  // Step 4: The high outliers 70 and 90 occupy interior positions in the input.
  @Test
  void bothChangesCanLowerLargeValues() {
    assertThat(sut.minimizeSum(new int[] {6, 90, 3, 70, 8})).isEqualTo(5);
  }

  // Step 5: Changing 1 and 80 together leaves values from 14 through 18. Either extreme
  //         left in place would keep the high score above 4.
  @Test
  void oneChangeAtEachExtremeCanBeBest() {
    assertThat(sut.minimizeSum(new int[] {15, 1, 18, 80, 14, 16})).isEqualTo(4);
  }

  // Step 6: A change affects one occurrence. Both 1s or both 9s can be changed for a score
  //         of 4. Changing one of each leaves the full range from 1 to 9.
  @Test
  void repeatedExtremesEachCostAChangePerOccurrence() {
    assertThat(sut.minimizeSum(new int[] {1, 9, 5, 1, 9})).isEqualTo(4);
  }

  // Step 7: Changing 1 first shrinks the range by 4, beating the immediate gain of 3 from
  //         changing 34. That choice leaves a best score of 26. Changing 31 and 34 gives 6.
  @Test
  void largestImmediateReductionCanWasteTheFirstChange() {
    assertThat(sut.minimizeSum(new int[] {1, 5, 6, 7, 31, 34})).isEqualTo(6);
  }

  // Official examples

  // Step 8: Making both replacements 6 creates a pair with difference zero. Adding the
  //         original array's smallest difference would overcharge the final score.
  @Test
  void leetCodeExample1() {
    assertThat(sut.minimizeSum(new int[] {1, 4, 7, 8, 5})).isEqualTo(3);
  }

  // Step 9: Both edits can copy the same survivor, making even a distinct triple all equal.
  @Test
  void leetCodeExample2() {
    assertThat(sut.minimizeSum(new int[] {1, 4, 3})).isZero();
  }

  // Constraint bounds. At maximum length, enumerating all pairs takes nearly five billion
  // iterations. Sorting or scanning fits the timeout.

  // Step 10: Three copies of each endpoint guarantee that both survive two edits. The answer
  //          reaches the full allowed range even though the low score is zero.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void repeatedValueBoundsForceTheLargestPossibleScore() {
    int[] nums = {1, 1_000_000_000, 1, 1_000_000_000, 1, 1_000_000_000};
    assertThat(sut.minimizeSum(nums)).isEqualTo(999_999_999);
  }

  // Step 11: The 99,998 surviving values span at least 99,997 gaps of 10,000 each.
  //          Keeping consecutive values attains that bound.
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthDescendingArray() {
    int[] nums = new int[100_000];
    Arrays.setAll(nums, i -> 1 + (nums.length - 1 - i) * 10_000);
    assertThat(sut.minimizeSum(nums)).isEqualTo(999_970_000);
  }

  // Instance reuse

  // Step 12: The optimum rises and then falls across calls. A running minimum stored on the
  //          instance would carry the first answer into the second call.
  @Test
  void oneInstanceScoresDifferentArraysIndependently() {
    assertThat(sut.minimizeSum(new int[] {8, 1, 20, 5})).isEqualTo(3);
    assertThat(sut.minimizeSum(new int[] {14, 1, 16, 70, 15, 19})).isEqualTo(5);
    assertThat(sut.minimizeSum(new int[] {1, 4, 3})).isZero();
  }
}
