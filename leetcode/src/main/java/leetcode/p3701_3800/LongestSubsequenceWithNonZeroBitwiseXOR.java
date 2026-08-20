package leetcode.p3701_3800;

/**
 * <a href="https://leetcode.com/problems/longest-subsequence-with-non-zero-bitwise-xor/">3702.
 * Longest Subsequence With Non-Zero Bitwise XOR</a>
 */
public class LongestSubsequenceWithNonZeroBitwiseXOR {
  /** @implNote Time {@code O(n)}, space {@code O(1)}, where {@code n = nums.length}. */
  public int longestSubsequence(int[] nums) {
    int n = nums.length;
    int xor = 0;
    boolean hasNonZero = false;
    for (int num : nums) {
      xor ^= num;
      hasNonZero |= num != 0;
    }
    if (xor != 0) {
      return n;
    }
    return hasNonZero ? n - 1 : 0;
  }
}
