package com.giwankim.leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class TriangleTest {
  Triangle sut = new Triangle();

  // Step 1: a one-row triangle returns its only element — the base case the DP unwinds to.
  @Test
  void singleElementReturnsThatElement() {
    assertThat(sut.minimumTotal(triangle(new int[] {5}))).isEqualTo(5);
  }

  // Step 2: LC Example 2 — the lone element may be negative.
  @Test
  void singleNegativeElementReturnsThatElement() {
    assertThat(sut.minimumTotal(triangle(new int[] {-10}))).isEqualTo(-10);
  }

  // Step 3: two rows pick the smaller adjacent child below the apex.
  @Test
  void twoRowsPicksSmallerChild() {
    assertThat(sut.minimumTotal(triangle(new int[] {1}, new int[] {2, 3}))).isEqualTo(3);
  }

  // Step 4: when the right child is smaller, the i+1 move must be taken (not just index i).
  @Test
  void twoRowsPicksRightChildWhenSmaller() {
    assertThat(sut.minimumTotal(triangle(new int[] {1}, new int[] {3, 2}))).isEqualTo(3);
  }

  // Step 5: equal children make the tie-break irrelevant — either path costs the same.
  @Test
  void twoRowsWithEqualChildren() {
    assertThat(sut.minimumTotal(triangle(new int[] {1}, new int[] {2, 2}))).isEqualTo(3);
  }

  // Step 6: LC Example 1 — the canonical 4-row triangle, min path 2 + 3 + 5 + 1.
  @Test
  void leetCodeExampleOne() {
    List<List<Integer>> triangle =
        triangle(new int[] {2}, new int[] {3, 4}, new int[] {6, 5, 7}, new int[] {4, 1, 8, 3});
    assertThat(sut.minimumTotal(triangle)).isEqualTo(11);
  }

  // Step 7: the greedy trap — the locally-cheaper first step (2 over 7) leads to a worse total,
  // so only a true DP that looks ahead finds 1 + 7 + 1 = 9 instead of 1 + 2 + 100 = 103.
  @Test
  void greedyChoiceIsNotOptimal() {
    List<List<Integer>> triangle =
        triangle(new int[] {1}, new int[] {2, 7}, new int[] {100, 100, 1});
    assertThat(sut.minimumTotal(triangle)).isEqualTo(9);
  }

  // Step 8: when the left edge is cheapest, every step stays at index i.
  @Test
  void leftEdgePathIsOptimal() {
    List<List<Integer>> triangle = triangle(new int[] {1}, new int[] {1, 9}, new int[] {1, 9, 9});
    assertThat(sut.minimumTotal(triangle)).isEqualTo(3);
  }

  // Step 9: when the right edge is cheapest, every step moves to index i+1.
  @Test
  void rightEdgePathIsOptimal() {
    List<List<Integer>> triangle = triangle(new int[] {1}, new int[] {9, 1}, new int[] {9, 9, 1});
    assertThat(sut.minimumTotal(triangle)).isEqualTo(3);
  }

  // Step 10: an all-negative triangle accumulates to the most-negative reachable path.
  @Test
  void allNegativeValuesAccumulate() {
    List<List<Integer>> triangle =
        triangle(new int[] {-1}, new int[] {-2, -3}, new int[] {-4, -5, -6});
    assertThat(sut.minimumTotal(triangle)).isEqualTo(-10);
  }

  // Step 11: a path may dip negative mid-way; mixed signs must sum correctly (2 - 5 - 2 = -5).
  @Test
  void mixedSignValues() {
    List<List<Integer>> triangle =
        triangle(new int[] {2}, new int[] {-5, 10}, new int[] {3, -2, 1});
    assertThat(sut.minimumTotal(triangle)).isEqualTo(-5);
  }

  // Step 12: a uniform triangle costs one cell per row — the path length equals the row count.
  @Test
  void uniformTriangleSumsOnePerRow() {
    List<List<Integer>> triangle =
        triangle(new int[] {1}, new int[] {1, 1}, new int[] {1, 1, 1}, new int[] {1, 1, 1, 1});
    assertThat(sut.minimumTotal(triangle)).isEqualTo(4);
  }

  // Step 13: values at the ±10^4 constraint bound are handled (10000 + -10000 = 0).
  @Test
  void constraintMagnitudeBoundaryValues() {
    assertThat(sut.minimumTotal(triangle(new int[] {10_000}, new int[] {-10_000, 10_000})))
        .isEqualTo(0);
  }

  // Step 14: the largest triangle (200 rows) of max values stays within int range (2_000_000).
  @Test
  void maximumSizeTriangleDoesNotOverflowInt() {
    int rows = 200;
    int value = 10_000;
    List<List<Integer>> triangle = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      List<Integer> row = new ArrayList<>();
      for (int j = 0; j <= i; j++) {
        row.add(value);
      }
      triangle.add(row);
    }
    assertThat(sut.minimumTotal(triangle)).isEqualTo(rows * value);
  }

  /**
   * Builds a triangle from row arrays so tests can pass int literals instead of list boilerplate.
   */
  private static List<List<Integer>> triangle(int[]... rows) {
    return Arrays.stream(rows).map(row -> Arrays.stream(row).boxed().toList()).toList();
  }
}
