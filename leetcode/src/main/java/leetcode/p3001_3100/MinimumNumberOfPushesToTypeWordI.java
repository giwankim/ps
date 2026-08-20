package leetcode.p3001_3100;

public class MinimumNumberOfPushesToTypeWordI {
  /**
   * Time complexity: {@code O(n)}, where {@code n} is {@code word.length()}, because the loop runs
   * {@code ceil(n / 8)} times. Under the constraint {@code n <= 26}, it runs at most four times.
   *
   * <p>Space complexity: {@code O(1)} — only scalar counters are used.
   */
  public int minimumPushes(String word) {
    int n = word.length();
    int ans = 0;
    int pushes = 1;
    while (n > 0) {
      int cnt = Math.min(n, 8);
      ans += pushes * cnt;
      n -= cnt;
      pushes++;
    }
    return ans;
  }
}
