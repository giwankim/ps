package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class CountSubarraysWithMajorityElementITest {
  CountSubarraysWithMajorityElementI sut = new CountSubarraysWithMajorityElementI();

  // Step 1: smallest valid input — a length-1 subarray that equals target.
  //         One occurrence out of length 1 is strictly more than half, so it counts.
  @Test
  void singleTargetElementIsItsOwnMajority() {
    assertThat(sut.countMajoritySubarrays(new int[] {5}, 5)).isEqualTo(1);
  }

  // Step 2: the only other length-1 case — the lone element is not target, so target
  //         appears zero times and can never be the majority.
  @Test
  void singleNonTargetElementHasNoMajoritySubarray() {
    assertThat(sut.countMajoritySubarrays(new int[] {5}, 7)).isEqualTo(0);
  }

  // Step 3: two equal-to-target elements — both singletons plus the whole pair qualify
  //         (n(n+1)/2 = 3 subarrays, all majority).
  @Test
  void allEqualPairCountsEverySubarray() {
    assertThat(sut.countMajoritySubarrays(new int[] {2, 2}, 2)).isEqualTo(3);
  }

  // Step 4: the defining edge case — "strictly more than half" excludes an even split.
  //         In [2,3] only [2] qualifies; the length-2 subarray is a 1-1 tie, not a majority.
  @Test
  void evenSplitIsNotStrictMajority() {
    assertThat(sut.countMajoritySubarrays(new int[] {2, 3}, 2)).isEqualTo(1);
  }

  // Step 5: a single target surrounded by non-targets can only be the majority of itself —
  //         any extension dilutes it to at most half.
  @Test
  void loneTargetAmongNonTargetsCountsOnlyItself() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 2, 1}, 2)).isEqualTo(1);
  }

  // Step 6: a single non-target need not break the majority — in [1,2,2] the subarrays
  //         [2], [2], [2,2], and [1,2,2] all keep target strictly above half (4 total).
  @Test
  void oneNonTargetStillLeavesTargetInMajority() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 2, 2}, 2)).isEqualTo(4);
  }

  // Step 7: subarrays are contiguous, so two targets split by non-targets do not combine —
  //         in [2,1,1,2] each target only counts itself, and the full array is a 2-2 tie.
  @Test
  void separatedTargetsDoNotCombineAcrossNonTargets() {
    assertThat(sut.countMajoritySubarrays(new int[] {2, 1, 1, 2}, 2)).isEqualTo(2);
  }

  // Step 8: LeetCode Example 3 — target absent from the array entirely yields zero.
  @Test
  void targetAbsentFromArrayYieldsZero() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 2, 3}, 4)).isEqualTo(0);
  }

  // Step 9: LeetCode Example 1 — [2], [2], [2,2], [1,2,2], and [2,2,3] all have 2 as majority.
  @Test
  void leetCodeExample1() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 2, 2, 3}, 2)).isEqualTo(5);
  }

  // Step 10: LeetCode Example 2 — when every element equals target, all n(n+1)/2 subarrays
  //          qualify (4*5/2 = 10).
  @Test
  void leetCodeExample2AllElementsEqualTarget() {
    assertThat(sut.countMajoritySubarrays(new int[] {1, 1, 1, 1}, 1)).isEqualTo(10);
  }

  // Step 11: values range up to 1e9 — equality, not magnitude, drives the count, so a pair of
  //          maximal values behaves exactly like Step 3 (3 subarrays).
  @Test
  void handlesMaximumElementValues() {
    assertThat(sut.countMajoritySubarrays(new int[] {1_000_000_000, 1_000_000_000}, 1_000_000_000))
        .isEqualTo(3);
  }

  // Step 12: the upper constraint bound (length 1000) — an all-target array has every one of its
  //          1000*1001/2 = 500500 subarrays as a majority, and the result must not overflow int.
  @Test
  void maximumLengthAllTargetArrayCountsEverySubarray() {
    int[] nums = new int[1000];
    Arrays.fill(nums, 7);
    assertThat(sut.countMajoritySubarrays(nums, 7)).isEqualTo(500500);
  }
}
