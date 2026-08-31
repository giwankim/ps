package programmers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class 신고_결과_받기 {
  public int[] solution(String[] id_list, String[] report, int k) {
    int n = id_list.length;
    Map<String, Set<String>> m = new HashMap<>();
    for (String id : id_list) {
      m.put(id, new HashSet<>());
    }
    for (String r : report) {
      String[] parts = r.split(" ");
      m.get(parts[1]).add(parts[0]);
    }

    Map<String, Integer> count = new HashMap<>();
    for (var e : m.entrySet()) {
      Set<String> by = e.getValue();
      if (by.size() >= k) {
        for (String reporter : by) {
          count.merge(reporter, 1, Integer::sum);
        }
      }
    }

    int[] ans = new int[n];
    for (int i = 0; i < n; i++) {
      String id = id_list[i];
      ans[i] = count.getOrDefault(id, 0);
    }
    return ans;
  }
}
