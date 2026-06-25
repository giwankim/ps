package naver;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class 점수_옮기기 {

  public int moveNumbers(int cap, int k, int[] score, int m) {
    Set<Integer> set = new HashSet<>();
    Deque<int[]> upper = new LinkedList<>();
    Deque<int[]> lower = new LinkedList<>();

    for (int i = 0; i < score.length; i++) {
      if (score[i] > k) {
        upper.addLast(new int[] {score[i], i});
      } else if (score[i] < k) {
        lower.addLast(new int[] {score[i], i});
      }
    }

    return set.size();
  }
}
