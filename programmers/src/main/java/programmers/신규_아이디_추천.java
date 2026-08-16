package programmers;

import java.util.Locale;

public class 신규_아이디_추천 {
  public String solution(String new_id) {
    Character prev = null;
    StringBuilder sb = new StringBuilder();
    for (char c : new_id.toLowerCase(Locale.ROOT).toCharArray()) {
      if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' && c != '.') {
        continue;
      }
      if (prev != null && prev == '.' && c == '.') {
        continue;
      }
      sb.append(c);
      prev = c;
    }
    while (!sb.isEmpty() && sb.charAt(0) == '.') {
      sb.deleteCharAt(0);
    }
    while (!sb.isEmpty() && sb.charAt(sb.length() - 1) == '.') {
      sb.deleteCharAt(sb.length() - 1);
    }
    if (sb.isEmpty()) {
      sb.append('a');
    }
    if (sb.length() >= 16) {
      sb.setLength(15);
    }
    if (sb.charAt(sb.length() - 1) == '.') {
      sb.deleteCharAt(sb.length() - 1);
    }
    while (sb.length() < 3) {
      sb.append(sb.charAt(sb.length() - 1));
    }
    return sb.toString();
  }
}
