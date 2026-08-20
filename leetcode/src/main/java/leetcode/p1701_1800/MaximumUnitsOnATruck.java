package leetcode.p1701_1800;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/maximum-units-on-a-truck/">1710. Maximum Units on a
 * Truck</a>
 */
public class MaximumUnitsOnATruck {
  public int maximumUnits(int[][] boxTypes, int truckSize) {
    int result = 0;
    //    Arrays.sort(boxTypes, Comparator.<int[]>comparingInt(a -> a[1]).reversed());
    Arrays.sort(boxTypes, (a, b) -> b[1] - a[1]);
    for (int[] boxType : boxTypes) {
      int take = Math.min(boxType[0], truckSize);
      result += take * boxType[1];
      truckSize -= take;
    }
    return result;
  }
}
