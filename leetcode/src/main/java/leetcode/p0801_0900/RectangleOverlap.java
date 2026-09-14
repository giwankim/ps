package leetcode.p0801_0900;

/** <a href="https://leetcode.com/problems/rectangle-overlap/">836. Rectangle Overlap</a> */
public class RectangleOverlap {
  /**
   * @implNote Time {@code O(1)}: the intersection's corners come from four fixed max/min
   *     comparisons. Space {@code O(1)} because only four scalar coordinates are stored.
   */
  public boolean isRectangleOverlap(int[] rec1, int[] rec2) {
    int x1 = Math.max(rec1[0], rec2[0]);
    int y1 = Math.max(rec1[1], rec2[1]);
    int x2 = Math.min(rec1[2], rec2[2]);
    int y2 = Math.min(rec1[3], rec2[3]);
    return x1 < x2 && y1 < y2;
  }
}
