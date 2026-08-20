package leetcode.p1301_1400;

/**
 * <a href="https://leetcode.com/problems/angle-between-hands-of-a-clock/">1344. Angle Between Hands
 * of a Clock</a>
 */
public class AngleBetweenHandsOfAClock {
  public double angleClock(int hour, int minutes) {
    double shortAngle = 30 * hour + 0.5 * minutes;
    double longAngle = (double) 6 * minutes;
    double angle = Math.abs(shortAngle - longAngle);
    return Math.min(angle, 360 - angle);
  }
}
