package leetcode;

public class ProcessStringWithSpecialOperationsII {
  public char processStr(String s, long k) {
    long len = 0;
    int n = s.length();
    for (int i = 0; i < n; i++) {
      switch (s.charAt(i)) {
        case '*' -> {
          len = Math.max(0, len - 1);
        }
        case '#' -> {
          len *= 2;
        }
        case '%' -> {}
        default -> len++;
      }
    }

    if (k >= len) {
      return '.';
    }

    for (int i = n - 1; i >= 0; i--) {
      switch (s.charAt(i)) {
        case '*' -> {
          len++;
        }
        case '#' -> {
          if (k >= len / 2) {
            k -= len / 2;
          }
          len /= 2;
        }
        case '%' -> {
          k = len - k - 1;
        }
        default -> {
          if (k + 1 == len) {
            return s.charAt(i);
          }
          len--;
        }
      }
    }
    return '.';
  }
}
