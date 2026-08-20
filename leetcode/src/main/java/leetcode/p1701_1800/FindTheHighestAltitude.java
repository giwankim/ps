package leetcode.p1701_1800;

/**
 * <a href="https://leetcode.com/problems/find-the-highest-altitude/">1732. Find the Highest
 * Altitude</a>
 */
public class FindTheHighestAltitude {
  public int largestAltitude(int[] gain) {
    int result = 0;
    int altitude = 0;
    for (int g : gain) {
      altitude += g;
      result = Math.max(result, altitude);
    }
    return result;
  }
}
