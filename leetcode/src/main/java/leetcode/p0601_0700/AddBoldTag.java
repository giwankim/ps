package leetcode.p0601_0700;

import java.util.Arrays;

/**
 * <a href="https://leetcode.com/problems/add-bold-tag-in-string/">616. Add Bold Tag in String</a>
 */
public class AddBoldTag {
  public String addBoldTag(String s, String[] words) {
    boolean[] bold = new boolean[s.length()];
    for (String word : words) {
      int index = s.indexOf(word);
      while (index != -1) {
        Arrays.fill(bold, index, index + word.length(), true);
        index = s.indexOf(word, index + 1);
      }
    }

    StringBuilder sb = new StringBuilder();
    int i = 0;
    while (i < bold.length) {
      if (!bold[i]) {
        sb.append(s.charAt(i));
        i += 1;
      } else {
        int j = i;
        while (j < bold.length && bold[j]) {
          j += 1;
        }
        sb.append("<b>");
        sb.append(s, i, j);
        sb.append("</b>");
        i = j;
      }
    }
    return sb.toString();
  }
}
