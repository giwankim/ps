package leetcode;

import java.util.ArrayList;
import java.util.List;

public class TextJustification {
  public List<String> fullJustify(String[] words, int maxWidth) {
    List<String> result = new ArrayList<>();
    int lineStart = 0;
    while (lineStart < words.length) {
      List<String> currentLine = getCurrentLine(lineStart, words, maxWidth);
      lineStart += currentLine.size();
      result.add(justifyLine(currentLine, words, maxWidth));
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

  public String justifyLine(List<String> line, String[] words, int maxWidth) {
    return "";
  }
}
