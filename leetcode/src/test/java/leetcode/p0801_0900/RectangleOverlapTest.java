package leetcode.p0801_0900;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RectangleOverlapTest {
  private static final int MAX = 1_000_000_000;

  RectangleOverlap sut = new RectangleOverlap();

  // ===========================================================================================
  // The floor and the four ways to be apart (Steps 1-5).
  // ===========================================================================================

  // Step 1: the smallest rectangles the constraints allow, placed on top of each other. The
  //         intersection is the whole unit square. A solution that asks whether some corner of one
  //         rectangle lies strictly inside the other answers false, because every corner sits on
  //         the other's boundary, never in its interior
  @Test
  void identicalUnitSquaresOverlap() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 1, 1}, new int[] {0, 0, 1, 1}))
        .isTrue();
  }

  // Step 2: rec2 lies to the right with a gap of 1, while the y ranges coincide. A solution that
  //         only compares y ranges answers true, and so does one that forgot the clause
  //         rec1.x2 <= rec2.x1. The rectangles are tall rather than square so that reading y2
  //         where x2 belongs also answers true instead of hiding behind equal width and height
  @Test
  void rec2RightOfRec1WithAGapDoesNotOverlap() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 1, 3}, new int[] {2, 0, 3, 3}))
        .isFalse();
  }

  // Step 3: the mirror of Step 2 with the arguments swapped, so rec2 lies to the left. It kills a
  //         solution that tests "rec1 is left of rec2" but never "rec2 is left of rec1"
  @Test
  void rec2LeftOfRec1WithAGapDoesNotOverlap() {
    assertThat(sut.isRectangleOverlap(new int[] {2, 0, 3, 3}, new int[] {0, 0, 1, 3}))
        .isFalse();
  }

  // Step 4: rec2 lies above with a gap of 1, while the x ranges coincide. A solution that only
  //         compares x ranges, or forgot the clause rec1.y2 <= rec2.y1, answers true. Wide
  //         rectangles here for the same reason Step 2 uses tall ones
  @Test
  void rec2AboveRec1WithAGapDoesNotOverlap() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 3, 1}, new int[] {0, 2, 3, 3}))
        .isFalse();
  }

  // Step 5: the mirror of Step 4, so rec2 lies below. It kills a solution that tests "rec1 is below
  //         rec2" but never "rec2 is below rec1"
  @Test
  void rec2BelowRec1WithAGapDoesNotOverlap() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 2, 3, 3}, new int[] {0, 0, 3, 1}))
        .isFalse();
  }

  // ===========================================================================================
  // Touching is not overlapping (Steps 6-8).
  // ===========================================================================================

  // Step 6: the statement's "To be clear" sentence. rec2 shares the segment from y 1 to y 2 of
  //         rec1's right edge. The intersection is a line with zero area, so a solution that treats
  //         the rectangles as closed sets and uses < where <= belongs answers true
  @Test
  void sharingPartOfAVerticalEdgeDoesNotOverlap() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 2, 2}, new int[] {2, 1, 4, 3}))
        .isFalse();
  }

  // Step 7: the mirror of Step 6 on the other axis. rec2 shares the segment from x 1 to x 2 of
  //         rec1's top edge, and a strict-versus-inclusive slip in the y comparison answers true
  @Test
  void sharingPartOfAHorizontalEdgeDoesNotOverlap() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 2, 2}, new int[] {1, 2, 3, 4}))
        .isFalse();
  }

  // Step 8: the Note names corners separately from edges. The only common point is the corner at
  //         x 1, y 1, where both extents of the intersection are 0. A solution that rejects
  //         "one extent 0, the other positive" as edge contact but forgets the both-zero case
  //         answers true, as does any corner-in-closed-rectangle test
  @Test
  void touchingOnlyAtACornerDoesNotOverlap() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 1, 1}, new int[] {1, 1, 2, 2}))
        .isFalse();
  }

  // ===========================================================================================
  // Overlap with no corner strictly inside the other rectangle (Steps 9-13).
  // ===========================================================================================

  // Step 9: equal heights, shifted right by 1, so the intersection is the strip from x 1 to x 2.
  //         Each corner lies either outside the other rectangle or on its boundary, never strictly
  //         inside, so a strict corner test answers false even though the strip has area 2
  @Test
  void equalHeightsShiftedByOneOverlapInAStrip() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 2, 2}, new int[] {1, 0, 3, 2}))
        .isTrue();
  }

  // Step 10: rec2 sits strictly inside rec1, so the intersection is all of rec2. A solution that
  //          only asks whether a corner of rec1 lies inside rec2 answers false, because rec1's
  //          corners are all outside the smaller rectangle
  @Test
  void rec2StrictlyInsideRec1Overlaps() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 4, 4}, new int[] {1, 1, 2, 2}))
        .isTrue();
  }

  // Step 11: the mirror of Step 10, with rec1 inside rec2. It kills the opposite one-sided corner
  //          test, the one that only looks for a corner of rec2 inside rec1
  @Test
  void rec1StrictlyInsideRec2Overlaps() {
    assertThat(sut.isRectangleOverlap(new int[] {1, 1, 2, 2}, new int[] {0, 0, 4, 4}))
        .isTrue();
  }

  // Step 12: a plus sign. rec1 is a wide bar and rec2 a tall bar crossing it, overlapping in the
  //          unit square from x 1, y 1 to x 2, y 2. No corner of either lies inside the other, not
  //          even on its boundary, so every corner-containment test answers false. Overlap is a
  //          property of the two projections, not of the corners
  @Test
  void crossedBarsOverlapWithNoCornerInsideEither() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 1, 3, 2}, new int[] {1, 0, 2, 3}))
        .isTrue();
  }

  // Step 13: rec1 fills the bottom half of rec2, sharing three of its edges. A center-distance
  //          solution that halves with integer division answers false: the y centers 0.5 and 1
  //          truncate to 0 and 1, a distance of 1, while the summed heights 1 + 2 halve to 1, and
  //          1 < 1 fails. Doubled centers against summed extents involve no rounding at all
  @Test
  void rec1FillingTheBottomHalfOfRec2Overlaps() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 1, 1}, new int[] {0, 0, 1, 2}))
        .isTrue();
  }

  // ===========================================================================================
  // LeetCode examples (Steps 14-16).
  // ===========================================================================================

  // Step 14: LeetCode Example 1. The ordinary case, where the intersection is the unit square from
  //          x 1, y 1 to x 2, y 2 and a corner of each rectangle lies inside the other. Nearly
  // every
  //          wrong reading gets this one right, which is why it cannot be the only test
  @Test
  void leetCodeExample1() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 2, 2}, new int[] {1, 1, 3, 3}))
        .isTrue();
  }

  // Step 15: LeetCode Example 2. The unit squares share the whole edge at x 1, which rules out
  //          reading "overlap" as "the closed rectangles intersect"
  @Test
  void leetCodeExample2() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 1, 1}, new int[] {1, 0, 2, 1}))
        .isFalse();
  }

  // Step 16: LeetCode Example 3. Apart on both axes, so the signed intersection extents are
  //          1 - 2 = -1 wide and 1 - 2 = -1 tall. An area formula that multiplies them without
  //          clamping each to 0 gets (-1) * (-1) = 1 > 0 and answers true
  @Test
  void leetCodeExample3() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 1, 1}, new int[] {2, 2, 3, 3}))
        .isFalse();
  }

  // ===========================================================================================
  // Coordinate bounds, -10^9 <= rec[i] <= 10^9 (Steps 17-19). The answer is O(1) for any input,
  // so these steps carry no timeout. What they separate is arithmetic: a single extent is at most
  // 2 * 10^9 and still fits in int, but a product of two extents or a sum of two widths does not.
  // ===========================================================================================

  // Step 17: both rectangles span the whole coordinate range. The intersection extents are
  //          2 * 10^9 each, so the area 4 * 10^18 wraps to -1,651,507,200 in int and an area test
  //          answers false. A center-distance test fails the same way: the summed widths 4 * 10^9
  //          wrap to -294,967,296, and no distance is below that
  @Test
  void identicalFullRangeRectanglesOverlap() {
    assertThat(sut.isRectangleOverlap(
            new int[] {-MAX, -MAX, MAX, MAX}, new int[] {-MAX, -MAX, MAX, MAX}))
        .isTrue();
  }

  // Step 18: a 65536 by 65536 square inside the full range. The intersection is the square
  //          itself, with area 2^32, which is exactly 0 in int, so an int area test answers false.
  //          Unlike Step 17 the wrapped value is not even negative, just zero
  @Test
  void overlapAreaOfTwoToThe32ndOverlaps() {
    assertThat(sut.isRectangleOverlap(
            new int[] {0, 0, 65536, 65536}, new int[] {-MAX, -MAX, MAX, MAX}))
        .isTrue();
  }

  // Step 19: unit squares in opposite corners of the range, Example 3 stretched to the extremes.
  //          The signed extents are -1,999,999,998 on both axes, and their product, about
  //          4 * 10^18, is positive and fits in long. Widening an unclamped area formula to long
  //          removes the overflow but not the sign bug, and it still answers true
  @Test
  void unitSquaresInOppositeCornersOfTheRangeDoNotOverlap() {
    assertThat(sut.isRectangleOverlap(
            new int[] {-MAX, -MAX, -MAX + 1, -MAX + 1}, new int[] {MAX - 1, MAX - 1, MAX, MAX}))
        .isFalse();
  }

  // ===========================================================================================
  // Hygiene (Steps 20-21).
  // ===========================================================================================

  // Step 20: the caller's arrays come back untouched. A solution that clips rec1 to the
  //          intersection in place, taking the max of the lower corners and the min of the upper
  //          ones, answers Example 1 correctly but leaves rec1 as 1, 1, 2, 2
  @Test
  void inputArraysAreNotModified() {
    int[] rec1 = {0, 0, 2, 2};
    int[] rec2 = {1, 1, 3, 3};

    assertThat(sut.isRectangleOverlap(rec1, rec2)).isTrue();
    assertThat(rec1).containsExactly(0, 0, 2, 2);
    assertThat(rec2).containsExactly(1, 1, 3, 3);
  }

  // Step 21: one instance answers several inputs in a row, mixing false and true answers with the
  //          full-range case in the middle. It catches a solution that keeps a rectangle or an
  //          answer in a field and lets it leak into the next call
  @Test
  void oneInstanceAnswersManyInputs() {
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 1, 1}, new int[] {2, 2, 3, 3}))
        .isFalse();
    assertThat(sut.isRectangleOverlap(new int[] {0, 1, 3, 2}, new int[] {1, 0, 2, 3}))
        .isTrue();
    assertThat(sut.isRectangleOverlap(
            new int[] {-MAX, -MAX, MAX, MAX}, new int[] {-MAX, -MAX, MAX, MAX}))
        .isTrue();
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 1, 1}, new int[] {1, 1, 2, 2}))
        .isFalse();
    assertThat(sut.isRectangleOverlap(new int[] {0, 0, 2, 2}, new int[] {1, 1, 3, 3}))
        .isTrue();
  }
}
