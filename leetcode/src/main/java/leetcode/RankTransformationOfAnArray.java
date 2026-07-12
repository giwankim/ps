package leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RankTransformationOfAnArray {
  /**
   * @implNote Time {@code O(n log n)} from sorting the copy, auxiliary space {@code O(n)} for the
   *     sorted copy and the {@code rank} map, where {@code n = arr.length}.
   */
  public int[] arrayRankTransform(int[] arr) {
    int[] sorted = arr.clone();
    Arrays.sort(sorted);
    int n = arr.length;
    Map<Integer, Integer> rank = new HashMap<>();
    for (int num : sorted) {
      rank.putIfAbsent(num, rank.size() + 1);
    }
    int[] result = new int[n];
    for (int i = 0; i < n; i++) {
      result[i] = rank.get(arr[i]);
    }
    return result;
  }
}
