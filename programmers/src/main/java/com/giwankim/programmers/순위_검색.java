package com.giwankim.programmers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class 순위_검색 {

  public int[] solution(String[] info, String[] query) {
    Map<String, List<Integer>> infoToScores = new HashMap<>();

    // populate infoToScores
    for (String s : info) {
      String[] tokens = s.split(" ");
      addInfo(0, "", tokens, infoToScores);
    }

    // sort scores
    infoToScores.values().forEach(Collections::sort);

    int[] result = new int[query.length];

    // query
    for (int i = 0; i < query.length; i++) {
      String[] tokens = query[i].replace(" and ", " ").split("\\s+");
      String key = tokens[0] + tokens[1] + tokens[2] + tokens[3];
      int score = Integer.parseInt(tokens[tokens.length - 1]);

      result[i] = search(score, infoToScores.getOrDefault(key, Collections.emptyList()));
    }

    return result;
  }

  private int search(int target, List<Integer> scores) {
    if (scores.isEmpty()) {
      return 0;
    }
    if (target > scores.getLast()) {
      return 0;
    }

    int lo = 0;
    int hi = scores.size() - 1;
    while (lo < hi) {
      int mid = lo + (hi - lo) / 2;
      if (scores.get(mid) < target) {
        lo = mid + 1;
      } else {
        hi = mid;
      }
    }
    return scores.size() - lo;
  }

  private void addInfo(int index, String key, String tokens[],
      Map<String, List<Integer>> infoToScores) {
    if (index + 1 == tokens.length) {
      int score = Integer.parseInt(tokens[index]);
      infoToScores.putIfAbsent(key, new ArrayList<>());
      infoToScores.get(key).add(score);
      return;
    }

    addInfo(index + 1, key + tokens[index], tokens, infoToScores);
    addInfo(index + 1, key + "-", tokens, infoToScores);
  }
}
