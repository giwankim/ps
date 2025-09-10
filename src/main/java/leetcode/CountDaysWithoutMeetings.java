package leetcode;

import java.util.Arrays;

public class CountDaysWithoutMeetings {

  public int countDays(int days, int[][] meetings) {
    // sort the meetings by start time then end time
    Arrays.sort(meetings, (a, b) -> Integer.compare(a[0], b[0]));

    // add the days until the first meeting
    int result = meetings[0][0] - 1;

    for (int i = 0; i + 1 < meetings.length; i++) {
      if (meetings[i][1] >= meetings[i + 1][0]) { // meetings overlap
        // merge meetings
        meetings[i + 1][1] = Math.max(meetings[i][1], meetings[i + 1][1]);
      } else {
        result += meetings[i + 1][0] - meetings[i][1] - 1;
      }
    }

    // add days from the end of last meeting
    result += days - meetings[meetings.length - 1][1];

    return result;
  }
}
