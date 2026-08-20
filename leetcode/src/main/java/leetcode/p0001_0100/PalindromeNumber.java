package leetcode.p0001_0100;

/** <a href="https://leetcode.com/problems/palindrome-number/">9. Palindrome Number</a> */
public class PalindromeNumber {
  /** @implNote Time {@code O(log_10(x))}, space {@code O(1)}. */
  public boolean isPalindrome(int x) {
    if (x < 0) {
      return false;
    }
    int reversed = 0;
    int number = x;
    while (number > 0) {
      reversed *= 10;
      reversed += number % 10;
      number /= 10;
    }
    return reversed == x;
  }
}
