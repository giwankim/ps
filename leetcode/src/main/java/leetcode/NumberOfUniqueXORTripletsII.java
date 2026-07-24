package leetcode;

public class NumberOfUniqueXORTripletsII {
  /**
   * <b>Time complexity:</b> {@code O(n^2 + n * U)}, where {@code n} is {@code nums.length} and
   * {@code U = 2048} is the XOR-value domain. Because {@code U} is constant, this simplifies to
   * {@code O(n^2)}.
   *
   * <p><b>Space complexity:</b> {@code O(U)} for the pair and triplet lookup arrays, which is
   * {@code O(1)} under the fixed input constraints.
   */
  public int uniqueXorTriplets(int[] nums) {
    int n = nums.length;

    boolean[] xorPairs = new boolean[2048];
    for (int i = 0; i < n; i++) {
      for (int j = i; j < n; j++) {
        xorPairs[nums[i] ^ nums[j]] = true;
      }
    }

    boolean[] xorTriplets = new boolean[2048];
    for (int num : nums) {
      for (int pair = 0; pair < 2048; pair++) {
        if (!xorPairs[pair]) {
          continue;
        }
        xorTriplets[num ^ pair] = true;
      }
    }

    int result = 0;
    for (boolean triplet : xorTriplets) {
      if (triplet) {
        result++;
      }
    }
    return result;
  }
}
