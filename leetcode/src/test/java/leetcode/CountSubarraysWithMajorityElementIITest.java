package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CountSubarraysWithMajorityElementIITest {
  CountSubarraysWithMajorityElementII sut = new CountSubarraysWithMajorityElementII();

  // Step 1: smallest valid input (1 <= length) — a lone target is the majority of itself
  @Test
  void singleTargetElementIsMajorityOfItself() {
    assertThat(sut.countMajoritySubarrays(new int[] {5}, 5)).isEqualTo(1);
  }

  // Step 2: the only other length-1 case — a lone non-target has no majority subarray
  @Test
  void singleNonTargetElementHasNoMajoritySubarray() {
    assertThat(sut.countMajoritySubarrays(new int[] {5}, 3)).isEqualTo(0);
  }

  // Step 3: a target that never appears can never be a majority (LeetCode Example 3)
  @Test
  void targetAbsentFromArrayYieldsZero() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 2, 3}, 4)).isEqualTo(0);
  }

  // Step 4: a uniform target pair — both singletons and the pair qualify (3 subarrays)
  @Test
  void uniformTargetPairCountsEverySubarray() {
    assertThat(sut.countMajoritySubarrays(new int[] {2, 2}, 2)).isEqualTo(3);
  }

  // Step 5: "strictly more than half" — a 50/50 split is a tie, not a majority, so only the
  //         lone target counts in [2, 1]
  @Test
  void tiedHalfIsNotAMajority() {
    assertThat(sut.countMajoritySubarrays(new int[] {2, 1}, 2)).isEqualTo(1);
  }

  // Step 6: two targets can absorb a leading non-target — [1,2,2] is 2-of-3, a true majority —
  //         giving [2], [2], [2,2], and [1,2,2] for 4 subarrays
  @Test
  void targetsCanAbsorbALeadingNonTarget() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 2, 2}, 2)).isEqualTo(4);
  }

  // Step 7: LeetCode Example 1 — non-targets flank on both sides; only windows where 2s strictly
  //         dominate survive: [2], [2], [2,2], [1,2,2], [2,2,3]
  @Test
  void leetCodeExample1() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 2, 2, 3}, 2)).isEqualTo(5);
  }

  // Step 8: LeetCode Example 2 — when every element is the target, all n*(n+1)/2 subarrays qualify
  @Test
  void leetCodeExample2AllElementsAreTarget() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 1, 1, 1}, 1)).isEqualTo(10);
  }

  // Step 9: an outnumbered target — every window wider than the lone 2 is at best a tie, so only
  //         the singleton [2] counts amid the surrounding 1s
  @Test
  void outnumberedTargetOnlyCountsAsSingletons() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 1, 2, 1, 1}, 2)).isEqualTo(1);
  }

  // Step 10: a uniform array of the wrong value still yields zero — presence of duplicates is
  //          irrelevant when none of them is the target
  @Test
  void uniformArrayOfWrongValueYieldsZero() {
    assertThat(sut.countMajoritySubarrays(new int[] {5, 5, 5}, 7)).isEqualTo(0);
  }

  // Step 11: the "II" distinction — at the constraint bound (length 10^5) an all-target array has
  //          10^5 * (10^5 + 1) / 2 = 5_000_050_000 subarrays, which overflows int and requires
  //          the long return type
  @Test
  void countExceedsIntRangeAtTheConstraintBound() {
    int n = 100_000;
    int[] nums = new int[n];
    Arrays.fill(nums, 7);
    assertThat(sut.countMajoritySubarrays(nums, 7)).isEqualTo(5_000_050_000L);
  }

  // Step 12: TLE guard — at the upper bound (length 10^5) with the maximum value (10^9) every
  //          subarray qualifies; an O(n^2) brute force times out, while the prefix-sum + Fenwick
  //          tree approach finishes well within the limit. The maximum value also catches solutions
  //          that mistakenly index an array by element value.
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void largeUniformTargetArrayCompletesWithinTimeLimit() {
    int n = 100_000;
    int[] nums = new int[n];
    Arrays.fill(nums, 1_000_000_000);
    assertThat(sut.countMajoritySubarrays(nums, 1_000_000_000)).isEqualTo(5_000_050_000L);
  }

  // Step 13: TLE guard with an oscillating prefix sum — alternating target / non-target makes the
  //          running sum swing between 0 and 1, so the structure cannot assume monotonic prefix
  //          sums; it must count earlier prefix sums below the current one across 10^5 positions.
  //          For length 2m the answer is m * (m + 1) / 2.
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void largeAlternatingArrayCompletesWithinTimeLimit() {
    int n = 100_000;
    int[] nums = new int[n];
    for (int i = 0; i < n; i++) {
      nums[i] = i % 2 == 0 ? 1_000_000_000 : 1;
    }
    assertThat(sut.countMajoritySubarrays(nums, 1_000_000_000)).isEqualTo(1_250_025_000L);
  }
}
