package programmers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class 개인정보_수집_유효기간 {
  public int[] solution(String today, String[] terms, String[] privacies) {
    MyDate now = MyDate.from(today);
    Map<String, Integer> termMap = new HashMap<>();
    for (String term : terms) {
      String[] tokens = term.split(" ");
      termMap.put(tokens[0], Integer.parseInt(tokens[1]));
    }

    List<Integer> ans = new ArrayList<>();
    for (int i = 0; i < privacies.length; i++) {
      String[] tokens = privacies[i].split(" ");
      MyDate start = MyDate.from(tokens[0]);
      int term = termMap.get(tokens[1]);
      if (now.isExpired(start, term)) {
        ans.add(i + 1);
      }
    }
    return ans.stream().mapToInt(Integer::intValue).toArray();
  }

  private static LocalDate parse(String s) {
    String[] tokens = s.split("\\.");
    return LocalDate.of(
        Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
  }

  private static class MyDate {
    private int year;
    private int month;
    private int day;

    private MyDate(int year, int month, int day) {
      this.year = year;
      this.month = month;
      this.day = day;
    }

    private boolean isExpired(MyDate start, int term) {
      int expirationYear = start.year + term / 12;
      int expirationMonth = start.month + term % 12;
      expirationYear += (expirationMonth - 1) / 12;
      expirationMonth = (expirationMonth - 1) % 12 + 1;
      int expirationDay = start.day;
      if (expirationYear != this.year) {
        return expirationYear < this.year;
      }
      if (expirationMonth != this.month) {
        return expirationMonth < this.month;
      }
      return expirationDay <= this.day;
    }

    public static MyDate from(String s) {
      String[] tokens = s.split("\\.");
      return new MyDate(
          Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
    }
  }
}
