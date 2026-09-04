package leetcode.p0601_0700;

/** <a href="https://leetcode.com/problems/can-place-flowers/">605. Can Place Flowers</a> */
public class CanPlaceFlowers {
  /**
   * @implNote Time {@code O(m)}, auxiliary space {@code O(1)}, where {@code m = flowerbed.length}.
   *     Planting is recorded in place, so the caller's array is modified.
   */
  public boolean canPlaceFlowers(int[] flowerbed, int n) {
    int m = flowerbed.length;
    if (m == 1) {
      return (flowerbed[0] == 0 ? 1 : 0) >= n;
    }
    int cnt = 0;
    if (flowerbed[0] == 0 && flowerbed[1] == 0) {
      cnt++;
      flowerbed[0] = 1;
    }
    for (int i = 1; i < m - 1; i++) {
      if (flowerbed[i] == 0 && flowerbed[i - 1] == 0 && flowerbed[i + 1] == 0) {
        cnt++;
        flowerbed[i] = 1;
      }
    }
    if (flowerbed[m - 2] == 0 && flowerbed[m - 1] == 0) {
      cnt++;
      flowerbed[m - 1] = 1;
    }
    return cnt >= n;
  }
}
