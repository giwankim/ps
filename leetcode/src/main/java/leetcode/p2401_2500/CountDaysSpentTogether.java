package leetcode.p2401_2500;

/**
 * <a href="https://leetcode.com/problems/count-days-spent-together/">2409. Count Days Spent
 * Together</a>
 */
public class CountDaysSpentTogether {
  public static final int[] DAYS = {-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  /**
   * @implNote Time {@code O(1)}: {@link #parse(String)} turns each {@code "MM-DD"} string into its
   *     day of the year by summing at most 11 {@link #DAYS} entries, and the shared stay is the
   *     intersection running from the later arrival to the earlier departure, whose inclusive
   *     length is the answer, or {@code 0} when the stays are disjoint. Space {@code O(1)}.
   */
  public int countDaysTogether(
      String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
    int aliceStart = parse(arriveAlice);
    int aliceEnd = parse(leaveAlice);
    int bobStart = parse(arriveBob);
    int bobEnd = parse(leaveBob);
    int start = Math.max(aliceStart, bobStart);
    int end = Math.min(aliceEnd, bobEnd);
    if (start > end) {
      return 0;
    }
    return end - start + 1;
  }

  private int parse(String date) {
    String[] parts = date.split("-");
    int month = Integer.parseInt(parts[0]);
    int day = Integer.parseInt(parts[1]);

    int result = 0;
    for (int i = 1; i < month; i++) {
      result += DAYS[i];
    }
    result += day;
    return result;
  }
}
