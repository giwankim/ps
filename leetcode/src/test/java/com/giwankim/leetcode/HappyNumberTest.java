package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HappyNumberTest {
  HappyNumber sut = new HappyNumber();

  @Test
  void baseCase() {
    assertThat(sut.isHappy(1)).isTrue();
  }

  @Test
  void notHappyNumber() {
    assertThat(sut.isHappy(2)).isFalse();
  }

  @Test
  void happyNumber() {
    assertThat(sut.isHappy(19)).isTrue();
  }
}
