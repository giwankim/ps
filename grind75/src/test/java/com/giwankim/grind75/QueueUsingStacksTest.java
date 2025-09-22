package com.giwankim.grind75;

import static org.assertj.core.api.Assertions.assertThat;

import com.giwankim.grind75.QueueUsingStacks.MyQueue;
import org.junit.jupiter.api.Test;

class QueueUsingStacksTest {

  @Test
  void myQueue() {
    MyQueue queue = new MyQueue();
    queue.push(1);
    queue.push(2);
    assertThat(queue.peek()).isEqualTo(1);
    assertThat(queue.pop()).isEqualTo(1);
    assertThat(queue.empty()).isFalse();
  }

  @Test
  void popThenPush() {
    MyQueue queue = new MyQueue();
    queue.push(1);
    queue.push(2);
    queue.push(3);
    queue.push(4);
    assertThat(queue.pop()).isEqualTo(1);
    queue.push(5);
    assertThat(queue.pop()).isEqualTo(2);
    assertThat(queue.pop()).isEqualTo(3);
    assertThat(queue.pop()).isEqualTo(4);
    assertThat(queue.pop()).isEqualTo(5);
  }
}
