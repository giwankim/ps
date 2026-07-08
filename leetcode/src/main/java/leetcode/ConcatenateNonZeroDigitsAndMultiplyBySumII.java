package leetcode;

public class ConcatenateNonZeroDigitsAndMultiplyBySumII {
  public static final int MOD = 1_000_000_007;

  /**
   * @implNote Time {@code O(m + q)}, space {@code O(m)}, where {@code m = s.length()} and {@code q
   *     = queries.length}.
   */
  public int[] sumAndMultiply(String s, int[][] queries) {
    int[] ps = new int[s.length() + 1];
    long[] px = new long[s.length() + 1];
    long[] pow = new long[s.length() + 1];
    int[] nonzero = new int[s.length() + 1];
    pow[0] = 1;
    for (int i = 1; i <= s.length(); i++) {
      int digit = s.charAt(i - 1) - '0';
      ps[i] = ps[i - 1] + digit;
      if (digit == 0) {
        px[i] = px[i - 1];
      } else {
        px[i] = (px[i - 1] * 10 + digit) % MOD;
      }
      pow[i] = (pow[i - 1] * 10) % MOD;
      nonzero[i] = nonzero[i - 1] + (digit != 0 ? 1 : 0);
    }

    int[] result = new int[queries.length];
    for (int i = 0; i < queries.length; i++) {
      int l = queries[i][0];
      int r = queries[i][1];
      int sum = ps[r + 1] - ps[l];
      int k = nonzero[r + 1] - nonzero[l];
      long x = ((px[r + 1] - px[l] * pow[k]) % MOD + MOD) % MOD;
      result[i] = (int) (x * sum % MOD);
    }
    return result;
  }
}
