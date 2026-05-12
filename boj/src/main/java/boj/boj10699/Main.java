package boj.boj10699;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.LocalDate;

public class Main {
  public static void main(String[] args) throws IOException {
    PrintWriter pw = new PrintWriter(System.out);
    today(pw, Clock.systemDefaultZone());
    pw.close();
  }

  public static void today(PrintWriter pw, Clock clock) {
    pw.println(LocalDate.now(clock));
  }
}
