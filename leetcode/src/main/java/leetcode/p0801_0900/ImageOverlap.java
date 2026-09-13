package leetcode.p0801_0900;

/** <a href="https://leetcode.com/problems/image-overlap/">835. Image Overlap</a> */
public class ImageOverlap {
  /**
   * @implNote Time {@code O(n^4)}: each of the {@code (2n - 1)^2} translations scans all
   *     {@code n^2} pixels, where {@code n = img1.length}. Auxiliary space is {@code O(1)} because
   *     only scalar counters and coordinates are stored.
   */
  public int largestOverlap(int[][] img1, int[][] img2) {
    int result = 0;
    int n = img1.length;
    for (int dx = -n + 1; dx < n; dx++) {
      for (int dy = -n + 1; dy < n; dy++) {
        int count = 0;
        for (int x = 0; x < n; x++) {
          for (int y = 0; y < n; y++) {
            int nx = x + dx;
            int ny = y + dy;
            if (nx < 0 || nx >= n || ny < 0 || ny >= n) {
              continue;
            }
            if (img1[x][y] == 1 && img2[nx][ny] == 1) {
              count++;
            }
          }
        }
        result = Math.max(result, count);
      }
    }
    return result;
  }
}
