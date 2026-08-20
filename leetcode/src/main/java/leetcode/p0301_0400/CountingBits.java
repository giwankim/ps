package leetcode.p0301_0400;

/** <a href="https://leetcode.com/problems/counting-bits/">338. Counting Bits</a> */
public class CountingBits {
  public int[] countBits(int n) {
    int[] result = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      result[i] = result[i >> 1] + (i & 1);
    }
    return result;
  }
}
