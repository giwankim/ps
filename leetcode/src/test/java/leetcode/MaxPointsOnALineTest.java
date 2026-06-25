package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaxPointsOnALineTest {
  MaxPointsOnALine sut = new MaxPointsOnALine();

  // Step 1: minimum input per LeetCode constraint points.length >= 1.
  @Test
  void singlePointReturnsOne() {
    assertThat(sut.maxPoints(new int[][] {{1, 1}})).isEqualTo(1);
  }

  // Step 2: any two unique points always define one line.
  @Test
  void twoPointsReturnTwo() {
    assertThat(sut.maxPoints(new int[][] {{1, 1}, {2, 3}})).isEqualTo(2);
  }

  // Step 3: LC Example 1 — all points lie on y = x.
  @Test
  void leetCodeExampleOneAllPointsOnSameDiagonal() {
    assertThat(sut.maxPoints(new int[][] {{1, 1}, {2, 2}, {3, 3}})).isEqualTo(3);
  }

  // Step 4: LC Example 2 — the best line has four points among distractors.
  @Test
  void leetCodeExampleTwoFindsBestLineAmongDistractors() {
    assertThat(sut.maxPoints(new int[][] {{1, 1}, {3, 2}, {5, 3}, {4, 1}, {2, 3}, {1, 4}}))
        .isEqualTo(4);
  }

  // Step 5: with no three collinear points, the maximum is any pair.
  @Test
  void noThreePointsOnSameLineReturnsTwo() {
    assertThat(sut.maxPoints(new int[][] {{0, 0}, {1, 2}, {2, 5}, {3, 10}})).isEqualTo(2);
  }

  // Step 6: vertical lines have an undefined slope and must be counted separately.
  @Test
  void verticalLineCountsPointsWithSameXCoordinate() {
    assertThat(sut.maxPoints(new int[][] {{2, -1}, {2, 0}, {2, 3}, {0, 0}, {3, 1}}))
        .isEqualTo(3);
  }

  // Step 7: horizontal lines have zero slope and must include negative x-coordinates.
  @Test
  void horizontalLineCountsPointsWithSameYCoordinate() {
    assertThat(sut.maxPoints(new int[][] {{-3, 4}, {-1, 4}, {2, 4}, {5, 4}, {0, 0}}))
        .isEqualTo(4);
  }

  // Step 8: negative-slope lines should normalize signs consistently across quadrants.
  @Test
  void negativeSlopeLineAcrossQuadrants() {
    assertThat(sut.maxPoints(new int[][] {{-1, 1}, {0, 0}, {1, -1}, {2, -2}, {2, 2}}))
        .isEqualTo(4);
  }

  // Step 9: equivalent fractional slopes such as 1/2 and 2/4 represent the same line.
  @Test
  void reducedFractionalSlopeCountsAsSameLine() {
    assertThat(sut.maxPoints(new int[][] {{0, 0}, {2, 1}, {4, 2}, {6, 3}, {3, 3}}))
        .isEqualTo(4);
  }

  // Step 10: parallel lines with the same slope but different intercepts must not be merged.
  @Test
  void parallelLinesAreNotCombinedBySlopeOnly() {
    assertThat(sut.maxPoints(new int[][] {{0, 0}, {1, 1}, {2, 2}, {0, 1}, {1, 2}, {2, 3}}))
        .isEqualTo(3);
  }

  // Step 11: coordinate bounds include -10^4 and 10^4.
  @Test
  void coordinateBoundsCanBePartOfTheBestLine() {
    assertThat(sut.maxPoints(
            new int[][] {{-10_000, -10_000}, {0, 0}, {10_000, 10_000}, {-10_000, 10_000}, {9_999, 0}
            }))
        .isEqualTo(3);
  }

  // Step 12: maximum input length per LeetCode constraint points.length <= 300.
  @Test
  void maxLengthAllPointsOnSameLineReturnsThreeHundred() {
    int[][] points = new int[300][2];
    for (int i = 0; i < points.length; i++) {
      int x = i - 150;
      points[i] = new int[] {x, 2 * x + 3};
    }

    assertThat(sut.maxPoints(points)).isEqualTo(300);
  }

  // Step 13: the lowest-index point on the best line may sit between its neighbors,
  // so collinear points on both sides of an anchor must group under one slope.
  @Test
  void collinearPointsStraddlingTheAnchorAreCounted() {
    assertThat(sut.maxPoints(new int[][] {{0, 0}, {-2, -2}, {2, 2}, {1, 0}, {0, 1}}))
        .isEqualTo(3);
  }

  // Step 14: slopes differing only in sign (+1 vs -1) are different lines through the
  // same anchor and must not be merged.
  @Test
  void oppositeSignSlopesThroughSameAnchorAreNotMerged() {
    assertThat(sut.maxPoints(new int[][] {{0, 0}, {1, 1}, {2, 2}, {1, -1}})).isEqualTo(3);
  }

  // Step 15: a negative fractional slope such as -1/2 must reduce consistently
  // (-2/4 and -3/6 represent the same line).
  @Test
  void negativeFractionalSlopeReducesConsistently() {
    assertThat(sut.maxPoints(new int[][] {{0, 0}, {2, -1}, {4, -2}, {6, -3}, {1, 1}}))
        .isEqualTo(4);
  }
}
