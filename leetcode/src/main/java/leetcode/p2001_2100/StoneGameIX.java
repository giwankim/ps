package leetcode.p2001_2100;

/** <a href="https://leetcode.com/problems/stone-game-ix/">2029. Stone Game IX</a> */
public class StoneGameIX {
  /**
   * @implNote Time {@code O(n)} for the single residue-tallying pass, auxiliary space {@code O(1)}
   *     since {@code cnt} keeps one bucket per residue class mod 3 however large the input grows,
   *     where {@code n = stones.length}.
   */
  public boolean stoneGameIX(int[] stones) {
    int[] cnt = new int[3];
    for (int stone : stones) {
      cnt[stone % 3]++;
    }
    if (cnt[0] % 2 == 0) {
      return cnt[1] > 0 && cnt[2] > 0;
    }
    return Math.abs(cnt[1] - cnt[2]) > 2;
  }
}
