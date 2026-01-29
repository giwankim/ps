package com.giwankim.leetcode;

import java.util.Arrays;
import java.util.Comparator;

public class MeetingRooms {
  public boolean canAttendMeetings(int[][] intervals) {
    Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
    for (int i = 1; i < intervals.length; i++) {
      int previousEnd = intervals[i - 1][1];
      int start = intervals[i][0];
      if (previousEnd > start) {
        return false;
      }
    }
    return true;
  }
}
