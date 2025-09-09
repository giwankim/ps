package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountDaysWithoutMeetings {

  public int countDays(int days, int[][] meetings) {
    // sort the meetings by start time then end time
    /*
    Arrays.sort(
        meetings,
        Comparator.comparingInt((int[] meeting) -> meeting[0])
            .thenComparingInt(meeting -> meeting[1]));
     */
    Arrays.sort(
        meetings,
        (a, b) -> a[0] != b[0] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));

    // merge overlapping meetings
    List<int[]> mergedMeetings = new ArrayList<>();

    int start = meetings[0][0];
    int end = meetings[0][1];

    for (int i = 1; i < meetings.length; i++) {
      if (meetings[i][0] <= end) {
        // merge the meeting times
        end = Math.max(end, meetings[i][1]);
      } else {
        // meeting times do not overlap
        mergedMeetings.add(new int[] {start, end});
        start = meetings[i][0];
        end = meetings[i][1];
      }
    }
    mergedMeetings.add(new int[] {start, end});

    // find complementary days to mergedMeetings interval
    int result = mergedMeetings.getFirst()[0] - 1;
    for (int i = 0; i + 1 < mergedMeetings.size(); i++) {
      if (mergedMeetings.get(i + 1)[0] <= days) {
        result += mergedMeetings.get(i + 1)[0] - mergedMeetings.get(i)[1] - 1;
      }
    }
    if (mergedMeetings.getLast()[1] < days) {
      result += days - mergedMeetings.getLast()[1];
    }
    return result;
  }
}
