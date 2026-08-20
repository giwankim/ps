package leetcode.p1501_1600;

/** <a href="https://leetcode.com/problems/stone-game-iv/">1510. Stone Game IV</a> */
public class StoneGameIV {
  /**
   * @implNote Time {@code O(n * sqrt(n))} since {@code dp} settles each of the {@code n} piles once
   *     and every pile scans the {@code O(sqrt(n))} squares that fit in it, stopping at the first
   *     move that leaves the opponent a losing pile — auxiliary space {@code O(n)} for {@code dp},
   *     with no recursion.
   */
  public boolean winnerSquareGame(int n) {
    boolean[] dp = new boolean[n + 1];
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j * j <= i; j++) {
        if (!dp[i - j * j]) {
          dp[i] = true;
          break;
        }
      }
    }
    return dp[n];
  }
}
