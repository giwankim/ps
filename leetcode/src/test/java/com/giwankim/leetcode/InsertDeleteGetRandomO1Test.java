package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.leetcode.InsertDeleteGetRandomO1.RandomizedSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsertDeleteGetRandomO1Test {
  RandomizedSet sut;

  @BeforeEach
  void setUp() {
    sut = new RandomizedSet();
  }

  @Test
  void insert() {
    assertThat(sut.insert(1)).isTrue();
    assertThat(sut.insert(1)).isFalse();
    assertThat(sut.insert(2)).isTrue();
  }

  @Test
  void remove() {
    sut.insert(1);
    assertThat(sut.remove(1)).isTrue();
    assertThat(sut.remove(1)).isFalse();
  }

  @Test
  void removeTwice() {
    sut.insert(2);
    assertThat(sut.remove(2)).isTrue();
    assertThat(sut.remove(2)).isFalse();
    sut.insert(2);
    assertThat(sut.remove(2)).isTrue();
  }

  @Test
  void getRandom() {
    sut.insert(1);
    assertThat(sut.getRandom()).isEqualTo(1);
    sut.insert(2);
    sut.insert(3);
    assertThat(sut.getRandom()).isIn(1, 2, 3);
  }

  @Test
  void mixedOperations() {
    sut.insert(1);
    sut.remove(2);
    sut.insert(2);
    assertThat(sut.getRandom()).isIn(1, 2);
    sut.remove(1);
    sut.insert(2);
    assertThat(sut.getRandom()).isEqualTo(2);
  }
}
