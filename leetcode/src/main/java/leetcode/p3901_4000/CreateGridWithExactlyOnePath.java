package leetcode.p3901_4000;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/create-grid-with-exactly-one-path/">3963. Create Grid With
 * Exactly One Path</a>
 */
public class CreateGridWithExactlyOnePath {
  /**
   * @implNote Time {@code O(m + n)}, space {@code O(m + n)}: the {@code m - 1} blocked rows share
   *     one {@code String}, so only two rows are ever built.
   */
  public String[] createGrid(int m, int n) {
    String[] res = new String[m];
    Arrays.fill(res, "." + "#".repeat(n - 1));
    res[m - 1] = ".".repeat(n);
    return res;
  }
}
