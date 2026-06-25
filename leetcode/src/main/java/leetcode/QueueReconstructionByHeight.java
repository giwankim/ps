package leetcode;

import java.util.*;

public class QueueReconstructionByHeight {
  public int[][] reconstructQueue(int[][] people) {
    Queue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);
    Collections.addAll(pq, people);
    List<int[]> result = new ArrayList<>();
    while (!pq.isEmpty()) {
      int[] person = pq.poll();
      result.add(person[1], person);
    }
    return result.toArray(new int[people.length][2]);
  }

  public int[][] reconstructQueue2(int[][] people) {
    Arrays.sort(people, (a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);
    List<int[]> result = new ArrayList<>();
    for (int[] person : people) {
      result.add(person[1], person);
    }
    return result.toArray(new int[0][]);
  }
}
