package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SqrtTest {
  Sqrt sut = new Sqrt();

  @Test
  void zeroReturnsZero() {
    assertThat(sut.mySqrt(0)).isZero();
  }

  @Test
  void oneReturnsOne() {
    assertThat(sut.mySqrt(1)).isEqualTo(1);
  }

  @Test
  void twoIsRoundedDownToOne() {
    assertThat(sut.mySqrt(2)).isEqualTo(1);
  }

  @Test
  void threeIsRoundedDownToOne() {
    assertThat(sut.mySqrt(3)).isEqualTo(1);
  }

  @Test
  void leetCodeExampleOnePerfectSquare() {
    assertThat(sut.mySqrt(4)).isEqualTo(2);
  }

  @Test
  void leetCodeExampleTwoNonPerfectSquareRoundsDown() {
    assertThat(sut.mySqrt(8)).isEqualTo(2);
  }

  @Test
  void nineReturnsThree() {
    assertThat(sut.mySqrt(9)).isEqualTo(3);
  }

  @Test
  void fifteenIsRoundedDownToThree() {
    assertThat(sut.mySqrt(15)).isEqualTo(3);
  }

  @Test
  void sixteenReturnsFour() {
    assertThat(sut.mySqrt(16)).isEqualTo(4);
  }

  @Test
  void ninetyNineIsRoundedDownToNine() {
    assertThat(sut.mySqrt(99)).isEqualTo(9);
  }

  @Test
  void oneHundredReturnsTen() {
    assertThat(sut.mySqrt(100)).isEqualTo(10);
  }

  @Test
  void largestPerfectSquareWithinIntRange() {
    assertThat(sut.mySqrt(2_147_395_600)).isEqualTo(46340);
  }

  @Test
  void oneBelowLargestPerfectSquareReturnsPreviousRoot() {
    assertThat(sut.mySqrt(2_147_395_599)).isEqualTo(46339);
  }

  @Test
  void integerMaxValueReturnsLargestIntRoot() {
    assertThat(sut.mySqrt(Integer.MAX_VALUE)).isEqualTo(46340);
  }
}
