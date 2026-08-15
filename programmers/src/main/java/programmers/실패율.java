package programmers;

import java.util.ArrayList;
import java.util.List;

public class 실패율 {
  public int[] solution(int N, int[] stages) {
    int[] psum = new int[N + 2];
    for (int stage : stages) {
      psum[stage]++;
    }
    for (int i = 1; i < psum.length; i++) {
      psum[i] += psum[i - 1];
    }

    int total = stages.length;
    List<Integer> result = new ArrayList<>();
    for (int i = 1; i <= N; i++) {
      result.add(i);
    }

    result.sort((a, b) -> {
      long ad = (long) (psum[a] - psum[a - 1]) * (total - psum[b - 1]);
      long bc = (long) (psum[b] - psum[b - 1]) * (total - psum[a - 1]);
      int cmp = Long.compare(bc, ad); // descending
      return cmp != 0 ? cmp : a - b;
    });

    return result.stream().mapToInt(Integer::intValue).toArray();
  }
}
