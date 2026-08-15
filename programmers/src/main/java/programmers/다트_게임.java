package programmers;

import java.util.ArrayList;
import java.util.List;

public class 다트_게임 {
  public int solution(String dartResult) {
    int n = dartResult.length();
    List<Integer> scores = new ArrayList<>();
    int i = 0;
    while (i < n) {
      // 점수
      int score = 0;
      while (i < n && Character.isDigit(dartResult.charAt(i))) {
        score = score * 10 + (dartResult.charAt(i) - '0');
        i++;
      }

      // 보너스
      switch (dartResult.charAt(i++)) {
        case 'S' -> {}
        case 'D' -> score *= score;
        case 'T' -> score *= score * score;
        default -> throw new IllegalArgumentException();
      }

      // 옵션
      if (i < n && dartResult.charAt(i) == '*') {
        score = 2 * score;
        if (!scores.isEmpty()) {
          scores.set(scores.size() - 1, scores.getLast() * 2);
        }
        i++;
      } else if (i < n && dartResult.charAt(i) == '#') {
        score = -score;
        i++;
      }
      scores.add(score);
    }

    return scores.stream().mapToInt(Integer::intValue).sum();
  }
}
