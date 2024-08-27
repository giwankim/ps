package leetcode;

public class ValidPalindrome {
  public boolean isPalindrome(String s) {
    int start = 0;
    int end = s.length() - 1;
    while (start < end) {
      if (!Character.isLetterOrDigit(s.charAt(start))) {
        start += 1;
      } else if (!Character.isLetterOrDigit(s.charAt(end))) {
        end -= 1;
      } else if (Character.toLowerCase(s.charAt(start)) != Character.toLowerCase(s.charAt(end))) {
        return false;
      } else {
        start += 1;
        end -= 1;
      }
    }
    return true;
  }

  public boolean isPalindrome2(String s) {
    String filtered = s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
    String reversed = new StringBuilder(filtered).reverse().toString();
    return filtered.equals(reversed);
  }
}
