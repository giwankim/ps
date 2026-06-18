package com.giwankim.leetcode;

public class AngleBetweenHandsOfAClock {
  public double angleClock(int hour, int minutes) {
    double shortAngle = 30 * hour + 0.5 * minutes;
    double longAngle = (double) 6 * minutes;
    double angle = Math.abs(shortAngle - longAngle);
    return Math.min(angle, 360 - angle);
  }
}
