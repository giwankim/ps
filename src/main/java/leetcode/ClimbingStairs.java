package leetcode;

public class ClimbingStairs {
  public int climbStairs(int n) {
    int[] a = new int[n + 1];
    a[0] = 1;
    a[1] = 1;
    for (int i = 2; i <= n; i++) {
      if (i >= 1) {
        a[i] += a[i - 1];
      }
      if (i >= 2) {
        a[i] += a[i - 2];
      }
    }
    return a[n];
  }
}
