package leetcode.p2401_2500;

import java.util.Objects;

/**
 * <a href="https://leetcode.com/problems/count-days-spent-together/">2409. Count Days Spent
 * Together</a>
 */
public class CountDaysSpentTogether {
  public static final int[] DAYS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  /**
   * @implNote Time {@code O(1)}: {@link #parse(String)} splits four fixed-length {@code "MM-DD"}
   *     strings, and the day-by-day walk from the later arrival to the earlier departure runs once
   *     per shared day, at most 365 in a non-leap year. Space {@code O(1)}.
   */
  public int countDaysTogether(
      String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
    MyDate aliceArrive = parse(arriveAlice);
    MyDate aliceLeave = parse(leaveAlice);
    MyDate bobArrive = parse(arriveBob);
    MyDate bobLeave = parse(leaveBob);

    if (aliceLeave.compareTo(bobArrive) < 0 || bobLeave.compareTo(aliceArrive) < 0) {
      return 0;
    }

    MyDate start = aliceArrive.compareTo(bobArrive) > 0 ? aliceArrive : bobArrive;
    MyDate end = aliceLeave.compareTo(bobLeave) < 0 ? aliceLeave : bobLeave;

    int ans = 0;
    while (start.compareTo(end) <= 0) {
      ans++;
      start.day++;
      if (start.day > DAYS[start.month - 1]) {
        start.day = 1;
        start.month++;
      }
    }
    return ans;
  }

  private MyDate parse(String s) {
    String[] parts = s.split("-");
    return new MyDate(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
  }

  private static class MyDate implements Comparable<MyDate> {
    public int month;
    public int day;

    public MyDate(int day, int month) {
      this.day = day;
      this.month = month;
    }

    @Override
    public int compareTo(MyDate other) {
      if (this.month != other.month) {
        return Integer.compare(this.month, other.month);
      }
      return Integer.compare(this.day, other.day);
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof MyDate myDate)) {
        return false;
      }
      return month == myDate.month && day == myDate.day;
    }

    @Override
    public int hashCode() {
      return Objects.hash(month, day);
    }
  }
}
