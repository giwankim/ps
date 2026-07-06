package leetcode;

import java.util.Arrays;
import java.util.List;

public class NumberOfPathsWithMaxScore {
  private static final int MOD = 1_000_000_007;
  private static final int[][] DIRS = {{1, 0}, {0, 1}, {1, 1}};

  /** @implNote Time {@code O(n^2)}, space {@code O(n^2)}, where {@code n = board.size()}. */
  public int[] pathsWithMaxScore(List<String> board) {
    int n = board.size();
    int[][] dp = new int[n][n];
    int[][] cnt = new int[n][n];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }

    for (int i = n - 1; i >= 0; i--) {
      for (int j = n - 1; j >= 0; j--) {
        char ch = board.get(i).charAt(j);
        if (ch == 'X') {
          continue;
        }
        if (ch == 'S') {
          dp[i][j] = 0;
          cnt[i][j] = 1;
          continue;
        }
        int gain = ch == 'E' ? 0 : ch - '0';
        for (int[] dir : DIRS) {
          int pi = i + dir[0];
          int pj = j + dir[1];
          if (pi >= n || pj >= n || dp[pi][pj] == -1) {
            continue;
          }
          int cand = dp[pi][pj] + gain;
          if (cand > dp[i][j]) {
            dp[i][j] = cand;
            cnt[i][j] = cnt[pi][pj];
          } else if (cand == dp[i][j]) {
            cnt[i][j] = (cnt[i][j] + cnt[pi][pj]) % MOD;
          }
        }
      }
    }
    return dp[0][0] == -1 ? new int[] {0, 0} : new int[] {dp[0][0], cnt[0][0]};
  }
}
