package leetcode.p3701_3800;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class LongestSubsequenceWithNonZeroBitwiseXORTest {
  LongestSubsequenceWithNonZeroBitwiseXOR sut = new LongestSubsequenceWithNonZeroBitwiseXOR();

  // Step 1: smallest valid input (1 <= nums.length) — a lone non-zero element already XORs to
  // itself, so it is its own answer
  @Test
  void singleNonZeroElementIsItsOwnSubsequence() {
    assertThat(sut.longestSubsequence(new int[] {5})).isEqualTo(1);
  }

  // Step 2: the other single-element case — a lone 0 has no non-zero subsequence at all. The empty
  // subsequence XORs to 0, so it never rescues an answer; this is the "return 0" branch
  @Test
  void singleZeroHasNoValidSubsequence() {
    assertThat(sut.longestSubsequence(new int[] {0})).isEqualTo(0);
  }

  // Step 3: LeetCode Example 2 — 2 XOR 3 XOR 4 = 5, so nothing needs dropping and the answer is the
  // full length. Taking the entire array is always the first thing to try
  @Test
  void wholeArrayCountsWhenItsXorIsNonZero() {
    assertThat(sut.longestSubsequence(new int[] {2, 3, 4})).isEqualTo(3);
  }

  // Step 4: the smallest input that forces a drop — 7 XOR 7 = 0, so one of the pair must go and a
  // single 7 remains
  @Test
  void equalPairForcesDroppingOneElement() {
    assertThat(sut.longestSubsequence(new int[] {7, 7})).isEqualTo(1);
  }

  // Step 5: LeetCode Example 1 — 1 XOR 2 XOR 3 = 0. Removing any one element x leaves 0 XOR x = x,
  // so exactly one drop is ever needed, never two
  @Test
  void xorZeroWholeArrayDropsExactlyOne() {
    assertThat(sut.longestSubsequence(new int[] {1, 2, 3})).isEqualTo(2);
  }

  // Step 6: the trap steps 4 and 5 set — "total is 0, so return length - 1" is wrong here. Every
  // subsequence of an all-zero array XORs to 0, so the answer is 0, not 2
  @Test
  void allZerosReturnZeroNotLengthMinusOne() {
    assertThat(sut.longestSubsequence(new int[] {0, 0, 0})).isEqualTo(0);
  }

  // Step 7: zeros are legal values (0 <= nums[i]) and they pad the answer for free — the total is
  // 5,
  // so all three elements count. A solution that filters zeros out would return 1
  @Test
  void zerosPadTheAnswerWhenTotalIsNonZero() {
    assertThat(sut.longestSubsequence(new int[] {0, 0, 5})).isEqualTo(3);
  }

  // Step 8: the second trap — the total is 0, so one element must go, but it has to be a non-zero
  // one. Dropping the 0 leaves 5 XOR 5 = 0; dropping a 5 leaves 0 XOR 5 = 5. Either way the length
  // is 2, and a solution that removes the zero instead would report 0 or 3
  @Test
  void droppedElementMustBeNonZero() {
    assertThat(sut.longestSubsequence(new int[] {0, 5, 5})).isEqualTo(2);
  }

  // Step 9: several cancelling pairs still cost only one element — the count of vanishing pairs has
  // no bearing on how many drops are needed
  @Test
  void manyCancellingPairsStillLoseOnlyOne() {
    assertThat(sut.longestSubsequence(new int[] {1, 1, 2, 2, 3, 3})).isEqualTo(5);
  }

  // Step 10: the value ceiling (nums[i] <= 10^9, which needs 30 bits) with a cancelling pair — the
  // running XOR must be a full int, not a narrower accumulator
  @Test
  void maximumValuesThatCancelStillLeaveOne() {
    assertThat(sut.longestSubsequence(new int[] {1_000_000_000, 1_000_000_000})).isEqualTo(1);
  }

  // Step 11: two distinct ceiling-sized values necessarily differ in some bit, so their XOR is
  // non-zero and both count
  @Test
  void distinctMaximumValuesXorToNonZero() {
    assertThat(sut.longestSubsequence(new int[] {1_000_000_000, 999_999_999})).isEqualTo(2);
  }

  // Step 12: the length ceiling (nums.length <= 10^5) taking the all-zero branch. Enumerating
  // subsequences to prove none works is 2^n and never returns; the answer must fall out of a single
  // linear pass
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void ceilingSizedAllZeroArrayReturnsZero() {
    assertThat(sut.longestSubsequence(filled(100_000, 0))).isEqualTo(0);
  }

  // Step 13: the length ceiling taking the drop-one branch — 100,000 copies of 7 is an even count,
  // so the total XOR is 0 and exactly one element is surrendered
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void ceilingSizedCancellingArrayLosesOnlyOne() {
    assertThat(sut.longestSubsequence(filled(100_000, 7))).isEqualTo(99_999);
  }

  // Step 14: the length ceiling taking the take-everything branch — one lonely 1 among 99,999 zeros
  // makes the total non-zero, and all 100,000 elements ride along
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void ceilingSizedArrayWithOneNonZeroKeepsEveryElement() {
    int[] nums = filled(100_000, 0);
    nums[0] = 1;
    assertThat(sut.longestSubsequence(nums)).isEqualTo(100_000);
  }

  /** An array of {@code n} copies of {@code value}. */
  private static int[] filled(int n, int value) {
    int[] nums = new int[n];
    Arrays.fill(nums, value);
    return nums;
  }
}
