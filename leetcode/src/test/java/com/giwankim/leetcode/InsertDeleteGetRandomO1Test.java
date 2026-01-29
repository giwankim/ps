package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsertDeleteGetRandomO1Test {
  private InsertDeleteGetRandomO1 sut;

  @BeforeEach
  void setUp() {
    sut = new InsertDeleteGetRandomO1();
  }

  @Test
  void insert() {
    assertThat(sut.insert(1)).isTrue();
    assertThat(sut.insert(1)).isFalse();
  }

  @Test
  void remove() {
    sut.insert(1);
    assertThat(sut.remove(1)).isTrue();
    assertThat(sut.remove(1)).isFalse();
  }

  @Test
  void remove2() {
    sut.insert(2);
    sut.insert(1);
    sut.insert(3);
    assertThat(sut.remove(2)).isTrue();
  }

  @Test
  void getRandom() {
    sut.insert(1);
    sut.remove(2);
    sut.insert(2);
    assertThat(sut.getRandom()).isIn(1, 2);
    sut.remove(1);
    sut.insert(2);
    assertThat(sut.getRandom()).isEqualTo(2);
  }
}
