package boj.boj2082;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
  private static final char[][][] DIGITS = {
    {
      "###".toCharArray(),
      "#.#".toCharArray(),
      "#.#".toCharArray(),
      "#.#".toCharArray(),
      "###".toCharArray()
    }, // 0
    {
      "..#".toCharArray(),
      "..#".toCharArray(),
      "..#".toCharArray(),
      "..#".toCharArray(),
      "..#".toCharArray()
    }, // 1
    {
      "###".toCharArray(),
      "..#".toCharArray(),
      "###".toCharArray(),
      "#..".toCharArray(),
      "###".toCharArray()
    }, // 2
    {
      "###".toCharArray(),
      "..#".toCharArray(),
      "###".toCharArray(),
      "..#".toCharArray(),
      "###".toCharArray()
    }, // 3
    {
      "#.#".toCharArray(),
      "#.#".toCharArray(),
      "###".toCharArray(),
      "..#".toCharArray(),
      "..#".toCharArray()
    }, // 4
    {
      "###".toCharArray(),
      "#..".toCharArray(),
      "###".toCharArray(),
      "..#".toCharArray(),
      "###".toCharArray()
    }, // 5
    {
      "###".toCharArray(),
      "#..".toCharArray(),
      "###".toCharArray(),
      "#.#".toCharArray(),
      "###".toCharArray()
    }, // 6
    {
      "###".toCharArray(),
      "..#".toCharArray(),
      "..#".toCharArray(),
      "..#".toCharArray(),
      "..#".toCharArray()
    }, // 7
    {
      "###".toCharArray(),
      "#.#".toCharArray(),
      "###".toCharArray(),
      "#.#".toCharArray(),
      "###".toCharArray()
    }, // 8
    {
      "###".toCharArray(),
      "#.#".toCharArray(),
      "###".toCharArray(),
      "..#".toCharArray(),
      "###".toCharArray()
    }, // 9
  };

  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      char[][] clock = new char[5][15];
      for (int i = 0; i < 5; i++) {
        clock[i] = r.readLine().toCharArray();
      }
      for (int hour = 0; hour < 24; hour++) {
        for (int minute = 0; minute < 60; minute++) {
          if (matches(clock, hour, minute)) {
            pw.printf("%02d:%02d%n", hour, minute);
            return;
          }
        }
      }
    }
  }

  private static boolean matches(char[][] clock, int hour, int minute) {
    return canDigitMatch(clock, 0, hour / 10)
        && canDigitMatch(clock, 4, hour % 10)
        && canDigitMatch(clock, 8, minute / 10)
        && canDigitMatch(clock, 12, minute % 10);
  }

  private static boolean canDigitMatch(char[][] clock, int offset, int digit) {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 3; j++) {
        if (clock[i][j + offset] == '#' && DIGITS[digit][i][j] == '.') {
          return false;
        }
      }
    }
    return true;
  }
}
