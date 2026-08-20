package leetcode.p0601_0700;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MaximumProductOfThreeNumbersTest {
  MaximumProductOfThreeNumbers sut = new MaximumProductOfThreeNumbers();

  // Step 1: smallest input the constraints allow (3 <= nums.length) — exactly one triple exists
  // (LeetCode Example 1)
  @Test
  void minimumLengthLeavesOneTriple() {
    assertThat(sut.maximumProduct(new int[] {1, 2, 3})).isEqualTo(6);
  }

  // Step 2: more elements than needed — the three largest win, not the first three
  // (LeetCode Example 2)
  @Test
  void extraPositivesUseTheThreeLargest() {
    assertThat(sut.maximumProduct(new int[] {1, 2, 3, 4})).isEqualTo(24);
  }

  // Step 3: the same values shuffled — the input arrives unsorted, so position must not matter
  @Test
  void unsortedPositivesGiveTheSameAnswer() {
    assertThat(sut.maximumProduct(new int[] {4, 1, 3, 2})).isEqualTo(24);
  }

  // Step 4: all negative at minimum length — the answer itself is negative (LeetCode Example 3).
  // A running maximum seeded with 0 returns 0 here.
  @Test
  void allNegativeMinimumLengthReturnsNegativeProduct() {
    assertThat(sut.maximumProduct(new int[] {-1, -2, -3})).isEqualTo(-6);
  }

  // Step 5: all negative with a choice — the three *least* negative win.
  // Picking the three largest magnitudes returns -24 here.
  @Test
  void allNegativeUsesTheThreeLeastNegative() {
    assertThat(sut.maximumProduct(new int[] {-1, -2, -3, -4})).isEqualTo(-6);
  }

  // Step 6: a lone negative can never help — it has no partner to cancel its sign.
  // Always multiplying the two smallest by the largest returns -15 here.
  @Test
  void loneNegativeIsLeftOut() {
    assertThat(sut.maximumProduct(new int[] {-5, 1, 2, 3})).isEqualTo(6);
  }

  // Step 7: the core insight — two large negatives multiply to a large positive and beat
  // the three largest values. Sorting and taking the top three returns 6 here.
  @Test
  void twoLargeNegativesBeatTheThreeLargest() {
    assertThat(sut.maximumProduct(new int[] {-100, -98, 1, 2, 3})).isEqualTo(29400);
  }

  // Step 8: the mirror of Step 7 — negatives are present but too small to be worth using.
  // Always pairing the two smallest with the largest returns 14 here.
  @Test
  void smallNegativesLoseToTheThreeLargest() {
    assertThat(sut.maximumProduct(new int[] {-2, -1, 5, 6, 7})).isEqualTo(210);
  }

  // Step 9: three negatives available, but a triple may use exactly two of them —
  // the two most negative, not all three
  @Test
  void threeNegativesContributeOnlyTheTwoMostNegative() {
    assertThat(sut.maximumProduct(new int[] {-5, -4, -3, 2})).isEqualTo(40);
  }

  // Step 10: zero is a legitimate answer — treating zeros as useless and skipping them
  // returns -10 here
  @Test
  void zeroCanBeTheMaximumProduct() {
    assertThat(sut.maximumProduct(new int[] {-5, 0, 1, 2})).isEqualTo(0);
  }

  // Step 11: nothing but zeros
  @Test
  void allZerosProduceZero() {
    assertThat(sut.maximumProduct(new int[] {0, 0, 0})).isEqualTo(0);
  }

  // Step 12: repeated values are separate elements, so the maximum may be used three times.
  // Working from distinct values alone cannot even form a triple here.
  @Test
  void repeatedMaximumIsUsedThreeTimes() {
    assertThat(sut.maximumProduct(new int[] {2, 2, 2, 1})).isEqualTo(8);
  }

  // Step 13: descending input — every element after the first must be demoted into a lower
  // slot, exercising the cascade a single-pass tracker needs
  @Test
  void descendingInputFindsTheLeadingTriple() {
    assertThat(sut.maximumProduct(new int[] {5, 4, 3, 2, 1})).isEqualTo(60);
  }

  // Step 14: largest value the constraints allow (nums[i] <= 1000) — 1000^3 is the biggest
  // answer possible, and it still fits in an int
  @Test
  void largestPositiveProductFitsInAnInt() {
    assertThat(sut.maximumProduct(new int[] {1000, 1000, 1000})).isEqualTo(1_000_000_000);
  }

  // Step 15: the same maximum reached through the negative branch instead
  @Test
  void twoExtremeNegativesReachTheSameMaximum() {
    assertThat(sut.maximumProduct(new int[] {-1000, -1000, 1000})).isEqualTo(1_000_000_000);
  }

  // Step 16: smallest value the constraints allow (-1000 <= nums[i]) — the most negative
  // answer possible, and the lower bound an int result must hold
  @Test
  void mostNegativeAnswerFitsInAnInt() {
    assertThat(sut.maximumProduct(new int[] {-1000, -1000, -1000})).isEqualTo(-1_000_000_000);
  }

  // Step 17: largest array the constraints allow (nums.length <= 10^4) with the winning triple
  // scattered across it — a cubic search over 10^4 elements will not finish
  @Test
  void largestArrayScansEveryElement() {
    int[] nums = new int[10_000];
    Arrays.fill(nums, 1);
    nums[0] = -1000;
    nums[5_000] = -999;
    nums[9_999] = 1000;
    assertThat(sut.maximumProduct(nums)).isEqualTo(999_000_000);
  }
}
