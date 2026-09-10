package leetcode.p1001_1100;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** <a href="https://leetcode.com/problems/high-five/">1086. High Five</a> */
public class HighFive {
  /**
   * @implNote Time {@code O(n log n)}: grouping is linear, and sorting every student's list plus
   *     the id list costs at most {@code n log n} in total. Space {@code O(n)} for the grouped
   *     scores, where {@code n = items.length}.
   */
  public int[][] highFive(int[][] items) {
    Map<Integer, List<Integer>> scoresById = new HashMap<>();
    for (int[] item : items) {
      scoresById.computeIfAbsent(item[0], k -> new ArrayList<>()).add(item[1]);
    }

    List<Integer> ids = new ArrayList<>(scoresById.keySet());
    ids.sort(Integer::compareTo);

    int[][] result = new int[ids.size()][2];
    for (int i = 0; i < ids.size(); i++) {
      int id = ids.get(i);
      List<Integer> scores = scoresById.get(id);
      scores.sort(Comparator.reverseOrder());
      int sum = 0;
      for (int j = 0; j < 5; j++) {
        sum += scores.get(j);
      }
      result[i][0] = id;
      result[i][1] = sum / 5;
    }
    return result;
  }
}
