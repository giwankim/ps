package com.giwankim.leetcode;

public class CorporateFlightBookings {
  public int[] corpFlightBookings(int[][] bookings, int n) {
    int[] result = new int[n];

    for (int[] booking : bookings) {
      int first = booking[0];
      int last = booking[1];
      int seats = booking[2];
      result[first - 1] += seats;
      if (last < n) {
        result[last] -= seats;
      }
    }

    for (int i = 1; i < result.length; i++) {
      result[i] += result[i - 1];
    }
    return result;
  }
}
