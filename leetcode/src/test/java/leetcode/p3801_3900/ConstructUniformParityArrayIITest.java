package leetcode.p3801_3900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ConstructUniformParityArrayIITest {
  ConstructUniformParityArrayII sut = new ConstructUniformParityArrayII();

  // ===========================================================================================
  // The floor, n == 1 (Steps 1-2). Keeping nums1[i] is always one of the two choices, so a
  // single element is uniform by itself and needs no partner index j.
  // ===========================================================================================

  // Step 1: the smallest legal input (1 <= n) holds one odd value. There is no j != i, so the
  //         subtraction rule is unavailable and nums2 = [1] is the only construction, already
  //         uniform. A solution that demands a partner index before it can answer, or that only
  //         ever tries to reach all-even, answers false
  @Test
  void n1OddKeepsItself() {
    assertThat(sut.uniformArray(new int[] {1})).isTrue();
  }

  // Step 2: the mirror of Step 1, one even value. nums2 = [2] is uniform. A solution that only
  //         ever tries to reach all-odd answers false, and so does one that reduces the whole
  //         problem to "the smallest odd is below the smallest even" with an infinite sentinel
  //         for the missing odd and no separate all-even check
  @Test
  void n1EvenKeepsItself() {
    assertThat(sut.uniformArray(new int[] {2})).isTrue();
  }

  // ===========================================================================================
  // One rule per step, on pairs (Steps 3-6). A subtraction is legal only when
  // nums1[i] - nums1[j] >= 1, so index i may borrow from a smaller value and never from a
  // larger one. That single clause is what separates this problem from 3875, where every
  // input was constructible.
  // ===========================================================================================

  // Step 3: the even is larger than the odd, so 4 - 1 = 3 is a legal subtraction and
  //         nums2 = [1, 3] is all odd. All-even is out of reach: 1 has nothing smaller to
  //         subtract and stays odd. A solution that only reports whether nums1 is already
  //         uniform answers false, as does one that only lets index i borrow from a later
  //         index j > i (index 1 has no later partner and is stuck at 4)
  @Test
  void evenSubtractsTheSmallerOdd() {
    assertThat(sut.uniformArray(new int[] {1, 4})).isTrue();
  }

  // Step 4: the mirror of Step 3, the even is the smaller value. 4 - 7 = -3 fails the
  //         nums1[i] - nums1[j] >= 1 rule, so 4 must stay 4 and even, while 7 keeps 7 or takes
  //         7 - 4 = 3, odd either way. No uniform nums2 exists. The 3875 solution, which allows
  //         a negative difference (or simply answers true for every input), answers true here,
  //         and so does a solution with the parity slip that odd minus a smaller even is even
  @Test
  void evenCannotSubtractALargerOdd() {
    assertThat(sut.uniformArray(new int[] {4, 7})).isFalse();
  }

  // Step 5: the difference is exactly 1. 2 - 1 = 1 satisfies >= 1, so nums2 = [1, 1] is all
  //         odd. A solution that reads the rule as a strict "greater than 1" finds no legal
  //         subtraction for 2 and answers false
  @Test
  void differenceOfExactlyOneIsAllowed() {
    assertThat(sut.uniformArray(new int[] {1, 2})).isTrue();
  }

  // Step 6: both values are odd, so nums2 = nums1 with no subtraction at all, and the order
  //         (larger first) does not matter. A solution that encodes "no even present" with a
  //         sentinel of 0 or -1 for the smallest even, and then asks whether the smallest odd
  //         is below it, answers false
  @Test
  void allOddNeedsNoSubtraction() {
    assertThat(sut.uniformArray(new int[] {3, 1})).isTrue();
  }

  // ===========================================================================================
  // The smallest value decides (Steps 7-9). Odd minus a smaller odd is even and even minus a
  // smaller odd is odd, but the smallest element of all has nothing to subtract and keeps its
  // parity. So all-even is reachable only when nothing is odd, and all-odd is reachable only
  // when the smallest even lies above the smallest odd.
  // ===========================================================================================

  // Step 7: two odds and a smaller even. 5 - 3 = 2 does turn 5 even, but 3 has no smaller odd
  //         (3 - 2 = 1 is odd), so all-even fails on the smallest odd. All-odd fails on 2,
  //         which has no odd below it. A solution that lets odds "pair off" (all-even whenever
  //         the odd count is 0 or at least 2) answers true
  @Test
  void twoOddsCannotBothTurnEven() {
    assertThat(sut.uniformArray(new int[] {2, 3, 5})).isFalse();
  }

  // Step 8: the only odd is larger than every even. No even can subtract 7, and 7 minus any of
  //         them (5, 3, 1) is odd, so 7 can never turn even either. A solution that answers
  //         true as soon as both parities are present ("some odd exists, so the evens can
  //         subtract it") answers true
  @Test
  void oddAboveEveryEvenHelpsNothing() {
    assertThat(sut.uniformArray(new int[] {2, 4, 6, 7})).isFalse();
  }

  // Step 9: the odd sits between the evens. 4 - 3 = 1 turns 4 odd, but 2 has no odd below it,
  //         and one stuck even is enough. A solution that compares the largest even (or any
  //         one even) against the smallest odd, instead of the smallest even, answers true
  @Test
  void everyEvenMustClearTheSmallestOdd() {
    assertThat(sut.uniformArray(new int[] {2, 3, 4})).isFalse();
  }

  // ===========================================================================================
  // Partner reuse and position (Steps 10-11). The same j may serve every i, and j may sit on
  // either side of i.
  // ===========================================================================================

  // Step 10: four evens and the single odd 1 at the front. Every even subtracts index 0,
  //          giving nums2 = [1, 1, 3, 5, 7]. A reading where each j may serve at most one i
  //          answers false (four evens compete for one partner), and so does the j > i reading
  //          (no even has an odd after it)
  @Test
  void oneOddServesEveryEven() {
    assertThat(sut.uniformArray(new int[] {1, 2, 4, 6, 8})).isTrue();
  }

  // Step 11: the mirror of Step 10, the lone odd moved to the end. The j < i reading answers
  //          false here (no even has an odd before it) while passing Step 10
  @Test
  void partnerMaySitAfterEveryEvenThatUsesIt() {
    assertThat(sut.uniformArray(new int[] {2, 4, 6, 8, 1})).isTrue();
  }

  // ===========================================================================================
  // The official examples (Steps 12-14).
  // ===========================================================================================

  // Step 12: LeetCode Example 1. The printed construction keeps 1 and 7 and sets
  //          nums2[1] = 4 - 1 = 3, so the answer needs one subtraction and a solution that only
  //          checks whether nums1 is already uniform answers false
  @Test
  void leetCodeExample1() {
    assertThat(sut.uniformArray(new int[] {1, 4, 7})).isTrue();
  }

  // Step 13: LeetCode Example 2. The same input was Example 1 of 3875 with answer true, via
  //          nums2 = [-1, 3]. Here 2 - 3 = -1 is illegal, 2 is stuck even, and 3 is stuck odd
  //          (3 - 2 = 1). This is the example that exists to rule out the 3875 reading
  @Test
  void leetCodeExample2() {
    assertThat(sut.uniformArray(new int[] {2, 3})).isFalse();
  }

  // Step 14: LeetCode Example 3. Both values are even, so nums2 = nums1. A solution that only
  //          tries to reach all-odd answers false: no odd value exists to subtract
  @Test
  void leetCodeExample3() {
    assertThat(sut.uniformArray(new int[] {4, 6})).isTrue();
  }

  // ===========================================================================================
  // The constraint bounds (Steps 15-20). Values reach 10^9, so a table indexed by value does
  // not fit in memory, and n reaches 10^5, where an all-pairs O(n^2) search needs 10^10
  // operations and cannot finish. A per-element search that stops at the first usable partner
  // is O(n^2) only in the worst layout, and Steps 18-19 are that layout. A single O(n) pass
  // over the two minima finishes in microseconds.
  // ===========================================================================================

  // Step 15: the two largest legal values. 10^9 is even and sits directly above the odd
  //          10^9 - 1, so the only subtraction has difference exactly 1 and the answer is true.
  //          Differences never leave int range, but a boolean[] or BitSet sized by the maximum
  //          value needs 10^9 entries
  @Test
  void largestValuesDifferByOne() {
    assertThat(sut.uniformArray(new int[] {1_000_000_000, 999_999_999})).isTrue();
  }

  // Step 16: maximum n, every even value from 2 to 200000. Step 14 at scale: nothing needs
  //          subtracting, and no odd can be manufactured
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllEven() {
    int[] nums1 = new int[100_000];
    Arrays.setAll(nums1, i -> 2 * i + 2);

    assertThat(sut.uniformArray(nums1)).isTrue();
  }

  // Step 17: maximum n, every odd value from 1 to 199999. Step 6 at scale
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAllOdd() {
    int[] nums1 = new int[100_000];
    Arrays.setAll(nums1, i -> 2 * i + 1);

    assertThat(sut.uniformArray(nums1)).isTrue();
  }

  // Step 18: 99999 evens followed by the lone odd 1. Step 11 at scale: the answer is true, but
  //          a per-element scan for a smaller odd walks past every even before it reaches the
  //          last index, 99999 times over, which is 10^10 steps
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void loneOddAfterEveryEvenAtMaximumLength() {
    int[] nums1 = new int[100_000];
    Arrays.setAll(nums1, i -> i < 99_999 ? 2 * i + 2 : 1);

    assertThat(sut.uniformArray(nums1)).isTrue();
  }

  // Step 19: the evens 4 to 199998, then 3, then 2 last. Step 9 at scale: every even but the
  //          last finds 3 to subtract, and only 2 is stuck, so the answer is false. A scan that
  //          stops at the first stuck element still walks 99998 evens for each of the 99998
  //          that succeed before it reaches the 2
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void smallestEvenLastAtMaximumLength() {
    int[] nums1 = new int[100_000];
    Arrays.setAll(nums1, i -> i < 99_998 ? 2 * i + 4 : i == 99_998 ? 3 : 2);

    assertThat(sut.uniformArray(nums1)).isFalse();
  }

  // Step 20: maximum n at the top of the value range, 999900000 through 999999999 in order.
  //          The smallest value is even and 50000 odds are present, so the answer is false
  //          even though every other element has a legal choice. A value-indexed table is at
  //          its largest here
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthAtTheTopOfTheRange() {
    int[] nums1 = new int[100_000];
    Arrays.setAll(nums1, i -> 999_900_000 + i);

    assertThat(sut.uniformArray(nums1)).isFalse();
  }

  // ===========================================================================================
  // Hygiene (Steps 21-22).
  // ===========================================================================================

  // Step 21: the array is the caller's. Sorting it to read the parity off nums1[0], or
  //          overwriting entries with their chosen differences to build nums2 in place, is a
  //          tempting shortcut
  @Test
  void inputArrayIsNotModified() {
    int[] nums1 = {6, 3, 4, 2};
    int[] original = nums1.clone();

    sut.uniformArray(nums1);

    assertThat(nums1).containsExactly(original);
  }

  // Step 22: several arrays answered by one instance, the largest in the middle, the lengths
  //          deliberately out of order, and the verdicts alternating so that a cached answer
  //          fails on the very next call. A smallest odd kept on the instance from the
  //          100000-element array (where it is 1) turns the false verdicts that follow it into
  //          true
  @Test
  void oneInstanceAnswersArraysOfAnyLengthInAnyOrder() {
    int[] full = new int[100_000];
    Arrays.setAll(full, i -> i < 99_999 ? 2 * i + 2 : 1);

    assertThat(sut.uniformArray(new int[] {2, 3})).isFalse();
    assertThat(sut.uniformArray(new int[] {1, 4, 7})).isTrue();
    assertThat(sut.uniformArray(full)).isTrue();
    assertThat(sut.uniformArray(new int[] {2, 3, 4})).isFalse();
    assertThat(sut.uniformArray(new int[] {2})).isTrue();
    assertThat(sut.uniformArray(new int[] {2, 4, 6, 7})).isFalse();
  }
}
