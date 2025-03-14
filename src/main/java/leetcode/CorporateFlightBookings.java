package leetcode;

public class CorporateFlightBookings {
  public int[] corpFlightBookings(int[][] bookings, int n) {
    int[] result = new int[n];
    for (int[] booking : bookings) {
      int first = booking[0] - 1;
      int last = booking[1] - 1;
      int seats = booking[2];
      for (int i = first; i <= last; i++) {
        result[i] += seats;
      }
    }
    return result;
  }
}
