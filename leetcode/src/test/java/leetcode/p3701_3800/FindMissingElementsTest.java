package leetcode.p3701_3800;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class FindMissingElementsTest {
  FindMissingElements sut = new FindMissingElements();

  // Step 1: smallest valid input (2 <= nums.length) with no gap at all — the range [1, 2] is full
  @Test
  void twoAdjacentValuesReturnEmptyList() {
    assertThat(sut.findMissingElements(new int[] {1, 2})).isEmpty();
  }

  // Step 2: smallest input that is missing something — the range [1, 3] lacks only its midpoint
  @Test
  void twoValuesWithASingleGapReturnThatValue() {
    assertThat(sut.findMissingElements(new int[] {1, 3})).containsExactly(2);
  }

  // Step 3: a longer contiguous run, handed over shuffled — order of input must not matter
  @Test
  void contiguousRunReturnsEmptyListRegardlessOfInputOrder() {
    assertThat(sut.findMissingElements(new int[] {3, 1, 4, 2})).isEmpty();
  }

  // Step 4: LeetCode Example 1 — min 1, max 5, so the full range is [1, 5] and only 3 is absent
  @Test
  void leetCodeExample1FindsTheSingleInteriorGap() {
    assertThat(sut.findMissingElements(new int[] {1, 4, 2, 5})).containsExactly(3);
  }

  // Step 5: LeetCode Example 3 — only the endpoints survive, so everything between them is missing
  @Test
  void leetCodeExample3ReturnsEveryValueBetweenTheEndpoints() {
    assertThat(sut.findMissingElements(new int[] {5, 1})).containsExactly(2, 3, 4);
  }

  // Step 6: two disjoint gaps (2, then 5 and 6) must come back merged into one ascending list
  @Test
  void multipleSeparateGapsAreReturnedInAscendingOrder() {
    assertThat(sut.findMissingElements(new int[] {7, 1, 3, 4})).containsExactly(2, 5, 6);
  }

  // Step 7: the gap starts right after the minimum — min itself must never be reported
  @Test
  void gapImmediatelyAfterTheMinimum() {
    assertThat(sut.findMissingElements(new int[] {1, 8, 9, 10})).containsExactly(2, 3, 4, 5, 6, 7);
  }

  // Step 8: the gap ends right before the maximum — max itself must never be reported
  @Test
  void gapImmediatelyBeforeTheMaximum() {
    assertThat(sut.findMissingElements(new int[] {1, 2, 3, 10})).containsExactly(4, 5, 6, 7, 8, 9);
  }

  // Step 9: LeetCode Example 2 — the range is [6, 9], not [1, 9]; nothing below min is "missing"
  @Test
  void leetCodeExample2TreatsTheRangeAsMinToMaxNotOneToMax() {
    assertThat(sut.findMissingElements(new int[] {7, 8, 6, 9})).isEmpty();
  }

  // Step 10: the same idea at the top of the value space (nums[i] <= 100)
  @Test
  void rangeHighInTheValueSpaceIgnoresEverythingBelowIt() {
    assertThat(sut.findMissingElements(new int[] {97, 100})).containsExactly(98, 99);
  }

  // Step 11: widest possible range from the fewest elements — 98 missing values between 1 and 100
  @Test
  void widestPossibleRangeReturnsEveryInteriorValue() {
    assertThat(sut.findMissingElements(new int[] {1, 100}))
        .containsExactlyElementsOf(IntStream.rangeClosed(2, 99).boxed().toList());
  }

  // Step 12: largest possible input (nums.length == 100) fully populated and reversed — no gaps
  @Test
  void denseFullRangeInDescendingOrderReturnsEmptyList() {
    int[] nums = IntStream.rangeClosed(1, 100).map(i -> 101 - i).toArray();
    assertThat(sut.findMissingElements(nums)).isEmpty();
  }
}
