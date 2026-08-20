package leetcode.p0401_0500;

/** <a href="https://leetcode.com/problems/string-compression/">443. String Compression</a> */
public class StringCompression {
  public int compress(char[] chars) {
    int i = 0;
    int j = 0;
    while (j < chars.length) {
      char c = chars[j];
      int count = 0;
      while (j < chars.length && chars[j] == c) {
        count++;
        j++;
      }
      chars[i++] = c;
      if (count > 1) {
        for (char d : String.valueOf(count).toCharArray()) {
          chars[i++] = d;
        }
      }
    }
    return i;
  }
}
