package com.giwankim.grind75;

abstract class VersionControl {

  protected int badVersion = 0;

  boolean isBadVersion(int version) {
    return version >= badVersion;
  }
}

public class FirstBadVersion extends VersionControl {

  public FirstBadVersion(int badVersion) {
    this.badVersion = badVersion;
  }

  public int firstBadVersion(int n) {
    int left = 1;
    int right = n;
    int result = -1;
    while (left <= right) {
      int mid = left + (right - left) / 2;
      if (isBadVersion(mid)) {
        // mid could be the first bad version
        result = mid;
        // there could be more bad versions to the left of mid
        right = mid - 1;
      } else {
        // first bad version is to the right of mid
        left = mid + 1;
      }
    }
    return result;
  }
}
