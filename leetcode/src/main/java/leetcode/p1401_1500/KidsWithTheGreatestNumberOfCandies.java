package leetcode.p1401_1500;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.com/problems/kids-with-the-greatest-number-of-candies/">1431. Kids With
 * the Greatest Number of Candies</a>
 */
public class KidsWithTheGreatestNumberOfCandies {
  /**
   * @implNote Time {@code O(n)} — one pass takes the maximum, a second compares each kid's total
   *     against it. Auxiliary space {@code O(1)} beyond the returned list of {@code n} booleans,
   *     and {@code candies} is left unmodified, where {@code n = candies.length}.
   */
  public List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
    int n = candies.length;
    List<Boolean> result = new ArrayList<>(n);
    int max = 0;
    for (int candy : candies) {
      max = Math.max(max, candy);
    }
    for (int candy : candies) {
      if (candy + extraCandies >= max) {
        result.add(true);
      } else {
        result.add(false);
      }
    }
    return result;
  }
}
