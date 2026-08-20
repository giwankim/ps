package leetcode.p2901_3000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class LengthOfLongestSubarrayWithAtMostKFrequencyTest {
  LengthOfLongestSubarrayWithAtMostKFrequency sut =
      new LengthOfLongestSubarrayWithAtMostKFrequency();

  // Step 1: smallest valid input — a subarray is non-empty, so a lone element is always good
  @Test
  void singleElementReturnsOne() {
    assertThat(sut.maxSubarrayLength(new int[] {7}, 1)).isEqualTo(1);
  }

  // Step 2: with no repeats there is nothing to cap — the whole array is good even at k = 1
  @Test
  void allDistinctElementsReturnFullLength() {
    assertThat(sut.maxSubarrayLength(new int[] {1, 2, 3, 4}, 1)).isEqualTo(4);
  }

  // Step 3: the first real constraint — a repeated value at k = 1 cannot share a window
  @Test
  void repeatedPairAtFrequencyOneShrinksToOne() {
    assertThat(sut.maxSubarrayLength(new int[] {3, 3}, 1)).isEqualTo(1);
  }

  // Step 4: the window slides rather than restarts — dropping the leading 1 keeps [2,1] alive
  @Test
  void windowDropsTheEarlierOccurrenceInsteadOfResetting() {
    assertThat(sut.maxSubarrayLength(new int[] {1, 2, 1}, 1)).isEqualTo(2);
  }

  // Step 5: the cap is per value, not per window — two values may each sit at k simultaneously,
  //         so the answer is not bounded by k or by the number of distinct values
  @Test
  void everyValueMayReachTheFrequencyCapAtOnce() {
    assertThat(sut.maxSubarrayLength(new int[] {1, 1, 2, 2}, 2)).isEqualTo(4);
  }

  // Step 6: restoring the invariant takes a loop, not one step — when the second 4 arrives the left
  //         edge must jump past the first 4 (index 2), leaving [4,3]; advancing left only once
  //         would leave the window still holding two 4s and report 3
  @Test
  void leftEdgeAdvancesPastTheOffendingDuplicate() {
    assertThat(sut.maxSubarrayLength(new int[] {1, 4, 4, 3}, 1)).isEqualTo(2);
  }

  // Step 7: counts must be decremented as the left edge moves — the leading 1 leaves the window
  //         while shrinking past the duplicate 2, so the later 1 is free to join [2,1,3]; an
  //         implementation that never decrements still believes 1 is present and reports 2
  @Test
  void countsAreDecrementedAsTheWindowShrinks() {
    assertThat(sut.maxSubarrayLength(new int[] {1, 2, 2, 1, 3}, 1)).isEqualTo(3);
  }

  // Step 8: the best window is interior — [1,2,3,4] starts at index 1 and ends at index 4, so
  //         neither the longest prefix nor the longest suffix is the answer
  @Test
  void bestWindowLiesInTheMiddleOfTheArray() {
    assertThat(sut.maxSubarrayLength(new int[] {1, 1, 2, 3, 4, 1, 1}, 1)).isEqualTo(4);
  }

  // Step 9: a single repeated value caps the window at exactly k (LeetCode Example 3)
  @Test
  void uniformArrayIsCappedAtK() {
    assertThat(sut.maxSubarrayLength(new int[] {5, 5, 5, 5, 5, 5, 5}, 4)).isEqualTo(4);
  }

  // Step 10: two interleaved values at k = 1 admit only adjacent pairs (LeetCode Example 2)
  @Test
  void alternatingValuesAtFrequencyOneReturnTwo() {
    assertThat(sut.maxSubarrayLength(new int[] {1, 2, 1, 2, 1, 2, 1, 2}, 1)).isEqualTo(2);
  }

  // Step 11: the full example — [1,2,3,1,2,3] spans two cycles before a third 1 breaks the cap
  //          (LeetCode Example 1)
  @Test
  void repeatingCycleAdmitsExactlyKCyclesLong() {
    assertThat(sut.maxSubarrayLength(new int[] {1, 2, 3, 1, 2, 3, 1, 2}, 2)).isEqualTo(6);
  }

  // Step 12: k may equal nums.length, which makes every subarray good no matter the repeats
  @Test
  void kEqualToArrayLengthAcceptsTheWholeArray() {
    assertThat(sut.maxSubarrayLength(new int[] {2, 2, 2}, 3)).isEqualTo(3);
  }

  // Step 13: values run up to 10^9, so frequencies must be hashed rather than stored in an array
  //          indexed by value
  @Test
  void handlesValuesAtTheUpperConstraintBound() {
    assertThat(sut.maxSubarrayLength(new int[] {1_000_000_000, 1_000_000_000, 999_999_999}, 1))
        .isEqualTo(2);
  }

  // Step 14: TLE guard — at the length bound (10^5) a uniform array makes the left edge chase the
  //          right edge for the whole scan; the two-pointer sweep is O(n) while an O(n^2) recheck
  //          of every window times out
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void largeUniformArrayCompletesWithinTimeLimit() {
    int n = 100_000;
    int[] nums = new int[n];
    Arrays.fill(nums, 1_000_000_000);
    assertThat(sut.maxSubarrayLength(nums, 50_000)).isEqualTo(50_000);
  }

  // Step 15: TLE guard with a moving window — alternating values let the window grow to 2k before
  //          the leading value hits the cap, so the answer is 2000 rather than the whole array;
  //          the window keeps sliding across all 10^5 positions instead of collapsing once
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void largeAlternatingArrayCompletesWithinTimeLimit() {
    int n = 100_000;
    int[] nums = new int[n];
    for (int i = 0; i < n; i++) {
      nums[i] = i % 2 == 0 ? 1 : 2;
    }
    assertThat(sut.maxSubarrayLength(nums, 1_000)).isEqualTo(2_000);
  }
}
