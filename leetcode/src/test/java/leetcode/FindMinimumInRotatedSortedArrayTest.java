package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class FindMinimumInRotatedSortedArrayTest {
  FindMinimumInRotatedSortedArray sut = new FindMinimumInRotatedSortedArray();

  // Step 1: the only value in the smallest valid array is the minimum.
  @Test
  void singleElementArrayReturnsThatElement() {
    assertThat(sut.findMin(new int[] {1})).isEqualTo(1);
  }

  // Step 2: the smallest rotated array places the minimum at the wrapped index.
  @Test
  void twoElementRotatedArrayReturnsLastElement() {
    assertThat(sut.findMin(new int[] {2, 1})).isEqualTo(1);
  }

  // Step 3: rotating by exactly n leaves the array unrotated, so the minimum is the first element.
  @Test
  void twoElementUnrotatedArrayReturnsFirstElement() {
    assertThat(sut.findMin(new int[] {1, 2})).isEqualTo(1);
  }

  // Step 4: LeetCode Example 3 - a longer unrotated array still returns the first element.
  @Test
  void leetCodeExampleThreeUnrotatedArrayReturnsFirstElement() {
    assertThat(sut.findMin(new int[] {11, 13, 15, 17})).isEqualTo(11);
  }

  // Step 5: LeetCode Example 1 - the minimum sits just past the rotation pivot.
  @Test
  void leetCodeExampleOneFindsMinimumAfterPivot() {
    assertThat(sut.findMin(new int[] {3, 4, 5, 1, 2})).isEqualTo(1);
  }

  // Step 6: LeetCode Example 2 - the minimum can be deeper in the wrapped half.
  @Test
  void leetCodeExampleTwoFindsMinimumInWrappedHalf() {
    assertThat(sut.findMin(new int[] {4, 5, 6, 7, 0, 1, 2})).isEqualTo(0);
  }

  // Step 7: rotation by one places the minimum at the last index.
  @Test
  void rotationByOneFindsMinimumAtLastIndex() {
    assertThat(sut.findMin(new int[] {2, 3, 4, 5, 6, 7, 1})).isEqualTo(1);
  }

  // Step 8: rotation by length minus one places the minimum at the second index,
  // which forces the search to step left from the midpoint.
  @Test
  void rotationByLengthMinusOneFindsMinimumAtSecondIndex() {
    assertThat(sut.findMin(new int[] {7, 1, 2, 3, 4, 5, 6})).isEqualTo(1);
  }

  // Step 9: an even-length array with the pivot at the exact midpoint
  // exercises the boundary between the high and low segments.
  @Test
  void evenLengthArrayWithPivotAtMidpointFindsMinimum() {
    assertThat(sut.findMin(new int[] {3, 4, 1, 2})).isEqualTo(1);
  }

  // Step 10: a deeper rotation where the minimum sits in the left half of the search range
  // forces multiple leftward narrowings.
  @Test
  void rotatedArrayWithMinimumInLeftHalfNarrowsLeftward() {
    assertThat(sut.findMin(new int[] {6, 7, 8, 1, 2, 3, 4, 5})).isEqualTo(1);
  }

  // Step 11: negative values must be ordered correctly, not treated as wrap-around sentinels.
  @Test
  void rotatedArrayWithNegativesFindsMinimum() {
    assertThat(sut.findMin(new int[] {3, 4, -2, -1, 0, 1, 2})).isEqualTo(-2);
  }

  // Step 12: values can span the full allowed range from -5000 to 5000.
  @Test
  void rotatedArrayFindsMinimumAtConstraintLowerBound() {
    assertThat(sut.findMin(new int[] {0, 1, 5_000, -5_000, -1})).isEqualTo(-5_000);
  }

  // Step 13: the maximum allowed input length still follows the same rotated ordering contract.
  @Test
  void maximumLengthRotatedArrayFindsMinimumInWrappedHalf() {
    int[] nums = IntStream.concat(IntStream.range(2_500, 5_000), IntStream.range(0, 2_500))
        .toArray();

    assertThat(sut.findMin(nums)).isZero();
  }
}
