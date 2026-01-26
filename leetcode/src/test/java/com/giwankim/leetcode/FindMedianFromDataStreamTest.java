package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.giwankim.leetcode.FindMedianFromDataStream.MedianFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FindMedianFromDataStreamTest {

  public static final double TOLERANCE = 1e-5;

  MedianFinder medianFinder;

  @BeforeEach
  void setUp() {
    medianFinder = new MedianFinder();
  }

  @Test
  void addingOneNumber() {
    medianFinder.addNum(2);
    assertThat(medianFinder.findMedian()).isCloseTo(2.0, within(TOLERANCE));
  }

  @Test
  void whenListIsEven() {
    medianFinder.addNum(2);
    medianFinder.addNum(3);
    assertThat(medianFinder.findMedian()).isCloseTo(2.5, within(TOLERANCE));
  }

  @Test
  void whenListIsEvenAndDescending() {
    medianFinder.addNum(3);
    medianFinder.addNum(2);
    assertThat(medianFinder.findMedian()).isCloseTo(2.5, within(TOLERANCE));
  }

  @Test
  void whenListIsOdd() {
    medianFinder.addNum(2);
    medianFinder.addNum(3);
    medianFinder.addNum(4);
    assertThat(medianFinder.findMedian()).isCloseTo(3.0, within(TOLERANCE));
  }

  @Test
  void whenListIsOddAndDescending() {
    medianFinder.addNum(4);
    medianFinder.addNum(3);
    medianFinder.addNum(2);
    assertThat(medianFinder.findMedian()).isCloseTo(3.0, within(TOLERANCE));
  }

  @Test
  void withDuplicates() {
    medianFinder.addNum(1);
    medianFinder.addNum(1);
    medianFinder.addNum(1);
    assertThat(medianFinder.findMedian()).isCloseTo(1.0, within(TOLERANCE));
  }

  @Test
  void withNegativeNumbers() {
    medianFinder.addNum(-1);
    medianFinder.addNum(-2);
    medianFinder.addNum(-3);
    assertThat(medianFinder.findMedian()).isCloseTo(-2.0, within(TOLERANCE));
  }

  @Test
  void leetcodeExample() {
    medianFinder.addNum(1);
    medianFinder.addNum(2);
    assertThat(medianFinder.findMedian()).isCloseTo(1.5, within(TOLERANCE));
    medianFinder.addNum(3);
    assertThat(medianFinder.findMedian()).isCloseTo(2.0, within(TOLERANCE));
  }

  @Test
  void largerSequence() {
    int[] nums = {6, 10, 2, 6, 5, 0, 6, 3, 1, 0, 0};
    double[] expected = {6.0, 8.0, 6.0, 6.0, 6.0, 5.5, 6.0, 5.5, 5.0, 4.0, 3.0};
    for (int i = 0; i < nums.length; i++) {
      medianFinder.addNum(nums[i]);
      assertThat(medianFinder.findMedian()).isCloseTo(expected[i], within(TOLERANCE));
    }
  }
}
