package leetcode.p0301_0400;

import java.util.LinkedList;
import java.util.Queue;

/** <a href="https://leetcode.com/problems/design-hit-counter/">362. Design Hit Counter</a> */
public class HitCounter {
  private final Queue<Integer> queue;

  public HitCounter() {
    queue = new LinkedList<>();
  }

  public void hit(int timestamp) {
    queue.offer(timestamp);
  }

  public int getHits(int timestamp) {
    while (!queue.isEmpty() && timestamp - queue.peek() >= 300) {
      queue.poll();
    }
    return queue.size();
  }
}
