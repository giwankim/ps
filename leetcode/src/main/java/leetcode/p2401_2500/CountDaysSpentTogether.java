package leetcode.p2401_2500;

/**
 * <a href="https://leetcode.com/problems/count-days-spent-together/">2409. Count Days Spent
 * Together</a>
 */
public class CountDaysSpentTogether {
  public static final int[] DAYS = {-1, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  /**
   * @implNote Time {@code O(1)}: {@link #parse(String)} turns each {@code "MM-DD"} string into its
   *     day of the year by summing at most 11 {@link #DAYS} entries, the difference array adds
   *     {@code +1} on each arrival and {@code -1} the day after each departure, and one prefix-sum
   *     pass followed by a scan of the 365 days counts those covered by both stays. Space
   *     {@code O(1)} for the fixed 367-slot {@code days} array.
   */
  public int countDaysTogether(
      String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
    int aliceStart = parse(arriveAlice);
    int aliceEnd = parse(leaveAlice);
    int bobStart = parse(arriveBob);
    int bobEnd = parse(leaveBob);
    int[] days = new int[367];
    days[aliceStart]++;
    days[aliceEnd + 1]--;
    days[bobStart]++;
    days[bobEnd + 1]--;
    for (int i = 1; i < days.length; i++) {
      days[i] += days[i - 1];
    }
    int ans = 0;
    for (int i = 1; i <= 365; i++) {
      if (days[i] == 2) {
        ans++;
      }
    }
    return ans;
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
