package programmers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class 정수_내림차순으로_배치하기 {
  public long solution(long n) {
    List<Integer> digits = new ArrayList<>();
    while (n > 0) {
      digits.add((int) (n % 10));
      n /= 10L;
    }
    digits.sort(Comparator.comparingInt(Integer::intValue).reversed());
    long ans = 0;
    for (int digit : digits) {
      ans *= 10L;
      ans += digit;
    }
    return ans;
  }
}
