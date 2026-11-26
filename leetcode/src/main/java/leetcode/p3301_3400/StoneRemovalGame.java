package leetcode.p3301_3400;

/** <a href="https://leetcode.com/problems/stone-removal-game/">3360. Stone Removal Game</a> */
public class StoneRemovalGame {
  /**
   * @implNote Time {@code O(1)}, space {@code O(1)}: the removals shrink 10, 9, 8, ... so at most 7
   *     turns fit within {@code n <= 50}.
   */
  public boolean canAliceWin(int n) {
    boolean ans = false;
    int stones = 10;
    while (n - stones >= 0) {
      n -= stones;
      ans = !ans;
      stones--;
    }
    return ans;
  }
}
