package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class MedianOfTwoSortedArraysTest {
  MedianOfTwoSortedArrays sut = new MedianOfTwoSortedArrays();

  // Step 1: the smallest valid input has a single element in nums1 and an empty nums2.
  // The median is simply that element.
  @Test
  void singleElementInFirstArrayReturnsThatElement() {
    assertThat(sut.findMedianSortedArrays(new int[] {1}, new int[] {})).isEqualTo(1.0);
  }

  // Step 2: the symmetric case - empty nums1, single element in nums2 - forces handling
  // either array being empty.
  @Test
  void singleElementInSecondArrayReturnsThatElement() {
    assertThat(sut.findMedianSortedArrays(new int[] {}, new int[] {2})).isEqualTo(2.0);
  }

  // Step 3: even total of two with one element in each array forces averaging across arrays.
  @Test
  void oneElementInEachArrayReturnsTheirAverage() {
    assertThat(sut.findMedianSortedArrays(new int[] {1}, new int[] {2})).isEqualTo(1.5);
  }

  // Step 4: even total of two with both elements in nums1 forces averaging within one array
  // while the other is empty.
  @Test
  void twoElementsInFirstArrayReturnsTheirAverage() {
    assertThat(sut.findMedianSortedArrays(new int[] {1, 2}, new int[] {})).isEqualTo(1.5);
  }

  // Step 5: LeetCode Example 1 - odd total of three with the median coming from the shorter array.
  @Test
  void leetCodeExampleOneOddTotalReturnsMiddleElement() {
    assertThat(sut.findMedianSortedArrays(new int[] {1, 3}, new int[] {2})).isEqualTo(2.0);
  }

  // Step 6: LeetCode Example 2 - even total of four with the median straddling the array boundary.
  @Test
  void leetCodeExampleTwoEvenTotalAveragesAcrossBoundary() {
    assertThat(sut.findMedianSortedArrays(new int[] {1, 2}, new int[] {3, 4})).isEqualTo(2.5);
  }

  // Step 7: nums1 entirely greater than nums2 - the merge must walk through nums2 first.
  @Test
  void firstArrayEntirelyGreaterThanSecond() {
    assertThat(sut.findMedianSortedArrays(new int[] {3, 4}, new int[] {1, 2})).isEqualTo(2.5);
  }

  // Step 8: fully interleaved values with an odd total exercises a true merge across both arrays.
  @Test
  void interleavedArraysWithOddTotalFindsMiddleElement() {
    assertThat(sut.findMedianSortedArrays(new int[] {1, 3, 5}, new int[] {2, 4}))
        .isEqualTo(3.0);
  }

  // Step 9: values duplicated across arrays must contribute on both sides of the median.
  @Test
  void duplicatesAcrossArraysContributeToMedian() {
    assertThat(sut.findMedianSortedArrays(new int[] {1, 2, 3}, new int[] {2, 3, 4}))
        .isEqualTo(2.5);
  }

  // Step 10: a tiny first array with a much larger second array - exercises that the implementation
  // handles large size disparity (important for the O(log(min(m,n))) follow-up).
  @Test
  void smallFirstArrayWithMuchLargerSecondArray() {
    assertThat(sut.findMedianSortedArrays(new int[] {1}, new int[] {2, 3, 4, 5, 6, 7, 8, 9}))
        .isEqualTo(5.0);
  }

  // Step 11: the symmetric unbalanced case - implementation must not assume nums1 is the shorter
  // (or longer) array.
  @Test
  void largeFirstArrayWithTinySecondArray() {
    assertThat(sut.findMedianSortedArrays(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, new int[] {9}))
        .isEqualTo(5.0);
  }

  // Step 12: every element identical - the median is still that value, with duplicates spanning
  // both arrays.
  @Test
  void allIdenticalElementsReturnThatValue() {
    assertThat(sut.findMedianSortedArrays(new int[] {2, 2, 2}, new int[] {2, 2, 2}))
        .isEqualTo(2.0);
  }

  // Step 13: negative values must be ordered numerically, not treated as wrap sentinels.
  @Test
  void negativeValuesAreOrderedNumerically() {
    assertThat(sut.findMedianSortedArrays(new int[] {-5, -3}, new int[] {-2, -1}))
        .isEqualTo(-2.5);
  }

  // Step 14: constraint boundary - values span -10^6 to 10^6; their average must be exact and
  // not overflow during summation.
  @Test
  void constraintBoundaryValuesAverageWithoutOverflow() {
    assertThat(sut.findMedianSortedArrays(new int[] {-1_000_000}, new int[] {1_000_000}))
        .isZero();
  }

  // Step 15: maximum allowed input length (m + n = 2000) with the median straddling the boundary
  // between the two arrays - confirms the algorithm finishes for the largest legal input and
  // exercises the O(log(min(m,n))) follow-up requirement.
  @Test
  void maximumLengthInterleavedInputsFindMedianAcrossBoundary() {
    // nums1 holds the even values 0, 2, ..., 1998 (1000 elements).
    // nums2 holds the odd values  1, 3, ..., 1999 (1000 elements).
    // Merged is 0..1999, so the median is (999 + 1000) / 2.0 = 999.5.
    int[] nums1 = IntStream.range(0, 1000).map(i -> 2 * i).toArray();
    int[] nums2 = IntStream.range(0, 1000).map(i -> 2 * i + 1).toArray();

    assertThat(sut.findMedianSortedArrays(nums1, nums2)).isEqualTo(999.5);
  }
}
