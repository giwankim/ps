package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class SearchInRotatedSortedArrayTest {
  SearchInRotatedSortedArray sut = new SearchInRotatedSortedArray();

  // Step 1: the only value in the smallest valid array can be the target.
  @Test
  void singleElementArrayFindsTarget() {
    assertThat(sut.search(new int[] {1}, 1)).isZero();
  }

  // Step 2: LeetCode Example 3 - the smallest valid array can also miss the target.
  @Test
  void leetCodeExampleThreeSingleElementArrayDoesNotFindTarget() {
    assertThat(sut.search(new int[] {1}, 0)).isEqualTo(-1);
  }

  // Step 3: an array may be left unrotated and still satisfy the "possibly rotated" contract.
  @Test
  void unrotatedArrayFindsTargetAtFirstIndex() {
    assertThat(sut.search(new int[] {1, 3, 5, 7, 9}, 1)).isZero();
  }

  // Step 4: the normal sorted-array path must also find the right edge.
  @Test
  void unrotatedArrayFindsTargetAtLastIndex() {
    assertThat(sut.search(new int[] {1, 3, 5, 7, 9}, 9)).isEqualTo(4);
  }

  // Step 5: a miss in an unrotated array still returns -1.
  @Test
  void unrotatedArrayReturnsMinusOneWhenTargetIsAbsent() {
    assertThat(sut.search(new int[] {1, 3, 5, 7, 9}, 6)).isEqualTo(-1);
  }

  // Step 6: two-element rotated arrays keep the targetable left side tiny.
  @Test
  void twoElementRotatedArrayFindsTargetAtFirstIndex() {
    assertThat(sut.search(new int[] {3, 1}, 3)).isZero();
  }

  // Step 7: two-element rotated arrays must also search the wrapped right side.
  @Test
  void twoElementRotatedArrayFindsTargetAtLastIndex() {
    assertThat(sut.search(new int[] {3, 1}, 1)).isEqualTo(1);
  }

  // Step 8: two-element rotated arrays can still miss the target.
  @Test
  void twoElementRotatedArrayReturnsMinusOneWhenTargetIsAbsent() {
    assertThat(sut.search(new int[] {3, 1}, 2)).isEqualTo(-1);
  }

  // Step 9: LeetCode Example 1 - find the target after the rotation pivot.
  @Test
  void leetCodeExampleOneFindsTargetAfterPivot() {
    assertThat(sut.search(new int[] {4, 5, 6, 7, 0, 1, 2}, 0)).isEqualTo(4);
  }

  // Step 10: LeetCode Example 2 - return -1 for a value in the original gap.
  @Test
  void leetCodeExampleTwoReturnsMinusOneForMissingTarget() {
    assertThat(sut.search(new int[] {4, 5, 6, 7, 0, 1, 2}, 3)).isEqualTo(-1);
  }

  // Step 11: the target can be the first element of the rotated array.
  @Test
  void rotatedArrayFindsTargetAtFirstIndex() {
    assertThat(sut.search(new int[] {5, 6, 7, 1, 2, 3, 4}, 5)).isZero();
  }

  // Step 12: the target can be the last element after rotation.
  @Test
  void rotatedArrayFindsTargetAtLastIndex() {
    assertThat(sut.search(new int[] {5, 6, 7, 1, 2, 3, 4}, 4)).isEqualTo(6);
  }

  // Step 13: the target can be the largest value just before the pivot.
  @Test
  void rotatedArrayFindsLargestValueBeforePivot() {
    assertThat(sut.search(new int[] {4, 5, 6, 7, 8, 1, 2, 3}, 8)).isEqualTo(4);
  }

  // Step 14: the target can be the minimum value at the rotation pivot.
  @Test
  void rotatedArrayFindsMinimumValueAtPivot() {
    assertThat(sut.search(new int[] {4, 5, 6, 7, 8, 1, 2, 3}, 1)).isEqualTo(5);
  }

  // Step 15: rotation by one places the smallest value at the last index.
  @Test
  void rotationByOneFindsSmallestValueAtLastIndex() {
    assertThat(sut.search(new int[] {2, 3, 4, 5, 6, 7, 1}, 1)).isEqualTo(6);
  }

  // Step 16: rotation by length minus one places the largest value at the first index.
  @Test
  void rotationByLengthMinusOneFindsLargestValueAtFirstIndex() {
    assertThat(sut.search(new int[] {7, 1, 2, 3, 4, 5, 6}, 7)).isZero();
  }

  // Step 17: values can span the full allowed range from -10^4 to 10^4.
  @Test
  void rotatedArrayFindsTargetsAtConstraintValueBounds() {
    int[] nums = {0, 1, 10_000, -10_000, -1};

    assertThat(sut.search(nums, 10_000)).isEqualTo(2);
    assertThat(sut.search(nums, -10_000)).isEqualTo(3);
  }

  // Step 18: a target at the lower constraint bound can still be absent.
  @Test
  void rotatedArrayReturnsMinusOneWhenLowerBoundTargetIsAbsent() {
    assertThat(sut.search(new int[] {0, 3, 10_000, -9_999, -4}, -10_000)).isEqualTo(-1);
  }

  // Step 19: a target at the upper constraint bound can still be absent.
  @Test
  void rotatedArrayReturnsMinusOneWhenUpperBoundTargetIsAbsent() {
    assertThat(sut.search(new int[] {0, 3, 9_999, -10_000, -4}, 10_000)).isEqualTo(-1);
  }

  // Step 20: the maximum allowed input length still follows the same rotated ordering contract.
  @Test
  void maximumLengthRotatedArrayFindsTargetInWrappedHalf() {
    int[] nums = IntStream.concat(IntStream.range(2_500, 5_000), IntStream.range(0, 2_500))
        .toArray();

    assertThat(sut.search(nums, 1_234)).isEqualTo(3_734);
  }
}
