package leetcode.p0701_0800;

import java.util.ArrayDeque;
import java.util.Deque;

/** <a href="https://leetcode.com/problems/daily-temperatures/">739. Daily Temperatures</a> */
public class DailyTemperatures {
  public int[] dailyTemperatures(int[] temperatures) {
    int[] result = new int[temperatures.length];
    Deque<Integer> stack = new ArrayDeque<>();
    for (int i = 0; i < temperatures.length; i++) {
      while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
        int last = stack.pop();
        result[last] = i - last;
      }
      stack.push(i);
    }
    return result;
  }
}
