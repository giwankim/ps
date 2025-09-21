package com.giwankim.programmers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class 순위_검색 {

  public int[] solution(String[] info, String[] query) {
    Map<String, List<Integer>> keyToScores = new HashMap<>();

    // populate keyToScores
    for (String s : info) {
      String[] tokens = s.split(" ");
      addScore(0, "", tokens, keyToScores);
    }

    // sort scores
    keyToScores.values().forEach(Collections::sort);

    int[] result = new int[query.length];

    // query
    for (int i = 0; i < query.length; i++) {
      String[] tokens = query[i].replace(" and ", " ").split("\\s+");
      String key = Arrays.stream(tokens, 0, tokens.length - 1).collect(Collectors.joining());
      int score = Integer.parseInt(tokens[tokens.length - 1]);
      result[i] =
          countGreaterThanOrEqual(score, keyToScores.getOrDefault(key, Collections.emptyList()));
    }

    return result;
  }

  /**
   * Calculates the number of integers in the given sorted list that are greater than or equal to
   * the specified target value.
   *
   * @param target the target integer to compare against elements in the list
   * @param scores a sorted list of integers to search; must be sorted in ascending order
   * @return the count of elements in the list that are greater than or equal to the target
   */
  private int countGreaterThanOrEqual(int target, List<Integer> scores) {
    return scores.size() - lowerBound(target, scores);
  }

  /**
   * Searches for the lower bound index in a sorted list where the specified target value can be
   * inserted while maintaining the sorted order. The lower bound is defined as the smallest index
   * at which the target is less than or equal to the element at that index.
   *
   * @param target the target value to find or compare against
   * @param scores a sorted list of integers to search; must be sorted in ascending order
   * @return the index of the lower bound for the target in the sorted list; if the target is
   * greater than all elements in the list, returns the size of the list
   */
  public int lowerBound(int target, List<Integer> scores) {
    int result = scores.size();

    int lo = 0;
    int hi = scores.size() - 1;

    while (lo <= hi) {
      int mid = lo + (hi - lo) / 2;
      if (target <= scores.get(mid)) {
        result = mid; // scores[mid] is a potential lower bound
        hi = mid - 1;
      } else {
        lo = mid + 1;
      }
    }

    return result;
  }

  /**
   * Recursively populate keyToScores with keys and list of scores. Keys also include "-" as a
   * wildcard placeholder.
   *
   * @param index
   * @param key
   * @param tokens
   * @param keyToScores
   */
  private void addScore(
      int index, String key, String[] tokens, Map<String, List<Integer>> keyToScores) {
    if (index + 1 == tokens.length) {
      int score = Integer.parseInt(tokens[index]);
      // insert empty list if absent
      keyToScores.putIfAbsent(key, new ArrayList<>());
      keyToScores.get(key).add(score);
      return;
    }

    addScore(index + 1, key + tokens[index], tokens, keyToScores);
    addScore(index + 1, key + "-", tokens, keyToScores);
  }
}
