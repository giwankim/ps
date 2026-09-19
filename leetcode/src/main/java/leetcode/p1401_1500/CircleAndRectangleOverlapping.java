package leetcode.p1401_1500;

/**
 * <a href="https://leetcode.com/problems/circle-and-rectangle-overlapping/">1401. Circle and
 * Rectangle Overlapping</a>
 */
public class CircleAndRectangleOverlapping {
  /**
   * @implNote Time {@code O(1)}: the squared distance from the center to the rectangle comes from
   *     at most two fixed comparisons per axis. Space {@code O(1)} because only one scalar
   *     accumulator is stored.
   */
  public boolean checkOverlap(
      int radius, int xCenter, int yCenter, int x1, int y1, int x2, int y2) {
    int dist = 0;
    if (x2 < xCenter) {
      dist += (xCenter - x2) * (xCenter - x2);
    } else if (xCenter < x1) {
      dist += (x1 - xCenter) * (x1 - xCenter);
    }
    if (yCenter > y2) {
      dist += (yCenter - y2) * (yCenter - y2);
    } else if (yCenter < y1) {
      dist += (y1 - yCenter) * (y1 - yCenter);
    }
    return dist <= radius * radius;
  }
}
