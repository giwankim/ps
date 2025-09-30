package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FirstBadVersionTest {

  @Test
  void firstBadVersion() {
    FirstBadVersion firstBadVersion = new FirstBadVersion(4);
    int actual = firstBadVersion.firstBadVersion(5);
    assertThat(actual).isEqualTo(4);
  }

  @Test
  void singleton() {
    int actual = new FirstBadVersion(1).firstBadVersion(1);
    assertThat(actual).isEqualTo(1);
  }
}
