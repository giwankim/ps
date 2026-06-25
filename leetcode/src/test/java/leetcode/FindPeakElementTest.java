package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FindPeakElementTest {
  FindPeakElement sut = new FindPeakElement();

  // Step 1: a single element is its own peak (both boundaries are -infinity)
  @Test
  void singleElementReturnsIndexZero() {
    assertThat(sut.findPeakElement(new int[] {1})).isZero();
  }

  // Step 2: two ascending elements — second beats both the first and the right -infinity boundary
  @Test
  void twoElementsAscendingPeakAtSecond() {
    assertThat(sut.findPeakElement(new int[] {1, 2})).isEqualTo(1);
  }

  // Step 3: two descending elements — first beats the left -infinity boundary and the second
  @Test
  void twoElementsDescendingPeakAtFirst() {
    assertThat(sut.findPeakElement(new int[] {2, 1})).isZero();
  }

  // Step 4: classic three-element peak in the middle
  @Test
  void threeElementsPeakAtMiddle() {
    assertThat(sut.findPeakElement(new int[] {1, 3, 2})).isEqualTo(1);
  }

  // Step 5: strictly ascending — last index is the only peak (right boundary is -infinity)
  @Test
  void threeElementsAscendingPeakAtLast() {
    assertThat(sut.findPeakElement(new int[] {1, 2, 3})).isEqualTo(2);
  }

  // Step 6: strictly descending — first index is the only peak (left boundary is -infinity)
  @Test
  void threeElementsDescendingPeakAtFirst() {
    assertThat(sut.findPeakElement(new int[] {3, 2, 1})).isZero();
  }

  // Step 7: LeetCode Example 1 — interior peak at index 2
  @Test
  void leetCodeExampleOnePeakAtIndexTwo() {
    assertThat(sut.findPeakElement(new int[] {1, 2, 3, 1})).isEqualTo(2);
  }

  // Step 8: LeetCode Example 2 — two valid peaks (index 1 and index 5); either is acceptable
  @Test
  void leetCodeExampleTwoReturnsAnyValidPeak() {
    int[] nums = {1, 2, 1, 3, 5, 6, 4};
    assertThat(sut.findPeakElement(nums)).isIn(1, 5);
  }

  // Step 9: longer strictly ascending array — peak must be the last index
  @Test
  void longAscendingPeakAtLastIndex() {
    assertThat(sut.findPeakElement(new int[] {1, 2, 3, 4, 5, 6, 7, 8})).isEqualTo(7);
  }

  // Step 10: longer strictly descending array — peak must be the first index
  @Test
  void longDescendingPeakAtFirstIndex() {
    assertThat(sut.findPeakElement(new int[] {8, 7, 6, 5, 4, 3, 2, 1})).isZero();
  }

  // Step 11: peak at index 0 of a multi-element array (left-boundary peak)
  @Test
  void peakAtStartOfArray() {
    assertThat(sut.findPeakElement(new int[] {9, 1, 2, 3})).isIn(0, 3);
  }

  // Step 12: peak at the last index of a multi-element array (right-boundary peak)
  @Test
  void peakAtEndOfArray() {
    assertThat(sut.findPeakElement(new int[] {1, 2, 3, 9})).isEqualTo(3);
  }

  // Step 13: multiple equal-pattern peaks — return any valid one
  @Test
  void multiplePeaksReturnsValidPeakIndex() {
    int[] nums = {1, 5, 1, 6, 1, 7, 1};
    assertThat(sut.findPeakElement(nums)).isIn(1, 3, 5);
  }

  // Step 14: zigzag pattern — every odd index plus the last is a valid peak
  @Test
  void zigzagPatternReturnsLocalPeak() {
    int[] nums = {1, 3, 2, 4, 3, 5};
    int idx = sut.findPeakElement(nums);
    assertIsPeak(nums, idx);
  }

  // Step 15: all-negative values with a clear interior peak
  @Test
  void negativeValuesPeakInMiddle() {
    assertThat(sut.findPeakElement(new int[] {-5, -2, -8})).isEqualTo(1);
  }

  // Step 16: mixed positive and negative values — confirm a real peak is returned
  @Test
  void mixedSignedValuesFindsValidPeak() {
    int[] nums = {-3, -1, 4, 2, -5};
    int idx = sut.findPeakElement(nums);
    assertIsPeak(nums, idx);
  }

  // Step 17: Integer.MIN_VALUE adjacent to 0 — the 0 is the peak (MIN_VALUE is not greater than 0)
  @Test
  void integerMinValueAtStart() {
    assertThat(sut.findPeakElement(new int[] {Integer.MIN_VALUE, 0})).isEqualTo(1);
  }

  // Step 18: Integer.MAX_VALUE at the end — must be returned as the peak
  @Test
  void integerMaxValueAtEndIsPeak() {
    assertThat(sut.findPeakElement(new int[] {0, Integer.MAX_VALUE})).isEqualTo(1);
  }

  // Step 19: extreme MIN/MAX neighbors — MAX_VALUE at index 0 is the peak
  @Test
  void integerMaxThenMinReturnsZero() {
    assertThat(sut.findPeakElement(new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE}))
        .isZero();
  }

  // Step 20: maximum constraint length, strictly ascending — peak at the last index (n=1000)
  @Test
  void maximumLengthAscendingArrayReturnsLastIndex() {
    int n = 1000;
    int[] nums = new int[n];
    for (int i = 0; i < n; i++) {
      nums[i] = i;
    }
    assertThat(sut.findPeakElement(nums)).isEqualTo(n - 1);
  }

  // Step 21: maximum constraint length, strictly descending — peak at the first index (n=1000)
  @Test
  void maximumLengthDescendingArrayReturnsFirstIndex() {
    int n = 1000;
    int[] nums = new int[n];
    for (int i = 0; i < n; i++) {
      nums[i] = n - i;
    }
    assertThat(sut.findPeakElement(nums)).isZero();
  }

  // Step 22: long array with a single interior peak — exercises log-n descent toward the peak
  @Test
  void interiorPeakOfLongArray() {
    int n = 100;
    int[] nums = new int[n];
    for (int i = 0; i <= 50; i++) {
      nums[i] = i;
    }
    for (int i = 51; i < n; i++) {
      nums[i] = n - i;
    }
    assertThat(sut.findPeakElement(nums)).isEqualTo(50);
  }

  // Step 23: returned index must always be in bounds and satisfy the peak predicate
  @Test
  void returnsInBoundsAndStrictlyGreaterThanExistingNeighbors() {
    int[] nums = {2, 4, 1, 5, 3, 6, 0};
    int idx = sut.findPeakElement(nums);
    assertIsPeak(nums, idx);
  }

  private static void assertIsPeak(int[] nums, int idx) {
    assertThat(idx).isBetween(0, nums.length - 1);
    if (idx > 0) {
      assertThat(nums[idx]).isGreaterThan(nums[idx - 1]);
    }
    if (idx < nums.length - 1) {
      assertThat(nums[idx]).isGreaterThan(nums[idx + 1]);
    }
  }
}
