package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FirstBadVersionTest {
  private static FirstBadVersion firstBadAt(int firstBad) {
    return new FirstBadVersion() {
      @Override
      boolean isBadVersion(int version) {
        return version >= firstBad;
      }
    };
  }

  @Test
  void singleVersionIsBad() {
    assertThat(firstBadAt(1).firstBadVersion(1)).isEqualTo(1);
  }

  @Test
  void twoVersionsFirstIsBad() {
    assertThat(firstBadAt(1).firstBadVersion(2)).isEqualTo(1);
  }

  @Test
  void twoVersionsSecondIsBad() {
    assertThat(firstBadAt(2).firstBadVersion(2)).isEqualTo(2);
  }

  @Test
  void threeVersionsMiddleIsBad() {
    assertThat(firstBadAt(2).firstBadVersion(3)).isEqualTo(2);
  }

  @Test
  void leetCodeExample() {
    assertThat(firstBadAt(4).firstBadVersion(5)).isEqualTo(4);
  }

  @Test
  void firstOfTenIsBad() {
    assertThat(firstBadAt(1).firstBadVersion(10)).isEqualTo(1);
  }

  @Test
  void lastOfTenIsBad() {
    assertThat(firstBadAt(10).firstBadVersion(10)).isEqualTo(10);
  }

  @Test
  void middleOfTenIsBad() {
    assertThat(firstBadAt(5).firstBadVersion(10)).isEqualTo(5);
  }

  @Test
  void powerOfTwoRangeWithInteriorFirstBad() {
    assertThat(firstBadAt(5).firstBadVersion(8)).isEqualTo(5);
  }

  @Test
  void integerMaxRangeFirstBadAtStart() {
    assertThat(firstBadAt(1).firstBadVersion(Integer.MAX_VALUE)).isEqualTo(1);
  }

  @Test
  void integerMaxRangeFirstBadAtEnd() {
    assertThat(firstBadAt(Integer.MAX_VALUE).firstBadVersion(Integer.MAX_VALUE))
        .isEqualTo(Integer.MAX_VALUE);
  }

  @Test
  void integerMaxRangeFirstBadInMiddle() {
    assertThat(firstBadAt(1_073_741_824).firstBadVersion(Integer.MAX_VALUE))
        .isEqualTo(1_073_741_824);
  }
}
