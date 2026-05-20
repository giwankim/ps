package boj.boj7696;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  private static final int[] CACHE = new int[1_000_001];

  static {
    int num = 1;
    int idx = 1;
    while (idx < CACHE.length) {
      if (!hasRepeatedDigits(num)) {
        CACHE[idx++] = num;
      }
      num++;
    }
  }

  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      int n = Integer.parseInt(r.readLine());
      while (n > 0) {
        pw.println(CACHE[n]);
        n = Integer.parseInt(r.readLine());
      }
    }
  }

  private static boolean hasRepeatedDigits(int num) {
    boolean[] seen = new boolean[10];
    while (num > 0) {
      int digit = num % 10;
      if (seen[digit]) {
        return true;
      }
      seen[digit] = true;
      num /= 10;
    }
    return false;
  }
}
