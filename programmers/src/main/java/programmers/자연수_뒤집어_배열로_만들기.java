package programmers;

import java.util.ArrayList;
import java.util.List;

public class 자연수_뒤집어_배열로_만들기 {
  public int[] solution(long n) {
    List<Integer> result = new ArrayList<>();
    while (n > 0) {
      result.add((int) (n % 10));
      n /= 10L;
    }
    return result.stream().mapToInt(Integer::intValue).toArray();
  }
}
