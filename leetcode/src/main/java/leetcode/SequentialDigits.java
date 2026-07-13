package leetcode;

import java.util.ArrayList;
import java.util.List;

public class SequentialDigits {
  /**
   * @implNote Time {@code O(D^2)} to walk the stall-and-extend recursion tree ({@code D} stall
   *     levels, each spawning at most nine runs of at most {@code D} digits), auxiliary space
   *     {@code O(D)} for the recursion stack, where {@code D = 10} is the fixed recursion depth —
   *     both constant and independent of {@code low} and {@code high}, since only 36
   *     sequential-digit integers exist.
   */
  public List<Integer> sequentialDigits(int low, int high) {
    List<Integer> result = new ArrayList<>();
    backtrack(0, 0, result, low, high);
    return result;
  }

  private void backtrack(int depth, int curr, List<Integer> result, int low, int high) {
    if (depth == 10) {
      if (curr >= low && curr <= high) {
        result.add(curr);
      }
      return;
    }
    if (curr > high) {
      return;
    }
    if (curr == 0) {
      for (int i = 0; i < 10; i++) {
        backtrack(depth + 1, i, result, low, high);
      }
    } else {
      int digit = curr % 10;
      if (digit != 9) {
        backtrack(depth + 1, curr * 10 + digit + 1, result, low, high);
      }
    }
  }
}
