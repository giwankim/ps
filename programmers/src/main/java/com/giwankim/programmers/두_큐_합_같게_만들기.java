package com.giwankim.programmers;

public class 두_큐_합_같게_만들기 {

  public int solution(int[] queue1, int[] queue2) {
    int n = queue1.length;
    // Concatenate queue1 and queue2 into a single array
    // Treat the current queue1 as a sliding window [left..right]
    int[] arr = new int[2 * n];
    long sum1 = 0L;
    long sum2 = 0L;

    int idx = 0;
    for (int v : queue1) {
      arr[idx++] = v;
      sum1 += v;
    }
    for (int v : queue2) {
      arr[idx++] = v;
      sum2 += v;
    }

    long total = sum1 + sum2;

    // if total is odd, then impossible
    if (total % 2L != 0L) {
      return -1;
    }

    long target = total / 2;

    int result = 0;

    int left = 0;
    int right = n - 1;
    long cur = sum1;
    // right can advance at most n times, left can advance at most 2n times
    int limit = 3 * n;

    while (result < limit && left < 2 * n && right < 2 * n) {
      if (cur == target) {
        return result;
      }
      if (cur < target) {
        // pop from queue2, insert into queue1
        right += 1;
        if (right >= 2 * n) { // cannot expand queue1 further
          break;
        }
        cur += arr[right];
      } else {
        // pop from queue1, insert into queue2
        cur -= arr[left];
        left += 1;
      }
      result += 1;
    }

    return -1;
  }
}
