package leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class DiagonalTraverseII {
  public int[] findDiagonalOrder(List<List<Integer>> nums) {
    List<Integer> result = new ArrayList<>();

    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
      int sumA = a[0] + a[1];
      int sumB = b[0] + b[1];
      if (sumA != sumB) {
        return Integer.compare(sumA, sumB);
      }
      return Integer.compare(a[1], b[1]);
    });

    for (int i = 0; i < nums.size(); i++) {
      for (int j = 0; j < nums.get(i).size(); j++) {
        pq.offer(new int[]{i, j});
      }
    }

    while (!pq.isEmpty()) {
      int[] coordinates = pq.poll();
      result.add(nums.get(coordinates[0]).get(coordinates[1]));
    }

    return result.stream().mapToInt(Integer::intValue).toArray();
  }
}
