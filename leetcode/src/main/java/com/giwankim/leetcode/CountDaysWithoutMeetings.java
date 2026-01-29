package com.giwankim.leetcode;

import java.util.Arrays;

public class CountDaysWithoutMeetings {

  public int countDays(int days, int[][] meetings) {
    int result = 0;

    // sort the meetings by start time
    Arrays.sort(meetings, (a, b) -> a[0] - b[0]);

    int end = 0;
    for (int[] meeting : meetings) {
      if (end + 1 < meeting[0]) { // days exist between last meeting and current meeting
        result += meeting[0] - end - 1;
      }
      // update the end of last meeting
      end = Math.max(end, meeting[1]);
    }
    result += days - end;

    return result;
  }
}
