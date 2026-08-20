package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class DistributeElementsIntoTwoArraysITest {
  DistributeElementsIntoTwoArraysI sut = new DistributeElementsIntoTwoArraysI();

  // Step 1: smallest valid input (n >= 3) — arr1's last beats arr2's, so nums[2] joins arr1
  // (LeetCode Example 1)
  @Test
  void threeElementsAppendToArr1WhenItsLastIsGreater() {
    assertThat(sut.resultArray(new int[] {2, 1, 3})).containsExactly(2, 3, 1);
  }

  // Step 2: the mirror branch — arr1's last loses, so nums[2] joins arr2
  @Test
  void threeElementsAppendToArr2WhenItsLastIsSmaller() {
    assertThat(sut.resultArray(new int[] {1, 2, 3})).containsExactly(1, 2, 3);
  }

  // Step 3: seeding is positional, not sorted — nums[0] always starts arr1, nums[1] always arr2,
  // and the concatenation emits arr1's block first
  @Test
  void firstTwoElementsSeedArr1AndArr2InOrder() {
    assertThat(sut.resultArray(new int[] {3, 1, 2})).containsExactly(3, 2, 1);
  }

  // Step 4: an append can flip which array is winning, sending the next element the other way
  // (LeetCode Example 2)
  @Test
  void appendingFlipsTheWinnerAndRedirectsTheNextElement() {
    assertThat(sut.resultArray(new int[] {5, 4, 3, 8})).containsExactly(5, 3, 4, 8);
  }

  // Step 5: an ascending input keeps arr2 ahead forever, so arr1 never grows past its seed
  @Test
  void ascendingInputLeavesArr1WithOnlyItsSeed() {
    assertThat(sut.resultArray(new int[] {1, 2, 3, 4, 5})).containsExactly(1, 2, 3, 4, 5);
  }

  // Step 6: a dominant seed in arr1 keeps every later element on that side, leaving arr2 alone
  @Test
  void dominantArr1SeedKeepsEveryLaterElementOnThatSide() {
    assertThat(sut.resultArray(new int[] {10, 1, 2, 3, 4})).containsExactly(10, 2, 3, 4, 1);
  }

  // Step 7: a descending input hands the lead back and forth on every operation
  @Test
  void descendingInputAlternatesBetweenBothArrays() {
    assertThat(sut.resultArray(new int[] {5, 4, 3, 2, 1})).containsExactly(5, 3, 1, 4, 2);
  }

  // Step 8: only the last elements are compared — arr1 holds the overall max (9) yet still loses
  // the next round because its last is 1
  @Test
  void comparesOnlyLastElementsNotTheLargestSeen() {
    assertThat(sut.resultArray(new int[] {9, 5, 1, 6})).containsExactly(9, 1, 5, 6);
  }

  // Step 9: array sizes never break the tie either — arr1 is longer here and still loses
  @Test
  void comparesOnlyLastElementsNotTheArraySizes() {
    assertThat(sut.resultArray(new int[] {7, 6, 2, 9})).containsExactly(7, 2, 6, 9);
  }

  // Step 10: both value bounds are in range (1 <= nums[i] <= 100)
  @Test
  void handlesTheValueBounds() {
    assertThat(sut.resultArray(new int[] {100, 1, 99, 2})).containsExactly(100, 99, 2, 1);
  }

  // Step 11: a longer interleaved run — arr1 stalls at 1 and arr2 collects the rest
  @Test
  void longerInputKeepsAppendingToTheWinningArray() {
    assertThat(sut.resultArray(new int[] {4, 2, 6, 1, 7, 3, 8, 5}))
        .containsExactly(4, 6, 1, 2, 7, 3, 8, 5);
  }

  // Step 12: the upper length bound (n = 50) — ascending, so arr1 keeps its seed and arr2 takes
  // the other 49 in order
  @Test
  void handlesMaximumLengthInput() {
    int[] nums = IntStream.rangeClosed(1, 50).toArray();
    assertThat(sut.resultArray(nums)).containsExactly(nums);
  }
}
