package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import com.giwankim.leetcode.FindMedianFromDataStream.MedianFinder;
import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FindMedianFromDataStreamTest {
  private static final double TOLERANCE = 1e-5;

  MedianFinder sut;

  @BeforeEach
  void setUp() {
    sut = new MedianFinder();
  }

  // Step 1: smallest valid stream — one element, median is the element itself
  @Test
  void singleElementIsItsOwnMedian() {
    sut.addNum(2);
    assertThat(sut.findMedian()).isCloseTo(2.0, within(TOLERANCE));
  }

  // Step 2: two elements in ascending order — median is their average
  @Test
  void twoAscendingElementsAverageToMedian() {
    sut.addNum(2);
    sut.addNum(3);
    assertThat(sut.findMedian()).isCloseTo(2.5, within(TOLERANCE));
  }

  // Step 3: two elements in descending order — insertion order must not matter
  @Test
  void twoDescendingElementsAverageToMedian() {
    sut.addNum(3);
    sut.addNum(2);
    assertThat(sut.findMedian()).isCloseTo(2.5, within(TOLERANCE));
  }

  // Step 4: three elements ascending — odd count returns the middle value
  @Test
  void threeAscendingElementsReturnMiddleValue() {
    sut.addNum(2);
    sut.addNum(3);
    sut.addNum(4);
    assertThat(sut.findMedian()).isCloseTo(3.0, within(TOLERANCE));
  }

  // Step 5: three elements descending — same answer regardless of insertion order
  @Test
  void threeDescendingElementsReturnMiddleValue() {
    sut.addNum(4);
    sut.addNum(3);
    sut.addNum(2);
    assertThat(sut.findMedian()).isCloseTo(3.0, within(TOLERANCE));
  }

  // Step 6: four elements — average of the two middle values
  @Test
  void fourElementsAverageMiddlePair() {
    addAll(1, 2, 3, 4);
    assertThat(sut.findMedian()).isCloseTo(2.5, within(TOLERANCE));
  }

  // Step 7: five elements unsorted — must still pick the true middle after sort
  @Test
  void fiveUnsortedElementsReturnTrueMiddle() {
    addAll(5, 1, 4, 2, 3);
    assertThat(sut.findMedian()).isCloseTo(3.0, within(TOLERANCE));
  }

  // Step 8: LeetCode example — interleaved addNum / findMedian calls
  @Test
  void leetcodeExampleInterleavedCalls() {
    sut.addNum(1);
    sut.addNum(2);
    assertThat(sut.findMedian()).isCloseTo(1.5, within(TOLERANCE));
    sut.addNum(3);
    assertThat(sut.findMedian()).isCloseTo(2.0, within(TOLERANCE));
  }

  // Step 9: all duplicates — median equals the repeated value
  @Test
  void allDuplicatesReturnThatValue() {
    addAll(7, 7, 7, 7);
    assertThat(sut.findMedian()).isCloseTo(7.0, within(TOLERANCE));
  }

  // Step 10: all zeros — guards against any zero-as-empty confusion
  @Test
  void allZerosReturnZero() {
    addAll(0, 0, 0);
    assertThat(sut.findMedian()).isCloseTo(0.0, within(TOLERANCE));
  }

  // Step 11: only negative numbers — odd count
  @Test
  void onlyNegativeNumbersOddCount() {
    addAll(-1, -2, -3);
    assertThat(sut.findMedian()).isCloseTo(-2.0, within(TOLERANCE));
  }

  // Step 12: only negative numbers — even count, median is negative average
  @Test
  void onlyNegativeNumbersEvenCount() {
    addAll(-1, -2, -3, -4);
    assertThat(sut.findMedian()).isCloseTo(-2.5, within(TOLERANCE));
  }

  // Step 13: mixed signs straddling zero — median can be zero
  @Test
  void mixedSignsStraddlingZero() {
    addAll(-2, -1, 1, 2);
    assertThat(sut.findMedian()).isCloseTo(0.0, within(TOLERANCE));
  }

  // Step 14: mixed signs producing a fractional median
  @Test
  void mixedSignsProduceFractionalMedian() {
    addAll(-1, 2);
    assertThat(sut.findMedian()).isCloseTo(0.5, within(TOLERANCE));
  }

  // Step 15: constraint lower bound — single addNum at minimum value
  @Test
  void minimumConstraintValue() {
    sut.addNum(-100_000);
    assertThat(sut.findMedian()).isCloseTo(-100_000.0, within(TOLERANCE));
  }

  // Step 16: constraint upper bound — single addNum at maximum value
  @Test
  void maximumConstraintValue() {
    sut.addNum(100_000);
    assertThat(sut.findMedian()).isCloseTo(100_000.0, within(TOLERANCE));
  }

  // Step 17: full-range pair — average of -10^5 and 10^5 is 0; guards int-sum precision
  @Test
  void constraintBoundsPairAveragesToZero() {
    sut.addNum(-100_000);
    sut.addNum(100_000);
    assertThat(sut.findMedian()).isCloseTo(0.0, within(TOLERANCE));
  }

  // Step 18: two large positives — sum is 2*10^5 (in int range), divided by 2.0 must be exact
  @Test
  void twoLargePositivesAverageExactly() {
    sut.addNum(99_999);
    sut.addNum(100_000);
    assertThat(sut.findMedian()).isCloseTo(99_999.5, within(TOLERANCE));
  }

  // Step 19: already-sorted ascending input
  @Test
  void alreadySortedAscendingInput() {
    addAll(1, 2, 3, 4, 5, 6);
    assertThat(sut.findMedian()).isCloseTo(3.5, within(TOLERANCE));
  }

  // Step 20: already-sorted descending input — heap rebalancing must still work
  @Test
  void alreadySortedDescendingInput() {
    addAll(6, 5, 4, 3, 2, 1);
    assertThat(sut.findMedian()).isCloseTo(3.5, within(TOLERANCE));
  }

  // Step 21: alternating extremes — stress test for heap rebalancing
  @Test
  void alternatingExtremes() {
    addAll(100_000, -100_000, 100_000, -100_000);
    assertThat(sut.findMedian()).isCloseTo(0.0, within(TOLERANCE));
  }

  // Step 22: median walks the sequence — every prefix matches the LeetCode-style problem
  @Test
  void medianWalksTheSequence() {
    int[] nums = {6, 10, 2, 6, 5, 0, 6, 3, 1, 0, 0};
    double[] expected = {6.0, 8.0, 6.0, 6.0, 6.0, 5.5, 6.0, 5.5, 5.0, 4.0, 3.0};
    for (int i = 0; i < nums.length; i++) {
      sut.addNum(nums[i]);
      assertThat(sut.findMedian())
          .as("after inserting nums[%d]=%d", i, nums[i])
          .isCloseTo(expected[i], within(TOLERANCE));
    }
  }

  // Step 23: property — median always lies between min and max of inserted values
  @Test
  void medianAlwaysWithinObservedRange() {
    int[] nums = {3, -7, 0, 9, 9, -2, 4, 4, 1, -5};
    int observedMin = Integer.MAX_VALUE;
    int observedMax = Integer.MIN_VALUE;
    for (int n : nums) {
      sut.addNum(n);
      observedMin = Math.min(observedMin, n);
      observedMax = Math.max(observedMax, n);
      assertThat(sut.findMedian()).isBetween((double) observedMin, (double) observedMax);
    }
  }

  // Step 24: property — for any prefix, half the values are ≤ median and half are ≥ median
  @Test
  void halfOfValuesLieOnEachSideOfMedian() {
    int[] nums = {3, -7, 0, 9, 9, -2, 4, 4, 1, -5, 12, -1};
    for (int i = 0; i < nums.length; i++) {
      sut.addNum(nums[i]);
      double median = sut.findMedian();
      int below = 0;
      int above = 0;
      for (int j = 0; j <= i; j++) {
        if (nums[j] < median) below++;
        else if (nums[j] > median) above++;
      }
      int n = i + 1;
      assertThat(below).as("below count at size %d", n).isLessThanOrEqualTo(n / 2);
      assertThat(above).as("above count at size %d", n).isLessThanOrEqualTo(n / 2);
    }
  }

  // Step 25: cross-check against brute-force sort on randomized input
  @Test
  void matchesBruteForceSortOnRandomizedStream() {
    Random rng = new Random(42);
    int[] nums = new int[200];
    for (int i = 0; i < nums.length; i++) {
      nums[i] = rng.nextInt(200_001) - 100_000;
      sut.addNum(nums[i]);
      int[] prefix = Arrays.copyOf(nums, i + 1);
      Arrays.sort(prefix);
      double expected =
          (prefix.length % 2 == 1)
              ? prefix[prefix.length / 2]
              : (prefix[prefix.length / 2 - 1] + prefix[prefix.length / 2]) / 2.0;
      assertThat(sut.findMedian())
          .as("after inserting %d values", i + 1)
          .isCloseTo(expected, within(TOLERANCE));
    }
  }

  // Step 26: scale regression guard — 5*10^4 ops (the LeetCode upper bound) must finish quickly.
  // A naive O(n) addNum would take roughly minutes for this volume; the two-heap O(log n)
  // implementation finishes in well under the timeout. Tune the bound if CI is noisy.
  @Test
  void handlesUpperBoundCallVolumeWithinTimeout() {
    assertTimeoutPreemptively(
        Duration.ofSeconds(2),
        () -> {
          Random rng = new Random(7);
          for (int i = 0; i < 50_000; i++) {
            sut.addNum(rng.nextInt(200_001) - 100_000);
            if ((i & 1023) == 0) {
              sut.findMedian();
            }
          }
          sut.findMedian();
        });
  }

  private void addAll(int... nums) {
    for (int n : nums) {
      sut.addNum(n);
    }
  }
}
