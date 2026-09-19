package leetcode.p1401_1500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class CircleAndRectangleOverlappingTest {
  CircleAndRectangleOverlapping sut = new CircleAndRectangleOverlapping();

  // ===========================================================================================
  // The floor (Steps 1-2). Arguments are radius, xCenter, yCenter, then x1, y1, x2, y2.
  // ===========================================================================================

  // Step 1: the smallest radius (1 <= radius) with the center strictly inside the rectangle, so
  //         the nearest rectangle point is the center itself at distance 0. All four corners sit
  //         at squared distance 2 > 1, so a solution that only asks whether some rectangle corner
  //         lies inside the circle answers false
  @Test
  void centerInsideRectangleOverlaps() {
    assertThat(sut.checkOverlap(1, 0, 0, -1, -1, 1, 1)).isTrue();
  }

  // Step 2: the other answer. The rectangle is far up and to the right, nearest corner at squared
  //         distance 50 against radius squared 1. Kills "always true", and a clamp that applies
  //         only the upper bounds x2 and y2 leaves the point on the center and answers true
  @Test
  void farApartDoesNotOverlap() {
    assertThat(sut.checkOverlap(1, 0, 0, 5, 5, 6, 6)).isFalse();
  }

  // ===========================================================================================
  // One side at a time (Steps 3-6). Circle of radius 3 centered at x 2, y 1. The center's other
  // coordinate is inside the rectangle's span, so only one axis contributes distance. Each step
  // pairs the tangent position (touching counts, distance == radius) with the rectangle moved
  // one unit further away.
  // ===========================================================================================

  // Step 3: rectangle to the right, left edge at x 5, exactly 3 from the center. Touching is
  //         overlapping, so a strict less-than comparison answers false for the first call. No
  //         corner is inside the circle (nearest is 9 + 25 = 34 > 9), so corners-only answers
  //         false as well. One unit further the gap is 4 > 3
  @Test
  void rectangleToTheRightTouchesAtDistanceEqualToRadius() {
    assertThat(sut.checkOverlap(3, 2, 1, 5, -4, 9, 6)).isTrue();
    assertThat(sut.checkOverlap(3, 2, 1, 6, -4, 9, 6)).isFalse();
  }

  // Step 4: the mirror, rectangle to the left, right edge at x -1. Now x2 is the bound that
  //         matters. A clamp that applies only the lower bounds x1 and y1 never pulls the point
  //         left of the center and answers true for the second call
  @Test
  void rectangleToTheLeftTouchesAtDistanceEqualToRadius() {
    assertThat(sut.checkOverlap(3, 2, 1, -5, -4, -1, 6)).isTrue();
    assertThat(sut.checkOverlap(3, 2, 1, -5, -4, -2, 6)).isFalse();
  }

  // Step 5: the same rule on the other axis, rectangle above with bottom edge at y 4. A solution
  //         that measures only the x axis, or that swaps the x and y bounds, answers true for the
  //         second call
  @Test
  void rectangleAboveTouchesAtDistanceEqualToRadius() {
    assertThat(sut.checkOverlap(3, 2, 1, -4, 4, 8, 9)).isTrue();
    assertThat(sut.checkOverlap(3, 2, 1, -4, 5, 8, 9)).isFalse();
  }

  // Step 6: the mirror, rectangle below with top edge at y -2. Together Steps 3-6 exercise each of
  //         x1, x2, y1, y2 as the deciding bound exactly once
  @Test
  void rectangleBelowTouchesAtDistanceEqualToRadius() {
    assertThat(sut.checkOverlap(3, 2, 1, -4, -7, 8, -2)).isTrue();
    assertThat(sut.checkOverlap(3, 2, 1, -4, -7, 8, -3)).isFalse();
  }

  // ===========================================================================================
  // Corners (Steps 7-11). Circle of radius 5 at the origin, rectangle diagonal to it, so both
  // axes contribute and the distance is Euclidean: dx^2 + dy^2 against 25.
  // ===========================================================================================

  // Step 7: the classic wrong answer. The circle's bounding square reaches x 5 and y 5, past the
  //         rectangle's corner at x 4, y 4, so a bounding-box intersection test answers true. The
  //         corner is at 16 + 16 = 32 > 25, outside the round circle
  @Test
  void boundingBoxesIntersectButCircleMissesTheCorner() {
    assertThat(sut.checkOverlap(5, 0, 0, 4, 4, 8, 8)).isFalse();
  }

  // Step 8: a 3-4-5 triangle puts the top-right rectangle's corner exactly on the circle,
  //         9 + 16 = 25. Strict less-than answers false. Manhattan distance 3 + 4 = 7 > 5 also
  //         answers false
  @Test
  void topRightRectangleCornerExactlyOnTheCircle() {
    assertThat(sut.checkOverlap(5, 0, 0, 3, 4, 8, 8)).isTrue();
  }

  // Step 9: top-left quadrant, deciding corner is x2 with y1. Tangent at x -4, y 3, then a miss
  //         with the corner at x -4, y 4 where 32 > 25
  @Test
  void topLeftRectangleCornerTangentThenMiss() {
    assertThat(sut.checkOverlap(5, 0, 0, -8, 3, -4, 8)).isTrue();
    assertThat(sut.checkOverlap(5, 0, 0, -8, 4, -4, 8)).isFalse();
  }

  // Step 10: bottom-left quadrant, deciding corner is x2 with y2, both upper bounds
  @Test
  void bottomLeftRectangleCornerTangentThenMiss() {
    assertThat(sut.checkOverlap(5, 0, 0, -8, -8, -3, -4)).isTrue();
    assertThat(sut.checkOverlap(5, 0, 0, -8, -8, -4, -4)).isFalse();
  }

  // Step 11: bottom-right quadrant, deciding corner is x1 with y2. Steps 8-11 cover all four
  //          corner pairings, so any single mixed-up bound flips one of them
  @Test
  void bottomRightRectangleCornerTangentThenMiss() {
    assertThat(sut.checkOverlap(5, 0, 0, 4, -8, 8, -3)).isTrue();
    assertThat(sut.checkOverlap(5, 0, 0, 4, -8, 8, -4)).isFalse();
  }

  // ===========================================================================================
  // Containment and slicing (Steps 12-16). The statement asks for any shared point, and both
  // shapes are filled regions, not outlines.
  // ===========================================================================================

  // Step 12: a small circle deep inside a large rectangle. The outlines never cross and every
  //          corner is far outside the circle, so outline-intersection and corners-only both
  //          answer false. The circle's own points all belong to the rectangle
  @Test
  void circleDeepInsideLargeRectangleOverlaps() {
    assertThat(sut.checkOverlap(1, 0, 0, -100, -100, 100, 100)).isTrue();
  }

  // Step 13: the mirror, a small rectangle inside a large circle, not containing the center.
  //          Outline-intersection answers false, and so does "is the center in the rectangle"
  @Test
  void rectangleInsideLargeCircleOverlaps() {
    assertThat(sut.checkOverlap(10, 0, 0, 1, 1, 2, 2)).isTrue();
  }

  // Step 14: a thin strip slices through the circle. Its corners are at 100 + 9 = 109 > 25, all
  //          outside, and the center is not in the strip, so "any corner inside the circle or
  //          center inside the rectangle" answers false. The nearest point is x 0, y 3, at 3 <= 5
  @Test
  void thinStripSlicingThroughTheCircleOverlaps() {
    assertThat(sut.checkOverlap(5, 0, 0, -10, 3, 10, 4)).isTrue();
  }

  // Step 15: the same strip raised to y 6 passes clear of the circle, nearest point at 6 > 5. A
  //          solution that measures only the x axis answers true
  @Test
  void thinStripPassingClearOfTheCircleDoesNotOverlap() {
    assertThat(sut.checkOverlap(5, 0, 0, -10, 6, 10, 7)).isFalse();
  }

  // Step 16: the center sits on the rectangle's left edge, away from its corners. The edge belongs
  //          to the rectangle, so the distance is 0. Corners are at 25 > 1
  @Test
  void centerOnRectangleEdgeOverlaps() {
    assertThat(sut.checkOverlap(1, 0, 0, 0, -5, 7, 5)).isTrue();
  }

  // ===========================================================================================
  // The official examples, verbatim (Steps 17-19).
  // ===========================================================================================

  // Step 17: LeetCode Example 1. The shapes share exactly one point, x 1, y 0, the midpoint of the
  //          rectangle's left edge. Rules out strict less-than, corners-only (corners are at
  //          1 + 1 = 2 > 1), and center-in-rectangle
  @Test
  void leetCodeExample1() {
    assertThat(sut.checkOverlap(1, 0, 0, 1, -1, 3, 1)).isTrue();
  }

  // Step 18: LeetCode Example 2. The center's x lies inside the rectangle's x span, so the x axis
  //          contributes nothing, but the rectangle's top at y -1 is 2 below the center. Rules
  //          out measuring x alone, and a clamp that forgets the upper bound y2
  @Test
  void leetCodeExample2() {
    assertThat(sut.checkOverlap(1, 1, 1, 1, -3, 2, -1)).isFalse();
  }

  // Step 19: LeetCode Example 3. The center coincides with the rectangle's bottom-right corner.
  //          Rules out containment checks written with open intervals, which see the center as
  //          outside the rectangle
  @Test
  void leetCodeExample3() {
    assertThat(sut.checkOverlap(1, 0, 0, -1, 0, 0, 1)).isTrue();
  }

  // ===========================================================================================
  // Constraint bounds (Steps 20-24): radius 2000, coordinates at -10^4 and 10^4. The largest
  // squared distance these allow is 19999^2 + 19999^2 = 799_920_002, which still fits in an int,
  // so exact integer arithmetic is always available and no step needs a tolerance. The closed
  // form is O(1). The timeouts are there for sampling solutions: scanning the 4 * 10^8 integer
  // points of a full-range rectangle may squeak through, but any finer grid (step 0.1 is
  // 4 * 10^10 points) cannot finish.
  // ===========================================================================================

  // Step 20: maximum radius with the corner exactly on the circle, 1200^2 + 1600^2 = 2000^2.
  //          Squared integers compare exactly. A float square root or a strict comparison has no
  //          slack here
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxRadiusCornerExactlyOnTheCircle() {
    assertThat(sut.checkOverlap(2000, 0, 0, 1200, 1600, 1300, 1700)).isTrue();
  }

  // Step 21: the same corner moved one unit out along either axis. The squared distance becomes
  //          4_002_401 or 4_003_201 against 4_000_000, a relative gap under 0.1 percent, so any
  //          epsilon added to forgive rounding must stay smaller than that
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maxRadiusCornerOneUnitOutsideTheCircle() {
    assertThat(sut.checkOverlap(2000, 0, 0, 1201, 1600, 1300, 1700)).isFalse();
    assertThat(sut.checkOverlap(2000, 0, 0, 1200, 1601, 1300, 1700)).isFalse();
  }

  // Step 22: opposite corners of the coordinate range, the largest distance the constraints
  //          allow. dx = dy = 19999 and the squared distance is 799_920_002 against 4_000_000
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void oppositeCornersOfTheCoordinateRangeDoNotOverlap() {
    assertThat(sut.checkOverlap(2000, -10_000, -10_000, 9_999, 9_999, 10_000, 10_000))
        .isFalse();
  }

  // Step 23: the largest rectangle containing the largest circle. Step 12 at full scale
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void fullRangeRectangleContainsMaxRadiusCircle() {
    assertThat(sut.checkOverlap(2000, 0, 0, -10_000, -10_000, 10_000, 10_000)).isTrue();
  }

  // Step 24: a nearly full-range rectangle whose top edge stops at y 7999, 2001 below a center on
  //          the top boundary, then at y 8000, exactly 2000 below. The false call gives a scanning
  //          solution no early exit, so it must visit every sample point
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void nearlyFullRangeRectangleMissesThenTouchesByOneUnit() {
    assertThat(sut.checkOverlap(2000, 0, 10_000, -10_000, -10_000, 10_000, 7_999))
        .isFalse();
    assertThat(sut.checkOverlap(2000, 0, 10_000, -10_000, -10_000, 10_000, 8_000))
        .isTrue();
  }

  // ===========================================================================================
  // Hygiene (Step 25). All arguments are primitives, so there is no input to protect from
  // mutation. Only instance state can go wrong.
  // ===========================================================================================

  // Step 25: one instance answers many inputs, different scales out of order with the largest in
  //          the middle and the answers alternating. Catches a nearest point or a result cached
  //          on the instance instead of computed per call
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.checkOverlap(1, 0, 0, 1, -1, 3, 1)).isTrue();
    assertThat(sut.checkOverlap(5, 0, 0, 4, 4, 8, 8)).isFalse();
    assertThat(sut.checkOverlap(2000, 0, 0, 1200, 1600, 1300, 1700)).isTrue();
    assertThat(sut.checkOverlap(2000, -10_000, -10_000, 9_999, 9_999, 10_000, 10_000))
        .isFalse();
    assertThat(sut.checkOverlap(1, 0, 0, -1, 0, 0, 1)).isTrue();
    assertThat(sut.checkOverlap(1, 1, 1, 1, -3, 2, -1)).isFalse();
  }
}
