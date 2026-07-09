package leetcode;

public class PathExistenceQueriesInAGraphI {
  /**
   * @implNote Time {@code O(n + q)} from one gap scan plus a component-id comparison per query,
   *     auxiliary space {@code O(n)} for the {@code components} array, where {@code q =
   *     queries.length}.
   */
  public boolean[] pathExistenceQueries(int n, int[] nums, int maxDiff, int[][] queries) {
    int[] components = new int[n];
    for (int i = 1; i < n; i++) {
      if (nums[i] - nums[i - 1] <= maxDiff) {
        components[i] = components[i - 1];
      } else {
        components[i] = components[i - 1] + 1;
      }
    }

    boolean[] result = new boolean[queries.length];
    for (int i = 0; i < queries.length; i++) {
      int u = queries[i][0];
      int v = queries[i][1];
      result[i] = components[u] == components[v];
    }
    return result;
  }
}
