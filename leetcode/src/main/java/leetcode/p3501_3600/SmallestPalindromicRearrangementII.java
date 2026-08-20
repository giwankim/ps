package leetcode.p3501_3600;

/**
 * <a href="https://leetcode.com/problems/smallest-palindromic-rearrangement-ii/">3518. Smallest
 * Palindromic Rearrangement II</a>
 */
public class SmallestPalindromicRearrangementII {
  private static final int MAX = (int) 10e6 + 1;

  /**
   * @implNote Time {@code O(n * 26^2 * log MAX)} since each of the {@code n / 2} half positions
   *     tries up to 26 candidate letters and every candidate re-evaluates the 26-factor
   *     multinomial, whose {@code binom} calls each saturate within {@code log2(MAX)} steps —
   *     saturating at {@code MAX} rather than counting exactly is what keeps the arrangement
   *     totals, which reach {@code ~1e1500} at the constraint bound, inside a {@code long} —
   *     auxiliary space {@code O(n)} for the {@code half} builder, the result and its reversed
   *     copy, while {@code cnt} and {@code h} stay fixed at 128 entries, where {@code n =
   *     s.length()}.
   */
  public String smallestPalindrome(String s, int k) {
    int[] cnt = new int[128];
    for (char c : s.toCharArray()) {
      cnt[c]++;
    }

    char middle = 0;
    for (char c = 'a'; c <= 'z'; c++) {
      if (cnt[c] % 2 == 1) {
        middle = c;
        cnt[c]--;
        break;
      }
    }

    int[] h = new int[128];
    for (char c = 'a'; c <= 'z'; c++) {
      h[c] = cnt[c] / 2;
    }

    if (multinom(h) < k) {
      return "";
    }

    StringBuilder half = new StringBuilder();
    while (half.length() < s.length() / 2) {
      for (char c = 'a'; c <= 'z'; c++) {
        if (h[c] == 0) {
          continue;
        }
        h[c]--;
        int n = multinom(h);
        if (n >= k) {
          half.append(c);
          break;
        }
        h[c]++;
        k -= n;
      }
    }

    StringBuilder result = new StringBuilder(half);
    if (middle > 0) {
      result.append(middle);
    }
    return result.append(new StringBuilder(half).reverse()).toString();
  }

  /**
   * @implNote Time {@code O(A + 26 log MAX)} since the {@code Arrays.stream} sum sweeps the whole
   *     table once before the 26-letter fold splits the remaining slots one letter at a time, and
   *     the running product short-circuits the moment it reaches {@code MAX} because the caller
   *     only compares it against {@code k} — auxiliary space {@code O(1)}, where {@code A =
   *     h.length}.
   */
  private static int multinom(int[] h) {
    int total = 0;
    for (int x : h) {
      total += x;
    }
    long result = 1;
    for (char c = 'a'; c <= 'z'; c++) {
      if (h[c] == 0) {
        continue;
      }
      result *= binom(total, h[c]);
      total -= h[c];
      if (result >= MAX) {
        return MAX;
      }
    }
    return (int) result;
  }

  /**
   * @implNote Time {@code O(min(r, n - r, log MAX))} since folding {@code r} to {@code n - r} caps
   *     the trip count at the smaller side, and because the result is at least {@code 2^min(r, n -
   *     r)} the accumulator reaches {@code MAX} within {@code log2(MAX)} steps — each partial
   *     product is the exact binomial {@code C(n - r + i, i)}, a diagonal of Pascal's triangle that
   *     rises monotonically to the answer without ever exceeding it, so {@code result /= i} always
   *     divides evenly and the early return is sound — auxiliary space {@code O(1)}.
   */
  private static int binom(int n, int r) {
    if (r > n - r) {
      r = n - r;
    }
    long result = 1;
    for (int i = 1; i <= r; i++) {
      result *= n - r + i;
      result /= i;
      if (result >= MAX) {
        return MAX;
      }
    }
    return (int) result;
  }
}
