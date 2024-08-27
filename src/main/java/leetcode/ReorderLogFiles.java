package leetcode;

import java.util.ArrayList;
import java.util.List;

public class ReorderLogFiles {
  public String[] reorderLogFiles(String[] logs) {
    List<String> letterLogs = new ArrayList<>();
    List<String> digitLogs = new ArrayList<>();

    for (String log : logs) {
      if (Character.isDigit(log.split(" ")[1].charAt(0))) {
        digitLogs.add(log);
      } else {
        letterLogs.add(log);
      }
    }

    letterLogs.sort(
        (s1, s2) -> {
          String[] tokens1 = s1.split(" ", 2);
          String[] tokens2 = s2.split(" ", 2);
          int compared = tokens1[1].compareTo(tokens2[1]);
          if (compared == 0) {
            return tokens1[0].compareTo(tokens2[0]);
          }
          return compared;
        });

    letterLogs.addAll(digitLogs);
    return letterLogs.toArray(new String[0]);
  }
}
