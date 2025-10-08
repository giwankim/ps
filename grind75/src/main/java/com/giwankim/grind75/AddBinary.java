package com.giwankim.grind75;

public class AddBinary {

  public String addBinary(String a, String b) {
    StringBuilder result = new StringBuilder();

    // iterate from the end of the strings
    int i = a.length() - 1;
    int j = b.length() - 1;
    int carry = 0; // carry from previous iteration

    while (i >= 0 || j >= 0) {
      int sum = carry;
      if (i >= 0) {
        sum += a.charAt(i) - '0';
      }
      if (j >= 0) {
        sum += b.charAt(j) - '0';
      }
      result.append(sum % 2);
      carry = sum / 2;
      i -= 1;
      j -= 1;
    }

    // if there is a carry, append it to the result
    if (carry > 0) {
      result.append(carry);
    }

    return result.reverse().toString();
  }
}
