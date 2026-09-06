package leetcode.p0601_0700;

/** <a href="https://leetcode.com/problems/can-place-flowers/">605. Can Place Flowers</a> */
public class CanPlaceFlowers {
  /**
   * @implNote A single greedy scan plants every empty plot whose neighbors are empty, treating the
   *     positions past both ends as empty so the edges need no special case. The earliest legal
   *     plot can be swapped into any optimal plan, so the count is maximal. Each planting is
   *     recorded in place so the next plot's left check sees it, which means the caller's array is
   *     modified. Time {@code O(m)}, auxiliary space {@code O(1)}, where {@code m =
   *     flowerbed.length}.
   */
  public boolean canPlaceFlowers(int[] flowerbed, int n) {
    int m = flowerbed.length;
    int cnt = 0;
    for (int i = 0; i < m; i++) {
      boolean left = (i == 0 || flowerbed[i - 1] == 0);
      boolean right = (i == m - 1 || flowerbed[i + 1] == 0);
      if (flowerbed[i] == 0 && left && right) {
        cnt++;
        flowerbed[i] = 1;
      }
    }
    return cnt >= n;
  }
}
