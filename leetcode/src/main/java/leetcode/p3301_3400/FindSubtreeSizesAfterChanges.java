package leetcode.p3301_3400;

/**
 * <a href="https://leetcode.com/problems/find-subtree-sizes-after-changes/">3331. Find Subtree
 * Sizes After Changes</a>
 */
public class FindSubtreeSizesAfterChanges {
  public int[] findSubtreeSizes(int[] parent, String s) {
    int n = parent.length;
    int[] next = new int[n];
    for (int i = 0; i < n; i++) {
      int p = parent[i];
      while (p != -1 && s.charAt(i) != s.charAt(p)) {
        p = parent[p];
      }
      if (p != -1) {
        next[i] = p;
      } else {
        next[i] = parent[i];
      }
    }

    // subtree sizes
    int[] ans = new int[n];
    for (int i = 0; i < n; i++) {
      int p = i;
      while (p != -1) {
        ans[p]++;
        p = next[p];
      }
    }
    return ans;
  }
}
