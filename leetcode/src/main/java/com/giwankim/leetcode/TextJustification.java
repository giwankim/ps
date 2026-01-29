package com.giwankim.leetcode;

import java.util.ArrayList;
import java.util.List;

public class TextJustification {
  public List<String> fullJustify(String[] words, int maxWidth) {
    List<String> result = new ArrayList<>();
    int lineStart = 0;
    while (lineStart < words.length) {
      List<String> currentLine = getCurrentLine(lineStart, words, maxWidth);
      lineStart += currentLine.size();
      boolean isLastLine = lineStart == words.length;
      result.add(justifyLine(isLastLine, currentLine, maxWidth));
    }
    return result;
  }

  public List<String> getCurrentLine(int lineStart, String[] words, int maxWidth) {
    List<String> result = new ArrayList<>();
    int i = lineStart;
    int length = 0;
    while (i < words.length && length + words[i].length() <= maxWidth) {
      result.add(words[i]);
      length += words[i].length() + 1;
      i += 1;
    }
    return result;
  }

  /**
   * Justify the line.
   *
   * <p>Extra spaces between words should be distributed as evenly as possible. If the number of
   * spaces on a line does not divide evenly between words, the empty slots on the left will be
   * assigned more spaces than the slots on the right.
   *
   * <p>For the last line of text, it should be left-justified and no extra space is inserted
   * between words.
   *
   * @param isLastLine Boolean indicating if this is the last line of text.
   * @param line List of words in the line
   * @param maxWidth Maximum width of the line
   * @return Justified line as a string
   */
  public String justifyLine(boolean isLastLine, List<String> line, int maxWidth) {
    StringBuilder result = new StringBuilder();
    if (isLastLine) {
      result.append(String.join(" ", line));
      result.append(" ".repeat(maxWidth - result.length()));
      return result.toString();
    }
    if (line.size() == 1) {
      String word = line.getFirst();
      result.append(word).append(" ".repeat(maxWidth - word.length()));
      return result.toString();
    }
    int numberOfGaps = line.size() - 1;
    int totalSpaces = maxWidth - line.stream().mapToInt(String::length).sum();
    int numberOfSpaces = totalSpaces / numberOfGaps;
    int extraSpaces = totalSpaces % numberOfGaps;

    for (String word : line) {
      result.append(word).append(" ".repeat(numberOfSpaces));
      if (extraSpaces > 0) {
        result.append(" ");
        extraSpaces -= 1;
      }
    }

    return result.toString().trim();
  }
}
