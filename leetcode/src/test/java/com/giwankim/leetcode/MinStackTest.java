package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MinStackTest {
  MinStack sut;

  @BeforeEach
  void setUp() {
    sut = new MinStack();
  }

  @Test
  void push() {
    sut.push(1);
    assertThat(sut.top()).isEqualTo(1);
  }

  @Test
  void pop() {
    sut.push(1);
    sut.push(2);

    sut.pop();

    assertThat(sut.top()).isEqualTo(1);
    assertThat(sut.getMin()).isEqualTo(1);
  }

  @Test
  void top() {
    sut.push(1);
    sut.push(2);
    assertThat(sut.top()).isEqualTo(2);
  }

  @Test
  void getMin() {
    sut.push(1);
    sut.push(2);
    assertThat(sut.getMin()).isEqualTo(1);
  }

  @Test
  void minStack() {
    sut.push(-2);
    sut.push(0);
    sut.push(-3);
    assertThat(sut.getMin()).isEqualTo(-3);
    sut.pop();
    assertThat(sut.top()).isEqualTo(0);
    assertThat(sut.getMin()).isEqualTo(-2);
  }
}
