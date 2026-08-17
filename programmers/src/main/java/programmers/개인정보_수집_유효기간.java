package programmers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class 개인정보_수집_유효기간 {
  public int[] solution(String today, String[] terms, String[] privacies) {
    Map<String, Integer> termMap = new HashMap<>();
    for (String term : terms) {
      String[] tokens = term.split(" ");
      termMap.put(tokens[0], Integer.parseInt(tokens[1]));
    }
    int days = days(today);
    List<Integer> ans = new ArrayList<>();
    for (int i = 0; i < privacies.length; i++) {
      String[] tokens = privacies[i].split(" ");
      String start = tokens[0];
      int term = termMap.get(tokens[1]);
      if (days >= days(start) + term * 28) {
        ans.add(i + 1);
      }
    }
    return ans.stream().mapToInt(Integer::intValue).toArray();
  }

  private static int days(String date) {
    String[] tokens = date.split("\\.");
    int y = Integer.parseInt(tokens[0]);
    int m = Integer.parseInt(tokens[1]);
    int d = Integer.parseInt(tokens[2]);
    return y * 12 * 28 + (m - 1) * 28 + d;
  }
}
