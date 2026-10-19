package leetcode.p2001_2100;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class FindTargetIndicesAfterSortingArrayTest {
  FindTargetIndicesAfterSortingArray sut = new FindTargetIndicesAfterSortingArray();

  // Step 1: smallest valid input (1 <= nums.length). A single element that equals the target sits
  //         at index 0 whether or not the array is sorted
  @Test
  void singleElementEqualToTargetIsIndexZero() {
    assertThat(sut.targetIndices(new int[] {5}, 5)).containsExactly(0);
  }

  // Step 2: the empty-result rule at the floor. A single element that is not the target leaves no
  //         target index, and the answer is an empty list, not null and not the insertion point
  //         [0] a lower-bound binary search would report
  @Test
  void singleElementNotEqualToTargetGivesEmptyList() {
    assertThat(sut.targetIndices(new int[] {5}, 3)).isEmpty();
  }

  // ===========================================================================================
  // Indices of the sorted array, not of the input (Steps 3-4).
  // ===========================================================================================

  // Step 3: the indices are read after sorting. nums = [3, 1] sorts to [1, 3], so 3 sits at index
  //         1. A solution that scans the input as given answers [0], and one that sorts in
  //         descending order answers [0] too
  @Test
  void largerValueFirstInInputLandsLastAfterSorting() {
    assertThat(sut.targetIndices(new int[] {3, 1}, 3)).containsExactly(1);
  }

  // Step 4: the mirror of Step 3 on the same input. The smaller value moves from index 1 to index
  //         0, so scanning the input, or sorting it in descending order, answers [1]
  @Test
  void smallerValueLastInInputLandsFirstAfterSorting() {
    assertThat(sut.targetIndices(new int[] {3, 1}, 1)).containsExactly(0);
  }

  // ===========================================================================================
  // A run of duplicates (Steps 5-9).
  //
  // After sorting, every copy of the target sits in one contiguous run. Its start is the number of
  // elements smaller than the target and its length is the number of copies, so the answer is
  // determined by two counts and the sort itself is optional.
  // ===========================================================================================

  // Step 5: every element is the target, so the run covers the whole array. The list holds one
  //         index per copy, not just the first one found
  @Test
  void everyElementEqualToTargetReturnsEveryIndex() {
    assertThat(sut.targetIndices(new int[] {2, 2, 2}, 2)).containsExactly(0, 1, 2);
  }

  // Step 6: copies scattered through the input come back as one contiguous run.
  //         [2, 1, 2, 1, 2] sorts to [1, 1, 2, 2, 2], so the answer is [2, 3, 4]. Scanning the
  //         input answers the original positions [0, 2, 4]
  @Test
  void scatteredCopiesOfTargetBecomeOneContiguousRun() {
    assertThat(sut.targetIndices(new int[] {2, 1, 2, 1, 2}, 2)).containsExactly(2, 3, 4);
  }

  // Step 7: the run starts after every smaller element, duplicates included. [3, 1, 3, 5, 1, 5, 3]
  //         sorts to [1, 1, 3, 3, 3, 5, 5]: two 1s come first, so the run of 3s starts at 2. A
  //         solution that ranks by distinct value, as in Rank Transform of an Array, counts the
  //         1s once and answers [1, 2, 3]. One that starts the run at the count of elements <= 3
  //         answers [5, 6, 7]
  @Test
  void duplicatesOfSmallerValuesEachPushTheRunRight() {
    assertThat(sut.targetIndices(new int[] {3, 1, 3, 5, 1, 5, 3}, 3)).containsExactly(2, 3, 4);
  }

  // Step 8: the target is the smallest value, so its run starts at index 0 and the larger
  //         elements that follow it change nothing
  @Test
  void smallestValueRunStartsAtIndexZero() {
    assertThat(sut.targetIndices(new int[] {4, 1, 4, 1}, 1)).containsExactly(0, 1);
  }

  // Step 9: the mirror of Step 8 on the same input. The largest value's run ends at the last index,
  //         n - 1 = 3. A solution whose run end is off by one reports index 4, which does not
  //         exist
  @Test
  void largestValueRunEndsAtLastIndex() {
    assertThat(sut.targetIndices(new int[] {4, 1, 4, 1}, 4)).containsExactly(2, 3);
  }

  // ===========================================================================================
  // No target index (Steps 10-11).
  //
  // A sorted-array search naturally produces the target's insertion point, which exists even when
  // the target does not. The answer here is always an empty list.
  // ===========================================================================================

  // Step 10: the target lies strictly between two values in the array. A lower-bound binary
  //          search lands at index 1 without the element there being the target, and reporting it
  //          unchecked answers [1]
  @Test
  void targetBetweenTwoValuesGivesEmptyList() {
    assertThat(sut.targetIndices(new int[] {1, 3, 5}, 2)).isEmpty();
  }

  // Step 11: the target lies outside the array's values, below the smallest and above the
  //          largest. The unchecked insertion points are [0] and [3], and index 3 does not even
  //          exist in a three-element array
  @Test
  void targetOutsideTheValuesGivesEmptyList() {
    assertThat(sut.targetIndices(new int[] {3, 4, 5}, 1)).isEmpty();
    assertThat(sut.targetIndices(new int[] {3, 4, 5}, 9)).isEmpty();
  }

  // ===========================================================================================
  // Output order (Step 12).
  // ===========================================================================================

  // Step 12: the statement's last rule, that the returned list is sorted in increasing order. The
  //          input arrives in descending order, so it has to be fully reversed. Sorting in
  //          descending order answers [1, 2, 3], and a solution that walks the sorted array from
  //          the right, or finds the last copy and walks left, answers [4, 3, 2]
  @Test
  void indicesComeBackInIncreasingOrder() {
    assertThat(sut.targetIndices(new int[] {5, 4, 4, 4, 2, 1}, 4)).containsExactly(2, 3, 4);
  }

  // ===========================================================================================
  // LeetCode examples (Steps 13-15), all on nums = [1, 2, 5, 2, 3], which sorts to
  // [1, 2, 2, 3, 5].
  // ===========================================================================================

  // Step 13: LeetCode Example 1. In the input the 2s sit at indices 1 and 3, so scanning it answers
  //          [1, 3]. After sorting they are adjacent at 1 and 2
  @Test
  void leetCodeExample1() {
    assertThat(sut.targetIndices(new int[] {1, 2, 5, 2, 3}, 2)).containsExactly(1, 2);
  }

  // Step 14: LeetCode Example 2. Both 2s precede the 3, so it lands at index 3. Ranking by distinct
  //          value counts the 2s once and answers [2], and scanning the input answers [4]
  @Test
  void leetCodeExample2() {
    assertThat(sut.targetIndices(new int[] {1, 2, 5, 2, 3}, 3)).containsExactly(3);
  }

  // Step 15: LeetCode Example 3. The 5 is the largest value and moves from index 2 to the end, so
  //          scanning the input answers [2] and ranking by distinct value answers [3]
  @Test
  void leetCodeExample3() {
    assertThat(sut.targetIndices(new int[] {1, 2, 5, 2, 3}, 5)).containsExactly(4);
  }

  // ===========================================================================================
  // Upper end of the constraints (1 <= nums.length <= 100, 1 <= nums[i], target <= 100).
  //
  // These carry no timeouts. At n = 100 even a cubic solution finishes in about 10^6 steps, so
  // running time cannot separate O(n) counting from O(n log n) sorting or anything slower. What
  // they pin down is that the run arithmetic holds at the extremes of length and value.
  // ===========================================================================================

  // Step 16: the maximum length, every element at the top of the value range and equal to the
  //          target. The run is the whole array, all 100 indices in order
  @Test
  void maximumLengthAllEqualToTargetReturnsEveryIndex() {
    assertThat(sut.targetIndices(filled(100, 100), 100))
        .containsExactlyElementsOf(range(0, 100))
        .hasSize(100);
  }

  // Step 17: both ends of the value range at the maximum length, with the target at the opposite
  //          end each time. Nothing matches, and the unchecked insertion points would be [100],
  //          past the end, and [0]
  @Test
  void maximumLengthTargetAtTheOppositeExtremeGivesEmptyList() {
    assertThat(sut.targetIndices(filled(100, 1), 100)).isEmpty();
    assertThat(sut.targetIndices(filled(100, 100), 1)).isEmpty();
  }

  // Step 18: the maximum length, holding ten copies each of 1 to 10, interleaved as
  //          1, 8, 5, 2, 9, 6, 3, 10, 7, 4 repeated. Thirty elements are smaller than 4, so its run
  //          is indices 30 to 39. Scanning the input answers 9, 19, ..., 99, and ranking by
  //          distinct value answers 3 to 12
  @Test
  void maximumLengthInterleavedDuplicatesFormRunAfterAllSmallerCopies() {
    int[] nums = new int[100];
    Arrays.setAll(nums, i -> (i * 7) % 10 + 1);

    assertThat(sut.targetIndices(nums, 4))
        .containsExactlyElementsOf(range(30, 40))
        .hasSize(10);
  }

  // ===========================================================================================
  // Hygiene (Step 19).
  // ===========================================================================================

  // Step 19: several inputs answered by one instance, the largest in the middle and the lengths
  //          deliberately out of order. A solution that keeps a count table or a sorted copy on the
  //          instance instead of building it per call answers the later inputs from the earlier
  //          ones' leftovers
  @Test
  void oneInstanceAnswersInputsOfAnyLengthInAnyOrder() {
    int[] interleaved = new int[100];
    Arrays.setAll(interleaved, i -> (i * 7) % 10 + 1);

    assertThat(sut.targetIndices(new int[] {1, 2, 5, 2, 3}, 2)).containsExactly(1, 2);
    assertThat(sut.targetIndices(interleaved, 4)).containsExactlyElementsOf(range(30, 40));
    assertThat(sut.targetIndices(new int[] {5}, 3)).isEmpty();
    assertThat(sut.targetIndices(new int[] {2, 1, 2, 1, 2}, 2)).containsExactly(2, 3, 4);
    assertThat(sut.targetIndices(new int[] {4, 1, 4, 1}, 4)).containsExactly(2, 3);
  }

  private static int[] filled(int n, int value) {
    int[] nums = new int[n];
    Arrays.fill(nums, value);
    return nums;
  }

  private static List<Integer> range(int from, int to) {
    return IntStream.range(from, to).boxed().toList();
  }
}
