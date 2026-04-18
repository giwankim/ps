package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextJustification {
  /**
   * @implNote Time {@code O(m * W)}, auxiliary space {@code O(W)}, where {@code m = number of
   *     output lines} and {@code W = maxWidth}.
   */
  public List<String> fullJustify(String[] words, int maxWidth) {
    List<String> result = new ArrayList<>();
    int start = 0;
    while (start < words.length) {
      int end = getEnd(words, start, maxWidth);
      result.add(justify(words, start, end, maxWidth, end == words.length));
      start = end;
    }
    return result;
  }

  private int getEnd(String[] words, int start, int maxWidth) {
    int i = start;
    int width = 0;
    for (; i < words.length; i++) {
      int needed = (i > start ? 1 : 0) + words[i].length();
      if (width + needed > maxWidth) {
        break;
      }
      width += needed;
    }
    return i;
  }

  private String justify(String[] words, int start, int end, int maxWidth, boolean isLastLine) {
    if (isLastLine || end - start == 1) {
      String prefix = String.join(" ", Arrays.copyOfRange(words, start, end));
      return prefix + " ".repeat(maxWidth - prefix.length());
    }
    int numChars = 0;
    for (int i = start; i < end; i++) {
      numChars += words[i].length();
    }
    int gaps = end - start - 1;
    int numSpaces = maxWidth - numChars;
    int spacePerWord = numSpaces / gaps;
    int remainder = numSpaces % gaps;
    StringBuilder sb = new StringBuilder(maxWidth);
    for (int i = start; i + 1 < end; i++) {
      sb.append(words[i]);
      sb.append(" ".repeat(spacePerWord + (i - start < remainder ? 1 : 0)));
    }
    sb.append(words[end - 1]);
    return sb.toString();
  }
}
