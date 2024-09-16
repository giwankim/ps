package boj.boj10699;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDate;

public class Main {
  public static void main(String[] args) throws IOException {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out)) {
      today(r, pw, Clock.systemDefaultZone());
    }
  }

  public static void today(BufferedReader r, PrintWriter pw, Clock clock) {
    pw.println(LocalDate.now(clock));
  }
}
