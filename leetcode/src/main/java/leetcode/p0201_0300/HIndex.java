package leetcode.p0201_0300;

/** <a href="https://leetcode.com/problems/h-index/">274. H-Index</a> */
public class HIndex {
  /** @implNote Time {@code O(n)}, space {@code O(1)}. */
  public int hIndex(int[] citations) {
    int n = citations.length;
    int[] counts = new int[n + 1];
    for (int citation : citations) {
      if (citation >= n) {
        counts[n]++;
      } else {
        counts[citation]++;
      }
    }
    int count = 0;
    for (int i = n; i >= 0; i--) {
      count += counts[i];
      if (count >= i) {
        return i;
      }
    }
    return 0;
  }
}
