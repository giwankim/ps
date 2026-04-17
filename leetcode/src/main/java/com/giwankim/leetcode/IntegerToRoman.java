package com.giwankim.leetcode;

public class IntegerToRoman {
  private static final String[] ONES = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
  private static final String[] TENS = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
  private static final String[] HUNDREDS = {
    "", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"
  };
  private static final String[] THOUSANDS = {"", "M", "MM", "MMM"};

  /**
   * @implNote Time {@code O(log(n)) but O(1) since 1<=n<=3999}, space {@code similarly O(1)}.
   */
  public String intToRoman(int num) {
    int thousands = num / 1000;
    int hundreds = (num % 1000) / 100;
    int tens = (num % 100) / 10;
    int ones = num % 10;

    StringBuilder sb = new StringBuilder();
    sb.append(THOUSANDS[thousands]);
    sb.append(HUNDREDS[hundreds]);
    sb.append(TENS[tens]);
    sb.append(ONES[ones]);
    return sb.toString();
  }
}
