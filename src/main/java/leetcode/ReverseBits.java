package leetcode;

public class ReverseBits {
  public int reverseBits(int n) {
    int result = 0;
    for (int i = 0; i < 32; i++) {
      if (((1 << i) & n) != 0) {
        result |= (1 << (32 - i - 1));
      }
    }
    return result;
  }
}
