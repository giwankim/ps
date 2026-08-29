package leetcode.p2901_3000;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MakeLexicographicallySmallestArrayBySwappingElementsTest {
  MakeLexicographicallySmallestArrayBySwappingElements sut =
      new MakeLexicographicallySmallestArrayBySwappingElements();

  // ===========================================================================================
  // Single swaps and the limit boundary (Steps 1-4).
  // ===========================================================================================

  // Step 1: smallest input the constraints permit (1 <= nums.length) — one element, no second
  //         index to swap with, so the array comes back as it is
  @Test
  void singleElementHasNothingToSwap() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {7}, 1)).containsExactly(7);
  }

  // Step 2: the basic operation — two values within limit of each other trade places when that
  //         makes the array smaller. A solution that never swaps answers [3,1]
  @Test
  void twoElementsWithinLimitSwapIntoOrder() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {3, 1}, 2)).containsExactly(1, 3);
  }

  // Step 3: a difference of limit + 1 blocks the swap — 5 and 1 differ by 4 against limit 3, so
  //         the smaller value is stuck behind the larger one. A plain sort answers [1,5]
  @Test
  void differenceOfLimitPlusOneCannotSwap() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {5, 1}, 3)).containsExactly(5, 1);
  }

  // Step 4: the boundary of Step 3 moved one closer — 4 and 1 differ by exactly limit, and the
  //         condition is "at most", not "strictly less". A solution comparing with < answers [4,1]
  @Test
  void differenceExactlyAtLimitStillSwaps() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {4, 1}, 3)).containsExactly(1, 4);
  }

  // ===========================================================================================
  // Chains and components (Steps 5-9).
  //
  // Swappability composes: values that can never swap directly still trade places through an
  // intermediate value, so the reachable arrangements are exactly the permutations within each
  // connected component, and components are runs of sorted values whose neighbors are within
  // limit of each other.
  // ===========================================================================================

  // Step 5: 7 and 3 differ by 4 against limit 2 and can never swap directly, but both are within
  //         2 of the 5 between them — sorted values 3, 5, 7 have gaps 2, 2, one component, so the
  //         whole array sorts. A bubble pass that only swaps adjacent positions within limit
  //         answers [7,3,5] unchanged, because the one out-of-order neighbor pair is too far apart
  @Test
  void valuesTradeThroughAnIntermediateChain() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {7, 3, 5}, 2)).containsExactly(3, 5, 7);
  }

  // Step 6: equal values differ by 0 and always share a component. Sorted values 1, 2, 5, 5 have
  //         gaps 1, 3, 0 — components are the pair 1, 2 owning positions 1 and 3, and the two 5s
  //         owning positions 0 and 2, so the 5s never move. A plain sort answers [1,2,5,5]
  @Test
  void duplicatesShareAComponentWhileDistantValuesStayPut() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {5, 2, 5, 1}, 1))
        .containsExactly(5, 1, 5, 2);
  }

  // Step 7: two components interleaved by position — the pair 1, 3 owns positions 1 and 3 while
  //         the pair 10, 12 owns positions 0 and 2, and each component sorts its own values into
  //         its own positions independently. A plain sort answers [1,3,10,12], and so does any
  //         solution that hands a component's sorted values a contiguous block of positions
  @Test
  void interleavedComponentsRearrangeIndependently() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {12, 3, 10, 1}, 2))
        .containsExactly(10, 1, 12, 3);
  }

  // Step 8: the component 4, 5, 6 owns positions 0, 2, 3, so the 4 travels from position 2 to
  //         position 0, hopping over the untouchable 9 in between. A greedy that swaps the front
  //         with the smallest value directly within limit of it finds only the 5 next to 6 and
  //         answers 5 at position 0 — the 4 arrives only through the chain 4 with 5, then 5 with 6
  @Test
  void smallestComponentValueTravelsToItsEarliestPosition() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {6, 9, 4, 5}, 1))
        .containsExactly(4, 9, 5, 6);
  }

  // Step 9: a fully descending run with every sorted gap equal to 1 is one component and sorts
  //         completely. A simulation that keeps applying improving adjacent swaps strands itself
  //         at [4,5,2,3,1] — after its first swaps, every remaining out-of-order neighbor pair
  //         differs by 2 — which is why the answer must come from components, not from replaying
  //         operations on current values
  @Test
  void descendingChainSortsCompletely() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {5, 4, 3, 2, 1}, 1))
        .containsExactly(1, 2, 3, 4, 5);
  }

  // ===========================================================================================
  // The worked examples (Steps 10-12).
  // ===========================================================================================

  // Step 10: LeetCode Example 1 — components 1, 3, 5 and 8, 9 each sort in place, reached in two
  //          swaps in the statement's explanation
  @Test
  void leetCodeExample1() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {1, 5, 3, 9, 8}, 2))
        .containsExactly(1, 3, 5, 8, 9);
  }

  // Step 11: LeetCode Example 2 — the reading the Explanation exists to kill. The component with
  //          both 1s and the 2 owns positions 0, 4, 5 and the component 6, 7 owns positions 1 and
  //          2, while 18 sits alone. A plain sort answers [1,1,2,6,7,18]
  @Test
  void leetCodeExample2() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {1, 7, 6, 18, 2, 1}, 3))
        .containsExactly(1, 6, 7, 18, 1, 2);
  }

  // Step 12: LeetCode Example 3 — no two values are within 3 of each other, every element is its
  //          own component, and the array is already the answer. A solution that sorts anyway
  //          answers [1,7,10,19,28]
  @Test
  void leetCodeExample3() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {1, 7, 28, 19, 10}, 3))
        .containsExactly(1, 7, 28, 19, 10);
  }

  // ===========================================================================================
  // Top of the value range (1 <= nums[i] <= 10^9, 1 <= limit <= 10^9).
  //
  // No sum in this problem can overflow: values and limit are each at most 10^9, so even
  // value + limit stays under Integer.MAX_VALUE. These steps pin the exact comparison at
  // billion scale, not overflow.
  // ===========================================================================================

  // Step 13: Steps 3 and 4 restated at the extremes of the range — 10^9 and 1 differ by
  //          999,999,999, which is exactly at one limit and one past the other
  @Test
  void billionScaleDifferencesCompareExactly() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {1000000000, 1}, 999999999))
        .containsExactly(1, 1000000000);
    assertThat(sut.lexicographicallySmallestArray(new int[] {1000000000, 1}, 999999998))
        .containsExactly(1000000000, 1);
  }

  // ===========================================================================================
  // Upper end of the length constraint, nums.length = 10^5 (Steps 14-15).
  //
  // Sorting the values and walking sorted neighbors once is about 10^5 log 10^5, or two million
  // operations. Anything that examines pairs — building the swap graph edge by edge, or
  // repeatedly scanning for the best remaining swap — is 10^10 comparisons and cannot finish.
  // The timeouts are two orders of magnitude above what the intended solution needs.
  // ===========================================================================================

  // Step 14: a scrambled permutation of 1..100000 with limit 1. Every sorted gap is 1, so the
  //          whole array is one component and the answer is 1..100000 in order — yet no two
  //          neighboring positions hold values within 1 of each other, so nothing local gets a
  //          solution there: only the component view does, in one sorted pass
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthSingleComponentSortsCompletely() {
    assertThat(sut.lexicographicallySmallestArray(scrambledOneToN(100_000), 1))
        .containsExactly(oneToN(100_000));
  }

  // Step 15: the opposite shape — 50,000 two-value components. Position 2k holds 3k + 2 and
  //          position 2k + 1 holds 3k + 1, so within a pair the gap is 1 and between pairs it is
  //          2, one past limit 1. Each pair swaps into order and nothing crosses a pair boundary,
  //          which catches component splitting that degrades when there are many components
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthWithFiftyThousandComponentsSortsEachPair() {
    int[] pairs = new int[100_000];
    Arrays.setAll(pairs, i -> 3 * (i / 2) + (i % 2 == 0 ? 2 : 1));
    int[] expected = new int[100_000];
    Arrays.setAll(expected, i -> 3 * (i / 2) + (i % 2 == 0 ? 1 : 2));

    assertThat(sut.lexicographicallySmallestArray(pairs, 1)).containsExactly(expected);
  }

  // ===========================================================================================
  // Hygiene (Steps 16-17).
  // ===========================================================================================

  // Step 16: the array is the caller's. Sorting nums in place to find the components, or writing
  //          the answer back into it, is a tempting shortcut the functional steps cannot see
  @Test
  void inputArrayIsNotModified() {
    int[] arr = {1, 7, 6, 18, 2, 1};
    int[] original = arr.clone();

    sut.lexicographicallySmallestArray(arr, 3);

    assertThat(arr).containsExactly(original);
  }

  // Step 17: one instance answers arrays of different lengths and limits, largest in the middle
  //          and each expecting a different grouping. A solution that caches sorted order, the
  //          union-find, or per-component value queues on the instance answers the later calls
  //          from the earlier call's state
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oneInstanceAnswersManyArraysInAnyOrder() {
    assertThat(sut.lexicographicallySmallestArray(new int[] {3, 1}, 2)).containsExactly(1, 3);
    assertThat(sut.lexicographicallySmallestArray(scrambledOneToN(100_000), 1))
        .containsExactly(oneToN(100_000));
    assertThat(sut.lexicographicallySmallestArray(new int[] {5, 1}, 3)).containsExactly(5, 1);
    assertThat(sut.lexicographicallySmallestArray(new int[] {7, 3, 5}, 2)).containsExactly(3, 5, 7);
  }

  /**
   * A permutation of 1..n where no two neighboring positions hold values within 1 of each other.
   */
  private static int[] scrambledOneToN(int n) {
    int[] arr = new int[n];
    Arrays.setAll(arr, i -> (int) ((i * 99991L) % n) + 1);
    return arr;
  }

  private static int[] oneToN(int n) {
    int[] arr = new int[n];
    Arrays.setAll(arr, i -> i + 1);
    return arr;
  }
}
