package leetcode.p2401_2500;

/**
 * <a href="https://leetcode.com/problems/determine-if-two-events-have-conflict/">2446. Determine if
 * Two Events Have Conflict</a>
 */
public class TwoEventsHaveConflict {
  public boolean haveConflict(String[] event1, String[] event2) {
    return event1[0].compareTo(event2[1]) <= 0 && event2[0].compareTo(event1[1]) <= 0;
  }
}
