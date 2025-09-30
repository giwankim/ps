package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PerfectRectangleTest {

  @Test
  void rectangle() {
    int[][] rectangles =
        new int[][] {
          {1, 1, 3, 3},
          {3, 1, 4, 2},
          {3, 2, 4, 4},
          {1, 3, 2, 4},
          {2, 3, 3, 4}
        };
    assertThat(new PerfectRectangle().isRectangleCover(rectangles)).isTrue();
  }

  @Test
  void disjoint() {
    int[][] rectangles =
        new int[][] {
          {1, 1, 2, 3},
          {1, 3, 2, 4},
          {3, 1, 4, 2},
          {3, 2, 4, 4}
        };
    assertThat(new PerfectRectangle().isRectangleCover(rectangles)).isFalse();
  }

  @Test
  void overlap() {
    int[][] rectangles =
        new int[][] {
          {1, 1, 3, 3},
          {3, 1, 4, 2},
          {1, 3, 2, 4},
          {2, 2, 4, 4}
        };
    assertThat(new PerfectRectangle().isRectangleCover(rectangles)).isFalse();
  }
}
