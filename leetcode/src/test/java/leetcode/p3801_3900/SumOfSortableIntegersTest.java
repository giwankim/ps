package leetcode.p3801_3900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class SumOfSortableIntegersTest {
  SumOfSortableIntegers sut = new SumOfSortableIntegers();

  // ===========================================================================================
  // The smallest arrays (1 <= n, so a single element is legal) (Steps 1-3).
  // ===========================================================================================

  // Step 1: the floor. n = 1 has the single divisor k = 1, and a one-element array is already
  //         sorted, so the answer is 1. Enumerating divisors in pairs d and n / d up to sqrt(n)
  //         and adding both members of each pair counts k = 1 twice here and answers 2
  @Test
  void singleElementIsSortableByItsOnlyDivisor() {
    assertThat(sut.sortableIntegers(new int[] {7})).isEqualTo(1);
  }

  // Step 2: two elements out of order. k = 1 leaves every element where it is, so it fails, but
  //         k = 2 rotates the whole array once and swaps them. Stopping the scan at the first
  //         divisor that fails answers 0, and asking only whether each block could be rotated
  //         into order on its own accepts k = 1 too, since a one-element block always can, and
  //         answers 3
  @Test
  void twoElementsOutOfOrderAreSortableOnlyAsOneBlock() {
    assertThat(sut.sortableIntegers(new int[] {8, 5})).isEqualTo(2);
  }

  // Step 3: non-decreasing, not increasing. Two equal elements are already in order, so k = 1
  //         and k = 2 both count. A strict comparison anywhere breaks this: rejecting equal
  //         neighbors across a block boundary answers 2, and counting a cyclic descent with >=
  //         finds two of them in the single block of k = 2 and answers 1
  @Test
  void twoEqualElementsAreAlreadyNonDecreasing() {
    assertThat(sut.sortableIntegers(new int[] {4, 4})).isEqualTo(3);
  }

  // ===========================================================================================
  // One block at a time: what a rotation can and cannot do (Steps 4-7).
  // ===========================================================================================

  // Step 4: an already sorted array needs no rotation, so every divisor counts and the answer is
  //         the divisor sum 1 + 2 + 4 = 7. Only divisors: summing every k from 1 to n, with a
  //         short last block, answers 10. Counting sqrt(4) = 2 twice answers 9, checking only
  //         k = 1 and k = n answers 5, and counting the sortable k instead of summing them
  //         answers 3
  @Test
  void sortedArrayMakesEveryDivisorSortable() {
    assertThat(sut.sortableIntegers(new int[] {1, 2, 3, 4})).isEqualTo(7);
  }

  // Step 5: a rotation is not a sort. The only multi-element block is [1, 3, 2], and its other
  //         rotations [3, 2, 1] and [2, 1, 3] are not sorted either. Checking that each block
  //         holds the right values in any order answers 3, and so does counting descents without
  //         the wrap-around pair: 3 > 2 is the only inner descent, and the second one is the last
  //         element 2 against the first element 1
  @Test
  void rotationCannotReorderWithinTheBlock() {
    assertThat(sut.sortableIntegers(new int[] {1, 3, 2})).isZero();
  }

  // Step 6: "any number of times". The block needs two left shifts, or three right ones, to
  //         reach [1, 2, 3, 4, 5]. Trying only zero or one shift in either direction answers 0
  @Test
  void rotationMayShiftByMoreThanOnePlace() {
    assertThat(sut.sortableIntegers(new int[] {3, 4, 5, 1, 2})).isEqualTo(5);
  }

  // Step 7: with a repeated minimum, the sorted rotation starts right after the descent, at
  //         index 2, giving [1, 1, 1, 2]. Rotating to the first copy of the minimum leaves
  //         [1, 2, 1, 1] and rotating to the last copy gives [1, 1, 2, 1], so both answer 0
  @Test
  void repeatedMinimumRotationStartsAfterTheDescent() {
    assertThat(sut.sortableIntegers(new int[] {1, 2, 1, 1})).isEqualTo(4);
  }

  // ===========================================================================================
  // Several blocks: each rotates on its own, but together they must line up (Steps 8-16).
  // ===========================================================================================

  // Step 8: a middle divisor alone. k = 2 rotates [2, 1] and [4, 3] into [1, 2, 3, 4], while
  //         k = 1 and k = 4 both fail. Checking only k = 1 and k = n answers 0, and assuming
  //         that once one divisor works every larger one does too answers 6
  @Test
  void middleDivisorCanBeTheOnlySortableOne() {
    assertThat(sut.sortableIntegers(new int[] {2, 1, 4, 3})).isEqualTo(2);
  }

  // Step 9: blocks rotate independently. For k = 2 the first block [1, 2] stays put while
  //         [4, 3] shifts once. Applying one shared shift to every block answers 0
  @Test
  void blocksRotateByIndependentAmounts() {
    assertThat(sut.sortableIntegers(new int[] {1, 2, 4, 3})).isEqualTo(2);
  }

  // Step 10: every block sorted is not enough. For k = 2 the blocks [3, 4] and [1, 2] are each
  //          in order but their concatenation is not, and no rotation moves a value into another
  //          block. Only k = 4 counts. Checking each block in isolation answers 7
  @Test
  void sortedBlocksInTheWrongOrderDoNotCount() {
    assertThat(sut.sortableIntegers(new int[] {3, 4, 1, 2})).isEqualTo(4);
  }

  // Step 11: equal values may meet across a block boundary. For k = 2, [2, 1] becomes [1, 2] and
  //          meets [2, 2] at 2 <= 2, so k = 2 and k = 4 both count. Requiring each block's
  //          maximum to be strictly below the next block's minimum answers 4
  @Test
  void equalValuesMayMeetAcrossABlockBoundary() {
    assertThat(sut.sortableIntegers(new int[] {2, 1, 2, 2})).isEqualTo(6);
  }

  // Step 12: the last block is checked like any other. Only the final pair is out of order, so
  //          k = 2 works by rotating [6, 5], while k = 3 fails on [4, 6, 5] and k = 6 on the
  //          whole array. A block loop that stops one block early answers 11
  @Test
  void lastBlockIsCheckedLikeAnyOther() {
    assertThat(sut.sortableIntegers(new int[] {1, 2, 3, 4, 6, 5})).isEqualTo(2);
  }

  // Step 13: a perfect square n = 9 where only the middle divisor sqrt(9) = 3 works, each block
  //          being a sorted run of three shifted right once. Enumerating divisors in pairs d and
  //          n / d without noticing d = n / d adds 3 twice and answers 6
  @Test
  void squareRootDivisorIsCountedOnce() {
    assertThat(sut.sortableIntegers(new int[] {3, 1, 2, 6, 4, 5, 9, 7, 8})).isEqualTo(3);
  }

  // Step 14: two sortable divisors say nothing about their multiples. k = 2 rotates [4, 3] and
  //          k = 3 rotates [3, 4, 3] into [3, 3, 4], yet k = 6 fails because the whole array has
  //          two cyclic descents, 4 > 3 inside and 3 > 1 wrapping around. The answer is
  //          2 + 3 = 5, and assuming every multiple of a sortable divisor is sortable answers 11
  @Test
  void k2AndK3SortableWhileK6IsNot() {
    assertThat(sut.sortableIntegers(new int[] {1, 1, 2, 3, 4, 3})).isEqualTo(5);
  }

  // Step 15: one displaced value near the end. The 2 belongs at index 5 but sits at index 4, and
  //          for every k >= 2 both positions fall in the last block, which a single right shift
  //          repairs. Only k = 1 fails, so the answer is 2 + 3 + 6 = 11
  @Test
  void valueDisplacedOneLeftOfItsSlotSpoilsOnlyK1() {
    assertThat(sut.sortableIntegers(new int[] {1, 1, 1, 1, 2, 1})).isEqualTo(11);
  }

  // Step 16: the mirror of Step 15. The 2 belongs at the end but sits at index 0, so for every
  //          k < 6 the first block holds a value that sorted order puts in the last block, and
  //          no rotation can carry it across. Only k = 6 works, by one left shift
  @Test
  void valueDisplacedToTheFrontLeavesOnlyTheWholeArray() {
    assertThat(sut.sortableIntegers(new int[] {2, 1, 1, 1, 1, 1})).isEqualTo(6);
  }

  // ===========================================================================================
  // The official examples, and a broad mixed case (Steps 17-20).
  // ===========================================================================================

  // Step 17: LeetCode Example 1. k = 1 fails yet k = 3 works by one left shift, so sortability
  //          is not inherited from smaller divisors: a scan that stops at the first failing
  //          divisor answers 0
  @Test
  void leetCodeExample1() {
    assertThat(sut.sortableIntegers(new int[] {3, 1, 2})).isEqualTo(3);
  }

  // Step 18: LeetCode Example 2. A strictly decreasing block of three has two cyclic descents
  //          however it is turned, so no rotation reverses it and nothing counts. Treating a
  //          block as freely rearrangeable answers 3
  @Test
  void leetCodeExample2() {
    assertThat(sut.sortableIntegers(new int[] {7, 6, 5})).isZero();
  }

  // Step 19: LeetCode Example 3. An array that is already sorted makes every divisor sortable,
  //          and the answer is their sum 1 + 2 = 3, not their count 2
  @Test
  void leetCodeExample3() {
    assertThat(sut.sortableIntegers(new int[] {5, 8})).isEqualTo(3);
  }

  // Step 20: a mixed twelve-element case. k = 4 and k = 6 both work by shifting the leading 4 to
  //          the end of the first block, which for k = 4 makes it meet the next block's 4 across
  //          the boundary. Their gcd k = 2 and their lcm k = 12 fail, as do k = 1 and k = 3, so
  //          the answer is 4 + 6 = 10. A strict boundary comparison loses k = 4 and answers 6,
  //          and one shared shift for every block answers 0
  @Test
  void mixedTwelveElementArraySumsK4AndK6() {
    assertThat(sut.sortableIntegers(new int[] {4, 2, 2, 3, 4, 4, 5, 7, 8, 9, 9, 9}))
        .isEqualTo(10);
  }

  // ===========================================================================================
  // Upper end of the constraints (1 <= n <= 10^5, 1 <= nums[i] <= 10^5) (Steps 21-27).
  //
  // A block can be checked in O(k): count its cyclic descents and compare its minimum with the
  // previous block's maximum. No n in range has more than 128 divisors (83160 and 98280 reach
  // it), so the intended scan is O(n * d(n)), about 10^7 steps and a few milliseconds.
  //
  // The trap is finding each block's rotation by trying shifts one at a time. On a sorted array
  // the first shift tried matches, so Steps 21-23 are fast for any such search. With distinct
  // values a wrong shift fails on its first comparison, so Step 24 is fast too, unless each
  // candidate rotation is built as a fresh copy, which costs k per shift whatever the values.
  // Among equal values, though, every wrong shift matches a long run before it fails, and one
  // block costs about k^2 / 2 steps, some 5 * 10^9 for the single block of k = n. Steps 25-27
  // hide one 2 among 1s so that a left-to-right shift order, a right-to-left order, and an order
  // alternating between the two are each the slow one. Measured with a naive Java search, each
  // takes 5.5 to 5.8 seconds on its step, against about 20 milliseconds for the intended scan.
  // Hint 4's doubled-block search is the left-to-right order in disguise unless the substring
  // search itself is linear (KMP or a Z-function).
  //
  // The 2-second timeouts sit at about a hundred times the intended scan, so they are evidence
  // about complexity class rather than constant factors. The largest possible answer is the
  // divisor sum of 98280, which is 403,200, so int is enough.
  // ===========================================================================================

  // Step 21: full length, already sorted. n = 10^5 = 2^5 * 5^5 has 36 divisors, and all of them
  //          count, so the answer is their sum, 246,078
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthSortedArraySumsEveryDivisor() {
    assertThat(sut.sortableIntegers(ramp(100_000))).isEqualTo(246_078);
  }

  // Step 22: full length at the value ceiling, every element 10^5. Every comparison is a tie, so
  //          each block has no descent at all and meets the next one at equality: again 246,078
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthAtTheValueCeilingIsAllTies() {
    assertThat(sut.sortableIntegers(filled(100_000, 100_000))).isEqualTo(246_078);
  }

  // Step 23: n = 98,280 = 2^3 * 3^3 * 5 * 7 * 13 has 128 divisors, the most any n in range has,
  //          and the largest divisor sum. Sorted, it reaches the largest possible answer, 403,200
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void mostDivisorsReachTheLargestPossibleAnswer() {
    assertThat(sut.sortableIntegers(ramp(98_280))).isEqualTo(403_200);
  }

  // Step 24: the sorted ramp rotated by half, so 50,001 comes first and 50,000 last. Only k = n
  //          works, by a shift of n / 2. For every smaller k the first block holds values that
  //          belong further right
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthRampRotatedByHalfIsSortableOnlyWhole() {
    int[] nums = new int[100_000];
    Arrays.setAll(nums, i -> (i + 50_000) % 100_000 + 1);

    assertThat(sut.sortableIntegers(nums)).isEqualTo(100_000);
  }

  // Step 25: Step 15 at full length, the 2 at index 99,998. Every divisor but k = 1 works, each
  //          by shifting its last block once to the right, so the answer is 246,078 - 1. A
  //          left-to-right shift search walks through almost all k shifts of that last block
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthValueDisplacedOneLeftOfItsSlotSpoilsOnlyK1() {
    assertThat(sut.sortableIntegers(onesWithTwoAt(100_000, 99_998))).isEqualTo(246_077);
  }

  // Step 26: Step 16 at full length, the 2 at index 0. Only k = n works, by a single left shift,
  //          which is the last shift a right-to-left search reaches
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthValueDisplacedToTheFrontLeavesOnlyTheWholeArray() {
    assertThat(sut.sortableIntegers(onesWithTwoAt(100_000, 0))).isEqualTo(100_000);
  }

  // Step 27: the 2 at index 49,999, halfway along. Only k = n works, and it needs a shift of
  //          n / 2 in either direction, which is the last one a search alternating between left
  //          and right shifts reaches
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxLengthValueDisplacedToTheMiddleLeavesOnlyTheWholeArray() {
    assertThat(sut.sortableIntegers(onesWithTwoAt(100_000, 49_999))).isEqualTo(100_000);
  }

  // ===========================================================================================
  // Hygiene (Step 28).
  // ===========================================================================================

  // Step 28: one instance answers arrays of different lengths, largest in the middle. A solution
  //          that keeps the sorted copy or the divisor list on the instance, filled on the first
  //          call, answers later calls about the wrong array
  @Test
  @Timeout(value = 2, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersArraysOfAnyLengthInAnyOrder() {
    assertThat(sut.sortableIntegers(new int[] {3, 1, 2})).isEqualTo(3);
    assertThat(sut.sortableIntegers(ramp(98_280))).isEqualTo(403_200);
    assertThat(sut.sortableIntegers(new int[] {7})).isEqualTo(1);
    assertThat(sut.sortableIntegers(new int[] {1, 1, 2, 3, 4, 3})).isEqualTo(5);
    assertThat(sut.sortableIntegers(new int[] {2, 1, 4, 3})).isEqualTo(2);
  }

  private static int[] ramp(int n) {
    int[] nums = new int[n];
    Arrays.setAll(nums, i -> i + 1);
    return nums;
  }

  private static int[] filled(int n, int value) {
    int[] nums = new int[n];
    Arrays.fill(nums, value);
    return nums;
  }

  private static int[] onesWithTwoAt(int n, int index) {
    int[] nums = filled(n, 1);
    nums[index] = 2;
    return nums;
  }
}
