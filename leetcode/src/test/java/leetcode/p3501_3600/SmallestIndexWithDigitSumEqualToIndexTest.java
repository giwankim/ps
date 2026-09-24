package leetcode.p3501_3600;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class SmallestIndexWithDigitSumEqualToIndexTest {
  SmallestIndexWithDigitSumEqualToIndex sut = new SmallestIndexWithDigitSumEqualToIndex();

  // ===========================================================================================
  // The floor (Steps 1-4). 1 <= nums.length, so every case here has exactly one index — 0 — and
  // only a digit sum of 0 can match it.
  // ===========================================================================================

  // Step 1: smallest valid input that matches. The digit sum of 0 is 0, which equals index 0. A
  //         digit-sum loop that treats 0 as having no digits still gets 0 here, so this is the
  //         one value where every sane reading agrees
  @Test
  void singleZeroMatchesIndexZero() {
    assertThat(sut.smallestIndex(new int[] {0})).isZero();
  }

  // Step 2: the mirror of Step 1 — the smallest input with no match. The sentinel is -1, not 0
  //         and not nums.length
  @Test
  void singleNonZeroValueReturnsMinusOne() {
    assertThat(sut.smallestIndex(new int[] {5})).isEqualTo(-1);
  }

  // Step 3: indices are 0-based. The digit sum of 1 is 1, which would match "position 1" — a
  //         solution that compares against i + 1 answers 0
  @Test
  void singleOneDoesNotMatchBecauseIndicesAreZeroBased() {
    assertThat(sut.smallestIndex(new int[] {1})).isEqualTo(-1);
  }

  // Step 4: the largest value on its own. 1000 has digit sum 1, not 0, so index 0 is not matched. A
  //         solution that looks only at the last digit (nums[i] % 10) sees 0 and answers 0
  @Test
  void singleThousandDoesNotMatchIndexZero() {
    assertThat(sut.smallestIndex(new int[] {1000})).isEqualTo(-1);
  }

  // ===========================================================================================
  // The digit-sum rule (Steps 5-11). Every case pads the front with 999, whose digit sum is 27, so
  // the padding can only ever match index 27 and never does in these short arrays.
  // ===========================================================================================

  // Step 5: the value equal to its index is not enough. nums[10] = 10 has digit sum 1, not 10. A
  //         solution that compares nums[i] == i answers 10
  @Test
  void valueEqualToItsIndexIsNotAMatch() {
    assertThat(sut.smallestIndex(afterPadding(10, 10))).isEqualTo(-1);
  }

  // Step 6: the mirror of Step 5. nums[10] = 19 has digit sum 1 + 9 = 10, which matches, although
  //         19 != 10. The nums[i] == i solution answers -1
  @Test
  void valueWhoseDigitsSumToItsIndexMatches() {
    assertThat(sut.smallestIndex(afterPadding(10, 19))).isEqualTo(10);
  }

  // Step 7: the digits summed belong to the value, not to the index. The digit sum of index 10 is
  //         1, which equals nums[10] = 1 — a solution that swaps the two operands answers 10. The
  //         real digit sum of nums[10] is 1, which is not 10
  @Test
  void digitSumIsTakenOfTheValueNotTheIndex() {
    assertThat(sut.smallestIndex(afterPadding(10, 1))).isEqualTo(-1);
  }

  // Step 8: the digit sum is every digit, not the last one. 12 % 10 = 2 matches index 2 for a
  //         last-digit solution, which answers 2. The real digit sum of 12 is 3, which is not 2,
  //         and the first real match is nums[3] = 3
  @Test
  void digitSumAddsEveryDigitNotJustTheLast() {
    assertThat(sut.smallestIndex(new int[] {999, 999, 12, 3})).isEqualTo(3);
  }

  // Step 9: the four-digit ceiling. 1000 -> 1 + 0 + 0 + 0 = 1, matching index 1. A loop that stops
  //         at three digits, or at the first zero digit, gets this wrong
  @Test
  void fourDigitThousandHasDigitSumOne() {
    assertThat(sut.smallestIndex(afterPadding(1, 1000))).isEqualTo(1);
  }

  // Step 10: a zero in the middle of the number contributes nothing, but the digits past it still
  //          count. 101 -> 1 + 0 + 1 = 2 at index 2, and 1009 -> 1 + 0 + 0 + 9 = 10 at index 10.
  //          A loop whose condition is "the current digit is nonzero" stops early on both
  @Test
  void innerZeroDigitDoesNotStopTheSum() {
    assertThat(sut.smallestIndex(afterPadding(2, 101))).isEqualTo(2);
    assertThat(sut.smallestIndex(afterPadding(10, 1009))).isEqualTo(10);
  }

  // Step 11: zero has digit sum 0, so it can only ever match index 0. Here index 0 holds 999, and
  //          the zeros at indices 1 and 2 match nothing
  @Test
  void zeroValueMatchesOnlyIndexZero() {
    assertThat(sut.smallestIndex(new int[] {999, 0, 0})).isEqualTo(-1);
  }

  // ===========================================================================================
  // What is returned (Steps 12-13).
  // ===========================================================================================

  // Step 12: the answer is the index, not the value found there. nums[2] = 11 -> 1 + 1 = 2 matches,
  //          and a solution that returns nums[i] answers 11
  @Test
  void returnsTheIndexNotTheValue() {
    assertThat(sut.smallestIndex(new int[] {999, 999, 11})).isEqualTo(2);
  }

  // Step 13: when several indices match, the smallest wins. nums[1] = 10 -> 1 and nums[4] = 40 -> 4
  //          both match. A solution that scans to the end and keeps the last match answers 4
  @Test
  void smallestOfSeveralMatchingIndicesWins() {
    assertThat(sut.smallestIndex(new int[] {999, 10, 999, 999, 40})).isEqualTo(1);
  }

  // ===========================================================================================
  // The official examples (Steps 14-16).
  // ===========================================================================================

  // Step 14: the match is at the last index, so the scan has to pass over two misses first. A
  //          solution that gives up after the first mismatch answers -1
  @Test
  void leetCodeExample1() {
    assertThat(sut.smallestIndex(new int[] {1, 3, 2})).isEqualTo(2);
  }

  // Step 15: indices 1 and 2 both match (10 -> 1, 11 -> 2). This is the example the Explanation
  //          exists for — the value-equals-index reading finds nothing and answers -1, the
  //          keep-the-last-match reading answers 2, and returning the value answers 10
  @Test
  void leetCodeExample2() {
    assertThat(sut.smallestIndex(new int[] {1, 10, 11})).isEqualTo(1);
  }

  // Step 16: every digit sum is exactly one more than its index, so nothing matches — the
  //          off-by-one counterpart of Step 3. A 1-based solution answers 0
  @Test
  void leetCodeExample3() {
    assertThat(sut.smallestIndex(new int[] {1, 2, 3})).isEqualTo(-1);
  }

  // ===========================================================================================
  // Constraint bounds (Steps 17-20). At most 100 values of at most 1000, so any linear scan is
  // instant and the bounds test correctness at the extremes, not complexity. The largest digit sum
  // any value can have is 27 (from 999), so no index past 27 can ever match. The timeouts guard
  // against a digit loop that never terminates (forgetting to divide by 10), which would otherwise
  // hang the build rather than fail it. They run on a separate thread because JUnit's default
  // SAME_THREAD mode only checks elapsed time after the method returns.
  // ===========================================================================================

  // Step 17: maximum length, all zeros. Index 0 matches immediately and the other 99 zeros must
  //          not displace it
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllZerosAnswersIndexZero() {
    assertThat(sut.smallestIndex(filled(100, 0))).isZero();
  }

  // Step 18: maximum length, every value at the ceiling 1000 (digit sum 1). Only index 1 matches.
  //          The last-digit reading answers 0 and returning the value answers 1000
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllThousandsAnswersIndexOne() {
    assertThat(sut.smallestIndex(filled(100, 1000))).isEqualTo(1);
  }

  // Step 19: maximum length, every value 999 (digit sum 27, the largest possible). Index 27 is the
  //          highest index that can ever be an answer, and it is the answer here. A solution that
  //          caps the scan at index 26, or that returns the value, misses it (the latter answers
  //          999)
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllNineNineNinesAnswersIndexTwentySeven() {
    assertThat(sut.smallestIndex(filled(100, 999))).isEqualTo(27);
  }

  // Step 20: Step 19 with index 27 lowered to 998 (digit sum 26). Nothing in 100 values matches,
  //          so the scan runs to the end and must still answer -1
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithNoMatchReturnsMinusOne() {
    int[] nums = filled(100, 999);
    nums[27] = 998;

    assertThat(sut.smallestIndex(nums)).isEqualTo(-1);
  }

  // ===========================================================================================
  // Hygiene (Steps 21-22).
  // ===========================================================================================

  // Step 21: the array is the caller's. Overwriting nums[i] with its digit sum in place is a
  //          tempting way to avoid a temporary, and no functional step would notice
  @Test
  void inputArrayIsNotModified() {
    int[] nums = {1, 10, 11, 999, 1000};
    int[] original = nums.clone();

    sut.smallestIndex(nums);

    assertThat(nums).containsExactly(original);
  }

  // Step 22: several arrays answered by one instance, the largest in the middle and the lengths
  //          deliberately out of order. A solution that remembers a match or a position on the
  //          instance instead of resetting it per call answers the later arrays from the earlier
  //          ones' leftovers
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersArraysOfAnyLengthInAnyOrder() {
    assertThat(sut.smallestIndex(new int[] {1, 10, 11})).isEqualTo(1);
    assertThat(sut.smallestIndex(filled(100, 999))).isEqualTo(27);
    assertThat(sut.smallestIndex(new int[] {5})).isEqualTo(-1);
    assertThat(sut.smallestIndex(new int[] {0})).isZero();
    assertThat(sut.smallestIndex(new int[] {1, 3, 2})).isEqualTo(2);
  }

  /** {@code count} copies of 999 (digit sum 27, matching no index below 27), then {@code last}. */
  private static int[] afterPadding(int count, int last) {
    int[] nums = filled(count + 1, 999);
    nums[count] = last;
    return nums;
  }

  private static int[] filled(int n, int value) {
    int[] nums = new int[n];
    Arrays.fill(nums, value);
    return nums;
  }
}
