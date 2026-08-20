package leetcode.p0301_0400;

/** <a href="https://leetcode.com/problems/reverse-string/">344. Reverse String</a> */
public class ReverseString {
  public void reverseString(char[] s) {
    int start = 0;
    int end = s.length - 1;
    while (start < end) {
      char temp = s[start];
      s[start] = s[end];
      s[end] = temp;
      start += 1;
      end -= 1;
    }
  }
}
