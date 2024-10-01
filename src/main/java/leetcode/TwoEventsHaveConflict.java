package leetcode;

import java.util.Arrays;

public class TwoEventsHaveConflict {
  public boolean haveConflict(String[] event1, String[] event2) {
    String[][] events = {event1, event2};
    Arrays.sort(
        events,
        (a, b) -> {
          if (a[0].compareTo(b[0]) == 0) {
            return a[1].compareTo(b[1]);
          }
          return a[0].compareTo(b[0]);
        });
    return events[0][1].compareTo(events[1][0]) >= 0;
  }
}
